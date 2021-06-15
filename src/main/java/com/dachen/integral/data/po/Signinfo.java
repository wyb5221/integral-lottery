package com.dachen.integral.data.po;

import lombok.Data;

import java.util.List;

/**
 * 签到记录对象
 * @Author: wangyongbin
 * @Date: 2021/4/25 10:51
 * @Description:
 */
@Data
public class Signinfo {

    private Integer year;
    /**签到天数记录*/
    private List<String> signDates;
}
