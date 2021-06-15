package com.dachen.integral.task;

import com.dachen.commons.micro.elasticJob.annotation.ElasticSimpleJobConfig;
import com.dachen.integral.biz.dao.IntegralTaskDao;
import com.dachen.integral.data.enums.TaskParentNoEnum;
import com.dachen.integral.data.enums.TaskStatusEnum;
import com.dachen.integral.data.po.IntegralTaskPO;
import com.dachen.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * @Author: wangyongbin
 * @Date: 2021/4/22 16:33
 * @Description:
 */
@Slf4j
@Component
public class ElasticTaskService {

    @Autowired
    private IntegralTaskDao integralTaskDao;

    /**
     * 每天0点过1秒执行
     */
//    @ElasticSimpleJobConfig(cron = "1 0 0 * * ? ")
//    @ElasticSimpleJobConfig(cron = "0 0/2 * * * ? ")
    /*public void updateTaskStatus(){
        //查询每日任务
        List<IntegralTaskPO> taskList = integralTaskDao.findTaskList(TaskParentNoEnum.everydayTask.getIndex(), TaskStatusEnum.completed.getIndex());
        if(!CollectionUtils.isEmpty(taskList)){
            for (IntegralTaskPO task : taskList) {
                Integer status = task.getStatus();
                //每日任务是已完成的，则每天需要更新状态为未完成， 按钮名称同步修改
                if(TaskStatusEnum.completed.getIndex().equals(status)){
                    List<IntegralTaskPO.ButtonNameOpsLog> nameOpsLog = task.getNameOpsLog();
                    String preButtonName = "";
                    if(!CollectionUtils.isEmpty(nameOpsLog)){
                        IntegralTaskPO.ButtonNameOpsLog buttonNameOpsLog = nameOpsLog.get(0);
                        if(Objects.nonNull(buttonNameOpsLog)){
                            preButtonName = buttonNameOpsLog.getPreButtonName();
                        }
                    }

                    IntegralTaskPO taskPO = new IntegralTaskPO();
                    taskPO.setId(task.getId());
                    taskPO.setStatus(TaskStatusEnum.unfinished.getIndex());
                    taskPO.setButtonName(StringUtils.isNotEmpty(preButtonName) ? preButtonName : "去完成");
                    //修改积分任务状态
                    integralTaskDao.updateIntegralTask(taskPO);
                }
            }
        }
    }*/
}
