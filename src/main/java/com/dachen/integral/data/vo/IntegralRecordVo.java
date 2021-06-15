package com.dachen.integral.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: wangyongbin
 * @Date: 2021/4/26 22:18
 * @Description:
 */
@Data
public class IntegralRecordVo implements Serializable {
    private static final long serialVersionUID = -3797771941754936861L;

    private Integer userId;
    @ApiModelProperty(value = "业务名称")
    private String bizName;
    @ApiModelProperty(value = "业务类型")
    private String bizType;
    @ApiModelProperty(value = "积分数")
    private Integer integral;
    @ApiModelProperty(value = "操作类型 1:新增 -1:减少")
    private Integer operateType;
    @ApiModelProperty(value = "创建时间")
    private Long createTime;
    private Long updateTime;
}
