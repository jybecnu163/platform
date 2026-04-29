package com.platform.ops.entity;

import java.time.LocalDateTime;

public class DeployTaskEntity {
    private Long id;
    private Long appId;
    private Long envId;
    private String version;
    private String packageUrl;
    private String status;
    private String rollbackToVersion;
    private String creator;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getEnvId() {
        return envId;
    }

    public void setEnvId(Long envId) {
        this.envId = envId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPackageUrl() {
        return packageUrl;
    }

    public void setPackageUrl(String packageUrl) {
        this.packageUrl = packageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRollbackToVersion() {
        return rollbackToVersion;
    }

    public void setRollbackToVersion(String rollbackToVersion) {
        this.rollbackToVersion = rollbackToVersion;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "DeployTaskEntity{" +
                "id=" + id +
                ", appId=" + appId +
                ", envId=" + envId +
                ", version='" + version + '\'' +
                ", packageUrl='" + packageUrl + '\'' +
                ", status='" + status + '\'' +
                ", rollbackToVersion='" + rollbackToVersion + '\'' +
                ", creator='" + creator + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}