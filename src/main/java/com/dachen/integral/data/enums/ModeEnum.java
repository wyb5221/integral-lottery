package com.dachen.integral.data.enums;

import lombok.Getter;

/**
 * 抽奖方式
 * @Author: wangyongbin
 * @Date: 2021/4/25 14:52
 * @Description:
 */
@Getter
public enum ModeEnum {
    //抽奖方式 1:单次抽奖 2:十连抽
    one(1, "单次抽奖"),
    ten(2, "十连抽");

    Integer index;
    String value;

    ModeEnum(Integer index, String value) {
        this.index = index;
        this.value = value;
    }
}
