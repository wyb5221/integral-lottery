package com.dachen.integral.data.po;

import com.dachen.integral.data.base.BasePO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.NotSaved;

/**
 * 积分任务表
 * @Author: wangyongbin
 * @Date: 2021/4/21 19:33
 * @Description:
 */
@Data
@Entity(value = "t_integral_task", noClassnameStored = true)
public class IntegralTaskPO extends BasePO {

    /**父级任务名称  每日任务、成长任务*/
    @ApiModelProperty(value = "父级任务名称  每日任务、成长任务")
    private String parentTask;
    /**父级任务编号*/
//    @ApiModelProperty(value = "父级任务编号 1:每日任务 2:成长任务")
    private Integer parentNo;
    /**任务名称*/
    @ApiModelProperty(value = "任务名称")
    private String taskName;
    /**任务图标*/
    @ApiModelProperty(value = "任务图标")
    private String taskIcon;
    /**任务说明*/
    @ApiModelProperty(value = "任务说明")
    private String description;
    /**积分*/
    @ApiModelProperty(value = "任务积分")
    private Integer integral;
    /**按钮名称*/
    @ApiModelProperty(value = "按钮名称")
    private String buttonName;
    /**状态 0:未完成 1:已完成*/
    @ApiModelProperty(value = "状态 0:未完成 1:已完成 2:已领取")
    private Integer status;
    /**业务类型*/
    @ApiModelProperty(value = "业务类型")
    private String bizType;
    @NotSaved
    @ApiModelProperty(value = "图标地址")
    private String imgUrl;
    /**排序值*/
    private Integer rank;
    private Integer deleteFlag;

}
