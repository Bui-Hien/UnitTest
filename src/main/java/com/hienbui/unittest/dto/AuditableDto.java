package com.hienbui.unittest.dto;


import com.hienbui.unittest.domain.AuditableEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class AuditableDto {
    private UUID id;
    private Boolean voided;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private String traceLog;

    public AuditableDto() {
    }

    public AuditableDto(AuditableEntity entity) {
        if (entity == null) return;
        this.id = entity.getId();
        this.voided = entity.getVoided();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
        this.createdBy = entity.getCreatedBy();
        this.updatedBy = entity.getUpdatedBy();
        this.traceLog = entity.getTraceLog();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Boolean getVoided() {
        return voided;
    }

    public void setVoided(Boolean voided) {
        this.voided = voided;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getTraceLog() {
        return traceLog;
    }

    public void setTraceLog(String traceLog) {
        this.traceLog = traceLog;
    }
}