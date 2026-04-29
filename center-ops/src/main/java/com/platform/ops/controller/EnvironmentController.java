package com.platform.ops.controller;

import com.platform.ops.entity.EnvironmentEntity;
import com.platform.ops.service.EnvironmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/environments")
public class EnvironmentController {
    @Autowired
    private EnvironmentService environmentService;

    @GetMapping
    public List<EnvironmentEntity> list(@RequestParam(required = false) Long appId) {
        return environmentService.listEnvironments(appId);
    }

    @PostMapping
    public EnvironmentEntity create(@RequestBody EnvironmentEntity env) {
        return environmentService.createEnvironment(env);
    }
}