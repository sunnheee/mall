package com.mall.dao;

import com.mall.model.IndexConfig;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IndexConfigMapper {
    int deleteByPrimaryKey(Long configId);

    int insert(IndexConfig record);

    int insertSelective(IndexConfig record);

    IndexConfig selectByPrimaryKey(Long configId);

    int updateByPrimaryKeySelective(IndexConfig record);

    int updateByPrimaryKey(IndexConfig record);

    @Select("select * from tb_index_config where config_type=#{type} and is_deleted=0 order by config_rank desc limit #{count}")
    //根据数据类型和数量查询首页配置项信息
    List<IndexConfig> selectByTypeAndCount(@Param("type") int type,@Param("count") int count);


    @Select("select * from tb_index_config where config_type=#{configType} limit #{offset},#{limit}")
    List<IndexConfig> selectByPage(@Param("configType")byte configType,
                                    @Param("offset") int offset,
                                    @Param("limit") int limit);
    @Select("select count(*) from tb_index_config where config_type=#{configType}")
    int selectCount(byte configType);
}