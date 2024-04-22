package com.mall.dao;

import com.mall.model.OrderItem;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface OrderItemMapper {
    int deleteByPrimaryKey(Long orderItemId);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Long orderItemId);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);

    @Select("select * from tb_order_item where order_id=#{orderId}")
    List<OrderItem> selectByOrderId(Long orderId);

}