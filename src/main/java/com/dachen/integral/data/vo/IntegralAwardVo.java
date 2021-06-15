package com.dachen.integral.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 抽奖奖品表
 * @Author: wangyongbin
 * @Date: 2021/4/23 14:30
 * @Description:
 */
@Data
public class IntegralAwardVo implements Serializable {

    private static final long serialVersionUID = -6191044271816463910L;
    /**奖品名称*/
    @ApiModelProperty(value = "奖品名称")
    private String awardName;
    /**奖品图标名称*/
    @ApiModelProperty(value = "奖品图标名称")
    private String awardIcon;
    /**抽奖类型 1:积分抽奖 */
    private Integer type;
    @ApiModelProperty(value = "奖品数量")
    private Integer prizeAmount;
    @ApiModelProperty(value = "奖品类型")
    private Integer prizeType;
    /**排序*/
    private Integer rank;
    @ApiModelProperty(value = "图标地址")
    private String iconUrl;

}
