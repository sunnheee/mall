package com.mall.controller;

import com.mall.constants.Constants;
import com.mall.constants.ResultEnum;
import com.mall.model.ShoppingCartItem;
import com.mall.model.User;
import com.mall.service.ShoppingCartService;
import com.mall.vo.CartVo;
import com.mall.vo.JsonResult;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ShoppingCartController {

    @Resource
    private ShoppingCartService shoppingCartService;


    @PostMapping("shopCart")
    @ResponseBody
    public JsonResult<String> shopCart(@RequestBody ShoppingCartItem cart, HttpSession session){
        User user =(User) session.getAttribute(Constants.LOGIN_USER_KEY);
        if(user == null){
            return JsonResult.fail(ResultEnum.NOT_LOGIN);
        }
        cart.setUserId(user.getUserId());
        return shoppingCartService.addCart(cart,session);
    }

    @GetMapping("shopCartList")
    public String shopCartList(HttpSession session, Model model){
        List<CartVo> voList = shoppingCartService.cartList(session);
        model.addAttribute("myShoppingCartItems",voList);
        model.addAttribute("itemsTotal",voList == null ? 0 : voList.size());

        int sum = 0;
        if(CollectionUtils.isNotEmpty(voList)) {
            for (CartVo v : voList) {
                sum += v.getSellingPrice() * v.getGoodsCount();
            }
        }
        model.addAttribute("priceTotal",sum);

        return "mall/cart";
    }

    @PutMapping("updateCartCount")
    @ResponseBody
    public JsonResult updateCartCount(@RequestBody CartVo vo){
        boolean b = shoppingCartService.updateCartCount(vo.getCartItemId(), vo.getGoodsCount());
        if(b){
            return JsonResult.success();
        }
        return JsonResult.fail(ResultEnum.SYSTEM_ERROR);

    }

    @GetMapping("deleteCart")
    @ResponseBody
    public JsonResult deleteCart(long id,HttpSession session){
       return shoppingCartService.deleteCart(id,session);
    }

}
