package com.mall.dao;

import com.mall.model.Order;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Long orderId);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Long orderId);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    @Select("select * from tb_order where order_no=#{orderNo}")
    Order selectByOrderNo(String orderNo);

    @Select("select * from tb_order where user_id=#{userId} and is_deleted=0 order by order_id desc limit #{offset},#{size}")
    List<Order> selectByUserId(@Param("userId") long userId,
                               @Param("offset") int offset,
                               @Param("size") int size);

    @Select("select count(*) from tb_order where user_id=#{userId} and is_deleted=0 ")
    int selectUserOrderCount(long userId);

    @Select("select * from tb_order order by order_id asc limit #{offset},#{limit}")
    List<Order> selectByPage(@Param("offset") int offset,@Param("limit") int limit);


    int checkDone(List<Long> ids);

    int checkOut(List<Long> ids);

    int close(List<Long> ids);

    @Update("update tb_order set is_deleted=1 where order_no =#{orderNo}")
    int orderCancel(String orderNo);

    @Select("select count(*) from tb_order")
    int selectCount();

    @Update("update tb_order set order_status=4 where order_no =#{orderNo}")
    int orderFinish(String orderNo);
}