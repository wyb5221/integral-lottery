package com.dachen.integral.biz.service;

import com.dachen.integral.data.po.UserLotteryRecordPO;
import com.dachen.integral.data.vo.IntegralAwardVo;
import com.dachen.integral.data.vo.LotteryResultVo;
import com.dachen.integral.data.vo.PageVO;
import com.dachen.integral.data.vo.UserLotteryRecordVo;

import java.util.List;

/**
 * @Author: wangyongbin
 * @Date: 2021/4/23 15:51
 * @Description:
 */
public interface LotteryService {

    /**
     * 刷新奖品和中奖概率池
     */
    void refresh();

    /**
     * 查询抽奖奖品
     * @return
     */
    List<IntegralAwardVo> queryAwardList(Integer lotteryType);

    /**
     * 单次抽奖
     * @param userId
     * @return
     */
    LotteryResultVo getOneLottery(Integer userId);

    /**
     * 十连抽奖
     * @param userId
     * @return
     */
    LotteryResultVo getManyLottery(Integer userId);

    /**
     * 查询用户的中奖记录
     * @param userId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    PageVO<UserLotteryRecordVo> getUserLotteryRecord(Integer userId, Integer pageIndex, Integer pageSize);

    /**
     * 查询中奖记录
     * @param pageSize
     * @return
     */
    List<String> getLotteryRecord(Integer pageSize);
}
