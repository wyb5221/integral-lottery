package com.dachen.integral;

import com.alibaba.fastjson.JSON;
import com.dachen.integral.biz.dao.IntegralLevelDao;
import com.dachen.integral.data.po.IntegralLevelPO;
import com.dachen.integral.mq.listener.CompleteUserDataListener;
import com.dachen.integral.mq.listener.MeetingFanoutListener;
import com.google.gson.JsonObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: wangyongbin
 * @Date: 2021/5/6 15:17
 * @Description:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class DaoServiceTest {

    @Autowired
    private IntegralLevelDao integralLevelDao;
    @Autowired
    private CompleteUserDataListener completeUserDataListener;
    @Autowired
    private MeetingFanoutListener meetingFanoutListener;

    @Test
    public void test(){
        IntegralLevelPO querylevel = integralLevelDao.querylevel(7500);
        System.out.println(querylevel);

    }

    public void test1() throws Exception {
        JsonObject object = new JsonObject();
        object.addProperty("bizId", "6077973204dd3c3b008e552d");
        object.addProperty("bizName", "faq");
        object.addProperty("bizType", "read_article");
        object.addProperty("userId", 881842);
        completeUserDataListener.handleMessage(object.toString());
    }

    @Test
    public void test2() throws Exception {
        String str = "{\"data\":{\"appId\":\"031\",\"bizId\":\"60ae01e504dd3c2a394ea623\",\"bizType\":\"live_video\",\"meetingId\":\"847144072215822336\",\"meetingModel\":\"yhq_meeting\",\"role\":3,\"userId\":\"42306\",\"userName\":\"狮测c1\",\"userPic\":\"http://avatar.test.file.mediportal.com.cn/b96a2a24221f49148a8d2786b4003b2b\"},\"action\":\"meeting_user_join\"}";

        JsonObject object = new JsonObject();
        object.addProperty("bizId", "6077973204dd3c3b008e552d");
        object.addProperty("bizName", "faq");
        object.addProperty("bizType", "read_article");
        object.addProperty("userId", 881842);
        meetingFanoutListener.handleMessage(str);
    }
}
