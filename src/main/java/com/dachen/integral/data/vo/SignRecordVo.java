package com.dachen.integral.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 签到记录，只展示本周的数据
 * @Author: wangyongbin
 * @Date: 2021/4/23 16:29
 * @Description:
 */
@Data
public class SignRecordVo implements Serializable {

    private static final long serialVersionUID = -2715212476805534435L;

    @ApiModelProperty(value = "连续签到天数")
    private Integer continueSign;
    @ApiModelProperty(value = "签到历史记录")
    private List<SignStatuInfo> signList;

}
