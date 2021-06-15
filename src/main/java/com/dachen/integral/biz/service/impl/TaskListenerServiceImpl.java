package com.dachen.integral.biz.service.impl;

import com.dachen.integral.biz.dao.IntegralTaskDao;
import com.dachen.integral.biz.dao.UserTaskRecordDao;
import com.dachen.integral.biz.service.remote.RemoteCallService;
import com.dachen.integral.data.enums.TaskParentNoEnum;
import com.dachen.integral.data.po.IntegralTaskPO;
import com.dachen.integral.data.po.UserTaskRecordPO;
import com.dachen.integral.data.vo.UserInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

/**
 * @Author: wangyongbin
 * @Date: 2021/5/9 13:32
 * @Description:
 */
@Service
@Slf4j
public class TaskListenerServiceImpl {

    @Autowired
    private IntegralTaskDao integralTaskDao;
    @Autowired
    private UserTaskRecordDao userTaskRecordDao;
    @Autowired
    private RemoteCallService remoteCallService;


    /**
     * 任务完成操作
     * @param userId
     * @param bizType
     * @param bizId
     */
    public void completeTask(Integer userId, String bizId, String bizType, String bizName){
        log.info("--completeTask userId:{}, bizType:{}, bizId:{}, bizName:{}", userId, bizType, bizId, bizName);
        //查询当前任务积分详情
        List<IntegralTaskPO> integralTaskList = integralTaskDao.queryTaskIntegral(bizType);
        if(!CollectionUtils.isEmpty(integralTaskList)){
            for (IntegralTaskPO integralTask : integralTaskList) {
                //系统当前日期
                String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
                String date = null;
                //查询当前任务详情，判断是否已完成
                UserTaskRecordPO userTaskRecordPO = new UserTaskRecordPO();
                if(TaskParentNoEnum.everydayTask.getIndex().equals(integralTask.getParentNo())){
                    //获取当天日期
                    date = currentDate;
                    //查询当前任务详情，判断是否已完成
                    userTaskRecordPO = userTaskRecordDao.queryTaskRecord(userId, date, integralTask.getId());
                }else {
                    if("read_article".equals(bizType)){
                        if("faq".equals(bizName)){
                            //查询当前任务详情，判断是否已完成
                            userTaskRecordPO = userTaskRecordDao.queryTaskRecord(userId, date, integralTask.getId());
                        }
                    }else{
                        //查询当前任务详情，判断是否已完成
                        userTaskRecordPO = userTaskRecordDao.queryTaskRecord(userId, date, integralTask.getId());
                    }
                }

                //用户完成记录为空则说明该任务还未完成
                if(Objects.isNull(userTaskRecordPO)){
                    //获取医生详情信息
                    UserInfoVO doctorInfo = remoteCallService.getDoctorInfo(userId);
                    //step1 添加任务操作记录
                    userTaskRecordDao.insertUserTaskRecord(userId, bizId, bizName, currentDate, integralTask, doctorInfo);
                }
            }
        }
    }
}
