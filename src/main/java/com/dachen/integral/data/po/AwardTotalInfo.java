package com.dachen.integral.data.po;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.mongodb.morphia.annotations.NotSaved;

/**
 * @Author: wangyongbin
 * @Date: 2021/5/5 22:17
 * @Description:
 */
@Data
public class AwardTotalInfo {

    @ApiModelProperty(value = "奖品数量")
    private Integer prizeAmount;
    @ApiModelProperty(value = "奖品类型 1:学币 2:积分")
    private Integer prizeType;
    private String iconUrl;

    public AwardTotalInfo(Integer prizeAmount, Integer prizeType, String iconUrl) {
        this.prizeAmount = prizeAmount;
        this.prizeType = prizeType;
        this.iconUrl = iconUrl;
    }

    public AwardTotalInfo() {

    }
}
