package com.dachen.integral.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 积分任务返回列表
 * @Author: wangyongbin
 * @Date: 2021/4/22 14:56
 * @Description:
 */
@Data
public class IntegralTaskVo implements Serializable {

    private static final long serialVersionUID = 3768666853171384930L;

    @ApiModelProperty(value = "每日任务")
    private IntegralTaskResult everydayTask;

    @ApiModelProperty(value = "成长任务")
    private IntegralTaskResult growTask;

    @ApiModelProperty(value = "当前积分")
    private Integer currentIntegral;
}
