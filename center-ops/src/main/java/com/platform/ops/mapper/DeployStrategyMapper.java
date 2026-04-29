package com.platform.ops.mapper;

import com.platform.ops.entity.DeployStrategyEntity;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface DeployStrategyMapper {
    @Select("SELECT * FROM deploy_strategies")
    List<DeployStrategyEntity> findAll();

    @Select("SELECT * FROM deploy_strategies WHERE id = #{id}")
    DeployStrategyEntity findById(Long id);

    @Insert("INSERT INTO deploy_strategies (#{columnList}) VALUES (#{valueList})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(DeployStrategyEntity entity);

    @Update("UPDATE deploy_strategies SET #{setClause} WHERE id = #{id}")
    int update(DeployStrategyEntity entity);

    @Delete("DELETE FROM deploy_strategies WHERE id = #{id}")
    int delete(Long id);
    DeployStrategyEntity findByAppIdAndEnvId(@Param("appId") Long appId, @Param("envId") Long envId);

}