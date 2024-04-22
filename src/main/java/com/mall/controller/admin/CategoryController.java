package com.mall.controller.admin;

import com.mall.constants.Constants;
import com.mall.dao.CategoryDao;
import com.mall.model.AdminUser;
import com.mall.model.Category;
import com.mall.vo.JsonResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/admin")
public class CategoryController {

    @Resource
    private CategoryDao categoryDao;

    @GetMapping("categories/list")
    public JsonResult list(int categoryLevel, int parentId, int page, int limit){

        Map<String,Object> res = new HashMap<>();
        int offset = (page-1)*limit;
        int count = categoryDao.selectCount(parentId,categoryLevel);
        int totalPage = count/limit;
        if(count%limit>0){
            totalPage++;
        }
        List<Category> list = categoryDao.selectByParentIdAndPage(parentId, categoryLevel,offset,limit);
        res.put("list",list);
        res.put("totalCount",count);
        res.put("totalPage",totalPage);
        return JsonResult.success(res);
    }

    @PostMapping("categories/save")
    public JsonResult add(@RequestBody Category category, HttpSession session){
        AdminUser adminUser = (AdminUser) session.getAttribute(Constants.ADMIN_LOGIN_USER_KEY);
        category.setCreateTime(new Date());
        category.setCreateUser(adminUser.getAdminUserId());
        category.setUpdateTime(new Date());
        categoryDao.insert(category);
        return JsonResult.success();
    }

    @PostMapping("categories/delete")
    public JsonResult delete(@RequestBody List<Integer> ids){
        for(int id:ids){
            categoryDao.deletedCategory(id);
        }
        return JsonResult.success();
    }

    @PostMapping("categories/update")
    public JsonResult update(@RequestBody Category category){
        categoryDao.updateByPrimaryKeySelective(category);
        return JsonResult.success();
    }

}
