package com.mall.vo;

import lombok.Data;

@Data
public class UserVo {
    private String loginName;
    private String password;
    private String verifyCode;
}
