package com.eazybuilder.ga.pojo;

import java.io.Serializable;

public class InitResultPojo implements Serializable {

    private static final long serialVersionUID = -1L;

    private String teamCode;

    private String projectCode;

    private String id;

    private String status;

    public String getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(String teamCode) {
        this.teamCode = teamCode;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
