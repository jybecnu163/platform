package com.platform.ops.controller;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.platform.ops.service.InstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/instances")
public class InstanceController {
    @Autowired
    private InstanceService instanceService;

    @GetMapping("/nacos/{envId}")
    public List<Instance> nacosInstances(@PathVariable Long envId) throws Exception {
        return instanceService.getNacosInstances(envId);
    }

    @PostMapping("/sync/{envId}")
    public String sync(@PathVariable Long envId) throws Exception {
        instanceService.syncInstancesFromNacos(envId);
        return "ok";
    }
}