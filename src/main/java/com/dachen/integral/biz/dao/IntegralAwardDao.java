package com.dachen.integral.biz.dao;

import com.dachen.integral.data.po.AwardInfoPO;
import com.dachen.integral.data.po.AwardProbabilityPO;
import com.dachen.support.annotation.Model;
import com.dachen.support.mongo.dao.BaseDao;
import lombok.extern.slf4j.Slf4j;
import org.mongodb.morphia.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 抽奖奖品
 * @Author: wangyongbin
 * @Date: 2021/4/23 15:41
 * @Description:
 */
@Service
@Slf4j
@Model(AwardProbabilityPO.class)
public class IntegralAwardDao extends BaseDao {

    /**
     * 查询奖品详情
     * @param type
     * @return
     */
    public List<AwardInfoPO> queryAwardInfo(Integer type){
        Query<AwardInfoPO> query = dsForRW.createQuery(AwardInfoPO.class);
        if(null != type){
            query.filter("type", type);
        }
        return query.asList();
    }

    /**
     * 查询抽奖奖品
     * @param type
     * @return
     */
    public List<AwardProbabilityPO> queryIntegralAwards(Integer type){
        Query<AwardProbabilityPO> query = dsForRW.createQuery(AwardProbabilityPO.class);
        if(null != type){
            query.filter("type", type);
        }
        query.order("rank");
        return query.asList();
    }

    /**
     * 查询抽奖奖品
     * @param firstPrize 1:是一等奖 0:不是
     * @return
     */
    public List<AwardProbabilityPO> queryAwardsList(Integer firstPrize){
        Query<AwardProbabilityPO> query = dsForRW.createQuery(AwardProbabilityPO.class);
        if(null != firstPrize){
            query.filter("firstPrize", firstPrize);
        }
        query.order("rank");
        return query.asList();
    }
}
