package com.eazybuilder.ci.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author: sanzhang
 * @createTime: 2022/8/17
 * @description:
 **/
@Entity
@Table(name="CI_BUILD_JOB_ARRANGE")
public class BuildJobArrange extends BaseEntry {

    private String name;

    @ManyToOne
    private Team team;

    /**
     * List<BuildJobArrangeChild></>
     */
    @Column(columnDefinition="TEXT")
    private String buildJobListJson;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getBuildJobListJson() {
        return buildJobListJson;
    }

    public void setBuildJobListJson(String buildJobListJson) {
        this.buildJobListJson = buildJobListJson;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
