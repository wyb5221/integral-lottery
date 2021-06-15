package com.dachen.integral.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 签到返回
 * @Author: wangyongbin
 * @Date: 2021/4/23 16:19
 * @Description:
 */
@Data
public class SignResultVo implements Serializable {

    private static final long serialVersionUID = -5012072988863901234L;

    @ApiModelProperty(value = "连续签到天数")
    private Integer continueSign;
    @ApiModelProperty(value = "获取的积分")
    private Integer integral;

}
