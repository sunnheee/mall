package com.mall.dao;

import com.mall.model.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    @Select("select * from tb_user where login_name=#{name} and password_md5=#{password}")
    User selectByNameAndPassword(@Param("name") String name,@Param("password") String password);

    @Select("select * from tb_user where login_name=#{name}")
    User selectByLoginName(String name);

    @Select("select * from tb_user limit #{offset},#{limit}")
    List<User> selectByPage(@Param("offset") int offset, @Param("limit")int limit);

    int lockByIds(@Param("lockStatus") int lockStatus,
                  @Param("ids") List<Long> ids);

    @Select("select count(*) from tb_user")
    int selectCount();

    @Select("select * from tb_user where nick_name=#{nickName} and login_name=#{loginUserName}")
    User selectByNameAndNickName(@Param("nickName")String nickName,@Param("loginUserName")String loginUserName);
}