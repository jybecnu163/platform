package com.platform.ops.mapper;

import com.platform.ops.entity.DeployTaskEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DeployTaskMapper {
    @Select("SELECT * FROM deploy_tasks")
    List<DeployTaskEntity> findAll();

    @Select("SELECT * FROM deploy_tasks WHERE id = #{id}")
    DeployTaskEntity findById(Long id);

    @Insert("<script>" +
            "INSERT INTO deploy_tasks " +
            "<trim prefix='(' suffix=')' suffixOverrides=','>" +
            "  <if test='appId != null'>app_id,</if>" +
            "  <if test='envId != null'>env_id,</if>" +
            "  <if test='version != null'>version,</if>" +
            "</trim>" +
            "VALUES " +
            "<trim prefix='(' suffix=')' suffixOverrides=','>" +
            "  <if test='appId != null'>#{appId},</if>" +
            "  <if test='envId != null'>#{envId},</if>" +
            "  <if test='version != null'>#{version},</if>" +
            "</trim>" +
            "</script>")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(DeployTaskEntity entity);

    /**
     * Caused by: java.io.IOException: Failed to parse mapping resource:
     * 'file [D:\IdeaProject\deepseek-dev\platform\center-ops\target\classes\mapper\DeployTaskMapper.xml]'
     * xml 和 java上只能有一个
     */
//    @Update("UPDATE deploy_tasks SET status = #{status} WHERE id = #{id}")
    int update(DeployTaskEntity entity);


    @Delete("DELETE FROM deploy_tasks WHERE id = #{id}")
    int delete(Long id);

    @Select("SELECT DISTINCT version FROM deploy_tasks WHERE app_id = #{appId} AND status = 'COMPLETED' ORDER BY created_at DESC")
    List<String> findCompletedVersionsByAppId(Long appId);


}