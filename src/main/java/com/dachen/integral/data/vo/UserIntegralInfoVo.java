package com.dachen.integral.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: wangyongbin
 * @Date: 2021/4/26 10:36
 * @Description:
 */
@Data
public class UserIntegralInfoVo implements Serializable {

    private static final long serialVersionUID = 7965980549289241811L;
    @ApiModelProperty(value = "当前积分")
    private Integer currentIntegral;
    @ApiModelProperty(value = "积分等级")
    private String level;
    @ApiModelProperty(value = "积分等级说明")
    private String levelName;
    @ApiModelProperty(value = "当天是否签到")
    private Boolean isSign;
}
