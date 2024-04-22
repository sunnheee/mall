package com.mall.vo;


import lombok.Data;

@Data
public class CartVo {


    private Long cartItemId;
    private Integer goodsCount;
    private String goodsName;
    private String goodsCoverImg;
    private Integer sellingPrice;
    private Long goodsId;


}

