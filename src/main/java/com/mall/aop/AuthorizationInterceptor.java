package com.mall.aop;

import com.mall.constants.Constants;
import com.mall.model.User;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
@Aspect
public class AuthorizationInterceptor {

    @Autowired
    private HttpSession httpSession;


    public User getUser(){
        User u = (User) httpSession.getAttribute(Constants.LOGIN_USER_KEY);
        return u;
    }

    @Pointcut("execution(public * com.mall.service.OrderService.*(..))")
    public void orderCut(){
    }

    @Before("orderCut()")
    public void orderBefore(JoinPoint jp){
        User u = getUser();
        String name = jp.getSignature().getName();
        System.out.println(u.getNickName()+"用户正在进行订单逻辑，执行"+name+"方法");
    }

    @After("orderCut()")
    public void orderAfter(JoinPoint jp){
        User u = getUser();
        String name = jp.getSignature().getName();
        System.out.println(u.getNickName()+"用户执行"+name+"方法完成");
    }

}