package com.dachen.integral.data.enums;

import lombok.Getter;

/**
 * @Author: wangyongbin
 * @Date: 2021/4/22 14:52
 * @Description:
 */
@Getter
public enum SignStatusEnum {

    UNSIGN(0, "未签到"),
    SIGN(1, "已签到");

    Integer index;
    String value;

    SignStatusEnum(Integer index, String value) {
        this.index = index;
        this.value = value;
    }
}
