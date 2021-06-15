package com.dachen.integral.data.enums;

import lombok.Getter;

/**
 * @Author: wangyongbin
 * @Date: 2021/5/25 9:54
 * @Description:
 */
@Getter
public enum MeetingModelEnum {

    /**预约会议*/
    meeting("meeting","预约会议"),
    /**线上会议*/
    online_meeting("online_meeting","线上会议"),
    /**个人会议*/
    private_meeting("private_meeting","个人会议"),
    /**精品课程会议*/
    course_meeting("course_meeting","精品课程会议"),
    /**科室会议*/
    dept_meeting("dept_meeting","医视圈会议"), //科室会议
    /**企视会议*/
    ecompany_meeting("ecompany_meeting","企视圈会议"), //企视会议
    /**大屏通用版会议*/
    common_meeting("common_meeting", "大屏通用版会议"),
    /**会议通用版*/
    common_equipment("common_equipment","设备通用版"), //会议通用版
    /**玄关视讯通*/
    video_comm("video_comm","玄关视讯通"),
    /**群直播*/
    yhq_meeting("yhq_meeting", "群直播"),
    /**超级科会*/
    activity_meeting("activity_meeting", "超级科会"),
    /**群会议*/
    imgroup_meeting("imgroup_meeting", "群会议"),
    /**公开群会议*/
    public_imgroup_meeting("public_imgroup_meeting", "全群会议"), //群会议
    /**数字化营销会议*/
    marketing_activity_meeting("marketing_activity_meeting", "数字化营销会议");

    String key;
    String value;

    MeetingModelEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * 判断数值是否属于枚举类的值
     * @param key
     * @return
     */
    public static boolean isInclude(String key){
        boolean include = false;
        for (MeetingModelEnum e: MeetingModelEnum.values()){
            if(e.getKey().equals(key)){
                include = true;
                break;
            }
        }
        return include;
    }

}
