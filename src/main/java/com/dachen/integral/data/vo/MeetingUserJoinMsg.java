package com.dachen.integral.data.vo;

import lombok.Data;

/**
 * @Author: wangyongbin
 * @Date: 2021/5/12 13:58
 * @Description:
 */
@Data
public class MeetingUserJoinMsg {

    /**会议id*/
    private String meetingId;
    /**关联会议业务id*/
    private String bizId;
    /**加入用户id*/
    private String userId;
    /**加入用户名称*/
    private String userName;
    /**加入用户头像*/
    private String userPic;
    /**加入用户appid*/
    private String appId;
    /**加入用户角色*/
    private Integer role;

    private String meetingModel;
}
