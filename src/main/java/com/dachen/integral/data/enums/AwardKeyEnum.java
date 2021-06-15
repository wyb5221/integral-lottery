package com.dachen.integral.data.enums;

import lombok.Getter;

/**
 * @Author: wangyongbin
 * @Date: 2021/4/27 17:46
 * @Description:
 */
@Getter
public enum AwardKeyEnum {
    //单次有一等奖的奖品key:  oneFirstAward
    //单次没有一等奖的奖品key:  oneUnFirstAward
    //单次有一等奖的中奖概率key:  oneFirstProbability
    //单次没有一等奖的中奖概率key:  oneUnFirstProbability

    //多次有一等奖的奖品key:  manyFirstAward
    //多次没有一等奖的奖品key:  manyUnFirstAward
    //多次有一等奖的中奖概率key:  manyFirstProbability
    //多次没有一等奖的中奖概率key:  manyUnFirstProbability

    //多次抽奖时第一次的奖品key:  firstAward
    //多次抽奖时第一次的中奖概率key:  firstProbability


    oneFirstAward("oneFirstAward", "单次有一等奖的奖品key"),
    oneUnFirstAward("oneUnFirstAward", "单次没有一等奖的奖品key"),
    oneFirstProbability("oneFirstProbability", "单次有一等奖的中奖概率key"),
    oneUnFirstProbability("oneUnFirstProbability", "单次没有一等奖的中奖概率key"),

    manyFirstAward("manyFirstAward", "多次有一等奖的奖品key"),
    manyUnFirstAward("manyUnFirstAward", "多次没有一等奖的奖品key"),
    manyFirstProbability("manyFirstProbability", "多次有一等奖的中奖概率key"),
    manyUnFirstProbability("manyUnFirstProbability", "多次没有一等奖的中奖概率key"),

    firstAward("firstAward", "多次抽奖时第一次的奖品"),
    firstProbability("firstProbability", "多次抽奖时第一次的中奖概率key"),
    ;

    String key;
    String value;

    AwardKeyEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
