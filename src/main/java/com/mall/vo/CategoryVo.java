package com.mall.vo;


import lombok.Data;

import java.util.List;

@Data
public class CategoryVo {
    private String categoryName;
    private List<CategoryVo> secondCategory;
    private int categoryId;
}
