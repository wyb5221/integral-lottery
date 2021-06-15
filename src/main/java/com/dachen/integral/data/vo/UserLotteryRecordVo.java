package com.dachen.integral.data.vo;

import com.dachen.integral.data.po.AwardTotalInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: wangyongbin
 * @Date: 2021/5/8 15:47
 * @Description:
 */
@Data
public class UserLotteryRecordVo implements Serializable {
    private static final long serialVersionUID = 1692466932757171045L;

    @ApiModelProperty(value = "用户id")
    private Integer userId;
    @ApiModelProperty(value = "用户姓名")
    private String name;
    @ApiModelProperty(value = "任务名称")
    private String taskName;
    @ApiModelProperty(value = "抽奖时间")
    private Long createTime;
    @ApiModelProperty(value = "抽奖结果总计")
    private List<AwardTotalInfo> awardTotal;
}
