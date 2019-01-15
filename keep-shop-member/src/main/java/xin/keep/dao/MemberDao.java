package xin.keep.dao;

import org.apache.ibatis.annotations.*;
import xin.keep.entity.UserEntity;

@Mapper
public interface MemberDao {
    @Select("select  id,username,password,phone,email,openId,created,updated from mb_user where id =#{userId}")
    UserEntity findByID(@Param("userId") Long userId);

    @Insert("INSERT  INTO `mb_user`  (username,password,phone,email,openId,created,updated) VALUES (#{username}, #{password},#{phone},#{email},#{created},#{updated});")
    Integer insertUser(UserEntity userEntity);

    @Select("select  id,username,password,phone,email,openId,created,updated from mb_user where username =#{username} and password = #{password}")
    UserEntity login(@Param("username") String username,@Param("password") String password);
    @Select("select  id,username,password,phone,email,openId,created,updated from mb_user where openId =#{openId}")
    UserEntity findOpenIdUser(@Param("openId") String openId);
    @Update("update mb_user set openId = #{openId} where id = #{userId}")
    Integer updateByOpenIdUser(@Param("openId")String openId,@Param("userId")Integer userId);
}
