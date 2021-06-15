package com.dachen.integral.biz.dao;

import com.dachen.integral.data.enums.DeleteFlagEnum;
import com.dachen.integral.data.po.IntegralTaskPO;
import com.dachen.support.annotation.Model;
import com.dachen.support.mongo.dao.BaseDao;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 积分任务
 * @Author: wangyongbin
 * @Date: 2021/4/22 10:10
 * @Description:
 */
@Service
@Slf4j
@Model(IntegralTaskPO.class)
public class IntegralTaskDao extends BaseDao {

    /**
     * 查询任务集合
     * @param parentNo 父级任务编号
     * @param status
     * @return
     */
    public List<IntegralTaskPO> findTaskList(Integer parentNo, Integer status){
        Query<IntegralTaskPO> query = dsForRW.createQuery(IntegralTaskPO.class).filter("deleteFlag", DeleteFlagEnum.NORMAL.getIndex());
        if(null != parentNo){
            query.filter("parentNo", parentNo);
        }
        if(null != status){
            query.filter("status", status);
        }
        query.order("rank");
        return query.asList();
    }

    /**
     * 查询当前任务积分
     * @param bizType 任务类型
     * @return
     */
    public List<IntegralTaskPO> queryTaskIntegral(String bizType){
        Query<IntegralTaskPO> query = dsForRW.createQuery(IntegralTaskPO.class).filter("bizType", bizType);
        query.filter("deleteFlag", DeleteFlagEnum.NORMAL.getIndex());
        return query.asList();
    }

    /**
     * 查询当前任务积分
     * @param id
     * @return
     */
    public IntegralTaskPO getTaskIntegralInfo(String id){
        Query<IntegralTaskPO> query = dsForRW.createQuery(IntegralTaskPO.class).filter("_id", new ObjectId(id));
        query.filter("deleteFlag", DeleteFlagEnum.NORMAL.getIndex());
        return query.get();
    }

    /**
     * 修改积分任务状态
     * @param param
     */
//    public void updateIntegralTask(IntegralTaskPO param){
//        Query<IntegralTaskPO> query = dsForRW.createQuery(IntegralTaskPO.class).filter("_id", new ObjectId(param.getId()));
//        UpdateOperations<IntegralTaskPO> ops = dsForRW.createUpdateOperations(IntegralTaskPO.class);
//        if(null != param.getStatus()){
//            ops.set("status", param.getStatus());
//        }
//        if(StringUtils.isNotEmpty(param.getButtonName())){
//            ops.set("buttonName", param.getButtonName());
//        }
//        if(null != param.getCompleteTime()){
//            ops.set("completeTime", param.getCompleteTime());
//        }
//        if(null != param.getSubmitTime()){
//            ops.set("submitTime", param.getSubmitTime());
//        }
//        if(!CollectionUtils.isEmpty(param.getNameOpsLog())){
//            ops.set("nameOpsLog", param.getNameOpsLog());
//        }
//        ops.set("updateTime", System.currentTimeMillis());
//
//        dsForRW.update(query, ops);
//    }

}
