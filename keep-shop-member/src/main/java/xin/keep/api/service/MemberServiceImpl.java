package xin.keep.api.service;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xin.keep.base.BaseApiService;
import xin.keep.base.BaseRedisService;
import xin.keep.base.ResponseBase;
import xin.keep.constants.Constants;
import xin.keep.dao.MemberDao;
import xin.keep.entity.UserEntity;
import xin.keep.mq.RegisterMailboxProducer;
import xin.keep.utils.MD5Util;
import xin.keep.utils.TokenUtils;

import java.util.Date;
import java.util.LinkedHashMap;

@RestController
@Slf4j
public class MemberServiceImpl extends BaseApiService implements MemberService {
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private RegisterMailboxProducer registerMailboxProducer;
    @Value("${messages.queue}")
    private String MESAGEQUEUE;
    /*@Autowired
    private BaseRedisService baseRedisService;*/
    @Override
    public ResponseBase findUserById(Long userId) {
        //id=1;
        System.out.println(memberDao);
        UserEntity byID = memberDao.findByID(userId);
        if(byID==null){
            return setResultError("未查找到用户信息！");
        }
        return setResultSuccess(byID);
    }

    @Override
    public ResponseBase regUser(@RequestBody UserEntity user) {

        //参数验证
        String password = user.getPassword();
        if(StringUtils.isEmpty(password)){
            return setResultError("密码不能为空！");
        }
        String newPassword = MD5Util.MD5(password);
        System.out.println(newPassword);
        user.setPassword(newPassword);
        user.setCreated(new Date());
        user.setUpdated(new Date());

        Integer result = memberDao.insertUser(user);
        if(result<=0){
            return setResultError("注册失败！");
        }
        //采用异步发送邮件
        String email = user.getEmail();
        String json = emailJson(email);
        log.info("########会员推送消息到消息服务平台#####json:{}",json);
        sendMsg(json);
        return setResultSuccess("注册成功！");
    }

    @Override
    public ResponseBase login(@RequestBody UserEntity user) {
        //1验证参数
        String username = user.getUsername();
        if(StringUtils.isEmpty(username)){
            return setResultError("用户名称不能为空");
        }
        String password = user.getPassword();
        if(StringUtils.isEmpty(password)){
            return setResultError("密码不能为空");
        }
        //2数据库查找账号密码是否正确
        String newPassword = MD5Util.MD5(password);
        UserEntity userEntity = memberDao.login(username, newPassword);
        if(userEntity==null){
            return setResultError("账号密码不正确");
        }
        return createLogin(userEntity);

    }

    @Override
    public ResponseBase findByTokenMember(@RequestParam("token")String token) {
        //1验证参数
        if(StringUtils.isEmpty(token)){
            return setResultError("token不能为空");
        }
        //2从reids中通过token找出userId
        String userIdStr = (String) baseRedisService.getString(token);
        if(StringUtils.isEmpty(userIdStr)){
            return setResultError("token无效或者已经过期");
        }
        //3通过userId从数据库中查找用户信息
        Long userId = Long.parseLong(userIdStr);
        UserEntity user = memberDao.findByID(userId);
        if(user==null){
            return setResultError("未查找到该用户信息");
        }
        user.setPassword(null);
        return setResultSuccess(user);
    }

    @Override
    public ResponseBase findOpenIdUser(@RequestParam("openId") String openId) {
        //1验证参数
        if (StringUtils.isEmpty(openId)){
            return setResultError("openId不能为空");
        }
        //2使用openid查询数据库user对象
        UserEntity openIdUser = memberDao.findOpenIdUser(openId);
        if(openIdUser==null){
            return setResultError(Constants.HTTP_RES_CODE_410,"该openId没有关联");
        }
        //3自动登录
        return createLogin(openIdUser);
    }

    @Override
    public ResponseBase QQlogin(@RequestBody UserEntity user) {
        //1验证参数
        String openId = user.getOpenId();
        if(StringUtils.isEmpty(openId)){
            return setResultError("openId不能为空");
        }
        //2先进行登入
        ResponseBase base = login(user);
        if(!base.getRtnCode().equals(Constants.HTTP_RES_CODE_200)){
            return base;
        }
        //3如果登入成功，数据库修改openid
        JSONObject data = (JSONObject) base.getData();
        //4获取token
        String memberToken = data.getString("memberToken");
        ResponseBase tokenMember = findByTokenMember(memberToken);
        if(!tokenMember.getRtnCode().equals(Constants.HTTP_RES_CODE_200)){
            return tokenMember;
        }
        UserEntity userEntity = (UserEntity) tokenMember.getData();
        Integer userId = userEntity.getId();
        Integer result = memberDao.updateByOpenIdUser(openId, userId);
        if(result<=0){
            return setResultError("QQ关联失败");
        }
        return base;
    }
    private ResponseBase createLogin(UserEntity userEntity){
        //3如果账号密码正确对应生成token
        String memberToken = TokenUtils.getMemberToken();
        //4存放在redis中  key为token  value为userId
        Integer userId = userEntity.getId();
        baseRedisService.setString(memberToken,userId+"",Constants.MEMBER_TOKEN_TIME);
        //5直接返回token
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("memberToken",memberToken);
        return setResultSuccess(jsonObject);
    }

    private String emailJson(String email){
        JSONObject root = new JSONObject();
        JSONObject header = new JSONObject();
        header.put("interfaceType", "sms_mail");
        JSONObject content = new JSONObject();
        content.put("email", email);
        root.put("header", header);
        root.put("content", content);
        return root.toJSONString();
    }
    private void sendMsg(String json){
        System.out.println(MESAGEQUEUE);
        ActiveMQQueue activeMQQueue = new ActiveMQQueue(MESAGEQUEUE);

        registerMailboxProducer.sendMsg(activeMQQueue,json);

    }
}
