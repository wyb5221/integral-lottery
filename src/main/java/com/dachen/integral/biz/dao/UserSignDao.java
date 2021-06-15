package com.dachen.integral.biz.dao;

import com.dachen.integral.data.po.UserSignPO;
import com.dachen.support.annotation.Model;
import com.dachen.support.mongo.dao.BaseDao;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 用户签到记录
 * @Author: wangyongbin
 * @Date: 2021/4/25 10:55
 * @Description:
 */
@Service
@Slf4j
@Model(UserSignPO.class)
public class UserSignDao extends BaseDao {

    /**
     * 新增签到记录
     * @param param
     * @return
     */
    public String insertUserSign(UserSignPO param) {
        param.setCreateTime(System.currentTimeMillis());
        param.setUpdateTime(System.currentTimeMillis());
        Key<UserSignPO> insert = dsForRW.insert(param);
        return insert.getId().toString();
    }

    /**
     * 查询用户的签到记录
     * @param userId
     * @return
     */
    public UserSignPO queryUserSignInfo(Integer userId){
        Query<UserSignPO> query = dsForRW.createQuery(UserSignPO.class).filter("userId", userId);
        return query.get();
    }

    /**
     * 查询用户当天是否签到,true已签到
     * @param userId
     * @param today
     * @return
     */
    public boolean queryIsSign(Integer userId, String today){
        Query<UserSignPO> query = dsForRW.createQuery(UserSignPO.class).
                filter("userId", userId).
                filter("latestSignDate", today);
        return Objects.nonNull(query.get());
    }

    /**
     * 修改用户签到记录
     * @param param
     */
    public void updateUserSign(UserSignPO param){
        Query<UserSignPO> query = dsForRW.createQuery(UserSignPO.class).filter("_id", new ObjectId(param.getId()));
        UpdateOperations<UserSignPO> ops = dsForRW.createUpdateOperations(UserSignPO.class);
        ops.set("updateTime", System.currentTimeMillis());
        ops.set("latestSignDate", param.getLatestSignDate());
        ops.set("count", param.getCount());
        ops.set("continueSign", param.getContinueSign());
        ops.set("signList", param.getSignList());

        dsForRW.update(query, ops);
    }

}
