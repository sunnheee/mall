package com.mall.vo;

import com.mall.model.OrderItem;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderDetailVo {

    private String orderNo;
    private int orderStatus;
    private String orderStatusString;
    private Date createTime;
    private String userAddress;
    private String payTypeString;
    private int totalPrice;
    private List<OrderItem> orderItemVOS;

}
