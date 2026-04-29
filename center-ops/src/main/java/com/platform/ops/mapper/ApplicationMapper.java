package com.platform.ops.mapper;

import com.platform.ops.entity.ApplicationEntity;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ApplicationMapper {
    @Select("SELECT * FROM applications")
    List<ApplicationEntity> findAll();

    @Select("SELECT * FROM applications WHERE id = #{id}")
    ApplicationEntity findById(Long id);

    @Insert("INSERT INTO applications (name, code, owner, repo_url, biz_line) VALUES (#{name}, #{code}, #{owner}, #{repoUrl}, #{bizLine})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ApplicationEntity entity);

    @Update("UPDATE applications SET name = #{name}, code = #{code}, owner = #{owner}, repo_url = #{repoUrl}, biz_line = #{bizLine} WHERE id = #{id}")
    int update(ApplicationEntity entity);

    @Delete("DELETE FROM applications WHERE id = #{id}")
    int delete(Long id);
}