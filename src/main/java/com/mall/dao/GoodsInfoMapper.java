package com.mall.dao;

import com.mall.model.GoodsInfo;
import com.mall.vo.SearchCondition;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface GoodsInfoMapper {
    int deleteByPrimaryKey(Long goodsId);

    int insert(GoodsInfo record);

    int insertSelective(GoodsInfo record);

    GoodsInfo selectByPrimaryKey(Long goodsId);

    int updateByPrimaryKeySelective(GoodsInfo record);

    int updateByPrimaryKeyWithBLOBs(GoodsInfo record);

    int updateByPrimaryKey(GoodsInfo record);

    List<GoodsInfo> selectByIds(List<Long> idList);

    List<GoodsInfo> selectByCondition(@Param("sc") SearchCondition sc,
                                      @Param("offset") int offset,
                                      @Param("size")int size);

    //查询符合条件的数据数量，在数据分页处使用
    int selectCountByCondition(SearchCondition sc);

    @Select("select * from tb_goods_info order by goods_id desc limit #{offset},#{size}")
    List<GoodsInfo> selectByPage(@Param("offset") int offset,
                                 @Param("size") int size);

//    @Select("select * from tb_goods_info order by goods_id desc")
//    List<GoodsInfo> selectByPage();

    @Select("select count(*) from tb_goods_info")
    int selectCount();

    int updateSellStatusByIds(@Param("status") int status,@Param("ids") List<Long> ids);
}