package com.mall.controller;

import com.mall.model.GoodsInfo;
import com.mall.service.CategoryService;
import com.mall.service.GoodsInfoService;
import com.mall.vo.PageResult;
import com.mall.vo.SearchCondition;
import com.mall.vo.SearchPageCategoryVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;

@Controller
public class GoodsController {

    @Resource
    private GoodsInfoService goodsInfoService;

    @Resource
    private CategoryService categoryService;

    @GetMapping("goodsDetail/{goodsId}")
    public String goodsDetail(@PathVariable("goodsId") long goodsId, Model model){

        GoodsInfo goods = goodsInfoService.getGoodsById(goodsId);
        model.addAttribute("goodsDetail",goods);

        return "mall/detail";
    }

    @GetMapping("search")
    public String search(SearchCondition sc, Model model){

        PageResult<GoodsInfo> pr = goodsInfoService.search(sc);

        if(StringUtils.isNotBlank(sc.getGoodsCategoryId())){

            int catId = Integer.parseInt(sc.getGoodsCategoryId());
            SearchPageCategoryVO vo = categoryService.getSearchCategory(catId);
            model.addAttribute("searchPageCategoryVO",vo);

        }

        model.addAttribute("pageResult",pr);
        model.addAttribute("keyword",sc.getKeyword());
        model.addAttribute("orderBy",sc.getOrderBy());
        model.addAttribute("goodsCategoryId",sc.getGoodsCategoryId());


        return "mall/search";
    }

}
