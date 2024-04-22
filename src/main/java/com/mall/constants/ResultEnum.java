package com.mall.constants;


public enum ResultEnum {

    SUCCESS(0,"SUCCESS"),
    SYSTEM_ERROR(1,"fail"),
    NO_ACCESS_RIGHT(2,"没有访问权限"),
    USERNAME_PASSWORD_ERROR(3,"用户名或密码错误"),
    CAPTCHA_ERROR(4,"验证码错误"),
    PHONE_EXISTS(5,"手机号重复"),
    NOT_LOGIN(6,"请登录后操作");

    private int code;
    private String reason;

    public int getCode(){
        return code;
    }

    public String getReason(){
        return reason;
    }

    ResultEnum(int code,String reason){
        this.code = code;
        this.reason = reason;
    }


}
