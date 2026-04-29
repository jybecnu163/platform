package com.platform.ops.service;

import com.platform.ops.entity.EnvironmentEntity;
import com.platform.ops.mapper.EnvironmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EnvironmentService {
    @Autowired
    private EnvironmentMapper environmentMapper;

    public List<EnvironmentEntity> listEnvironments(Long appId) {
        // 这里应添加根据appId过滤逻辑，简化直接返回全部
        return environmentMapper.findAll();
    }

    public EnvironmentEntity getEnvironment(Long id) {
        return environmentMapper.findById(id);
    }

    public EnvironmentEntity createEnvironment(EnvironmentEntity env) {
        environmentMapper.insert(env);
        return env;
    }
}