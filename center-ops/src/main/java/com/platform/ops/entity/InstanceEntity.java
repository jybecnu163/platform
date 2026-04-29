package com.platform.ops.entity;

import java.time.LocalDateTime;

public class InstanceEntity {
    private Long id;
    private Long envId;
    private String ip;
    private String hostname;
    private String status;
    private String agentVersion;
    private LocalDateTime lastHeartbeat;
    private String appVersion;
    private String metadata;

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEnvId() { return envId; }
    public void setEnvId(Long envId) { this.envId = envId; }
    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }
    public String getHostname() { return hostname; }
    public void setHostname(String hostname) { this.hostname = hostname; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getAgentVersion() { return agentVersion; }
    public void setAgentVersion(String agentVersion) { this.agentVersion = agentVersion; }
    public LocalDateTime getLastHeartbeat() { return lastHeartbeat; }
    public void setLastHeartbeat(LocalDateTime lastHeartbeat) { this.lastHeartbeat = lastHeartbeat; }
    public String getAppVersion() { return appVersion; }
    public void setAppVersion(String appVersion) { this.appVersion = appVersion; }
    public String getMetadata() { return metadata; }
    public void setMetadata(String metadata) { this.metadata = metadata; }

    @Override
    public String toString() {
        return "InstanceEntity{" +
                "id=" + id +
                ", envId=" + envId +
                ", ip='" + ip + '\'' +
                ", hostname='" + hostname + '\'' +
                ", status='" + status + '\'' +
                ", agentVersion='" + agentVersion + '\'' +
                ", lastHeartbeat=" + lastHeartbeat +
                ", appVersion='" + appVersion + '\'' +
                ", metadata='" + metadata + '\'' +
                '}';
    }
}