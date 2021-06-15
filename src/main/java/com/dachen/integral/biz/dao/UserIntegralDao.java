package com.dachen.integral.biz.dao;

import com.dachen.integral.data.po.UserIntegralPO;
import com.dachen.integral.data.po.UserSignPO;
import com.dachen.support.annotation.Model;
import com.dachen.support.mongo.dao.BaseDao;
import com.dachen.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.stereotype.Service;

/**
 * 用户积分数据
 * @Author: wangyongbin
 * @Date: 2021/4/25 17:50
 * @Description:
 */
@Service
@Slf4j
@Model(UserIntegralPO.class)
public class UserIntegralDao extends BaseDao {

    /**
     * 查询用户积分信息
     * @param userId
     * @return
     */
    public UserIntegralPO queryUserIntegral(Integer userId){
        Query<UserIntegralPO> query = dsForRW.createQuery(UserIntegralPO.class).filter("userId", userId);
        return query.get();
    }

    /**
     * 新增用户积分数据
     * @param param
     * @return
     */
    public String insertUserIntegral(UserIntegralPO param) {
        param.setCreateTime(System.currentTimeMillis());
        param.setUpdateTime(System.currentTimeMillis());
        Key<UserIntegralPO> insert = dsForRW.insert(param);
        return insert.getId().toString();
    }

    /**
     * 修改用户积分数据
     * @param param
     */
    public void updateUserIntegral(UserIntegralPO param){
        Query<UserIntegralPO> query = dsForRW.createQuery(UserIntegralPO.class).filter("_id", new ObjectId(param.getId()));
        UpdateOperations<UserIntegralPO> ops = dsForRW.createUpdateOperations(UserIntegralPO.class);
        ops.set("totalAmount", param.getTotalAmount());
        ops.set("currentIntegral", param.getCurrentIntegral());
        if(StringUtils.isNotEmpty(param.getLevel())){
            ops.set("level", param.getLevel());
        }
        if(StringUtils.isNotEmpty(param.getLevelName())){
            ops.set("levelName", param.getLevelName());
        }

        dsForRW.update(query, ops);
    }
}
