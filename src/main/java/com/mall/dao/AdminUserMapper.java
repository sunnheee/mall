package com.mall.dao;

import com.mall.model.AdminUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface AdminUserMapper {
    int deleteByPrimaryKey(Integer adminUserId);

    int insert(AdminUser record);

    int insertSelective(AdminUser record);

    AdminUser selectByPrimaryKey(Integer adminUserId);

    int updateByPrimaryKeySelective(AdminUser record);

    int updateByPrimaryKey(AdminUser record);

    @Select("select * from tb_admin_user where login_user_name=#{name} and login_password=#{password}")
    AdminUser selectByNameAndPassword(@Param("name") String name, @Param("password") String password);
}