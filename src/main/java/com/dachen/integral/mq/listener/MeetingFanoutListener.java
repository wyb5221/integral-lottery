package com.dachen.integral.mq.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dachen.integral.biz.service.impl.TaskListenerServiceImpl;
import com.dachen.integral.biz.service.remote.RemoteCallService;
import com.dachen.integral.data.constant.BaseConstants;
import com.dachen.integral.data.constant.MqConstants;
import com.dachen.integral.data.enums.MeetingModelEnum;
import com.dachen.integral.data.vo.LiveDurationVO;
import com.dachen.integral.data.vo.MeetingUserJoinMsg;
import com.dachen.integral.data.vo.MeetingUserLeaveMsg;
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
@MqConsumeMapping(exchangeType = ExchangeType.TOPIC, exchangeName = MqConstants.MEETING_TOPIC, bindingKey = "meeting.user.#", queueName = "integral_lottery_meeting_queue")
public class MeetingFanoutListener extends AbstractMqConsumerListener {
    @Autowired
    private TaskListenerServiceImpl taskListenerService;
    @Autowired
    private RemoteCallService remoteCallService;

    @Override
    public void handleMessage(String jsonMessage) throws Exception {
        log.info("MeetingFanoutListener handleMessage jsonMessage:{}", jsonMessage);
        JSONObject object = JSON.parseObject(jsonMessage);
        String data = object.getString("data");

        String action = object.getString("action");

        //加入就算完成任务
        if("meeting_user_join".equals(action)){
            MeetingUserJoinMsg meetingUser = JSON.parseObject(data, MeetingUserJoinMsg.class);
            if(Objects.nonNull(meetingUser) && null != meetingUser.getUserId()){
                if(MeetingModelEnum.isInclude(meetingUser.getMeetingModel())){
                    //任务完成操作
                    taskListenerService.completeTask(Integer.parseInt(meetingUser.getUserId()), meetingUser.getMeetingId(), "watch_meeting", "watch_meeting");
                }
            }
        }else if("meeting_user_leave".equals(action)){
            MeetingUserLeaveMsg meetingUserLeaveMsg = JSON.parseObject(data, MeetingUserLeaveMsg.class);
            if(Objects.nonNull(meetingUserLeaveMsg)){
                //查询用户观看直播的时长
                LiveDurationVO liveDuration = remoteCallService.getLiveDuration(Integer.parseInt(meetingUserLeaveMsg.getUserId()), meetingUserLeaveMsg.getMeetingId());
                if(Objects.nonNull(liveDuration)){
                    //用户参加会议总时长 单位秒
                    Integer totalUserDuration = liveDuration.getTotalUserDuration();
                    if(totalUserDuration >= BaseConstants.duration){
                        //任务完成操作
                        taskListenerService.completeTask(Integer.parseInt(meetingUserLeaveMsg.getUserId()), meetingUserLeaveMsg.getMeetingId(), "live", "live");
                    }
                }
            }
        }
    }
}
