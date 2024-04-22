package com.mall.controller;

import com.mall.constants.Constants;
import com.mall.service.UserService;
import com.mall.vo.JsonResult;
import com.mall.vo.UserVo;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Resource
    private UserService userService;

    @GetMapping("/loginView")
    public String loginView(){
        return "mall/login";
    }

    @GetMapping("registerView")
    public String registerView(){
        return "mall/register";
    }

    @PostMapping("register")
    @ResponseBody
    public JsonResult register(UserVo vo, HttpSession session){
        return userService.register(vo,session);
    }

    @PostMapping("login")
    @ResponseBody//加这个注解返回json格式的数据
    public JsonResult login(UserVo vo, HttpSession session){
        return userService.login(vo,session);
    }

    @GetMapping("captcha")
    public void captcha(HttpServletResponse response, HttpServletRequest request)throws Exception{
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires",0);
        response.setContentType("image/png");

        //三个参数分别是宽、高、位数
        SpecCaptcha captcha = new SpecCaptcha(110,40,4);
        //设置类型 数字和字母组合
        captcha.setCharType(Captcha.TYPE_DEFAULT);
        //设置字体
        captcha.setCharType(Captcha.FONT_9);
        //验证码存入session
        request.getSession().setAttribute(Constants.CAPTCHA_SESSION_KEY,captcha.text().toLowerCase());
        //输出图片流
        captcha.out(response.getOutputStream());
    }

    @GetMapping("logout")
    public String logout(HttpSession session){
        session.invalidate();

        return "redirect:index";
    }

}
