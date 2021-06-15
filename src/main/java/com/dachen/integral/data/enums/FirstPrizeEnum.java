package com.dachen.integral.data.enums;

import lombok.Getter;

/**
 * @Author: wangyongbin
 * @Date: 2021/4/22 14:52
 * @Description:
 */
@Getter
public enum FirstPrizeEnum {

    unFirstPrize(0, "非一等奖"),
    firstPrize(1, "一等奖");

    Integer index;
    String value;

    FirstPrizeEnum(Integer index, String value) {
        this.index = index;
        this.value = value;
    }
}
