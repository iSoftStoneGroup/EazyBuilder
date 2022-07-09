package com.eazybuilder.ci.entity;


import java.util.List;

public class BuildJobVo extends BuildJob{
    private List<JobOnlineProjectVo> jobOnlineProjectVos;

    public List<JobOnlineProjectVo> getJobOnlineProjectVos() {
        return jobOnlineProjectVos;
    }

    public void setJobOnlineProjectVos(List<JobOnlineProjectVo> jobOnlineProjectVos) {
        this.jobOnlineProjectVos = jobOnlineProjectVos;
    }
}
