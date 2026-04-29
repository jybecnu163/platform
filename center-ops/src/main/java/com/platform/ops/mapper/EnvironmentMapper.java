package com.platform.ops.mapper;

import com.platform.ops.entity.EnvironmentEntity;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface EnvironmentMapper {
    @Select("SELECT * FROM environments")
    List<EnvironmentEntity> findAll();

    @Select("SELECT * FROM environments WHERE id = #{id}")
    EnvironmentEntity findById(Long id);

    @Insert("INSERT INTO environments (#{columnList}) VALUES (#{valueList})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(EnvironmentEntity entity);

    @Update("UPDATE environments SET #{setClause} WHERE id = #{id}")
    int update(EnvironmentEntity entity);

    @Delete("DELETE FROM environments WHERE id = #{id}")
    int delete(Long id);
}