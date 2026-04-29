package com.platform.ops.entity;

import java.time.LocalDateTime;

public class ApplicationEntity {
    private Long id;
    private String name;
    private String code;
    private String owner;
    private String repoUrl;
    private String bizLine;
    private LocalDateTime createdAt;

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public String getRepoUrl() { return repoUrl; }
    public void setRepoUrl(String repoUrl) { this.repoUrl = repoUrl; }
    public String getBizLine() { return bizLine; }
    public void setBizLine(String bizLine) { this.bizLine = bizLine; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "ApplicationEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", owner='" + owner + '\'' +
                ", repoUrl='" + repoUrl + '\'' +
                ", bizLine='" + bizLine + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}