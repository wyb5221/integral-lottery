package com.dachen.integral.data.po;

import com.dachen.integral.data.base.BasePO;
import lombok.Data;
import org.mongodb.morphia.annotations.Entity;

/**
 * 积分等级表
 * @Author: wangyongbin
 * @Date: 2021/4/23 14:24
 * @Description:
 */
@Data
@Entity(value = "t_integral_level", noClassnameStored = true)
public class IntegralLevelPO extends BasePO {

    /**积分等级*/
    private String level;
    /**积分等级说明*/
    private String levelName;
    /**积分要求*/
    private Integer integral;
    /**排序*/
    private Integer rank;
}
