package com.mall.vo;


import lombok.Data;

import java.util.List;

@Data
public class SearchPageCategoryVO {

    private String secondLevelCategoryName;
    private List<SearchPageCategoryVO> thirdLevelCategoryList;
    private String currentCategoryName;
    private int categoryId;

}
