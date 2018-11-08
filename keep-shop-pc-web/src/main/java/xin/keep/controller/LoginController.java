package xin.keep.controller;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import xin.keep.base.ResponseBase;
import xin.keep.constants.Constants;
import xin.keep.entity.UserEntity;
import xin.keep.feign.MemberServiceFeign;
import xin.keep.utils.CookieUtil;

import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;

@Controller
public class LoginController {
    private static final String LOGIN = "login";
    private static final String INDEX = "redirect:/";

    @Autowired
    private MemberServiceFeign memberServiceFeign;
    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String toLogin(){
        return LOGIN;
    }
    @RequestMapping(value = "/loginPost",method = RequestMethod.POST)
    public String loginPost(UserEntity user, HttpServletRequest request, HttpServletResponse response){
        //1验证参数
        //2调用登入接口，获取token信息
        ResponseBase loginResponse = memberServiceFeign.login(user);
        if(!loginResponse.getRtnCode().equals(Constants.HTTP_RES_CODE_200)){
            //跳转报错页面
            request.setAttribute("error","账号密码错误！");
            return LOGIN;
        }

        LinkedHashMap data = (LinkedHashMap) loginResponse.getData();
        String memberToken = (String) data.get("memberToken");
        if(StringUtils.isEmpty(memberToken)){
            request.setAttribute("error","会话失效！");
            return LOGIN;
        }
        //3把token放在cookie里
        CookieUtil.addCookie(response,Constants.COOKIE_MEMBER_TOKEN,memberToken,Constants.COOKIE_MEMBER_TOKEN_TIME);



        return INDEX;
    }
}
