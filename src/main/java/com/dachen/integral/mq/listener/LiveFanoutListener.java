package com.dachen.integral.mq.listener;

import com.alibaba.fastjson.JSON;
import com.dachen.integral.biz.service.impl.TaskListenerServiceImpl;
import com.dachen.integral.data.constant.MqConstants;
import com.dachen.integral.data.vo.LiveMqMsgVo;
import com.dachen.mq.ExchangeType;
import com.dachen.mq.consume.annotation.MqConsumeMapping;
import com.dachen.mq.consume.listener.AbstractMqConsumerListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @Author: wangyongbin
 * @Date: 2021/5/12 14:00
 * @Description:
 */
@Slf4j
@Component
@MqConsumeMapping(exchangeType = ExchangeType.FANOUT, exchangeName = MqConstants.LIVE_SEND_SMS_INACTIVE, queueName = "integral_lottery_make_live_queue")
public class LiveFanoutListener extends AbstractMqConsumerListener {
    @Autowired
    private TaskListenerServiceImpl taskListenerService;

    @Override
    public void handleMessage(String jsonMessage) throws Exception {
        log.info("LiveFanoutListener handleMessage jsonMessage:{}", jsonMessage);
        LiveMqMsgVo liveMqMsg = JSON.parseObject(jsonMessage, LiveMqMsgVo.class);

        if(Objects.nonNull(liveMqMsg)){
            if(null != liveMqMsg.getUserId()){
                taskListenerService.completeTask(liveMqMsg.getUserId(), null, "make_live", liveMqMsg.getSubject());
            }
        }
    }
}
