package com.dachen.integral.data.enums;

import lombok.Getter;

/**
 * @Author: wangyongbin
 * @Date: 2021/5/8 18:41
 * @Description:
 */
@Getter
public enum sourceTypeEnum {

    platform(0, "平台"),
    user(1, "个人"),
    company(2,"企业"),
    dept(3, "圈子"),
    virtual(4, "虚拟账户");

    Integer index;
    String value;

    sourceTypeEnum(Integer index, String value) {
        this.index = index;
        this.value = value;
    }
}
