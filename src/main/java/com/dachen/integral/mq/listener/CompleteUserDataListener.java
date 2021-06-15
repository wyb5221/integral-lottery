package com.dachen.integral.mq.listener;

import com.alibaba.fastjson.JSON;
import com.dachen.integral.biz.service.impl.TaskListenerServiceImpl;
import com.dachen.integral.data.constant.MqConstants;
import com.dachen.integral.data.vo.MqMessageVo;
import com.dachen.mq.ExchangeType;
import com.dachen.mq.consume.annotation.MqConsumeMapping;
import com.dachen.mq.consume.listener.AbstractMqConsumerListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 完善用户资料
 * @Author: wangyongbin
 * @Date: 2021/5/10 9:56
 * @Description:
 */
@Slf4j
@Component
@MqConsumeMapping(exchangeType = ExchangeType.FANOUT, exchangeName = MqConstants.COMPLETE_INTEGRAL_TASK, queueName = "complete_integral_task_queue")
public class CompleteUserDataListener extends AbstractMqConsumerListener {

    @Autowired
    private TaskListenerServiceImpl taskListenerService;


    @Override
    public void handleMessage(String jsonMessage) throws Exception {
        log.info("CompleteUserDataListener handleMessage jsonMessage:{}", jsonMessage);
        MqMessageVo mqMessage = JSON.parseObject(jsonMessage, MqMessageVo.class);
        if(Objects.nonNull(mqMessage)){
            if("article_remark".equals(mqMessage.getBizType())){
                String condition = mqMessage.getCondition();
                if(Integer.parseInt(condition) < 15){
                    return;
                }
            }
            //任务完成操作
            taskListenerService.completeTask(mqMessage.getUserId(), mqMessage.getBizId(), mqMessage.getBizType(), mqMessage.getBizName());

        }
    }
}
