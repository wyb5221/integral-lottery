package com.dachen.integral.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: wangyongbin
 * @Date: 2021/4/23 16:30
 * @Description:
 */
@Data
public class SignStatuInfo {

    @ApiModelProperty(value = "日期")
    private String date;
    @ApiModelProperty(value = "是否签到 1:已签到 0:未签到")
    private Integer Sign = 0;
    @ApiModelProperty(value = "是否是当天")
    private Boolean same = Boolean.FALSE;

}
