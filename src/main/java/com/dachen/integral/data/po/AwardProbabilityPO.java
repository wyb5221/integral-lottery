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
@Entity(value = "t_award_probability", noClassnameStored = true)
public class AwardProbabilityPO extends BasePO {

    /**奖品名称*/
    @ApiModelProperty(value = "奖品名称")
    private String awardName;
    /**奖品图标名称*/
    @ApiModelProperty(value = "奖品图标名称")
    private String awardIcon;
    /**抽奖类型 1:积分抽奖*/
    private Integer type;
    /**抽奖方式 1:单次抽奖 2:十连抽*/
    private Integer mode;
    /**有一等奖的中奖概率*/
    private double firstProbability;
    /**没有一等奖的中奖概率*/
    private double unFirstProbability;
    /**多次第一次的中奖概率key*/
    private double oneProbability;

    @ApiModelProperty(value = "奖品数量")
    private Integer prizeAmount;
    @ApiModelProperty(value = "奖品类型 1:学币 2:积分")
    private Integer prizeType;
    /**排序*/
    private Integer rank;
    /**是否是一等奖  1:是*/
    private Integer firstPrize;
    @NotSaved
    @ApiModelProperty(value = "图标地址")
    private String iconUrl;
    @NotSaved
    private double probability;

}
