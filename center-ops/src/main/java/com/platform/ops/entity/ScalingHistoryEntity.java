package com.platform.ops.entity;

import java.time.LocalDateTime;

public class ScalingHistoryEntity {
    private Long id;
    private Long envId;
    private String action;
    private Integer oldCount;
    private Integer newCount;
    private String triggeredBy;
    private String status;
    private String detail;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

    // getters and setters (略)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEnvId() { return envId; }
    public void setEnvId(Long envId) { this.envId = envId; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public Integer getOldCount() { return oldCount; }
    public void setOldCount(Integer oldCount) { this.oldCount = oldCount; }
    public Integer getNewCount() { return newCount; }
    public void setNewCount(Integer newCount) { this.newCount = newCount; }
    public String getTriggeredBy() { return triggeredBy; }
    public void setTriggeredBy(String triggeredBy) { this.triggeredBy = triggeredBy; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDetail() { return detail; }
    public void setDetail(String detail) { this.detail = detail; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
    public LocalDateTime getFinishedAt() { return finishedAt; }
    public void setFinishedAt(LocalDateTime finishedAt) { this.finishedAt = finishedAt; }

    @Override
    public String toString() {
        return "ScalingHistoryEntity{" +
                "id=" + id +
                ", envId=" + envId +
                ", action='" + action + '\'' +
                ", oldCount=" + oldCount +
                ", newCount=" + newCount +
                ", triggeredBy='" + triggeredBy + '\'' +
                ", status='" + status + '\'' +
                ", detail='" + detail + '\'' +
                ", startedAt=" + startedAt +
                ", finishedAt=" + finishedAt +
                '}';
    }
}