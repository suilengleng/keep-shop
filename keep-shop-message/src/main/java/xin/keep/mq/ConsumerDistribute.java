package xin.keep.mq;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import xin.keep.adapter.MessageAdapter;
import xin.keep.constants.Constants;
import xin.keep.email.service.EmailService;

@Component
@Slf4j
public class ConsumerDistribute {
    @Autowired
    private EmailService mailService;
    private MessageAdapter messageAdapter;
    @JmsListener(destination = "messages_queue")
    public void distribute(String json) {
        log.info("####ConsumerDistribute###distribute() 消息服务平台接受 json参数:" + json);
        if (StringUtils.isEmpty(json)) {
            return;
        }
        JSONObject jsonObecjt = new JSONObject().parseObject(json);
        JSONObject header = jsonObecjt.getJSONObject("header");
        String interfaceType = header.getString("interfaceType");

        if (StringUtils.isEmpty(interfaceType)) {
            return;
        }
        if (interfaceType.equals(Constants.MSG_EMAIL)) {
            messageAdapter = mailService;
        }
        if (messageAdapter == null) {
            return;
        }
        JSONObject body = jsonObecjt.getJSONObject("content");
        messageAdapter.sendMsg(body);
    }

}
