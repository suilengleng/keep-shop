package xin.keep.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import xin.keep.base.ResponseBase;
import xin.keep.constants.Constants;
import xin.keep.entity.UserEntity;
import xin.keep.feign.MemberServiceFeign;

import java.util.Date;

@Controller
public class RegistController {
    private static final String REGIST = "regist";
    private static final String LOGIN = "login";
    @Autowired
    private MemberServiceFeign memberServiceFeign;
    //跳转注册页面
    @RequestMapping(value = "/toRegist",method = RequestMethod.GET)
    public String toRegist(){
        return REGIST;
    }
    @RequestMapping(value = "/registPost",method = RequestMethod.POST)
    public String registPost(UserEntity userEntity){
        //验证参数
        //调用会员注册接口

        ResponseBase responseBase = memberServiceFeign.regUser(userEntity);
        if(!responseBase.getRtnCode().equals(Constants.HTTP_RES_CODE_200)){
            return REGIST;
        }
        //失败到失败页面
        //成功到成功页面
        return LOGIN;
    }
}
