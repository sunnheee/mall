package com.mall.model;

import lombok.Data;

import java.util.Date;

@Data
public class Category {

    private int categoryId;
    private int categoryLevel;
    private int parentId;
    private String categoryName;
    private int categoryRank;
    private byte isDeleted;
    private Date createTime;
    private int createUser;
    private Date updateTime;
    private int updateUser;



}
