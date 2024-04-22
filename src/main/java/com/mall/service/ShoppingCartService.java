package com.mall.service;

import com.mall.constants.Constants;
import com.mall.constants.ResultEnum;
import com.mall.dao.GoodsInfoMapper;
import com.mall.dao.ShoppingCartItemMapper;
import com.mall.model.GoodsInfo;
import com.mall.model.ShoppingCartItem;
import com.mall.model.User;
import com.mall.vo.CartVo;
import com.mall.vo.JsonResult;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ShoppingCartService {

    @Resource
    private ShoppingCartItemMapper shoppingCartItemMapper;

    @Resource
    private GoodsInfoMapper goodsInfoMapper;
    public JsonResult<String> addCart(ShoppingCartItem cart, HttpSession session){

        ShoppingCartItem item = shoppingCartItemMapper.selectByUserIdAndGoodId(cart.getUserId(), cart.getGoodsId());

        if(item != null){
            item.setGoodsCount(item.getGoodsCount() + cart.getGoodsCount());
            shoppingCartItemMapper.updateByPrimaryKey(item);
            return JsonResult.success();
        }
        cart.setIsDeleted((byte)0);
        cart.setCreateTime(new Date());
        cart.setUpdateTime(new Date());

        shoppingCartItemMapper.insert(cart);

        Object o = session.getAttribute(Constants.SHOP_CART_COUNT_KEY);

        int count = shoppingCartItemMapper.selectCountByUserId(cart.getUserId());

        session.setAttribute(Constants.SHOP_CART_COUNT_KEY,count);

        return JsonResult.success();
    }
    public List<CartVo> cartList(HttpSession session){

        User user =(User)session.getAttribute(Constants.LOGIN_USER_KEY);

        List<ShoppingCartItem> items = shoppingCartItemMapper.selectCartByUserId(user.getUserId());

        if(CollectionUtils.isEmpty(items)){
            return null;
        }

        List<Long> goodsIds = items.stream().map(ShoppingCartItem::getGoodsId).collect(Collectors.toList());

        List<GoodsInfo> goodsInfoList = goodsInfoMapper.selectByIds(goodsIds);

        Map<Long,GoodsInfo> goodsMap = new HashMap<>();
        for(GoodsInfo gi : goodsInfoList){
            goodsMap.put(gi.getGoodsId(),gi);
        }

        List<CartVo> result = new ArrayList<>();
        for(ShoppingCartItem sci: items){
            CartVo vo = new CartVo();
            vo.setCartItemId(sci.getCartItemId());
            vo.setGoodsCount(sci.getGoodsCount());
            GoodsInfo goods = goodsMap.get(sci.getGoodsId());
            vo.setGoodsId(goods.getGoodsId());
            vo.setGoodsName(goods.getGoodsName());
            vo.setGoodsCoverImg(goods.getGoodsCoverImg());
            vo.setSellingPrice(goods.getSellingPrice());
            result.add(vo);
        }
        return result;
    }

    public boolean updateCartCount(long cartId,int count){
        ShoppingCartItem item = new ShoppingCartItem();
        item.setCartItemId(cartId);
        item.setGoodsCount(count);

        int i = shoppingCartItemMapper.updateByPrimaryKeySelective(item);
        return i>0;
    }

    public JsonResult deleteCart(long cartId,HttpSession session){
        ShoppingCartItem item = new ShoppingCartItem();
        item.setCartItemId(cartId);
        item.setIsDeleted((byte)1);
        int i = shoppingCartItemMapper.updateByPrimaryKeySelective(item);
        if (i > 0) {
            session.setAttribute(Constants.SHOP_CART_COUNT_KEY,(int)session.getAttribute(Constants.SHOP_CART_COUNT_KEY)-1);
            return JsonResult.success();
        }
        return JsonResult.fail(ResultEnum.SYSTEM_ERROR);
    }
}
