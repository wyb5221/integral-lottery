package com.dachen.integral.data.enums;

import lombok.Getter;

/**
 * @Author: wangyongbin
 * @Date: 2021/4/22 14:52
 * @Description:
 */
@Getter
public enum PrizeTypeEnum {

    coin(1, "学币", "1monetary"),
    integral(2, "积分", "icon_jifen");

    Integer index;
    String value;
    String uname;

    PrizeTypeEnum(Integer index, String value, String uname) {
        this.index = index;
        this.value = value;
        this.uname = uname;
    }

    /**
     * 根据index获取uname
     * @param index
     * @return
     */
    public static String getUname(Integer index){
        for(PrizeTypeEnum in : PrizeTypeEnum.values()){
            if(in.index.equals(index)){
                return in.uname;
            }
        }
        return null;
    }

    /**
     * 根据index获取value
     * @param index
     * @return
     */
    public static String getValue(Integer index){
        for(PrizeTypeEnum in : PrizeTypeEnum.values()){
            if(in.index.equals(index)){
                return in.value;
            }
        }
        return null;
    }
}
