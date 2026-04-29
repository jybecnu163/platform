package com.platform.ops.entity;

public class DeployStrategyEntity {
    private Long id;
    private Long appId;
    private Long envId;
    private String strategyType;
    private String batchConfig;
    private String healthCheckUrl;
    private Boolean autoPromote;
    private Boolean rollbackEnabled;

    // getters and setters (略，实际生成完整)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getAppId() { return appId; }
    public void setAppId(Long appId) { this.appId = appId; }
    public Long getEnvId() { return envId; }
    public void setEnvId(Long envId) { this.envId = envId; }
    public String getStrategyType() { return strategyType; }
    public void setStrategyType(String strategyType) { this.strategyType = strategyType; }
    public String getBatchConfig() { return batchConfig; }
    public void setBatchConfig(String batchConfig) { this.batchConfig = batchConfig; }
    public String getHealthCheckUrl() { return healthCheckUrl; }
    public void setHealthCheckUrl(String healthCheckUrl) { this.healthCheckUrl = healthCheckUrl; }
    public Boolean getAutoPromote() { return autoPromote; }
    public void setAutoPromote(Boolean autoPromote) { this.autoPromote = autoPromote; }
    public Boolean getRollbackEnabled() { return rollbackEnabled; }
    public void setRollbackEnabled(Boolean rollbackEnabled) { this.rollbackEnabled = rollbackEnabled; }

    @Override
    public String toString() {
        return "DeployStrategyEntity{" +
                "id=" + id +
                ", appId=" + appId +
                ", envId=" + envId +
                ", strategyType='" + strategyType + '\'' +
                ", batchConfig='" + batchConfig + '\'' +
                ", healthCheckUrl='" + healthCheckUrl + '\'' +
                ", autoPromote=" + autoPromote +
                ", rollbackEnabled=" + rollbackEnabled +
                '}';
    }
}