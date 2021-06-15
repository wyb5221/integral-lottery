package com.dachen.integral.data.vo;

import lombok.Data;

/**
 * 预约直播消息
 * @Author: wangyongbin
 * @Date: 2021/5/27 16:41
 * @Description:
 */
@Data
public class LiveMqMsgVo {

    private Integer userId;

    private String userName;

    private String shortUrl;

    private String subject;
}
