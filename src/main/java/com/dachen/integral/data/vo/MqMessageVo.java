package com.dachen.integral.data.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: wangyongbin
 * @Date: 2021/5/10 10:10
 * @Description:
 */
@Data
public class MqMessageVo implements Serializable {
    private static final long serialVersionUID = 7556934992449978688L;

    /**
     * 操作人用户id
     */
    Integer userId;
    /**
     * 业务id
     */
    String bizId;
    /**
     * 业务类型
     */
    String bizType;
    /**
     * 业务名称
     */
    String bizName;
    /**
     * 业务要求
     */
    String condition;
}
