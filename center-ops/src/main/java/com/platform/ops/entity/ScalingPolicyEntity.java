package com.platform.ops.entity;

public class ScalingPolicyEntity {
    private Long id;
    private Long envId;
    private String metric;
    private Integer thresholdUp;
    private Integer thresholdDown;
    private Integer scaleOutCount;
    private Integer scaleInCount;
    private Integer duration;
    private Integer cooldown;
    private Integer minInstances;
    private Integer maxInstances;

    // getters and setters (略)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEnvId() { return envId; }
    public void setEnvId(Long envId) { this.envId = envId; }
    public String getMetric() { return metric; }
    public void setMetric(String metric) { this.metric = metric; }
    public Integer getThresholdUp() { return thresholdUp; }
    public void setThresholdUp(Integer thresholdUp) { this.thresholdUp = thresholdUp; }
    public Integer getThresholdDown() { return thresholdDown; }
    public void setThresholdDown(Integer thresholdDown) { this.thresholdDown = thresholdDown; }
    public Integer getScaleOutCount() { return scaleOutCount; }
    public void setScaleOutCount(Integer scaleOutCount) { this.scaleOutCount = scaleOutCount; }
    public Integer getScaleInCount() { return scaleInCount; }
    public void setScaleInCount(Integer scaleInCount) { this.scaleInCount = scaleInCount; }
    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }
    public Integer getCooldown() { return cooldown; }
    public void setCooldown(Integer cooldown) { this.cooldown = cooldown; }
    public Integer getMinInstances() { return minInstances; }
    public void setMinInstances(Integer minInstances) { this.minInstances = minInstances; }
    public Integer getMaxInstances() { return maxInstances; }
    public void setMaxInstances(Integer maxInstances) { this.maxInstances = maxInstances; }

    @Override
    public String toString() {
        return "ScalingPolicyEntity{" +
                "id=" + id +
                ", envId=" + envId +
                ", metric='" + metric + '\'' +
                ", thresholdUp=" + thresholdUp +
                ", thresholdDown=" + thresholdDown +
                ", scaleOutCount=" + scaleOutCount +
                ", scaleInCount=" + scaleInCount +
                ", duration=" + duration +
                ", cooldown=" + cooldown +
                ", minInstances=" + minInstances +
                ", maxInstances=" + maxInstances +
                '}';
    }
}