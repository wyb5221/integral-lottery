package com.dachen.integral.data.vo;

import com.dachen.integral.data.po.IntegralTaskPO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @Author: wangyongbin
 * @Date: 2021/4/22 14:58
 * @Description:
 */
@Data
public class IntegralTaskResult implements Serializable {

    private static final long serialVersionUID = -8901777999748202216L;

    @ApiModelProperty(value = "已完成数据")
    private Integer completedAmount;

    @ApiModelProperty(value = "未完成数据")
    private Integer unAmount;

    @ApiModelProperty(value = "未完成的积分数")
    private Integer unIntegralAmount;

    @ApiModelProperty(value = "任务集合")
    private List<IntegralTaskPO> taskList;

}
