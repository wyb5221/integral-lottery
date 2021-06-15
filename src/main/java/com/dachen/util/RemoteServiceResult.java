package com.dachen.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RemoteServiceResult {

    //失败
    public static final int FAILURE = 0;
    //成功
    public static final int SUCCESS = 1;

    private String detailMsg;
    private Object data;
    private int resultCode = 1;
    private String resultMsg;

}
