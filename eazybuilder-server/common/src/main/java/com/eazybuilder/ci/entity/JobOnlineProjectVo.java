package com.eazybuilder.ci.entity;

import com.eazybuilder.ci.entity.report.Stage;
import com.eazybuilder.ci.entity.report.Status;
import com.eazybuilder.ci.entity.report.StatusGuard;

import java.util.List;

public class JobOnlineProjectVo {

    private String name;
    private String description;
    private Status status;
    private StatusGuard statusGuard;
    private List<Stage> stages;
    private List<DtpReport> dtpReports;
    private List<Metric> metrics;
    private String logId;
    private String sonarKey;
    private String targetBranch;
    /**
     * 是否包含自动化测试任务
     */
    private boolean  dtpTask;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public StatusGuard getStatusGuard() {
        return statusGuard;
    }

    public void setStatusGuard(StatusGuard statusGuard) {
        this.statusGuard = statusGuard;
    }

    public List<Stage> getStages() {
        return stages;
    }

    public void setStages(List<Stage> stages) {
        this.stages = stages;
    }

    public List<DtpReport> getDtpReports() {
        return dtpReports;
    }

    public void setDtpReports(List<DtpReport> dtpReports) {
        this.dtpReports = dtpReports;
    }

    public List<Metric> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<Metric> metrics) {
        this.metrics = metrics;
    }

    public boolean isDtpTask() {
        return dtpTask;
    }

    public void setDtpTask(boolean dtpTask) {
        this.dtpTask = dtpTask;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getSonarKey() {
        return sonarKey;
    }

    public void setSonarKey(String sonarKey) {
        this.sonarKey = sonarKey;
    }

    public String getTargetBranch() {
        return targetBranch;
    }

    public void setTargetBranch(String targetBranch) {
        this.targetBranch = targetBranch;
    }
}
