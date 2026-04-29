package com.platform.ops.controller;

import com.platform.ops.entity.ApplicationEntity;
import com.platform.ops.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {
    @Autowired
    private ApplicationService applicationService;

    @GetMapping
    public List<ApplicationEntity> list() {
        return applicationService.listApplications();
    }

    @GetMapping("/{id}")
    public ApplicationEntity get(@PathVariable Long id) {
        return applicationService.getApplication(id);
    }

    @PostMapping
    public ApplicationEntity create(@RequestBody ApplicationEntity app) {
        return applicationService.createApplication(app);
    }

    @PutMapping("/{id}")
    public ApplicationEntity update(@PathVariable Long id, @RequestBody ApplicationEntity app) {
        return applicationService.updateApplication(id, app);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        applicationService.deleteApplication(id);
    }
}