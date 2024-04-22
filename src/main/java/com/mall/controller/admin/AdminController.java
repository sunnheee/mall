package com.mall.controller.admin;

import com.mall.dao.CategoryDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private CategoryDao categoryDao;

    @RequestMapping({"/","index"})
    public String index(){
        return "admin/index";
    }

    @GetMapping("carousels")
    public String carousel(){
        return "admin/mall_carousel";
    }

    @GetMapping("goods")
    public String goods(){
        return "admin/mall_goods";
    }

    @GetMapping("indexConfigs")
    public String indexConfig(Model model,byte configType){
        model.addAttribute("configType",configType);
        return "admin/mall_index_config";
    }

    @GetMapping("users")
    public String users(){
        return "admin/mall_user";
    }

    @GetMapping("orders")
    public String orders(){
        return "admin/mall_order";
    }

    @GetMapping("categories")
    public String category(int parentId,int categoryLevel,int backParentId,Model model){

        model.addAttribute("parentId",parentId);
        model.addAttribute("categoryLevel",categoryLevel);
        model.addAttribute("backParentId",backParentId);

        return "admin/mall_category";
    }

    @GetMapping("profile")
    public String profile(){
        return "admin/profile";
    }
}
