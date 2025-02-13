package com.iqiongzhi.SCB.mapper;

import com.iqiongzhi.SCB.data.po.User;
import com.iqiongzhi.SCB.data.po.Sound;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {

    @Select("select id from user where email=#{email}")
    Integer findUserByEmail(String email);
    @Select("select id, username, email, student_id, wechat, bio, avatar_url, bgurl, created_at, updated_at, last_login_at, status from user where id=#{id}")
    User findUserById(String id);
    @Select("select username from user where wechat=#{openid}")
    List<User> findUserByOpenId(String openid);
    @Select("select password from user where username=#{username}")
    String getPasswdByName(String username);
    @Select("select password from user where email=#{email}")
    String getPasswdByEmail(String email);
    @SelectProvider(type = UserSQLProvider.class, method = "buildGetUserIdQuery")
    Integer getUserId(@Param("id") String id, @Param("type") String type);
    @Select("select id, username, email, bio, avatar_url, bgurl, last_login_at, status from user where id=#{id}")
    User getUserInfo(String id);

    @Insert("insert into user (username, password, student_id) values (#{username}, #{password}, #{SDUId})")
    void addUser(String username, String password, String SDUId);
    @Insert("insert into user (username, wechat) values (CONCAT('wx_', #{openid}), #{openid})")
    void addWXUser(String openid);
    @Insert("insert into user (username, email, password) values (#{email}, #{email}, #{password})")
    void addEmailUser(String email, String password);

    @Update("update user set last_login_at = now() where id = #{id}")
    void updateLastLoginTime(int id);

    @UpdateProvider(type = UserSQLProvider.class, method = "updateById")
    void updateById(@Param("id") Integer id, @Param("user") User user);

    @Select("<script>" +
            "SELECT s.id as sound_id, u.username FROM sound s " +
            "JOIN `user` u ON s.user_id = u.id " +
            "WHERE s.id IN " +
            "<foreach item='id' collection='soundIds' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    List<Map<String, Object>> getUsernamesBySoundIds(@Param("soundIds") List<Integer> soundIds);
}
