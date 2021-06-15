package com.dachen.integral.data.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: wangyongbin
 * @Date: 2021/5/27 17:10
 * @Description:
 */
@Data
public class LiveDurationVO {

    //用户参加会议总时长:单位秒
    private Integer totalUserDuration;

    private String userId;

    private List<MeetingDuration> durationList;

    @Data
    public static class MeetingDuration {
        private Integer duration;
        private String meetingId;
        private Integer userDuration;

    }
}
