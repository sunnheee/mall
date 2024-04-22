package com.mall.interceptor;

import com.mall.constants.Constants;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class AdminLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Object adminUser = session.getAttribute(Constants.ADMIN_LOGIN_USER_KEY);
        if(adminUser == null){
            response.sendRedirect(request.getContextPath() + "/admin/loginView");
            return false;
        }
        return true;
    }
}
