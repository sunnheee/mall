package com.mall.controller.admin;

import com.mall.dao.OrderItemMapper;
import com.mall.dao.OrderMapper;
import com.mall.model.Order;
import com.mall.model.OrderItem;
import com.mall.vo.JsonResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@RestController("adminOrderController")
@RequestMapping("/admin")
public class OrderController {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderItemMapper orderItemMapper;

    @GetMapping("orders/list")
    public JsonResult list(int page, int limit){
        Map<String,Object> res = new HashMap<>();
        int offset = (page-1)*limit;
        int count = orderMapper.selectCount();
        int totalPage = count/limit;
        if(count%limit>0){
            totalPage++;
        }
        List<Order> orders = orderMapper.selectByPage(page, limit);
        res.put("list",orders);
        res.put("totalCount",count);
        res.put("totalPage",totalPage);
        return JsonResult.success(res);
    }


    @PostMapping("orders/update")
    @ResponseBody
    public JsonResult update(@RequestBody Order order){
        order.setUpdateTime(new Date());
        orderMapper.updateByPrimaryKeySelective(order);
        return JsonResult.success();
    }

    @PostMapping("orders/checkDone")
    @ResponseBody
    public JsonResult checkDone(@RequestBody List<Long> ids){
        orderMapper.checkDone(ids);
        return JsonResult.success();
    }

    @PostMapping("orders/checkOut")
    @ResponseBody
    public JsonResult checkOut(@RequestBody List<Long> ids){
        orderMapper.checkOut(ids);
        return JsonResult.success();
    }

    @PostMapping("orders/close")
    @ResponseBody
    public JsonResult closeOrder(@RequestBody List<Long> ids){
        orderMapper.close(ids);
        return JsonResult.success();
    }

    @GetMapping("order-items/{orderId}")
    public JsonResult orderItems(@PathVariable("orderId") long orderId){
        List<OrderItem> itemList = orderItemMapper.selectByOrderId(orderId);
        return JsonResult.success(itemList);
    }

}

