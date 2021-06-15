package com.dachen.integral.data.po;

import com.dachen.integral.data.base.BasePO;
import lombok.Data;
import org.mongodb.morphia.annotations.Entity;

/**
 * 用户积分表
 * @Author: wangyongbin
 * @Date: 2021/4/23 14:16
 * @Description:
 */
@Data
@Entity(value = "user_integral", noClassnameStored = true)
public class UserIntegralPO extends BasePO {

    /**用户id*/
    private Integer userId;
    /**总积分*/
    private Integer totalAmount;
    /**当前积分*/
    private Integer currentIntegral;
    /**积分等级*/
    private String level;
    /**积分等级说明*/
    private String levelName;
}
