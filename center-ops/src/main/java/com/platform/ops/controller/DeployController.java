package com.platform.ops.controller;

import com.platform.ops.entity.DeployTaskEntity;
import com.platform.ops.log.LogAnn;
import com.platform.ops.mapper.DeployTaskMapper;
import com.platform.ops.service.DeployService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Transactional
@RestController
@RequestMapping("/api/deployments")
public class DeployController {
    private static final Logger log = LoggerFactory.getLogger(DeployController.class);
    @Autowired
    private DeployService deployService;

    // 注入 DeployTaskMapper
    @Autowired
    private DeployTaskMapper deployTaskMapper;

    @GetMapping("/applications/{appId}/versions")
    public List<String> getVersions(@PathVariable Long appId) {
        // 从 deploy_tasks 表中查询该应用所有状态为 COMPLETED 的版本，去重、按时间倒序
        return deployTaskMapper.findCompletedVersionsByAppId(appId);
    }

    @LogAnn(key = "api/deployments/create")
    @PostMapping
    public DeployTaskEntity create(@RequestBody DeployTaskEntity task) {

        return deployService.createDeployTask(task);
    }

    // api/deployments
    @GetMapping
    public List<DeployTaskEntity> list(@RequestParam(required = false) Long appId,
                                       @RequestParam(required = false) Long envId) {
        return deployService.listTasks(appId, envId);
    }

    @PostMapping("/{id}/rollback")
    public String rollback(@PathVariable Long id) {
        deployService.rollbackDeploy(id);
        return "ok";
    }
}