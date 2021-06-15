package com.dachen.integral.controller;

import com.dachen.common.support.JSONMessage;
import com.dachen.integral.biz.service.LotteryService;
import com.dachen.integral.data.vo.IntegralAwardVo;
import com.dachen.integral.data.vo.LotteryResultVo;
import com.dachen.integral.data.vo.UserLotteryRecordVo;
import com.dachen.util.ReqUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: wangyongbin
 * @Date: 2021/4/23 15:49
 * @Description:
 */
@RestController
@Api(description = "抽奖相关操作controller")
@RequestMapping("/lottery")
public class LotteryController {

    @Autowired
    private LotteryService lotteryService;

    @ApiOperation(value = "刷新奖品池")
    @GetMapping("/nologin/refresh")
    public JSONMessage refresh() {
        lotteryService.refresh();
        return JSONMessage.success();
    }

    @ApiOperation(value = "查询积分抽奖奖品", response = IntegralAwardVo.class)
    @GetMapping("/queryAward")
    public JSONMessage queryAward() {
        //1:积分抽奖
        return JSONMessage.success(lotteryService.queryAwardList(1));
    }

    @ApiOperation(value = "单次积分抽奖", response = LotteryResultVo.class)
    @GetMapping("/one")
    public JSONMessage getOneLottery() {
        Integer userId = ReqUtil.instance.getUserId();
        return JSONMessage.success(lotteryService.getOneLottery(userId));
    }

    @ApiOperation(value = "十连抽奖", response = LotteryResultVo.class)
    @GetMapping("/ten")
    public JSONMessage getManyLottery() {
        Integer userId = ReqUtil.instance.getUserId();
        return JSONMessage.success(lotteryService.getManyLottery(userId));
    }

    @ApiOperation(value = "我的中奖记录", response = UserLotteryRecordVo.class)
    @GetMapping("/getUserLotteryRecord")
    public JSONMessage getUserRecord(@RequestParam(defaultValue = "0") Integer pageIndex,
                                     @RequestParam(defaultValue = "10") Integer pageSize) {
        Integer userId = ReqUtil.instance.getUserId();
        return JSONMessage.success(lotteryService.getUserLotteryRecord(userId, pageIndex, pageSize));
    }

    @ApiOperation(value = "轮播的中奖记录", response = String.class)
    @GetMapping("/getLotteryRecord")
    public JSONMessage getLotteryRecord(@RequestParam(defaultValue = "100") Integer pageSize) {
        return JSONMessage.success(lotteryService.getLotteryRecord(pageSize));
    }

}
