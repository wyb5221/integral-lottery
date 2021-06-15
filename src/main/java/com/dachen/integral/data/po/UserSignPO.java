package com.dachen.integral.data.po;

import com.dachen.integral.data.base.BasePO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.mongodb.morphia.annotations.Entity;

import java.util.List;

/**
 * 用户签到表
 * @Author: wangyongbin
 * @Date: 2021/4/23 15:19
 * @Description:
 */
@Data
@Entity(value = "user_sign", noClassnameStored = true)
public class UserSignPO extends BasePO {

    @ApiModelProperty(value = "用户id")
    private Integer userId;
    @ApiModelProperty(value = "签到日期(格式：年.月.日)")
    private List<Signinfo> signList;
    /**最新签到日期*/
    private String latestSignDate;
    @ApiModelProperty(value = "总签到数")
    private int count;
    @ApiModelProperty(value = "连续签到数")
    private int continueSign;
}
