package com.platform.ops.entity;

public class EnvironmentEntity {
    private Long id;
    private Long appId;
    private String envType;
    private String nacosNamespace;
    private String scalingGroupId;
    private String isolation;

    // getters and setters (省略，实际脚本会生成完整)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getAppId() { return appId; }
    public void setAppId(Long appId) { this.appId = appId; }
    public String getEnvType() { return envType; }
    public void setEnvType(String envType) { this.envType = envType; }
    public String getNacosNamespace() { return nacosNamespace; }
    public void setNacosNamespace(String nacosNamespace) { this.nacosNamespace = nacosNamespace; }
    public String getScalingGroupId() { return scalingGroupId; }
    public void setScalingGroupId(String scalingGroupId) { this.scalingGroupId = scalingGroupId; }
    public String getIsolation() { return isolation; }
    public void setIsolation(String isolation) { this.isolation = isolation; }

    @Override
    public String toString() {
        return "EnvironmentEntity{" +
                "id=" + id +
                ", appId=" + appId +
                ", envType='" + envType + '\'' +
                ", nacosNamespace='" + nacosNamespace + '\'' +
                ", scalingGroupId='" + scalingGroupId + '\'' +
                ", isolation='" + isolation + '\'' +
                '}';
    }
}