package com.dachen.integral.mq.listener;

import com.alibaba.fastjson.JSON;
import com.dachen.integral.biz.service.impl.TaskListenerServiceImpl;
import com.dachen.integral.data.constant.MqConstants;
import com.dachen.integral.data.vo.DoctorFollowMqVo;
import com.dachen.mq.ExchangeType;
import com.dachen.mq.consume.annotation.MqConsumeMapping;
import com.dachen.mq.consume.listener.AbstractMqConsumerListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 关注好友
 * @Author: wangyongbin
 * @Date: 2021/5/7 10:01
 * @Description:
 */
@Slf4j
@Component
@MqConsumeMapping(exchangeType = ExchangeType.FANOUT, exchangeName = MqConstants.DOCTOR_FOLLOW_DOCTOR, queueName = "integral_lottery_doctor_follow_queue")
public class FollowTaskListener extends AbstractMqConsumerListener {

    @Autowired
    private TaskListenerServiceImpl taskListenerService;


    @Override
    public void handleMessage(String jsonMessage) {
        log.info("FollowTaskListener handleMessage jsonMessage:{}", jsonMessage);
        DoctorFollowMqVo followVo = JSON.parseObject(jsonMessage, DoctorFollowMqVo.class);

        if(Objects.nonNull(followVo)){
            //操作类型 1关注 2取消关注
            Integer type = followVo.getType();
            if(1 == type){
                //任务完成操作
                taskListenerService.completeTask(followVo.getUserId(), String.valueOf(followVo.getToUserId()), "follow_friend", "follow");
            }
        }
    }

}
