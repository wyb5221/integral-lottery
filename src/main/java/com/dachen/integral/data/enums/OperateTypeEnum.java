package com.dachen.integral.data.enums;

import lombok.Getter;

/**
 * @Author: wangyongbin
 * @Date: 2021/4/22 14:52
 * @Description:
 */
@Getter
public enum OperateTypeEnum {
    //操作类型 1:新增 -1:减少
    SUBTRACT(-1, "减少"),
    ADD(1, "新增");

    Integer index;
    String value;

    OperateTypeEnum(Integer index, String value) {
        this.index = index;
        this.value = value;
    }

    /**
     * 根据index获取value
     * @param index
     * @return
     */
    public static String getValue(Integer index){
        for(OperateTypeEnum in : OperateTypeEnum.values()){
            if(in.index.equals(index)){
                return in.value;
            }
        }
        return null;
    }
}
