package com.mall.controller.admin;

import com.mall.constants.GlobalConfig;
import com.mall.dao.CategoryDao;
import com.mall.dao.GoodsInfoMapper;
import com.mall.model.Category;
import com.mall.model.GoodsInfo;
import com.mall.vo.JsonResult;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@Controller("adminGoodsController")
@RequestMapping("admin")
public class GoodsController {
    @Resource
    private GoodsInfoMapper goodsInfoMapper;
    @Resource
    private CategoryDao categoryDao;


    @GetMapping("goods/edit")
    public String edit(Model model,@RequestParam(defaultValue = "0") long goodsId){

        List<Category> firstLevel = categoryDao.selectByLevel(1);
        List<Category> secondLevel = categoryDao.selectByLevel(2);
        List<Category> thirdLevel = categoryDao.selectByLevel(3);

        if(goodsId > 0){
            GoodsInfo goods = goodsInfoMapper.selectByPrimaryKey(goodsId);
            //拿到的三级分类id，根据三级，找二级
            Long id3 = goods.getGoodsCategoryId();
            Category cat3 = categoryDao.selectById(id3.intValue());

            int id2 = cat3.getParentId();
            Category cat2 = categoryDao.selectById(id2);

            int id1 = cat2.getParentId();

            model.addAttribute("goods",goods);
            model.addAttribute("firstLevelCategories",id1);
            model.addAttribute("secondLevelCategories",id2);
            model.addAttribute("thirdLevelCategories",id3);

        }

        model.addAttribute("firstLevelCategories",firstLevel);
        model.addAttribute("secondLevelCategories",secondLevel);
        model.addAttribute("thirdLevelCategories",thirdLevel);

        return "admin/mall_goods_edit";

    }


    @GetMapping("categories/listForSelect")
    @ResponseBody
    public JsonResult categoryChanged(int categoryId){

        List<Category> categories = categoryDao.selectByParentId(categoryId);

        if(CollectionUtils.isEmpty(categories)){
            return JsonResult.success();
        }

        Map<String,List<Category>> result = new HashMap<>();
        Category c = categories.get(0);
        if(c.getCategoryLevel() == 2){
            List<Category> third = new ArrayList<>();
            for(Category cat : categories){
                //读取每个二级分类下的三级分类
                List<Category> c3 = categoryDao.selectByParentId(cat.getCategoryId());
                if(CollectionUtils.isNotEmpty(c3)){
                    third.addAll(c3);
                }
            }
            result.put("thirdLevelCategories",third);
            result.put("secondLevelCategories",categories);
        }else{
            result.put("thirdLevelCategories",categories);
        }

        return JsonResult.success(result);
    }

    @PostMapping("goods/save")
    @ResponseBody
    public JsonResult save(@RequestBody GoodsInfo goodsInfo){
        goodsInfo.setCreateTime(new Date());
        goodsInfo.setUpdateTime(new Date());
        goodsInfo.setCreateUser(1);
        goodsInfo.setUpdateUser(1);
        goodsInfoMapper.insert(goodsInfo);
        GlobalConfig.version++;
        return JsonResult.success();
    }

    @GetMapping("goods/list")
    @ResponseBody
    public JsonResult list(int page,int limit){
        Map<String,Object> res = new HashMap<>();
        int offset = (page-1)*limit;
        int count = goodsInfoMapper.selectCount();
        int totalPage = count/limit;
        if(count%limit>0){
            totalPage++;
        }
        List<GoodsInfo> goodsInfos = goodsInfoMapper.selectByPage(offset,limit);
        res.put("list",goodsInfos);
        res.put("totalCount",count);
        res.put("totalPage",totalPage);
        return JsonResult.success(res);
    }

    @PostMapping("goods/update")
    @ResponseBody
    public JsonResult update(@RequestBody GoodsInfo goodsInfo){
        goodsInfoMapper.updateByPrimaryKeySelective(goodsInfo);
        GlobalConfig.version++;
        return JsonResult.success();
    }

    @PutMapping("goods/status/{status}")
    @ResponseBody
    public JsonResult status(@PathVariable("status") int status,
                             @RequestBody List<Long> ids){
        goodsInfoMapper.updateSellStatusByIds(status, ids);
        GlobalConfig.version++;
        return JsonResult.success();
    }

}
