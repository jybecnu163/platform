package com.platform.ops.mapper;

import com.platform.ops.entity.InstanceEntity;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface InstanceMapper {
    @Select("SELECT * FROM instances")
    List<InstanceEntity> findAll();

    @Select("SELECT * FROM instances WHERE id = #{id}")
    InstanceEntity findById(Long id);

    @Insert("INSERT INTO instances (#{columnList}) VALUES (#{valueList})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(InstanceEntity entity);

    @Update("UPDATE instances SET #{setClause} WHERE id = #{id}")
    int update(InstanceEntity entity);

    @Delete("DELETE FROM instances WHERE id = #{id}")
    int delete(Long id);
}