package com.mall.service;

import com.mall.constants.Constants;
import com.mall.constants.ResultEnum;
import com.mall.dao.AdminUserMapper;
import com.mall.dao.ShoppingCartItemMapper;
import com.mall.dao.UserMapper;
import com.mall.model.AdminUser;
import com.mall.model.User;
import com.mall.util.MD5Util;
import com.mall.vo.AdminUserVo;
import com.mall.vo.JsonResult;
import com.mall.vo.UserVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private AdminUserMapper adminUserMapper;

    @Resource
    private ShoppingCartItemMapper shoppingCartItemMapper;

    public JsonResult login(UserVo vo, HttpSession session){

        String captcha = (String)session.getAttribute(Constants.CAPTCHA_SESSION_KEY);

        if(!captcha.equalsIgnoreCase(vo.getVerifyCode())){
            return JsonResult.fail(ResultEnum.CAPTCHA_ERROR);
        }

        String password = MD5Util.MD5Encode(vo.getPassword(),"UTF-8");

        User user = userMapper.selectByNameAndPassword(vo.getLoginName(), password);
        if(user != null){
            int count = shoppingCartItemMapper.selectCountByUserId(user.getUserId());
            session.setAttribute(Constants.SHOP_CART_COUNT_KEY,count);
            session.setAttribute(Constants.LOGIN_USER_KEY,user);

            return JsonResult.success();
        }

        return JsonResult.fail(ResultEnum.USERNAME_PASSWORD_ERROR);
    }

    public JsonResult register(UserVo vo,HttpSession session){

        Object attr = session.getAttribute(Constants.CAPTCHA_SESSION_KEY);
        String captcha = (String) attr;
        if(!captcha.equalsIgnoreCase(vo.getVerifyCode())){
            return JsonResult.fail(ResultEnum.CAPTCHA_ERROR);
        }
        User tmp = userMapper.selectByLoginName(vo.getLoginName());
        if(tmp != null){
            return JsonResult.fail(ResultEnum.PHONE_EXISTS);
        }

        String password = MD5Util.MD5Encode(vo.getPassword(),"UTF-8");

        User user = new User();
        user.setLoginName(vo.getLoginName());
        user.setPasswordMd5(password);

        userMapper.insertSelective(user);
        return JsonResult.success();
    }

    public boolean updatePersonal(User user){
        userMapper.updateByPrimaryKeySelective(user);

        return true;
    }

    public JsonResult adminLogin(AdminUserVo vo, HttpSession session){

        String captcha = (String)session.getAttribute(Constants.CAPTCHA_SESSION_KEY);
        if(!captcha.equalsIgnoreCase(vo.getVerifyCode())){
            return JsonResult.fail(ResultEnum.CAPTCHA_ERROR);
        }
        String password = MD5Util.MD5Encode(vo.getPassword(),"UTF-8");
        AdminUser adminUser = adminUserMapper.selectByNameAndPassword(vo.getUserName(), password);
        if(adminUser != null){
            session.setAttribute(Constants.ADMIN_LOGIN_USER_KEY,adminUser);
            return JsonResult.success();
        }
        return JsonResult.fail(ResultEnum.USERNAME_PASSWORD_ERROR);
    }
}
