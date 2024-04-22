package com.mall.controller.admin;

import com.mall.constants.Constants;
import com.mall.dao.CarouselMapper;
import com.mall.model.AdminUser;
import com.mall.model.Carousel;
import com.mall.vo.JsonResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class CarouselController {

    @Resource
    private CarouselMapper carouselMapper;



    @GetMapping("carousels/list")
    @ResponseBody
    public JsonResult list(int page, int limit){
        Map<String,Object> res = new HashMap<>();
        int offset = (page-1)*limit;
        int count = carouselMapper.selectCount();
        int totalPage = count/limit;
        if(count%limit>0){
            totalPage++;
        }
        List<Carousel> goodsInfos = carouselMapper.selectByPages(offset,limit);
        res.put("list",goodsInfos);
        res.put("totalCount",count);
        res.put("totalPage",totalPage);
        return JsonResult.success(res);
    }

    @PostMapping("carousels/save")
    @ResponseBody
    public JsonResult save(@RequestBody Carousel carousel, HttpSession session){
        AdminUser adminUser = (AdminUser) session.getAttribute(Constants.ADMIN_LOGIN_USER_KEY);
        carousel.setCreateTime(new Date());
        carousel.setUpdateTime(new Date());
        carousel.setCreateUser(adminUser.getAdminUserId());
        carousel.setIsDeleted((byte)0);
        carouselMapper.insert(carousel);
        return JsonResult.success();
    }

    @PostMapping("carousels/update")
    @ResponseBody
    public JsonResult update(@RequestBody Carousel carousel){
      carouselMapper.updateByPrimaryKeySelective(carousel);
      return JsonResult.success();
    }

    @GetMapping("carousels/info/{id}")
    @ResponseBody
    public JsonResult info(@PathVariable("id") int id){
        Carousel car = carouselMapper.selectByPrimaryKey(id);
        return JsonResult.success(car);
    }

    @PostMapping("carousels/delete")
    @ResponseBody
    public JsonResult delete(@RequestBody List<Integer> ids){
        for(int id : ids){
            Carousel c = new Carousel();
            c.setCarouselId(id);
            c.setIsDeleted((byte)1);
            carouselMapper.updateByPrimaryKeySelective(c);
        }
        return JsonResult.success();
    }
}
