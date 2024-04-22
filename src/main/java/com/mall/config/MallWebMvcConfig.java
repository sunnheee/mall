package com.mall.config;

import com.mall.interceptor.AdminLoginInterceptor;
import com.mall.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class MallWebMvcConfig implements WebMvcConfigurer {

    @Resource
    private LoginInterceptor loginInterceptor;

    @Resource
    private AdminLoginInterceptor adminLoginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns("/")
                .excludePathPatterns("/loginView")
                .excludePathPatterns("/login")
                .excludePathPatterns("/index")
                .excludePathPatterns("/registerView")
                .excludePathPatterns("/register")
                .excludePathPatterns("/captcha")
//                .excludePathPatterns("/", "/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg",
//                "/**/*.jpeg", "/**/*.gif", "/**/fonts/*", "/**/*.svg","/**/*.png")
                .excludePathPatterns("/mall/**")
                .excludePathPatterns("/admin/**");

        registry.addInterceptor(adminLoginInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/loginView")
                .excludePathPatterns("/admin/captcha")
                .excludePathPatterns("/admin/login")
                .excludePathPatterns("/mall/**")
                .excludePathPatterns("/", "/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg",
                "/**/*.jpeg", "/**/*.gif", "/**/fonts/*", "/**/*.svg","/**/*.png");
    }
}
