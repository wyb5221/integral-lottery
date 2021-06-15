package com.dachen.integral.biz.dao;

import com.dachen.integral.data.po.IntegralRecordPO;
import com.dachen.integral.data.vo.PageVO;
import com.dachen.support.annotation.Model;
import com.dachen.support.mongo.dao.BaseDao;
import lombok.extern.slf4j.Slf4j;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 积分操作记录
 * @Author: wangyongbin
 * @Date: 2021/4/25 17:29
 * @Description:
 */
@Service
@Slf4j
@Model(IntegralRecordPO.class)
public class IntegralRecordDao extends BaseDao {

    /**
     * 新增积分操作记录
     * @param param
     * @return
     */
    public String insertIntegralRecord(IntegralRecordPO param) {
        param.setCreateTime(System.currentTimeMillis());
        param.setUpdateTime(System.currentTimeMillis());
        Key<IntegralRecordPO> insert = dsForRW.insert(param);
        return insert.getId().toString();
    }

    /**
     * 查询我的积分明细
     * @param userId
     * @return
     */
    public PageVO<IntegralRecordPO> queryIntegralRecord(Integer userId, Integer pageIndex, Integer pageSize){
        Query<IntegralRecordPO> query = dsForRW.createQuery(IntegralRecordPO.class).filter("userId", userId);
        query.order("-createTime");

        long total = query.countAll();
        if (0 == total) {
            return new PageVO<IntegralRecordPO>();
        }

        int start = pageIndex * pageSize;
        query.offset(start).limit(pageSize);
        List<IntegralRecordPO> integralList = query.asList();

        PageVO<IntegralRecordPO> page = new PageVO<>();
        page.setPageData(integralList);
        page.setPageIndex(pageIndex);
        page.setPageSize(pageSize);
        page.setTotal(total);

        return page;
    }

    /**
     * 查询积分记录
     * @return
     */
    public List<IntegralRecordPO> queryIntegralRecord(){
        Query<IntegralRecordPO> query = dsForRW.createQuery(IntegralRecordPO.class);
        query.order("-createTime");
        return query.asList();
    }
}
