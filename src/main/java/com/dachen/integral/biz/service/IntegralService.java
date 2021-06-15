package com.dachen.integral.biz.service;

import com.dachen.integral.data.vo.*;

import java.util.List;
import java.util.Map;


/**
 * @Author: wangyongbin
 * @Date: 2021/4/22 14:55
 * @Description:
 */
public interface IntegralService {

    /**
     * 查询积分任务详情
     * @return
     */
    IntegralTaskVo queryTaskInfo(Integer userId);

    /**
     * 用户签到
     * @param userId
     * @return
     */
    SignResultVo sign(Integer userId);

    /**
     * 用户签到记录查询
     * @param userId
     * @return
     */
    SignRecordVo querySignRecord(Integer userId);

    /**
     * 查询用户当前积分数
     * @param userId
     * @return
     */
    UserIntegralInfoVo queryCurrentIntegral(Integer userId);

    /**
     * 查询我的积分记录
     * @param userId
     * @return
     */
    PageVO<IntegralRecordVo> queryUserIntegralRecord(Integer userId, Integer pageIndex, Integer pageSize);

    /**
     * 任务积分领取
     * @param taskId
     */
    GetIntegralVo getTaskIntegral(String taskId, Integer userId);

}
