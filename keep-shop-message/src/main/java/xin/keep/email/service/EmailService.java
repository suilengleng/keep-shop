package xin.keep.email.service;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import xin.keep.adapter.MessageAdapter;
@Service
@Slf4j
public class EmailService implements MessageAdapter {
    @Value("${msg.subject}")
    private String subject;
    @Value("${msg.text}")
    private String text;
    @Autowired
    private JavaMailSender mailSender; // 自动注入的Bean
    @Override
    public void sendMsg(JSONObject body) {
        //处理发送邮件
        String email = body.getString("email");
        if(StringUtils.isEmpty(email)){
            return;
        }
        //分装参数
        log.info("消息服务平台发送邮件：{}",email);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        // 发送
        simpleMailMessage.setFrom(email);
        simpleMailMessage.setTo(email);
        // 标题
        simpleMailMessage.setSubject(subject);
        // 内容
        simpleMailMessage.setText(text.replace("{}", email));
        mailSender.send(simpleMailMessage);

    }
}
