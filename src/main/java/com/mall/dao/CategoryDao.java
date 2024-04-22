package com.mall.dao;

import com.mall.model.Category;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface CategoryDao {

    List<Category> selectByLevel(int level);

    @Select("select * from tb_goods_category")
    List<Category> selectAll();

    @Select("select * from tb_goods_category where category_id=#{id} ")
    Category selectById(int id);


    @Select("select * from tb_goods_category where parent_id=#{parentId} and category_level=#{level} limit #{count}")
    List<Category> selectByParentAndLevel(@Param("parentId") int parentId,
                                          @Param("level") int level,
                                          @Param("count") int count);


    @Select("select * from tb_goods_category where parent_id=#{parentId}")
    List<Category> selectByParentId(int parentId);


    @Select("select * from tb_goods_category where parent_id=#{parentId} and category_level=#{level} limit #{offset},#{size}")
    List<Category> selectByParentIdAndPage(@Param("parentId") int parentId,
                                           @Param("level") int level,
                                           @Param("offset") int offset,
                                           @Param("size") int size);


    @Insert("insert into tb_goods_category values(null,#{categoryLevel},#{parentId},#{categoryName},#{categoryRank},0,#{createTime},#{createUser},#{updateTime},#{updateUser})")
    int insert(Category category);

    @Update("update tb_goods_category set is_deleted=1 where category_id=#{id}")
    int deletedCategory(int id);

    int selectCount(@Param("parentId") int parentId,
                    @Param("level") int level);

    int updateByPrimaryKeySelective(Category category);
}
