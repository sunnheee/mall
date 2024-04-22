package com.mall.model;

import lombok.Data;

import java.util.Date;

@Data
public class Order {
    private Long orderId;

    private String orderNo;

    private Long userId;

    private Integer totalPrice;

    private Byte payStatus;

    private Byte payType;

    private Date payTime;

    private Byte orderStatus;

    private String extraInfo;

    private String userName;

    private String userPhone;

    private String userAddress;

    private Byte isDeleted;

    private Date createTime;

    private Date updateTime;


}