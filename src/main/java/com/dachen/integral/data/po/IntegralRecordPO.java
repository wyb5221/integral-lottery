package com.dachen.integral.data.po;

import com.dachen.integral.data.base.BasePO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.mongodb.morphia.annotations.Entity;

/**
 * 积分操作明细表
 * @Author: wangyongbin
 * @Date: 2021/4/23 15:01
 * @Description:
 */
@Data
@Entity(value = "t_integral_record", noClassnameStored = true)
public class IntegralRecordPO extends BasePO {

    private String taskId;
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

    @ApiModelProperty(value = "业务名称")
    private String bizName;
    @ApiModelProperty(value = "业务类型")
    private String bizType;
    @ApiModelProperty(value = "积分数")
    private Integer integral;
    @ApiModelProperty(value = "操作类型 1:新增 -1:减少")
    private Integer operateType;


}
