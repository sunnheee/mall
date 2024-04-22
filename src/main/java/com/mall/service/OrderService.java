package com.mall.service;

import com.mall.constants.Constants;
import com.mall.constants.ResultEnum;
import com.mall.dao.OrderItemMapper;
import com.mall.dao.OrderMapper;
import com.mall.dao.ShoppingCartItemMapper;
import com.mall.model.Order;
import com.mall.model.OrderItem;
import com.mall.model.User;
import com.mall.vo.CartVo;
import com.mall.vo.JsonResult;
import com.mall.vo.OrderDetailVo;
import com.mall.vo.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class OrderService {

    @Resource
    private ShoppingCartItemMapper shoppingCartItemMapper;

    @Resource
    private ShoppingCartService shoppingCartService;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderItemMapper orderItemMapper;

    @Transactional(rollbackFor = Exception.class)
    public OrderDetailVo createOrder(HttpSession session){
        User user = (User)session.getAttribute(Constants.LOGIN_USER_KEY);

        log.info("{} 正在创建订单",user.getUserId());

        List<CartVo> voList = shoppingCartService.cartList(session);
        if(CollectionUtils.isEmpty(voList)){
            log.info("{}用户创建订单失败，购物车数据为空",user.getUserId());
            return null;
        }

        int totalPrice = 0;
        for(CartVo vo : voList){
            totalPrice += vo.getSellingPrice() * vo.getGoodsCount();
        }

        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(user.getUserId());
        order.setTotalPrice(totalPrice);
        byte status = 0;
        order.setPayStatus(status);
        order.setOrderStatus(status);
        order.setPayType(status);
        order.setExtraInfo("");
        order.setIsDeleted(status);
        order.setUpdateTime(new Date());
        order.setUserName(user.getNickName());
        order.setUserPhone(user.getLoginName());
        order.setUserAddress(user.getAddress());
        order.setCreateTime(new Date());

        int i = orderMapper.insert(order);
        log.info("{} 用户订单主表插入成功，订单号:{}",user.getUserId(),order.getOrderNo());

        if(i>0){
            List<OrderItem> itemList = new ArrayList<>();
            for(CartVo vo : voList){
                OrderItem item = new OrderItem();
                item.setOrderId(order.getOrderId());
                item.setGoodsId(vo.getGoodsId());
                item.setGoodsName(vo.getGoodsName());
                item.setGoodsCoverImg(vo.getGoodsCoverImg());
                item.setSellingPrice(vo.getSellingPrice());
                item.setGoodsCount(vo.getGoodsCount());
                item.setCreateTime(new Date());
                orderItemMapper.insert(item);
                itemList.add(item);
            }

            log.info("{} 用户订单条目表插入成功，一共{}个条目",user.getUserId(),itemList.size());
            shoppingCartItemMapper.deleteByUserId(user.getUserId());
            session.setAttribute(Constants.SHOP_CART_COUNT_KEY,0);
            log.info("{} 用户购物车清空成功",user.getUserId());
            return merge(order,itemList);
        }
        return null;
    }

    public Order getOrder(String orderNo){
        return orderMapper.selectByOrderNo(orderNo);
    }

    public JsonResult paySuccess(String orderNo, int payType){
        Order order = orderMapper.selectByOrderNo(orderNo);
        order.setPayStatus((byte)1);
        order.setPayType((byte)payType);
        order.setPayTime(new Date());
        order.setOrderStatus((byte)1);
        orderMapper.updateByPrimaryKey(order);
        return JsonResult.success();
    }

    public OrderDetailVo getOrderDetail(String orderNo){
        Order order = orderMapper.selectByOrderNo(orderNo);
        List<OrderItem> items = orderItemMapper.selectByOrderId(order.getOrderId());
        OrderDetailVo vo = merge(order, items);
        return vo;
    }

    public PageResult<OrderDetailVo> myOrders(HttpSession session,int page){

        User user =(User)session.getAttribute(Constants.LOGIN_USER_KEY);

        int count = orderMapper.selectUserOrderCount(user.getUserId());
        int offset = (page-1) * 20;
        int totalPage = count / 20;
        if(totalPage % 20 != 0){
            totalPage++;
        }
        List<Order> orders = orderMapper.selectByUserId(user.getUserId(),offset,20);
        List<OrderDetailVo> list = new ArrayList<>();
        for(Order o : orders){
            List<OrderItem> items = orderItemMapper.selectByOrderId(o.getOrderId());
            OrderDetailVo vo = merge(o, items);
            list.add(vo);
        }
        PageResult<OrderDetailVo> result = new PageResult<>();
        result.setCurrentPage(page);
        result.setTotalPage(totalPage);
        result.setList(list);
        return result;

    }
    public JsonResult orderCancel(String orderNo){
        int i = orderMapper.orderCancel(orderNo);
        if(i > 0){
            return JsonResult.success();
        }
        return JsonResult.fail(ResultEnum.SYSTEM_ERROR);
    }

    private String generateOrderNo(){
        String str = System.currentTimeMillis() + "";
        return str+rand();
    }

    private String rand(){
        String str = "";
        for(int i=0;i<4;i++){
            int rand =(int)(Math.random()*10);
            str = str + rand;
        }
        return str;
    }

    private OrderDetailVo merge(Order order,List<OrderItem> items){
        OrderDetailVo vo = new OrderDetailVo();
        vo.setOrderNo(order.getOrderNo());
        vo.setOrderStatus(order.getOrderStatus());
        vo.setTotalPrice(order.getTotalPrice());
        vo.setUserAddress(order.getUserAddress());
        vo.setCreateTime(order.getCreateTime());
        vo.setOrderStatusString(getStatusString(order.getOrderStatus()));
        vo.setOrderItemVOS(items);
        return vo;
    }
    private String getStatusString(byte status){
        switch (status){
            case 0:
                return "待支付";
            case 1:
                return "已支付";
            case 2:
                return "配货完成";
            case 3:
                return "出库成功";
            case 4:
                return "交易成功";
            default:
                return "交易关闭";
        }
    }
}
