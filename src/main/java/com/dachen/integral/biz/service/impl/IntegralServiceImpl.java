package com.dachen.integral.biz.service.impl;

import com.dachen.common.exception.ServiceException;
import com.dachen.integral.biz.dao.*;
import com.dachen.integral.biz.service.IntegralService;
import com.dachen.integral.data.enums.OperateTypeEnum;
import com.dachen.integral.data.enums.SignStatusEnum;
import com.dachen.integral.data.enums.TaskParentNoEnum;
import com.dachen.integral.data.enums.TaskStatusEnum;
import com.dachen.integral.data.po.*;
import com.dachen.integral.data.vo.*;
import com.dachen.util.BeanUtil;
import com.dachen.util.QiniuUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: wangyongbin
 * @Date: 2021/4/22 15:11
 * @Description:
 */
@Service
@Slf4j
public class IntegralServiceImpl implements IntegralService {
    @Autowired
    private IntegralTaskDao integralTaskDao;
    @Autowired
    private UserSignDao userSignDao;
    @Autowired
    private IntegralRecordDao integralRecordDao;
    @Autowired
    private UserIntegralDao userIntegralDao;
    @Autowired
    private UserRecordServiceImpl userRecordService;
    @Autowired
    private UserTaskRecordDao userTaskRecordDao;


    @Override
    public IntegralTaskVo queryTaskInfo(Integer userId) {
        log.info("--queryTaskInfo查询积分任务详情--userId:{}", userId);
        IntegralTaskVo result = new IntegralTaskVo();
        //查询任务集合
        List<IntegralTaskPO> taskList = integralTaskDao.findTaskList(null, null);
        taskList.forEach(task -> {
            task.setImgUrl(MessageFormat.format(QiniuUtil.getQiniuUrlPattern(), QiniuUtil.DEFAULT_BUCKET, "integral/" + task.getTaskIcon() + ".png"));
        });

        //获取当天日期
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        //查询用户每日任务的完成情况
        List<UserTaskRecordPO> everydayTaskList = userTaskRecordDao.queryDailyTask(userId, currentDate);
        Map<String, UserTaskRecordPO> everydayMap = everydayTaskList.stream().collect(Collectors.toMap(obj -> obj.getTaskId(), a -> a, (k1, k2) -> k1));

        //查询用户除每日任务外其他任务的完成情况
        List<UserTaskRecordPO> otherTaskList = userTaskRecordDao.queryOtherTask(userId);
        Map<String, UserTaskRecordPO> otherMap = otherTaskList.stream().collect(Collectors.toMap(obj -> obj.getTaskId(), a -> a, (k1, k2) -> k1));

        //根据父级编号分组
        Map<Integer, List<IntegralTaskPO>> taskMap = taskList.stream().collect(Collectors.groupingBy(IntegralTaskPO::getParentNo));
        //父级任务编号 1:每日任务 2:成长任务
        //每日任务
        IntegralTaskResult everydayTask = new IntegralTaskResult();
        List<IntegralTaskPO> everyList = taskMap.get(TaskParentNoEnum.everydayTask.getIndex());
        if(!CollectionUtils.isEmpty(everyList)){
            everyList.stream().sorted(Comparator.comparing(IntegralTaskPO:: getRank)).collect(Collectors.toList());
            for (IntegralTaskPO everyTask : everyList) {
                UserTaskRecordPO everyTaskRecord = everydayMap.get(everyTask.getId());
                if(Objects.nonNull(everyTaskRecord)){
                    everyTask.setStatus(everyTaskRecord.getStatus());
                    everyTask.setButtonName(everyTaskRecord.getButtonName());
                }
            }

            everydayTask.setTaskList(everyList);
            //处理任务数据
            this.manageTaskInfo(everydayTask, everyList);
        }

        //成长任务
        IntegralTaskResult growTask = new IntegralTaskResult();
        List<IntegralTaskPO> growList = taskMap.get(TaskParentNoEnum.growTask.getIndex());
        if(!CollectionUtils.isEmpty(growList)){
            growList.stream().sorted(Comparator.comparing(IntegralTaskPO:: getRank)).collect(Collectors.toList());

            for (IntegralTaskPO otherTask : growList) {
                UserTaskRecordPO otherTaskRecord = otherMap.get(otherTask.getId());
                if(Objects.nonNull(otherTaskRecord)){
                    otherTask.setStatus(otherTaskRecord.getStatus());
                    otherTask.setButtonName(otherTaskRecord.getButtonName());
                }
            }

            growTask.setTaskList(growList);
            //处理任务数据
            this.manageTaskInfo(growTask, growList);
        }
        //查询用户积分信息
        UserIntegralPO userIntegralPO = userIntegralDao.queryUserIntegral(userId);
        //当前积分
        Integer currentIntegral = 0;
        if(Objects.nonNull(userIntegralPO)){
            currentIntegral = userIntegralPO.getCurrentIntegral();
        }
        result.setCurrentIntegral(currentIntegral);
        result.setEverydayTask(everydayTask);
        result.setGrowTask(growTask);
        return result;
    }

    /**
     * 处理任务数据
     * @param taskResult
     * @param taskList
     */
    public void manageTaskInfo(IntegralTaskResult taskResult, List<IntegralTaskPO> taskList){
        //已完成数据
        int completedAmount = 0;
        //未完成数据
        int unAmount = 0;
        //未完成的积分数
        int unIntegralAmount = 0;
        for (IntegralTaskPO obj : taskList) {
            //状态 0:未完成 1:已完成
            Integer status = obj.getStatus();
            if(TaskStatusEnum.unfinished.getIndex().equals(status)){
                unAmount++;
                unIntegralAmount += null != obj.getIntegral() ? obj.getIntegral() : 0;
            }else{
                completedAmount++;
            }
        }
        taskResult.setCompletedAmount(completedAmount);
        taskResult.setUnAmount(unAmount);
        taskResult.setUnIntegralAmount(unIntegralAmount);
    }

    @Override
    public SignResultVo sign(Integer userId) {
        log.info("--sign签到 userId:{}", userId);
        //签到天数记录
        List<String> signDates = Lists.newArrayList();
        List<Signinfo> signList = Lists.newArrayList();
        //连续签到数
        int continueSign = 1;
        //总签到数
        int count = 1;
        //获取当天日期
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        //获取当前年
        int currentYear = LocalDate.now().getYear();

        //1 查询用户的签到记录
        UserSignPO userSignPO = userSignDao.queryUserSignInfo(userId);
        if(Objects.nonNull(userSignPO)){
            //最新签到日期
            String latestSignDate = userSignPO.getLatestSignDate();
            //连续签到数
            continueSign = userSignPO.getContinueSign();
            //总签到数
            count = userSignPO.getCount();
            count = count + 1;
            if(currentDate.equals(latestSignDate)){
                throw new ServiceException("当天已经签到，不能重复签到");
            }

            LocalDate localDate = LocalDate.now();
            //昨天
            String yesterday = localDate.plusDays(-1).format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
            //最新签到日期是昨天，则连续签到天数加1
            if(yesterday.equals(latestSignDate)){
                continueSign = continueSign + 1;
            }else{
                continueSign = 1;
            }
            signList = userSignPO.getSignList();
            for (Signinfo signinfo : signList) {
                Integer signYear = signinfo.getYear();
                if(currentYear == signYear){
                    signDates = signinfo.getSignDates();
                    //添加当天签到的数据记录
                    if(CollectionUtils.isEmpty(signDates)){
                        signDates = Lists.newArrayList();
                    }
                    signDates.add(currentDate);
                    signinfo.setSignDates(signDates);
                }
            }

            //修改签到记录
            userSignPO.setContinueSign(continueSign);
            userSignPO.setCount(count);
            userSignPO.setLatestSignDate(currentDate);
            userSignPO.setSignList(signList);
            userSignDao.updateUserSign(userSignPO);
        }else{
            if(CollectionUtils.isEmpty(signDates)){
                signDates = Lists.newArrayList();
            }
            signDates.add(currentDate);
            //签到记录对象
            Signinfo signinfo = new Signinfo();
            signinfo.setYear(currentYear);
            signinfo.setSignDates(signDates);

            signList.add(signinfo);

            //新增签到记录
            UserSignPO userSign = new UserSignPO();
            userSign.setUserId(userId);
            userSign.setContinueSign(continueSign);
            userSign.setCount(count);
            userSign.setLatestSignDate(currentDate);
            userSign.setSignList(signList);
            String signId = userSignDao.insertUserSign(userSign);
        }

        //查询当前任务的积分情况
        List<IntegralTaskPO> taskIntegrals = integralTaskDao.queryTaskIntegral("sign");
        if(CollectionUtils.isEmpty(taskIntegrals)){
            throw new ServiceException("当前任务不存在");
        }
        IntegralTaskPO taskIntegralInfo = taskIntegrals.get(0);
        //step1 更新用户积分
        userRecordService.updateUserIntegral(userId, taskIntegralInfo.getIntegral(), OperateTypeEnum.ADD.getIndex());
        //step2 添加用户积分操作记录
        userRecordService.insertIntegralRecord(userId, OperateTypeEnum.ADD.getIndex(), taskIntegralInfo, taskIntegralInfo.getIntegral());


        SignResultVo result = new SignResultVo();
        result.setContinueSign(continueSign);
        result.setIntegral(taskIntegralInfo.getIntegral());
        return result;
    }

    @Override
    public SignRecordVo querySignRecord(Integer userId) {
        log.info("签到记录查询querySignRecord.userId:{}", userId);
        SignRecordVo recordVo = new SignRecordVo();

        //当前时间
        LocalDate today = LocalDate.now();
        //当前年
        int year = today.getYear();
        //签到天数记录
        List<String> signDates = Lists.newArrayList();
        //查询用户的签到记录
        UserSignPO userSignPO = userSignDao.queryUserSignInfo(userId);
        //连续签到数
        Integer continueSign = 0;
        if(Objects.nonNull(userSignPO)){
            //最新签到日期
            String latestSignDate = userSignPO.getLatestSignDate();
            //获取当天日期
            String currentDate = today.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
            LocalDate yesterday = today.plusDays(-1);
            //获取前一天的日期
            String yesterDate = yesterday.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
            if(latestSignDate.equals(currentDate) || latestSignDate.equals(yesterDate)){
                //连续签到数
                continueSign = userSignPO.getContinueSign();
            }

            List<Signinfo> signList = userSignPO.getSignList();
            for (Signinfo info : signList) {
                if(year == info.getYear()){
                    signDates = info.getSignDates();
                }
            }
        }
        recordVo.setContinueSign(continueSign);

        List<SignStatuInfo> signList = Lists.newArrayList();

        //当前时间的月.日
        String currDate = today.format(DateTimeFormatter.ofPattern("MM.dd"));
        //本周一的日期
        LocalDate monday = today.with(TemporalAdjusters.previousOrSame( DayOfWeek.MONDAY));
        for (int i = 0; i < 7; i++) {
            SignStatuInfo signStatuInfo = new SignStatuInfo();
            LocalDate localDate = monday.plusDays(i);
            //转年月日
            String ymdStr = localDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
            //转月日
            String mdStr = localDate.format(DateTimeFormatter.ofPattern("MM.dd"));
            signStatuInfo.setDate(mdStr);
            if(currDate.equals(mdStr)){
                signStatuInfo.setSame(Boolean.TRUE);
            }
            if(!CollectionUtils.isEmpty(signDates) && signDates.contains(ymdStr)){
                signStatuInfo.setSign(SignStatusEnum.SIGN.getIndex());
            }
            signList.add(signStatuInfo);
        }
        recordVo.setSignList(signList);
        return recordVo;
    }

    @Override
    public UserIntegralInfoVo queryCurrentIntegral(Integer userId) {
        log.info("查询当前积分queryCurrentIntegral userId:{}", userId);
        UserIntegralInfoVo result = new UserIntegralInfoVo();
        //查询用户积分信息
        UserIntegralPO userIntegralPO = userIntegralDao.queryUserIntegral(userId);
        //当前积分
        Integer currentIntegral = 0;
        String level = "level 0";
        String levelName = "小白";
        if(Objects.nonNull(userIntegralPO)){
            currentIntegral = null != userIntegralPO.getCurrentIntegral() ? userIntegralPO.getCurrentIntegral() : 0;
            level = null != userIntegralPO.getLevel() ? userIntegralPO.getLevel() : "level 0";
            levelName = null != userIntegralPO.getLevelName() ? userIntegralPO.getLevelName() : "小白";
        }

        //获取当天日期
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        //查询用户当天是否签到,true已签到
        boolean b = userSignDao.queryIsSign(userId, today);
        result.setCurrentIntegral(currentIntegral);
        result.setLevel(level);
        result.setLevelName(levelName);
        result.setIsSign(b);
        return result;
    }

    @Override
    public PageVO<IntegralRecordVo> queryUserIntegralRecord(Integer userId, Integer pageIndex, Integer pageSize) {
        log.info("查询积分记录queryUserIntegralRecord userId:{},pageIndex:{},pageSize:{}", userId, pageIndex, pageSize);
        PageVO<IntegralRecordPO> integralPage = integralRecordDao.queryIntegralRecord(userId, pageIndex, pageSize);

        List<IntegralRecordPO> integralList = integralPage.getPageData();
        if(CollectionUtils.isEmpty(integralList)){
            return new PageVO<IntegralRecordVo>();
        }
        List<IntegralRecordVo> recordVo = BeanUtil.copyList(integralList, IntegralRecordVo.class);

        PageVO<IntegralRecordVo> page = new PageVO<>();
        page.setPageData(recordVo);
        page.setPageIndex(integralPage.getPageIndex());
        page.setPageSize(integralPage.getPageSize());
        page.setTotal(integralPage.getTotal());
        return page;
    }

    @Override
    public GetIntegralVo getTaskIntegral(String taskId, Integer userId) {
        log.info("--getTaskIntegral.taskId:{}", taskId);
        GetIntegralVo result = new GetIntegralVo();

        //查询积分任务详情
        IntegralTaskPO taskIntegralInfo = integralTaskDao.getTaskIntegralInfo(taskId);
        if(Objects.isNull(taskIntegralInfo)){
            throw new ServiceException("当前任务不存在");
        }

        //系统当前日期
        String date = null;
        if(TaskParentNoEnum.everydayTask.getIndex().equals(taskIntegralInfo.getParentNo())){
            //获取当天日期
            date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));;
        }
        //查询用户任务的完成信息
        UserTaskRecordPO userTaskRecordPO = userTaskRecordDao.queryTaskRecord(userId, date, taskId);
        if(Objects.isNull(userTaskRecordPO) || !TaskStatusEnum.completed.getIndex().equals(userTaskRecordPO.getStatus())){
            throw new ServiceException("当前任务未完成，不能领取");
        }

        //step1 更新用户积分
        userRecordService.updateUserIntegral(userId, taskIntegralInfo.getIntegral(), OperateTypeEnum.ADD.getIndex());
        //step2 添加用户积分操作记录
        userRecordService.insertIntegralRecord(userId, OperateTypeEnum.ADD.getIndex(), taskIntegralInfo, taskIntegralInfo.getIntegral());
        //step3 修改积分任务状态
        userTaskRecordDao.updateTaskStatus(userTaskRecordPO.getId(), TaskStatusEnum.received.getIndex());

        Integer resultCount = taskIntegralInfo.getIntegral();

        //附加分数
        Integer appendIntegral = 0;
        //领取的任务是每日任务的才计算附加条件
        if(TaskParentNoEnum.everydayTask.getIndex().equals(userTaskRecordPO.getParentNo())){
            //用户每日任务的完成数量
            Long receivedCount = userTaskRecordDao.queryReceivedCount(userId, date, TaskParentNoEnum.everydayTask.getIndex());

            if(receivedCount == 1){
                appendIntegral = 10;
            }else if(receivedCount == 3){
                appendIntegral = 30;
            }else if(receivedCount == 5){
                appendIntegral = 50;
            }
            if(appendIntegral > 0){
                //step1 更新用户积分
                userRecordService.updateUserIntegral(userId, appendIntegral, OperateTypeEnum.ADD.getIndex());
                //step2 添加用户积分操作记录
                IntegralTaskPO taskPO = new IntegralTaskPO();
                taskPO.setTaskName("每日任务完成后附加加分");
                userRecordService.insertIntegralRecord(userId, OperateTypeEnum.ADD.getIndex(), taskPO, appendIntegral);
                resultCount = resultCount + appendIntegral;
            }
        }

        result.setTotalIntegral(resultCount);
        result.setAppendIntegral(appendIntegral);
        return result;
    }

}
