package com.dachen.integral.biz.dao;

import com.dachen.integral.data.constant.BaseConstants;
import com.dachen.integral.data.enums.TaskParentNoEnum;
import com.dachen.integral.data.enums.TaskStatusEnum;
import com.dachen.integral.data.po.IntegralTaskPO;
import com.dachen.integral.data.po.UserTaskRecordPO;
import com.dachen.integral.data.vo.UserInfoVO;
import com.dachen.support.annotation.Model;
import com.dachen.support.mongo.dao.BaseDao;
import com.dachen.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 用户任务完成操作记录
 * @Author: wangyongbin
 * @Date: 2021/5/8 10:58
 * @Description:
 */
@Service
@Slf4j
@Model(UserTaskRecordPO.class)
public class UserTaskRecordDao extends BaseDao {

    /**
     * 新增用户完成任务数据
     * @param userId
     * @param bizId
     * @param integralTask
     * @return
     */
    public String insertUserTaskRecord(Integer userId, String bizId, String bizName, String currentDate, IntegralTaskPO integralTask, UserInfoVO doctorInfo) {
        long currentTime = System.currentTimeMillis();
        UserTaskRecordPO param = new UserTaskRecordPO();
        if(Objects.nonNull(doctorInfo)){
            param.setName(doctorInfo.getName());
            param.setTelephone(doctorInfo.getTelephone());
            param.setHospitalName(doctorInfo.getHospitalName());
            param.setDepartments(doctorInfo.getDepartments());
            param.setTitle(doctorInfo.getTitle());
        }

        param.setTaskId(integralTask.getId());
        param.setParentTask(integralTask.getParentTask());
        param.setParentNo(integralTask.getParentNo());
        param.setTaskName(integralTask.getTaskName());
        param.setBizType(integralTask.getBizType());
        param.setCompleteDate(currentDate);
        param.setButtonName(BaseConstants.completeButtonName);
        param.setStatus(TaskStatusEnum.completed.getIndex());
        param.setUserId(userId);
        param.setBizId(bizId);
        param.setBizName(bizName);
        param.setCompleteTime(currentTime);
        param.setCreateTime(currentTime);
        param.setUpdateTime(currentTime);
        Key<UserTaskRecordPO> insert = dsForRW.insert(param);
        return insert.getId().toString();
    }

    /**
     * 查询用户每日任务的完成情况
     * @param userId
     * @param date
     * @return
     */
    public List<UserTaskRecordPO> queryDailyTask(Integer userId, String date){
        Query<UserTaskRecordPO> query = dsForRW.createQuery(UserTaskRecordPO.class);
        query.filter("userId", userId);
        query.filter("parentNo", TaskParentNoEnum.everydayTask.getIndex());
        query.filter("completeDate", date);
        return query.asList();
    }

    /**
     * 查询用户每日任务外其他任务的完成情况
     * 父级任务编号 1:每日任务 2:成长任务
     * @param userId
     * @return
     */
    public List<UserTaskRecordPO> queryOtherTask(Integer userId){
        Query<UserTaskRecordPO> query = dsForRW.createQuery(UserTaskRecordPO.class);
        query.filter("userId", userId);
        query.field("parentNo").notEqual(TaskParentNoEnum.everydayTask.getIndex());
        return query.asList();
    }

    /**
     * 查询用户任务的完成信息
     * @param userId
     * @param date
     * @param taskId
     * @return
     */
    public UserTaskRecordPO queryTaskRecord(Integer userId, String date, String taskId){
        Query<UserTaskRecordPO> query = dsForRW.createQuery(UserTaskRecordPO.class);
        query.filter("userId", userId);
        query.filter("taskId", taskId);
        if(StringUtils.isNotEmpty(date)){
            query.filter("completeDate", date);
        }
        return query.get();
    }

    /**
     * 查询任务的完成信息
     * @param userId
     * @param date
     * @param bizType
     * @return
     */
    public UserTaskRecordPO queryTaskRecordByType(Integer userId, String date, String bizType){
        Query<UserTaskRecordPO> query = dsForRW.createQuery(UserTaskRecordPO.class);
        query.filter("userId", userId);
        query.filter("bizType", bizType);
        if(StringUtils.isNotEmpty(date)){
            query.filter("completeDate", date);
        }
        return query.get();
    }

    /**
     * 修改用户任务状态为已领取
     * @param id
     * @param status
     */
    public void updateTaskStatus(String id, Integer status){
        Query<UserTaskRecordPO> query = dsForRW.createQuery(UserTaskRecordPO.class).filter("_id", new ObjectId(id));
        UpdateOperations<UserTaskRecordPO> ops = dsForRW.createUpdateOperations(UserTaskRecordPO.class);
        ops.set("status", status);
        ops.set("buttonName", BaseConstants.submitButtonName);
        ops.set("submitTime", System.currentTimeMillis());
        ops.set("updateTime", System.currentTimeMillis());
        dsForRW.update(query, ops);
    }

    /**
     * 用户每日任务的领取数量
     * @param userId
     * @param date
     * @param parentNo
     * @return
     */
    public Long queryReceivedCount(Integer userId, String date, Integer parentNo){
        Query<UserTaskRecordPO> query = dsForRW.createQuery(UserTaskRecordPO.class);
        query.filter("userId", userId);
        query.filter("parentNo", parentNo);
        query.filter("completeDate", date);
        query.filter("status", TaskStatusEnum.received.getIndex());
        return query.count();
    }
}
