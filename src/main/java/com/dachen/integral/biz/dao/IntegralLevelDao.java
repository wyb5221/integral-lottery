package com.dachen.integral.biz.dao;

import com.dachen.integral.data.po.IntegralLevelPO;
import com.dachen.support.annotation.Model;
import com.dachen.support.mongo.dao.BaseDao;
import com.mongodb.BasicDBObject;
import lombok.extern.slf4j.Slf4j;
import org.mongodb.morphia.query.FindOptions;
import org.mongodb.morphia.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: wangyongbin
 * @Date: 2021/4/25 18:47
 * @Description:
 */
@Service
@Slf4j
@Model(IntegralLevelPO.class)
public class IntegralLevelDao extends BaseDao {

    /**
     * 根据积分数查询当前积分等级信息
     * @param amount
     * @return
     */
    public IntegralLevelPO querylevel(Integer amount){
        Query<IntegralLevelPO> query = dsForRW.createQuery(IntegralLevelPO.class);
        query.filter("integral", new BasicDBObject("$lte", amount));
        query.order("-integral");
        FindOptions options = new FindOptions();
        options.skip(0).limit(1);
        return query.get(options);
    }
}
