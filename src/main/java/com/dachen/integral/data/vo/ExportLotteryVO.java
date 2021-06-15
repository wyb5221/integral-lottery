package com.dachen.integral.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: wangyongbin
 * @Date: 2021/5/31 15:54
 * @Description:
 */
@Data
public class ExportLotteryVO {

    @ApiModelProperty(value = "用户id")
    private Integer userId;
    @ApiModelProperty(value = "用户姓名")
    private String name;
    @ApiModelProperty(value = "用户手机")
    private String telephone;
    @ApiModelProperty("所属医院名称, 如 康哲安徽医院")
    private String hospitalName;
    @ApiModelProperty("科室, 如 骨/关节外科")
    private String departments;
    @ApiModelProperty("职称, 如 主治医师")
    private String title;
    @ApiModelProperty(value = "抽奖方式 1:单次抽奖 2:十连抽")
    private Integer mode;
    @ApiModelProperty(value = "任务名称")
    private String taskName;
    @ApiModelProperty(value = "任务积分")
    private Integer integral;
    @ApiModelProperty(value = "学币数量")
    private Integer coinCount;
    @ApiModelProperty(value = "任务数量")
    private Integer integralCount;
    @ApiModelProperty(value = "抽奖时间")
    private String createTime;
}
