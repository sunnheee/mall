package com.mall.dao;

import com.mall.model.ShoppingCartItem;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ShoppingCartItemMapper {
    int deleteByPrimaryKey(Long cartItemId);

    int insert(ShoppingCartItem record);

    int insertSelective(ShoppingCartItem record);

    ShoppingCartItem selectByPrimaryKey(Long cartItemId);

    int updateByPrimaryKeySelective(ShoppingCartItem record);

    int updateByPrimaryKey(ShoppingCartItem record);

    @Select("select count(*) from tb_shopping_cart_item where user_id=#{userId} and is_deleted=0")
    int selectCountByUserId(long userId);

    @Select("select * from tb_shopping_cart_item where user_id=#{userId} and is_deleted=0")
    List<ShoppingCartItem> selectCartByUserId(long userId);

    @Select("select * from tb_shopping_cart_item where user_id=#{userId} and goods_id=#{goodsId} and is_deleted=0")
    ShoppingCartItem selectByUserIdAndGoodId(@Param("userId") long userId,@Param("goodsId") long goodsId);

    @Update("update tb_shopping_cart_item set is_deleted=1 where user_id=#{userId}")
    int deleteByUserId(long userId);
}