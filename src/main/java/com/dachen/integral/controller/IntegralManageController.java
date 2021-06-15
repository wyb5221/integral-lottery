package com.dachen.integral.controller;

import com.dachen.common.support.JSONMessage;
import com.dachen.integral.biz.service.IntegralService;
import com.dachen.integral.biz.service.impl.TaskListenerServiceImpl;
import com.dachen.integral.data.vo.*;
import com.dachen.util.ReqUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 积分相关操作controller
 * @Author: wangyongbin
 * @Date: 2021/4/22 9:56
 * @Description:
 */
@Slf4j
@RestController
@Api(description = "积分相关操作controller")
@RequestMapping("/integral")
public class IntegralManageController {

    @Autowired
    private IntegralService integralService;
    @Autowired
    private TaskListenerServiceImpl taskListenerService;


    @ApiOperation(value = "查询任务列表", response = IntegralTaskVo.class)
    @GetMapping("/queryTask")
    public JSONMessage queryTask() {
        Integer userId = ReqUtil.instance.getUserId();
        return JSONMessage.success(integralService.queryTaskInfo(userId));
    }

    @ApiOperation(value = "签到", response = SignResultVo.class)
    @GetMapping("/sign")
    public JSONMessage sign() {
        Integer userId = ReqUtil.instance.getUserId();
        return JSONMessage.success(integralService.sign(userId));
    }

    @ApiOperation(value = "签到记录", response = SignRecordVo.class)
    @GetMapping("/signRecord")
    public JSONMessage signRecord() {
        Integer userId = ReqUtil.instance.getUserId();
        return JSONMessage.success(integralService.querySignRecord(userId));
    }

    @ApiOperation(value = "我的当前积分", response = UserIntegralInfoVo.class)
    @GetMapping("/getCurrentIntegral")
    public JSONMessage getCurrentIntegral() {
        Integer userId = ReqUtil.instance.getUserId();
        return JSONMessage.success(integralService.queryCurrentIntegral(userId));
    }

    @ApiOperation(value = "我的积分记录", response = IntegralRecordVo.class)
    @GetMapping("/getRecord")
    public JSONMessage getIntegralRecord(@RequestParam(defaultValue = "0") Integer pageIndex,
                                         @RequestParam(defaultValue = "10") Integer pageSize) {
        Integer userId = ReqUtil.instance.getUserId();
        return JSONMessage.success(integralService.queryUserIntegralRecord(userId, pageIndex, pageSize));
    }

    @ApiOperation(value = "完成任务")
    @GetMapping("/completeTask")
    public JSONMessage completeTask(@RequestParam String bizType) {
        Integer userId = ReqUtil.instance.getUserId();
        taskListenerService.completeTask(userId, null, bizType, null);
        return JSONMessage.success();
    }

    @ApiOperation(value = "领取任务积分")
    @GetMapping("/getTaskIntegral/{id}")
    public JSONMessage getTaskIntegral(@PathVariable String id) {
        Integer userId = ReqUtil.instance.getUserId();
        return JSONMessage.success(integralService.getTaskIntegral(id, userId));
    }

}
