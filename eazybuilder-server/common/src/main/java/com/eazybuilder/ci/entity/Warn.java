package com.eazybuilder.ci.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.*;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name="CI_WARN")
public class Warn implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2716823743263258997L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",columnDefinition="int(8)")
	private String id;
	
	private Boolean isEnable=false;
	
	@ManyToMany(fetch=FetchType.EAGER)
    @NotFound(action=NotFoundAction.IGNORE)
    private Set<User> receivingUsers;
	
	@ManyToMany(fetch=FetchType.EAGER)
    @NotFound(action=NotFoundAction.IGNORE)
	@Deprecated
    private Set<Team> scanTeams;
	
	@ManyToMany(fetch=FetchType.EAGER)
    @NotFound(action=NotFoundAction.IGNORE)
	private Set<ProjectGroup> scanGroups;
	
	@ManyToMany(fetch=FetchType.EAGER)
    @NotFound(action=NotFoundAction.IGNORE)
    private Set<WarnRule> warnRules;

	private WarnType warnType;

	private String duLiangUrl;

	private DuLiangType duLiangType;

//	private Boolean isEvent=false;
	
	private String cron;
	/**
	 * 下次执行时间
	 */
	private Long nextTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Boolean isEnable) {
		this.isEnable = isEnable;
	}

	public Set<User> getReceivingUsers() {
		return receivingUsers;
	}

	public void setReceivingUsers(Set<User> receivingUsers) {
		this.receivingUsers = receivingUsers;
	}

	public Set<Team> getScanTeams() {
		return scanTeams;
	}

	public void setScanTeams(Set<Team> scanTeams) {
		this.scanTeams = scanTeams;
	}

//	public Boolean getIsEvent() {
//		return isEvent;
//	}
//
//	public void setIsEvent(Boolean isEvent) {
//		this.isEvent = isEvent;
//	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public Long getNextTime() {
		return nextTime;
	}

	public void setNextTime(Long nextTime) {
		this.nextTime = nextTime;
	}

	public Set<WarnRule> getWarnRules() {
		return warnRules;
	}

	public void setWarnRules(Set<WarnRule> warnRules) {
		this.warnRules = warnRules;
	}

	public Set<ProjectGroup> getScanGroups() {
		return scanGroups;
	}

	public void setScanGroups(Set<ProjectGroup> scanGroups) {
		this.scanGroups = scanGroups;
	}

	public WarnType getWarnType() {
		return warnType;
	}

	public void setWarnType(WarnType warnType) {
		this.warnType = warnType;
	}

	public String getDuLiangUrl() {
		return duLiangUrl;
	}

	public void setDuLiangUrl(String duLiangUrl) {
		this.duLiangUrl = duLiangUrl;
	}

    public DuLiangType getDuLiangType() {
        return duLiangType;
    }

    public void setDuLiangType(DuLiangType duLiangType) {
        this.duLiangType = duLiangType;
    }
}
