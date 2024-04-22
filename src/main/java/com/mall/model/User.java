package com.mall.model;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private Long userId;

    private String nickName;

    private String loginName;

    private String passwordMd5;

    private String introduceSign;

    private String address;

    private Byte isDeleted;

    private Byte lockedFlag;

    private Date createTime;

    //实名状态
    private Byte certification;

    //是否是个人商家
    private Byte isBusiness;
}