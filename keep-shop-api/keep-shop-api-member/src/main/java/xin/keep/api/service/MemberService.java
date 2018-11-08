package xin.keep.api.service;

import org.springframework.web.bind.annotation.*;
import xin.keep.base.ResponseBase;
import xin.keep.entity.UserEntity;
@RequestMapping("/member")
public interface MemberService {
    @RequestMapping(value = "/findByUserId")
    public ResponseBase findUserById(Long userId);
    @RequestMapping(value = "/regUser")
    ResponseBase regUser(@RequestBody UserEntity user);
    @RequestMapping("/login")
    ResponseBase login(@RequestBody UserEntity user);
    //使用token来查找用户信息
    @RequestMapping("/findByTokenMember")
    ResponseBase findByTokenMember(@RequestParam("token") String token);
}
