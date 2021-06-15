package com.dachen.integral.data.vo;

import com.dachen.integral.data.po.AwardTotalInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 抽奖返回结果
 * @Author: wangyongbin
 * @Date: 2021/4/27 18:33
 * @Description:
 */
@Data
public class LotteryResultVo implements Serializable {
    private static final long serialVersionUID = -2745785700542632735L;

    @ApiModelProperty(value = "是否中奖")
    private Boolean prize;
    @ApiModelProperty(value = "返回提示")
    private String hint;
    /**奖品名称*/
    @ApiModelProperty(value = "奖品信息详情")
    private List<LotteryDetail> awardInfo;
    @ApiModelProperty(value = "抽奖结果总计")
    private List<AwardTotalInfo> awardTotal;

}
