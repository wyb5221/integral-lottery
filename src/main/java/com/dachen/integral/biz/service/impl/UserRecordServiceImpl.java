package com.dachen.integral.biz.service.impl;

import com.dachen.integral.biz.dao.*;
import com.dachen.integral.biz.service.remote.RemoteCallService;
import com.dachen.integral.data.constant.BaseConstants;
import com.dachen.integral.data.enums.OperateTypeEnum;
import com.dachen.integral.data.enums.PrizeTypeEnum;
import com.dachen.integral.data.enums.sourceTypeEnum;
import com.dachen.integral.data.po.*;
import com.dachen.integral.data.vo.ExportInfo;
import com.dachen.integral.data.vo.ExportLotteryVO;
import com.dachen.integral.data.vo.UserInfoVO;
import com.dachen.util.BeanUtil;
import com.dachen.util.MapUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: wangyongbin
 * @Date: 2021/4/29 18:52
 * @Description:
 */
@Service
@Slf4j
public class UserRecordServiceImpl {

    @Autowired
    private IntegralRecordDao integralRecordDao;
    @Autowired
    private UserIntegralDao userIntegralDao;
    @Autowired
    private IntegralLevelDao integralLevelDao;
    @Autowired
    private UserLotteryRecordDao userLotteryRecordDao;
    @Autowired
    private RemoteCallService remoteCallService;
    @Autowired
    private UserPayCoinRecordDao userPayCoinRecordDao;


    /**
     * 更新用户积分信息
     * @param userId
     * @param integral 操作的积分数
     * @param operateType 操作类型 1:新增 -1:减少
     */
    public void updateUserIntegral(Integer userId, Integer integral, Integer operateType){
        log.info("--updateUserIntegral userId:{},integral:{},operateType:{}", userId, integral, operateType);
        //总积分
        Integer totalAmount = 0;
        //当前积分
        Integer currentIntegral = 0;
        UserIntegralPO userParam = new UserIntegralPO();
        //查询用户积分信息
        UserIntegralPO userIntegralPO = userIntegralDao.queryUserIntegral(userId);
        if(Objects.nonNull(userIntegralPO)){
            userParam.setId(userIntegralPO.getId());
            if(OperateTypeEnum.ADD.getIndex().equals(operateType)){
                //总积分
                totalAmount = userIntegralPO.getTotalAmount() + integral;
                //当前积分
                currentIntegral = userIntegralPO.getCurrentIntegral() + integral;
            }else if(OperateTypeEnum.SUBTRACT.getIndex().equals(operateType)){
                //总积分
                totalAmount = userIntegralPO.getTotalAmount();
                //当前积分
                currentIntegral = userIntegralPO.getCurrentIntegral() - integral;
            }
        }else{
            userParam.setCreateTime(System.currentTimeMillis());
            if(OperateTypeEnum.ADD.getIndex().equals(operateType)){
                //总积分
                totalAmount = integral;
                //当前积分
                currentIntegral = integral;
            }
        }

        //根据积分数查询当前积分等级信息
        IntegralLevelPO levelInfo = integralLevelDao.querylevel(totalAmount);

        userParam.setUserId(userId);
        userParam.setLevel(levelInfo.getLevel());
        userParam.setLevelName(levelInfo.getLevelName());
        userParam.setTotalAmount(totalAmount);
        userParam.setCurrentIntegral(currentIntegral);
        userParam.setUpdateTime(System.currentTimeMillis());

        userIntegralDao.saveOrUpdate(userParam);
    }

    /**
     * 我的积分操作记录
     * @param userId
     * @param taskPO 当前任务属性
     * @param operateType 操作类型 1:新增 -1:减少
     * @param integral 积分数量
     */
    public void insertIntegralRecord(Integer userId, Integer operateType, IntegralTaskPO taskPO, Integer integral){
        log.info("--insertIntegralRecord userId:{},taskPO:{},operateType:{}", userId, taskPO, operateType);
        IntegralRecordPO recordPO = new IntegralRecordPO();
        recordPO.setUserId(userId);
        recordPO.setOperateType(operateType);
        recordPO.setBizType(taskPO.getBizType());
        recordPO.setBizName(taskPO.getTaskName());
        recordPO.setIntegral(integral);
        recordPO.setTaskId(taskPO.getId());
        recordPO.setCreateTime(System.currentTimeMillis());
        recordPO.setUpdateTime(System.currentTimeMillis());

        //获取医生详情信息
        UserInfoVO doctorInfo = remoteCallService.getDoctorInfo(userId);

        if(Objects.nonNull(doctorInfo)){
            recordPO.setName(doctorInfo.getName());
            recordPO.setTelephone(doctorInfo.getTelephone());
            recordPO.setHospitalName(doctorInfo.getHospitalName());
            recordPO.setDepartments(doctorInfo.getDepartments());
            recordPO.setTitle(doctorInfo.getTitle());
        }

        integralRecordDao.insertIntegralRecord(recordPO);
    }


    /**
     * 保存抽奖记录
     * @param userId
     * @param userLotteryRecord
     */
    public void inserLotteryRecord(Integer userId, UserLotteryRecordPO userLotteryRecord){
        log.info("--inserLotteryRecord userId:{}, userLotteryRecord:{}", userId, userLotteryRecord);

        //获取医生详情信息
        UserInfoVO doctorInfo = remoteCallService.getDoctorInfo(userId);

        if(Objects.nonNull(doctorInfo)){
            userLotteryRecord.setName(doctorInfo.getName());
            userLotteryRecord.setTelephone(doctorInfo.getTelephone());
            userLotteryRecord.setHospitalName(doctorInfo.getHospitalName());
            userLotteryRecord.setDepartments(doctorInfo.getDepartments());
            userLotteryRecord.setTitle(doctorInfo.getTitle());
        }

        userLotteryRecord.setCreateTime(System.currentTimeMillis());
        userLotteryRecord.setUpdateTime(System.currentTimeMillis());

        userLotteryRecordDao.insertLotteryRecord(userLotteryRecord);
    }

    /**
     * 新增用户转账学币记录
     * @param userId
     * @param value
     * @return
     */
    public String insertPayCoinRecord(Integer userId, Integer value, String taskName){
        String id = userPayCoinRecordDao.insertUserSign("integral_lottery", String.valueOf(userId), taskName + "中奖学币", taskName + "中奖学币",
                BaseConstants.PLAT_ACCOUNTID, sourceTypeEnum.platform.getIndex(), String.valueOf(userId), sourceTypeEnum.user.getIndex(), taskName + "中奖学币", value.longValue());
        return id;
    }

    /**
     * 查询导出的积分操作明细
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> exportIntegralInfo() throws Exception {
        List<IntegralRecordPO> integralRecords = integralRecordDao.queryIntegralRecord();
        List<Map<String, Object>> listmap = new ArrayList<>();
        for (IntegralRecordPO obj : integralRecords) {
            ExportInfo exportInfo = BeanUtil.copy(obj, ExportInfo.class);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String sd = sdf.format(new Date(obj.getCreateTime()));
            exportInfo.setCreateTime(sd);
            exportInfo.setOperateType(OperateTypeEnum.getValue(obj.getOperateType()));
            listmap.add(MapUtil.beanToMap(exportInfo));
        }
        return listmap;
    }

    /**
     * 查询导出的中奖明细
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> exportLotteryInfo() throws Exception {
        //查询所有中奖记录
        List<UserLotteryRecordPO> userLotteryList = userLotteryRecordDao.queryLotteryRecordList();
        List<Map<String, Object>> listmap = new ArrayList<>();
        for (UserLotteryRecordPO lotteryRecord : userLotteryList) {
            ExportLotteryVO exportLottery = BeanUtil.copy(lotteryRecord, ExportLotteryVO.class);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String sd = sdf.format(new Date(lotteryRecord.getCreateTime()));
            exportLottery.setCreateTime(sd);
            List<AwardTotalInfo> awardTotals = lotteryRecord.getAwardTotal();
            for (AwardTotalInfo total : awardTotals) {
                //奖品类型 1:学币 2:积分
                Integer prizeType = total.getPrizeType();
                if(PrizeTypeEnum.coin.getIndex().equals(prizeType)){
                    exportLottery.setCoinCount(total.getPrizeAmount());
                }else if(PrizeTypeEnum.integral.getIndex().equals(prizeType)){
                    exportLottery.setIntegralCount(total.getPrizeAmount());
                }
            }

            listmap.add(MapUtil.beanToMap(exportLottery));
        }
        return listmap;
    }

}
