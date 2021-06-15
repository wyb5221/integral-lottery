package com.dachen.integral.data.enums;

import lombok.Getter;

/**
 * @Author: wangyongbin
 * @Date: 2021/4/22 15:22
 * @Description:
 */
@Getter
public enum TaskStatusEnum {
    //状态 0:未完成 1:已完成
    unfinished(0, "未完成"),
    completed(1, "已完成"),
    received(2, "已领取");

    Integer index;
    String value;

    TaskStatusEnum(Integer index, String value) {
        this.index = index;
        this.value = value;
    }
}
