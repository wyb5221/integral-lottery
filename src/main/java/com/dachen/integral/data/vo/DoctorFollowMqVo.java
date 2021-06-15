package com.dachen.integral.data.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: wangyongbin
 * @Date: 2021/5/8 9:20
 * @Description:
 */
@Data
public class DoctorFollowMqVo implements Serializable {

    private static final long serialVersionUID = 8831408364217536954L;
    /**
     * 操作人用户id
     */
    Integer userId;
    /**
     * 被操作人用户id
     */
    Integer toUserId;
    /**
     * 操作类型 1关注 2取消关注
     */
    Integer type;

}
