package com.dachen.integral.data.enums;

import lombok.Getter;

/**
 * @Author: wangyongbin
 * @Date: 2021/4/22 15:12
 * @Description:
 */
@Getter
public enum TaskParentNoEnum {
    //父级任务编号 1:每日任务 2:成长任务
    everydayTask(1, "每日任务"),
    growTask(2, "成长任务");

    Integer index;
    String value;

    TaskParentNoEnum(Integer index, String value) {
        this.index = index;
        this.value = value;
    }
}
