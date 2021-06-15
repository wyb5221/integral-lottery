package com.dachen.integral.data.po;

import com.dachen.integral.data.base.BasePO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.NotSaved;

/**
 * 抽奖奖品表
 * @Author: wangyongbin
 * @Date: 2021/4/23 14:30
 * @Description:
 */
@Data
@Entity(value = "t_award", noClassnameStored = true)
public class AwardInfoPO extends BasePO {

    /**奖品名称*/
    @ApiModelProperty(value = "奖品名称")
    private String awardName;
    /**奖品图标名称*/
    @ApiModelProperty(value = "奖品图标名称")
    private String awardIcon;
    /**抽奖类型 1:积分抽奖*/
    private Integer type;
    @ApiModelProperty(value = "奖品数量")
    private Integer prizeAmount;
    @ApiModelProperty(value = "奖品类型 1:学币 2:积分")
    private Integer prizeType;
    /**排序*/
    private Integer rank;
    @NotSaved
    @ApiModelProperty(value = "图标地址")
    private String iconUrl;

}
