package com.mall.vo;

import lombok.Data;

@Data
public class SearchCondition {

    private String keyword;
    private String goodsCategoryId;
    private String orderBy;
    private int page = 1;

}
