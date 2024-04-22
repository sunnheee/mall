package com.mall.dao;

import com.mall.model.Carousel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CarouselMapper {
    int deleteByPrimaryKey(Integer carouselId);

    int insert(Carousel record);

    int insertSelective(Carousel record);

    Carousel selectByPrimaryKey(Integer carouselId);

    int updateByPrimaryKeySelective(Carousel record);

    int updateByPrimaryKey(Carousel record);

    @Select("select * from tb_carousel where is_deleted=0 order by carousel_rank desc limit 3")
    List<Carousel> selectForIndex();

    @Select("select * from tb_carousel limit #{offset},#{size}")
    List<Carousel> selectByPages(@Param("offset") int offset,
                                 @Param("size") int size);

    @Select("select count(*) from tb_carousel")
    int selectCount();
}