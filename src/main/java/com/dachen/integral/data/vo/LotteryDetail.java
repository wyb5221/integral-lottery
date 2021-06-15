package com.dachen.integral.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: wangyongbin
 * @Date: 2021/5/6 14:18
 * @Description:
 */
@Data
public class LotteryDetail {

    private String id;
    @ApiModelProperty(value = "奖品名称")
    private String awardName;
    @ApiModelProperty(value = "奖品图标名称")
    private String awardIcon;
    @ApiModelProperty(value = "奖品数量")
    private Integer prizeAmount;
    @ApiModelProperty(value = "奖品类型 1:学币 2:积分")
    private Integer prizeType;
    @ApiModelProperty(value = "图标地址")
    private String iconUrl;
    @ApiModelProperty(value = "中奖次数")
    private Integer amount;

}
