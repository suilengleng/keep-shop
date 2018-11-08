package xin.keep.controller;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import xin.keep.base.ResponseBase;
import xin.keep.constants.Constants;
import xin.keep.feign.MemberServiceFeign;
import xin.keep.utils.CookieUtil;

import java.util.LinkedHashMap;

@Controller
public class IndexController {
    private static final String INDEX = "index";

    @Autowired
    private MemberServiceFeign memberServiceFeign;
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String index(HttpServletRequest request){
        //1从cookie中获得这个token
        String token = CookieUtil.getUid(request, Constants.COOKIE_MEMBER_TOKEN);


        //2存在这个token,调用会员服务接口（使用token查询会员信息）
        if(!StringUtils.isEmpty(token)){
            ResponseBase responseBase = memberServiceFeign.findByTokenMember(token);
            if(responseBase.getRtnCode().equals(Constants.HTTP_RES_CODE_200)){
                LinkedHashMap linkedHashMap = (LinkedHashMap) responseBase.getData();
                String username = (String) linkedHashMap.get("username");
                request.setAttribute("username",username);
            }else {
                //跳转到登录页面
            }

        }

        return INDEX;
    }
}
