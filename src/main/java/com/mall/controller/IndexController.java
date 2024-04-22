package com.mall.controller;

import com.mall.model.GoodsInfo;
import com.mall.service.CarouselService;
import com.mall.service.CategoryService;
import com.mall.service.GoodsInfoService;
import com.mall.vo.CarouselVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class IndexController {

    @Resource
    private CategoryService categoryService;

    @Resource
    private CarouselService carouselService;

    @Resource
    private GoodsInfoService goodsInfoService;

    @GetMapping({"/","/index"})
    public String index(Model model){

        List<CarouselVo> carouselVoList = carouselService.selectForIndex();

        List<GoodsInfo> hotGoodsList = goodsInfoService.indexGoods(3, 4);

        List<GoodsInfo> newGoodsList = goodsInfoService.indexGoods(4, 5);

        List<GoodsInfo> recommendGoodsList = goodsInfoService.indexGoods(5, 10);

        model.addAttribute("categorys",categoryService.getCategoryMenu());
        model.addAttribute("carouselList",carouselVoList);
        model.addAttribute("hotGoodsList",hotGoodsList);
        model.addAttribute("newGoodsList",newGoodsList);
        model.addAttribute("recommendGoodsList",recommendGoodsList);
        return "mall/index.html";
    }


}
