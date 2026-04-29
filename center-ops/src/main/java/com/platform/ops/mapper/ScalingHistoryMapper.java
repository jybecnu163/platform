package com.platform.ops.mapper;

import com.platform.ops.entity.ScalingHistoryEntity;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ScalingHistoryMapper {
    @Select("SELECT * FROM scalinghistory")
    List<ScalingHistoryEntity> findAll();

    @Select("SELECT * FROM scalinghistory WHERE id = #{id}")
    ScalingHistoryEntity findById(Long id);

    @Insert("INSERT INTO scalinghistory (#{columnList}) VALUES (#{valueList})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ScalingHistoryEntity entity);

    @Update("UPDATE scalinghistory SET #{setClause} WHERE id = #{id}")
    int update(ScalingHistoryEntity entity);

    @Delete("DELETE FROM scalinghistory WHERE id = #{id}")
    int delete(Long id);
}