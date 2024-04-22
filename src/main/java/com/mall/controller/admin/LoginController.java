package com.mall.controller.admin;

import com.mall.constants.Constants;
import com.mall.service.UserService;
import com.mall.vo.AdminUserVo;
import com.mall.vo.JsonResult;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller("adminLoginController")
@RequestMapping("admin")
public class LoginController {

    @Resource
    private UserService userService;

    @GetMapping("loginView")
    public String loginView(){
        return "admin/login";
    }

    @PostMapping("login")
    public String login(HttpSession session, AdminUserVo vo){
        JsonResult result = userService.adminLogin(vo, session);
        if(result.getCode()==4) {
            session.setAttribute("errorMsg",result.getMsg());
            return "admin/login";
        }else if(result.getCode()==3){
            session.setAttribute("errorMsg",result.getMsg());
            return "admin/login";
        }else {
            return "admin/index";
        }
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
        return "redirect:loginView";
    }

}
