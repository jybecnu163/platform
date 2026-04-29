package com.platform.ops.service;

import com.platform.ops.entity.ApplicationEntity;
import com.platform.ops.mapper.ApplicationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ApplicationService {
    @Autowired
    private ApplicationMapper applicationMapper;

    public List<ApplicationEntity> listApplications() {
        return applicationMapper.findAll();
    }

    public ApplicationEntity getApplication(Long id) {
        return applicationMapper.findById(id);
    }

    public ApplicationEntity createApplication(ApplicationEntity app) {
        applicationMapper.insert(app);
        return app;
    }

    public ApplicationEntity updateApplication(Long id, ApplicationEntity app) {
        app.setId(id);
        applicationMapper.update(app);
        return app;
    }

    public void deleteApplication(Long id) {
        applicationMapper.delete(id);
    }
}