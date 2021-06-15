package com.dachen.integral.biz.service.impl;

import com.dachen.common.exception.ServiceException;
import com.dachen.commons.micro.lock.RedisLock;
import com.dachen.integral.biz.dao.IntegralAwardDao;
import com.dachen.integral.biz.dao.IntegralTaskDao;
import com.dachen.integral.biz.dao.UserIntegralDao;
import com.dachen.integral.biz.dao.UserLotteryRecordDao;
import com.dachen.integral.biz.service.LotteryService;
import com.dachen.integral.biz.service.remote.RemoteCallService;
import com.dachen.integral.data.constant.BaseConstants;
import com.dachen.integral.data.enums.*;
import com.dachen.integral.data.po.*;
import com.dachen.integral.data.vo.*;
import com.dachen.redis.IJedisProxy;
import com.dachen.redis.core.JedisProxyFactory;
import com.dachen.util.BeanUtil;
import com.dachen.util.CalendarUtil;
import com.dachen.util.QiniuUtil;
import com.dachen.util.StringUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: wangyongbin
 * @Date: 2021/4/23 15:51
 * @Description:
 */
@Service
@Slf4j
public class LotteryServiceImpl implements LotteryService {

    @Autowired
    private IntegralAwardDao integralAwardDao;
    @Autowired
    private UserRecordServiceImpl userRecordService;
    @Autowired
    private IntegralTaskDao integralTaskDao;
    @Autowired
    private UserIntegralDao userIntegralDao;
    @Autowired
    private UserLotteryRecordDao userLotteryRecordDao;
    @Autowired
    private RemoteCallService remoteCallService;


    //中奖奖品集合
    private static Map<String, List<AwardProbabilityPO>> awardMap = new ConcurrentHashMap();
    //中奖概率集合
    private static Map<String, List<Double>> probabilityMap = new ConcurrentHashMap();

    private IJedisProxy getJedisProxy() {
        return JedisProxyFactory.getJedisProxy();
    }


    @PostConstruct
    public void initLotteryInfo(){
        //查询有一等奖的奖品集合
        List<AwardProbabilityPO> awardList1 = integralAwardDao.queryAwardsList(null);
        //抽奖方式 1:单次抽奖 2:十连抽
        Map<Integer, List<AwardProbabilityPO>> award1 = awardList1.stream().collect(Collectors.groupingBy(AwardProbabilityPO::getMode));

        //单次有一等奖的奖品
        List<AwardProbabilityPO> oneAwarsList = award1.get(ModeEnum.one.getIndex()).stream().filter(obj -> obj.getFirstProbability() > 0).collect(Collectors.toList());
        oneAwarsList.forEach(obj -> obj.setProbability(obj.getFirstProbability()));
        //多次有一等奖的奖品
        List<AwardProbabilityPO> tenAwarsList = award1.get(ModeEnum.ten.getIndex()).stream().filter(obj -> obj.getFirstProbability() > 0).collect(Collectors.toList());
        tenAwarsList.forEach(obj -> obj.setProbability(obj.getFirstProbability()));

        awardMap.put(AwardKeyEnum.oneFirstAward.getKey(), oneAwarsList);
        awardMap.put(AwardKeyEnum.manyFirstAward.getKey(), tenAwarsList);

        probabilityMap.put(AwardKeyEnum.oneFirstProbability.getKey(), getProbability(oneAwarsList));
        probabilityMap.put(AwardKeyEnum.manyFirstProbability.getKey(), getProbability(tenAwarsList));

        //多次抽奖时第一次的奖品
        List<AwardProbabilityPO> firstAwarsList = award1.get(ModeEnum.ten.getIndex()).stream().filter(obj -> obj.getOneProbability() > 0).collect(Collectors.toList());
        firstAwarsList.forEach(obj -> obj.setProbability(obj.getOneProbability()));
        awardMap.put(AwardKeyEnum.firstAward.getKey(), firstAwarsList);
        probabilityMap.put(AwardKeyEnum.firstProbability.getKey(), getProbability(firstAwarsList));


        //查询没有一等奖的奖品集合 firstPrize 1:是一等奖 0:不是
        List<AwardProbabilityPO> awardList2 = integralAwardDao.queryAwardsList(0);
        //抽奖方式 1:单次抽奖 2:十连抽
        Map<Integer, List<AwardProbabilityPO>> award2 = awardList2.stream().collect(Collectors.groupingBy(AwardProbabilityPO::getMode));

        //单次没有一等奖的奖品
        List<AwardProbabilityPO> oneUnFirstAwarsList = award2.get(ModeEnum.one.getIndex()).stream().filter(obj -> obj.getUnFirstProbability() > 0).collect(Collectors.toList());
        oneUnFirstAwarsList.forEach(obj -> obj.setProbability(obj.getUnFirstProbability()));
        //多次没有一等奖的奖品
        List<AwardProbabilityPO> tenUnFirstAwarsList = award2.get(ModeEnum.ten.getIndex()).stream().filter(obj -> obj.getUnFirstProbability() > 0).collect(Collectors.toList());
        tenUnFirstAwarsList.forEach(obj -> obj.setProbability(obj.getUnFirstProbability()));

        awardMap.put(AwardKeyEnum.oneUnFirstAward.getKey(), oneUnFirstAwarsList);
        awardMap.put(AwardKeyEnum.manyUnFirstAward.getKey(), tenUnFirstAwarsList);

        probabilityMap.put(AwardKeyEnum.oneUnFirstProbability.getKey(), getProbability(oneUnFirstAwarsList));
        probabilityMap.put(AwardKeyEnum.manyUnFirstProbability.getKey(), getProbability(tenUnFirstAwarsList));
    }

    /**
     * 计算各个奖品的中奖概率
     * @param awardList
     * @return
     */
    public List<Double> getProbability(List<AwardProbabilityPO> awardList){
        //奖品总数
        int size = awardList.size();
        //计算总概率
        double sumProbability = 0d;
        for (AwardProbabilityPO award : awardList) {
            sumProbability += award.getProbability();
        }
        //计算每个奖品的概率区间
        //例如奖品A概率区间0-0.1 奖品B概率区间 0.1-0.5 奖品C概率区间0.5-1
        //每个奖品的中奖率越大，所占的概率区间就越大
        List<Double> sortAwardProbabilityList = new ArrayList<Double>(size);
        //每个奖品的临时中奖概率
        Double tempSumProbability = 0d;
        for (AwardProbabilityPO award : awardList) {
            if(award.getProbability() > 0){
                tempSumProbability += award.getProbability();
                sortAwardProbabilityList.add(tempSumProbability / sumProbability);
            }
        }
        return sortAwardProbabilityList;
    }

    @Override
    public void refresh() {
        this.initLotteryInfo();
    }

    @Override
    public List<IntegralAwardVo> queryAwardList(Integer lotteryType) {
        log.info("queryAwardList查询中奖奖品:{}");
        //type 抽奖类型 1:积分抽奖
        List<AwardInfoPO> integralAwardList = integralAwardDao.queryAwardInfo(lotteryType);

        List<IntegralAwardVo> awardVoList = BeanUtil.copyList(integralAwardList, IntegralAwardVo.class);
        awardVoList.forEach(award -> {
            award.setIconUrl(MessageFormat.format(QiniuUtil.getQiniuUrlPattern(), QiniuUtil.DEFAULT_BUCKET, "integral/" + award.getAwardIcon() + ".png"));
        });
        return awardVoList;
    }

    @RedisLock(key = "user:one:lottery", msg = "正在抽奖，请不要重复提交。。", expireTime=10)
    @Override
    public LotteryResultVo getOneLottery(Integer userId) {
        log.info("getOneLottery单次抽奖:{}", userId);

        LotteryResultVo result = new LotteryResultVo();

        //查询积分任务详情
        List<IntegralTaskPO> taskIntegrals = integralTaskDao.queryTaskIntegral("oneLottery");
        if(CollectionUtils.isEmpty(taskIntegrals)){
            throw new ServiceException("当前任务不存在");
        }
        IntegralTaskPO taskIntegralInfo = taskIntegrals.get(0);
        //查询用户积分
        UserIntegralPO userIntegralPO = userIntegralDao.queryUserIntegral(userId);
        //用户当前积分
        Integer currentIntegral = 0;
        if(Objects.nonNull(userIntegralPO)){
            currentIntegral = userIntegralPO.getCurrentIntegral();
        }
        if(currentIntegral < taskIntegralInfo.getIntegral()){
            throw new ServiceException("用户当前积分不足，不能参与抽奖活动");
        }


        //设置redis保存的key
        String key = "one:lottery";
        //获取一等奖的领取情况
        String firstPrizeStatus = this.getJedisProxy().get(key);
        //中奖奖品
        List<AwardProbabilityPO> awardList;
        //中奖概率
        List<Double> probabilityList;
        if(StringUtils.isNotEmpty(firstPrizeStatus)){
            //一等奖已经被领取， 获取没有一等奖的奖品和中奖池
            awardList = awardMap.get(AwardKeyEnum.oneUnFirstAward.getKey());
            probabilityList = probabilityMap.get(AwardKeyEnum.oneUnFirstProbability.getKey());
        }else{
            //一等奖没有被领取， 获取有一等奖的奖品和中奖池
            awardList = awardMap.get(AwardKeyEnum.oneFirstAward.getKey());
            probabilityList = probabilityMap.get(AwardKeyEnum.oneFirstProbability.getKey());
        }

        //抽奖并校验一等奖的获取情况
        AwardProbabilityPO integralAward = this.checkfirstPrize(key, awardList, probabilityList);

        //step1 更新用户积分
        userRecordService.updateUserIntegral(userId, taskIntegralInfo.getIntegral(), OperateTypeEnum.SUBTRACT.getIndex());
        //step2 添加用户抽奖任务积分操作记录
        userRecordService.insertIntegralRecord(userId, OperateTypeEnum.SUBTRACT.getIndex(), taskIntegralInfo, taskIntegralInfo.getIntegral());

        //step3 中奖结果处理
        //奖品类型 1:学币 2:积分
        Integer prizeType = integralAward.getPrizeType();
        if(PrizeTypeEnum.integral.getIndex().equals(prizeType)){
            //如果中奖的是积分，则：新增用户的积分数和用户的积分操作记录
            //step1 更新用户积分
            userRecordService.updateUserIntegral(userId, integralAward.getPrizeAmount(), OperateTypeEnum.ADD.getIndex());
            //step2 添加用户中奖的积分操作记录
            userRecordService.insertIntegralRecord(userId, OperateTypeEnum.ADD.getIndex(), taskIntegralInfo, integralAward.getPrizeAmount());
        }else if(PrizeTypeEnum.coin.getIndex().equals(prizeType)){
            //如果中奖的是学币，则：给用户充值学币
            Integer prizeAmount = integralAward.getPrizeAmount();
            String taskName = taskIntegralInfo.getTaskName();
            String payId = userRecordService.insertPayCoinRecord(userId, prizeAmount, taskName);
            //学币转账
            remoteCallService.transfer("integral_lottery", payId, String.valueOf(userId), taskName + "中奖学币", taskName + "中奖学币",
                    BaseConstants.PLAT_ACCOUNTID, sourceTypeEnum.platform.getIndex(), String.valueOf(userId), sourceTypeEnum.user.getIndex(), taskName + "中奖学币", prizeAmount.longValue());
        }

        //step4 保存抽奖记录
        UserLotteryRecordPO userLotteryRecord = new UserLotteryRecordPO();
        userLotteryRecord.setUserId(userId);
        //抽奖类型 1:积分抽奖
        userLotteryRecord.setType(1);
        //抽奖方式 1:单次抽奖 2:十连抽
        userLotteryRecord.setMode(1);
        userLotteryRecord.setTaskId(taskIntegralInfo.getId());
        userLotteryRecord.setTaskName(taskIntegralInfo.getTaskName());
        userLotteryRecord.setIntegral(taskIntegralInfo.getIntegral());

        //抽奖结果总计
        List<AwardTotalInfo> awardTotals = new ArrayList<>(1);
        //返回提示
        String hint = "";
        if(null != integralAward.getPrizeAmount() && integralAward.getPrizeAmount() > 0){
            //抽奖结果 1:已中奖 0:未中奖
            userLotteryRecord.setStatus(1);
            result.setPrize(Boolean.TRUE);

            hint = MessageFormat.format(BaseConstants.content1, integralAward.getPrizeAmount() + PrizeTypeEnum.getValue(integralAward.getPrizeType()));

            AwardTotalInfo awardTotalInfo = new AwardTotalInfo();
            awardTotalInfo.setPrizeAmount(integralAward.getPrizeAmount());
            awardTotalInfo.setPrizeType(integralAward.getPrizeType());
            awardTotalInfo.setIconUrl(MessageFormat.format(QiniuUtil.getQiniuUrlPattern(), QiniuUtil.DEFAULT_BUCKET, "integral/" + PrizeTypeEnum.getUname(integralAward.getPrizeType()) + ".png"));
            awardTotals.add(awardTotalInfo);
        }else{
            hint = BaseConstants.content2;
            //抽奖结果 1:已中奖 0:未中奖
            userLotteryRecord.setStatus(0);
            result.setPrize(Boolean.FALSE);
        }

        //抽奖结果明细
        List<LotteryDetail> lotteryDetails = Lists.newArrayList();
        LotteryDetail lotteryDetail = BeanUtil.copy(integralAward, LotteryDetail.class);
        lotteryDetail.setIconUrl(MessageFormat.format(QiniuUtil.getQiniuUrlPattern(), QiniuUtil.DEFAULT_BUCKET, "integral/" + integralAward.getAwardIcon() + ".png"));
        lotteryDetail.setAmount(1);
        lotteryDetails.add(lotteryDetail);

        //抽奖结果明细
        userLotteryRecord.setLotteryDetails(lotteryDetails);
        //抽奖结果总计
        userLotteryRecord.setAwardTotal(awardTotals);
        //step4 保存抽奖记录
        userRecordService.inserLotteryRecord(userId, userLotteryRecord);

        result.setHint(hint);
        result.setAwardInfo(lotteryDetails);
        return result;
    }

    /**
     * 抽奖并校验一等奖的获取情况
     * @param key
     * @param awardList
     * @param probabilityList
     * @return
     */
    public AwardProbabilityPO checkfirstPrize(String key, List<AwardProbabilityPO> awardList, List<Double> probabilityList){
        //抽奖
        AwardProbabilityPO integralAward = this.lottery(awardList, probabilityList);

        //判断当前返回是否是一等奖
        if(FirstPrizeEnum.firstPrize.getIndex().equals(integralAward.getFirstPrize())){
            //获取一等奖的领取情况
            String firstPrizeStatus = this.getJedisProxy().get(key);
            if(StringUtils.isNotEmpty(firstPrizeStatus)){
                //一等奖已经被领取，重新抽奖
                //一等奖已经被领取， 获取没有一等奖的奖品和中奖池
                awardList = awardMap.get(AwardKeyEnum.oneUnFirstAward.getKey());
                probabilityList = probabilityMap.get(AwardKeyEnum.oneUnFirstProbability.getKey());
                //抽奖并校验一等奖的获取情况
                this.checkfirstPrize(key, awardList, probabilityList);
            }

            //当前时间距离第二天凌晨的秒数
            Long secondsNextEarlyMorning = CalendarUtil.getSecondsNextEarlyMorning();
            //设置缓存
            this.getJedisProxy().incr(key);
            this.getJedisProxy().expire(key, secondsNextEarlyMorning.intValue());
        }
        return integralAward;
    }

    @RedisLock(key = "user:ten:lottery", msg = "正在抽奖，请不要重复提交。。")
    @Override
    public LotteryResultVo getManyLottery(Integer userId) {
        log.info("getManyLottery十连抽奖:{}", userId);
        //查询积分任务详情
        List<IntegralTaskPO> taskIntegrals = integralTaskDao.queryTaskIntegral("tenLottery");
        if(CollectionUtils.isEmpty(taskIntegrals)){
            throw new ServiceException("当前任务不存在");
        }
        IntegralTaskPO taskIntegralInfo = taskIntegrals.get(0);

        //查询用户积分
        UserIntegralPO userIntegralPO = userIntegralDao.queryUserIntegral(userId);
        //用户当前积分
        Integer currentIntegral = 0;
        if(Objects.nonNull(userIntegralPO)){
            currentIntegral = userIntegralPO.getCurrentIntegral();
        }
        if(currentIntegral < taskIntegralInfo.getIntegral()){
            throw new ServiceException("用户当前积分不足，不能参与抽奖活动");
        }

        LotteryResultVo result = new LotteryResultVo();
        //奖品信息详情
        List<AwardProbabilityPO> awardInfos = Lists.newArrayList();
        //抽奖结果总计
        List<AwardTotalInfo> awardTotals = Lists.newArrayList();

        //设置redis保存的key
        String key = "ten:lottery";
        //中奖奖品
        List<AwardProbabilityPO> awardList;
        //中奖概率
        List<Double> probabilityList;
        //学币数量
        Integer coinAmount = 0;
        //积分数量
        Integer integralAmount = 0;

        for (int i = 0; i < 10; i++) {
            //获取一等奖的领取情况
            String firstPrizeStatus = this.getJedisProxy().get(key);
            if(i == 0){
                awardList = awardMap.get(AwardKeyEnum.firstAward.getKey());
                probabilityList = probabilityMap.get(AwardKeyEnum.firstProbability.getKey());
            }else{
                if(StringUtils.isNotEmpty(firstPrizeStatus)){
                    //一等奖已经被领取， 获取没有一等奖的奖品和中奖池
                    awardList = awardMap.get(AwardKeyEnum.manyUnFirstAward.getKey());
                    probabilityList = probabilityMap.get(AwardKeyEnum.manyUnFirstProbability.getKey());
                }else{
                    //一等奖没有被领取， 获取有一等奖的奖品和中奖池
                    awardList = awardMap.get(AwardKeyEnum.manyFirstAward.getKey());
                    probabilityList = probabilityMap.get(AwardKeyEnum.manyFirstProbability.getKey());
                }
            }

            //抽奖并校验一等奖的获取情况
            AwardProbabilityPO integralAward = this.checkfirstPrize(key, awardList, probabilityList);
            integralAward.setIconUrl(MessageFormat.format(QiniuUtil.getQiniuUrlPattern(), QiniuUtil.DEFAULT_BUCKET, "integral/" + integralAward.getAwardIcon() + ".png"));

            //奖品信息详情
            awardInfos.add(integralAward);
            //奖品类型 1:学币 2:积分
            if(PrizeTypeEnum.coin.getIndex().equals(integralAward.getPrizeType())){
                coinAmount = coinAmount + integralAward.getPrizeAmount();
            }else if(PrizeTypeEnum.integral.getIndex().equals(integralAward.getPrizeType())){
                integralAmount = integralAmount + integralAward.getPrizeAmount();
            }
        }
        //抽奖结果总计
        if(coinAmount > 0){
            String iconUrl = MessageFormat.format(QiniuUtil.getQiniuUrlPattern(), QiniuUtil.DEFAULT_BUCKET, "integral/" + PrizeTypeEnum.getUname(PrizeTypeEnum.coin.getIndex()) + ".png");
            awardTotals.add(new AwardTotalInfo(coinAmount, PrizeTypeEnum.coin.getIndex(), iconUrl));
        }
        if(integralAmount > 0){
            String iconUrl = MessageFormat.format(QiniuUtil.getQiniuUrlPattern(), QiniuUtil.DEFAULT_BUCKET, "integral/" + PrizeTypeEnum.getUname(PrizeTypeEnum.integral.getIndex()) + ".png");
            awardTotals.add(new AwardTotalInfo(integralAmount, PrizeTypeEnum.integral.getIndex(), iconUrl));
        }

        //step1 更新用户积分 减去当前抽奖消耗的积分
        userRecordService.updateUserIntegral(userId, taskIntegralInfo.getIntegral(), OperateTypeEnum.SUBTRACT.getIndex());

        //step2 添加用户积分操作记录
        userRecordService.insertIntegralRecord(userId, OperateTypeEnum.SUBTRACT.getIndex(), taskIntegralInfo, taskIntegralInfo.getIntegral());

        //step3 中奖结果处理
        //奖品类型 1:学币 2:积分
        Map<Integer, AwardTotalInfo> prizeMap = awardTotals.stream().collect(Collectors.toMap(AwardTotalInfo::getPrizeType, Function.identity()));
        //如果中奖的有积分，则：新增用户的积分数和用户的积分新增记录
        AwardTotalInfo integralInfo = prizeMap.get(PrizeTypeEnum.integral.getIndex());
        if(Objects.nonNull(integralInfo)){
            //step1 更新用户积分
            userRecordService.updateUserIntegral(userId, integralInfo.getPrizeAmount(), OperateTypeEnum.ADD.getIndex());
            //step2 添加用户中奖的积分操作记录
            userRecordService.insertIntegralRecord(userId, OperateTypeEnum.ADD.getIndex(), taskIntegralInfo, integralInfo.getPrizeAmount());
        }
        //如果中奖的是学币，则：给用户充值学币
        AwardTotalInfo coinInfo = prizeMap.get(PrizeTypeEnum.coin.getIndex());
        if(Objects.nonNull(coinInfo)){
            //如果中奖的是学币，则：给用户充值学币
            Integer prizeAmount = coinInfo.getPrizeAmount();
            String taskName = taskIntegralInfo.getTaskName();
            String payId = userRecordService.insertPayCoinRecord(userId, prizeAmount, taskName);
            //学币转账
            remoteCallService.transfer("integral_lottery", payId, String.valueOf(userId), taskName + "中奖学币", taskName + "中奖学币",
                    BaseConstants.PLAT_ACCOUNTID, sourceTypeEnum.platform.getIndex(), String.valueOf(userId), sourceTypeEnum.user.getIndex(), taskName + "中奖学币", prizeAmount.longValue());

        }

        //step4 保存抽奖记录
        UserLotteryRecordPO userLotteryRecord = new UserLotteryRecordPO();
        userLotteryRecord.setUserId(userId);
        //抽奖类型 1:积分抽奖
        userLotteryRecord.setType(1);
        //抽奖方式 1:单次抽奖 2:十连抽
        userLotteryRecord.setMode(2);
        userLotteryRecord.setTaskId(taskIntegralInfo.getId());
        userLotteryRecord.setTaskName(taskIntegralInfo.getTaskName());
        userLotteryRecord.setIntegral(taskIntegralInfo.getIntegral());

        //提示
        String hint = "";
        if(CollectionUtils.isEmpty(awardTotals)){
            hint = BaseConstants.content2;

            //抽奖结果 1:已中奖 0:未中奖
            userLotteryRecord.setStatus(0);
            result.setPrize(Boolean.FALSE);
        }else{
            StringBuffer buffer = new StringBuffer();
            for (AwardTotalInfo award : awardTotals) {
                if(buffer.length() > 0){
                    buffer = buffer.append(",").append(award.getPrizeAmount()).append(PrizeTypeEnum.getValue(award.getPrizeType()));
                }else{
                    buffer = buffer.append(award.getPrizeAmount()).append(PrizeTypeEnum.getValue(award.getPrizeType()));
                }
            }
            //提示
            hint = MessageFormat.format(BaseConstants.content1, buffer.toString());

            //抽奖结果 1:已中奖 0:未中奖
            userLotteryRecord.setStatus(1);
            result.setPrize(Boolean.TRUE);
        }

        Map<String, List<AwardProbabilityPO>> awardMap = awardInfos.stream().collect(Collectors.groupingBy(AwardProbabilityPO::getId));

        List<LotteryDetail> lotteryDetails = Lists.newArrayList();
        for (Map.Entry<String, List<AwardProbabilityPO>> entry : awardMap.entrySet()){
            List<AwardProbabilityPO> awards = entry.getValue();
            if(!CollectionUtils.isEmpty(awards)){
                LotteryDetail lotteryDetail = BeanUtil.copy(awards.get(0), LotteryDetail.class);
                lotteryDetail.setAmount(awards.size());
                lotteryDetails.add(lotteryDetail);
            }
        }

        //抽奖结果明细
        userLotteryRecord.setLotteryDetails(lotteryDetails);
        //抽奖结果总计
        userLotteryRecord.setAwardTotal(awardTotals);
        //step4 保存抽奖记录
        userRecordService.inserLotteryRecord(userId, userLotteryRecord);

        result.setHint(hint);
        result.setAwardInfo(lotteryDetails);
        return result;
    }

    /**
     * 抽奖
     * @param awardList
     * @param sortAwardProbabilityList
     * @return
     */
    public AwardProbabilityPO lottery(List<AwardProbabilityPO> awardList, List<Double> sortAwardProbabilityList){
        log.info("---抽奖开始--");
        if(CollectionUtils.isEmpty(awardList) || CollectionUtils.isEmpty(sortAwardProbabilityList)){
            log.info("奖品及中奖概率列表为空");
            throw new ServiceException("奖品及中奖概率列表为空");
        }
        List<Double> temp = new ArrayList<>(sortAwardProbabilityList);
        //产生0-1之间的随机数
        //随机数在哪个概率区间内，则是哪个奖品中奖
        double randomDouble = Math.random();
        //加入到概率区间中，排序后，返回的下标则是awardList中中奖的下标
        temp.add(randomDouble);
        //排序
        Collections.sort(temp);
        //随机数所在位置
        int lotteryIndex = temp.indexOf(randomDouble);
        log.info("---抽奖开始--lotteryIndex:{}", lotteryIndex);
        return awardList.get(lotteryIndex);
    }


    @Override
    public PageVO<UserLotteryRecordVo> getUserLotteryRecord(Integer userId, Integer pageIndex, Integer pageSize) {
        log.info("---getUserLotteryRecord userId:{}--", userId);
        PageVO<UserLotteryRecordPO> userLotteryRecordPOPageVO = userLotteryRecordDao.queryUserLotteryRecord(userId, pageIndex, pageSize);

        List<UserLotteryRecordPO> pageData = userLotteryRecordPOPageVO.getPageData();
        if(CollectionUtils.isEmpty(pageData)){
            return new PageVO<UserLotteryRecordVo>();
        }
        List<UserLotteryRecordVo> recordVo = BeanUtil.copyList(pageData, UserLotteryRecordVo.class);

        PageVO<UserLotteryRecordVo> page = new PageVO<>();
        page.setPageData(recordVo);
        page.setPageIndex(userLotteryRecordPOPageVO.getPageIndex());
        page.setPageSize(userLotteryRecordPOPageVO.getPageSize());
        page.setTotal(userLotteryRecordPOPageVO.getTotal());
        return page;
    }

    @Override
    public List<String> getLotteryRecord(Integer pageSize) {
        List<String> resultList = new ArrayList<>(pageSize);
        List<UserLotteryRecordPO> lotteryRecords = userLotteryRecordDao.queryLotteryRecord(pageSize);
        for (UserLotteryRecordPO record : lotteryRecords) {
            List<AwardTotalInfo> awardTotal = record.getAwardTotal();
            StringBuffer buffer = new StringBuffer();
            if(!CollectionUtils.isEmpty(awardTotal)){
                for (AwardTotalInfo award : awardTotal) {
                    if(PrizeTypeEnum.coin.getIndex().equals(award.getPrizeType())){
                        buffer = buffer.append(award.getPrizeAmount()).append(PrizeTypeEnum.getValue(award.getPrizeType()));
                    }
                }
            }
            //提示
            String hint = MessageFormat.format(BaseConstants.content, record.getName(), buffer.toString());
            resultList.add(hint);
        }
        return resultList;
    }

}
