package com.dachen.integral.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: wangyongbin
 * @Date: 2021/5/20 16:20
 * @Description:
 */
@Data
public class GetIntegralVo implements Serializable {
    private static final long serialVersionUID = -6029246380115115544L;

    @ApiModelProperty(value = "附加分数")
    private Integer appendIntegral;

    @ApiModelProperty(value = "总积分")
    private Integer totalIntegral;

}
