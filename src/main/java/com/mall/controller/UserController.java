package com.mall.controller;

import com.mall.constants.Constants;
import com.mall.dao.UserMapper;
import com.mall.model.User;
import com.mall.service.UserService;
import com.mall.vo.JsonResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserService userService;

    @GetMapping("personal")
    public String personal(){
        return "mall/personal";
    }

    @PostMapping("updateUser")
    @ResponseBody
    public JsonResult updatePersonal(@RequestBody User user, HttpSession session){
        userService.updatePersonal(user);
        user = userMapper.selectByPrimaryKey(user.getUserId());
        session.setAttribute(Constants.LOGIN_USER_KEY,user);
        return JsonResult.success();
    }
    @GetMapping("realnameView")
    public String realnameView(HttpSession session, Model model){
        User user = (User) session.getAttribute(Constants.LOGIN_USER_KEY);
        model.addAttribute("user",user);
        return "mall/realname";
    }


}
