package com.dachen.integral.data.constant;

/**
 * @Author: wangyongbin
 * @Date: 2020/7/8 17:55
 * @Description:
 */
public final class MicroUrlConstants {

    /**用户简单信息*/
    public static final String AUTH_GET_USERS_BY_IDS = "http://AUTH2/v2/user/getSimpleUser";

    /** 获取医生详情信息 */
    public static final String HEALTH_GET_DOCTOR_INFO = "http://HEALTH/inner/users/{id}";

    /**
     * 学币改造，新转账接口
     */
    public static final String TRANSFER_NEW = "http://CREDIT/inner/transfer";

    /**
     * 获取用户参加会议的时长列表
     */
    public static final String GET_JOIN_MEETING_DURATION = "http://meeting/inner/getJoinMeetingDurationList";

}
