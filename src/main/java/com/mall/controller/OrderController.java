package com.mall.controller;

import com.mall.dao.OrderMapper;
import com.mall.model.Order;
import com.mall.service.OrderService;
import com.mall.service.ShoppingCartService;
import com.mall.vo.CartVo;
import com.mall.vo.JsonResult;
import com.mall.vo.OrderDetailVo;
import com.mall.vo.PageResult;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class OrderController {

    @Resource
    private ShoppingCartService shoppingCartService;

    @Resource
    private OrderService orderService;

    @Resource
    private OrderMapper orderMapper;

    @GetMapping("orderSettle")
    public String orderSettle(HttpSession session, Model model){

        List<CartVo> voList = shoppingCartService.cartList(session);
        model.addAttribute("myShoppingCartItems",voList);

        int sum = 0;
        if(CollectionUtils.isNotEmpty(voList)) {
            for (CartVo v : voList) {
                sum += v.getSellingPrice() * v.getGoodsCount();
            }
        }
        model.addAttribute("priceTotal",sum);

        return "mall/order-settle";
    }

    @GetMapping("createOrder")
    public String createOrder(HttpSession session,Model model){
        OrderDetailVo detail = orderService.createOrder(session);
        model.addAttribute("orderDetailVO",detail);
        return "mall/order-detail";
    }

    @GetMapping("orders/{orderNo}")
    public String Orders(@PathVariable("orderNo") String orderNo,Model model){
        OrderDetailVo vo = orderService.getOrderDetail(orderNo);

        model.addAttribute("orderDetailVO", vo);

        return "mall/order-detail";
    }

    @GetMapping("paySelect")
    public String paySelect(String orderNo,Model model){
        Order order = orderService.getOrder(orderNo);
        model.addAttribute("orderNo",orderNo);
        model.addAttribute("totalPrice",order.getTotalPrice());

        return "mall/pay-select";
    }

    @GetMapping("payPage")
    public String payPage(String orderNo, int payType, Model model){
        Order order = orderService.getOrder(orderNo);

        model.addAttribute("orderNo",orderNo);
        model.addAttribute("totalPrice",order.getTotalPrice());

        if(payType == 1){
            return "mall/alipay";
        }else{
            return "mall/wxpay";
        }
    }

    @GetMapping("paySuccess")
    @ResponseBody
    public JsonResult paySuccess(String orderNo, int payType){
        return orderService.paySuccess(orderNo,payType);
    }

    @GetMapping("orderDetail")
    public String orderDetail(String orderNo,Model model){
        OrderDetailVo vo = orderService.getOrderDetail(orderNo);
        model.addAttribute("orderDetailVO", vo);
        return "mall/order-detail";
    }

    @GetMapping("myOrder")
    public String myOrder(@RequestParam(defaultValue = "1") int page, HttpSession session, Model model){
        PageResult<OrderDetailVo> result = orderService.myOrders(session, page);

        model.addAttribute("orderPageResult",result);
        return "mall/my-orders";
    }

    @PutMapping("orders/{orderNo}/cancel")
    @ResponseBody
    public JsonResult orderCancel(@PathVariable("orderNo")String orderNo){
        return orderService.orderCancel(orderNo);
    }

    @PutMapping("orders/{orderNo}/finish")
    @ResponseBody
    public JsonResult finishOrder(@PathVariable("orderNo")String orderNo){
        orderMapper.orderFinish(orderNo);
        return JsonResult.success();
    }
}
