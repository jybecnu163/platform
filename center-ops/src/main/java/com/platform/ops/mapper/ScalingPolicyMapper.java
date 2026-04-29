package com.platform.ops.mapper;

import com.platform.ops.entity.ScalingPolicyEntity;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ScalingPolicyMapper {
    @Select("SELECT * FROM scaling_policies")
    List<ScalingPolicyEntity> findAll();

    @Select("SELECT * FROM scaling_policies WHERE id = #{id}")
    ScalingPolicyEntity findById(Long id);

    @Insert("INSERT INTO scaling_policies (#{columnList}) VALUES (#{valueList})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ScalingPolicyEntity entity);

    @Update("UPDATE scaling_policies SET #{setClause} WHERE id = #{id}")
    int update(ScalingPolicyEntity entity);

    @Delete("DELETE FROM scaling_policies WHERE id = #{id}")
    int delete(Long id);
}