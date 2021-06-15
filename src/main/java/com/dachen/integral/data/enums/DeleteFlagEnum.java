package com.dachen.integral.data.enums;

import lombok.Getter;

/**
 * @Author: wangyongbin
 * @Date: 2021/4/22 14:52
 * @Description:
 */
@Getter
public enum DeleteFlagEnum {

    NORMAL(0, "正常"),
    DELETE(1, "删除");

    Integer index;
    String value;

    DeleteFlagEnum(Integer index, String value) {
        this.index = index;
        this.value = value;
    }
}
