package com.dachen.integral.data.vo;

import lombok.Data;

/**
 * @Author: wangyongbin
 * @Date: 2021/5/27 17:00
 * @Description:
 */
@Data
public class MeetingUserLeaveMsg {

    /**会议id*/
    private String meetingId;
    /**关联会议业务id*/
    private String bizId;
    /**离开用户id*/
    private String userId;
    /**离开用户appid*/
    private String appId;
    /**chat使用NotifyBusinessType.live_video.name()于bizId没有关联*/
    private String bizType;

}
