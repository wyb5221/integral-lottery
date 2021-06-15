package com.dachen.integral.data.po;

import com.dachen.integral.data.base.BasePO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.mongodb.morphia.annotations.Entity;

/**
 * 用户任务完成记录表
 * @Author: wangyongbin
 * @Date: 2021/5/8 10:55
 * @Description:
 */
@Data
@Entity(value = "user_task_record", noClassnameStored = true)
public class UserTaskRecordPO extends BasePO {

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

    private String taskId;
    @ApiModelProperty(value = "父级任务名称  每日任务、成长任务")
    private String parentTask;
    private Integer parentNo;
    @ApiModelProperty(value = "任务名称")
    private String taskName;
    @ApiModelProperty(value = "业务类型")
    private String bizType;
    @ApiModelProperty(value = "业务id")
    private String bizId;
    @ApiModelProperty(value = "业务名称")
    private String bizName;
    @ApiModelProperty(value = "状态 1:已完成 2:已领取")
    private Integer status;
    @ApiModelProperty(value = "按钮名称")
    private String buttonName;
    @ApiModelProperty(value = "完成日期")
    private String completeDate;
    @ApiModelProperty(value = "完成时间")
    private Long completeTime;
    @ApiModelProperty(value = "领取时间")
    private Long submitTime;


}
