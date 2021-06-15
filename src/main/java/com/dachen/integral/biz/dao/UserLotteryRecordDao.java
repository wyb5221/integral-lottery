package com.dachen.integral.biz.dao;

import com.dachen.integral.data.enums.PrizeTypeEnum;
import com.dachen.integral.data.po.UserLotteryRecordPO;
import com.dachen.integral.data.vo.PageVO;
import com.dachen.support.annotation.Model;
import com.dachen.support.mongo.dao.BaseDao;
import lombok.extern.slf4j.Slf4j;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户抽奖记录
 * @Author: wangyongbin
 * @Date: 2021/5/3 21:07
 * @Description:
 */
@Service
@Slf4j
@Model(UserLotteryRecordPO.class)
public class UserLotteryRecordDao extends BaseDao {

    /**
     * 新增用户抽奖数据
     * @param param
     * @return
     */
    public String insertLotteryRecord(UserLotteryRecordPO param) {
        param.setCreateTime(System.currentTimeMillis());
        param.setUpdateTime(System.currentTimeMillis());
        Key<UserLotteryRecordPO> insert = dsForRW.insert(param);
        return insert.getId().toString();
    }

    /**
     * 查询用户抽奖记录
     * @param userId
     * @return
     */
    public List<UserLotteryRecordPO> queryUserLotteryRecord(Integer userId){
        Query<UserLotteryRecordPO> query = dsForRW.createQuery(UserLotteryRecordPO.class).filter("userId", userId);
        query.order("-createTime");
        return query.asList();
    }

    /**
     * 查询抽奖记录
     * @param userId
     * @return
     */
    public PageVO<UserLotteryRecordPO> queryUserLotteryRecord(Integer userId, Integer pageIndex, Integer pageSize){
        Query<UserLotteryRecordPO> query = dsForRW.createQuery(UserLotteryRecordPO.class);
        query.filter("userId", userId);
        query.filter("status", 1);
        query.order("-createTime");

        long total = query.countAll();
        if (0 == total) {
            return new PageVO<UserLotteryRecordPO>();
        }

        int start = pageIndex * pageSize;
        query.offset(start).limit(pageSize);
        List<UserLotteryRecordPO> integralList = query.asList();

        PageVO<UserLotteryRecordPO> page = new PageVO<>();
        page.setPageData(integralList);
        page.setPageIndex(pageIndex);
        page.setPageSize(pageSize);
        page.setTotal(total);

        return page;
    }

    /**
     * 分页查询中奖明细
     * @param pageSize
     * @return
     */
    public List<UserLotteryRecordPO> queryLotteryRecord(Integer pageSize){
        Query<UserLotteryRecordPO> query = dsForRW.createQuery(UserLotteryRecordPO.class);
        query.filter("status", 1);
        query.filter("awardTotal.prizeType", PrizeTypeEnum.coin.getIndex());
        query.order("-createTime");
        query.offset(0).limit(pageSize);
        return query.asList();
    }

    /**
     * 查询所有中奖记录
     * @return
     */
    public List<UserLotteryRecordPO> queryLotteryRecordList() {
        Query<UserLotteryRecordPO> query = dsForRW.createQuery(UserLotteryRecordPO.class);
        query.order("-createTime");
        return query.asList();
    }

}
