package com.eazybuilder.ci.entity;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
@Entity
@Table(name="CI_TEAM",uniqueConstraints=@UniqueConstraint(columnNames="NAME"))
@Where(clause = "IS_DEL = 0 or IS_DEL is null")
public class Team extends BaseEntry implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6558755145243142175L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",columnDefinition="int(8)")
	private String id;

	private String devopsTeamId;

	/**
	 * upms群组id。
	 */
	private Long groupId;

	/**
	 * 项目编号
	 */
	private String code;
	/**
	 * 项目名称
	 */
	private String name;


	@ManyToMany(fetch=FetchType.EAGER,cascade=CascadeType.ALL)
    @NotFound(action=NotFoundAction.IGNORE)
    private List<User> members;

//	@ManyToMany(fetch=FetchType.EAGER)
//	@NotFound(action=NotFoundAction.IGNORE)
//	@Fetch(FetchMode.SUBSELECT)
	@Transient
	private List<Guard> guards;


//	@OneToMany(fetch=FetchType.EAGER)
	@OneToMany(fetch=FetchType.EAGER,cascade=CascadeType.ALL)
    @NotFound(action=NotFoundAction.IGNORE)
	private Set<TeamThreshold> teamThresholds;


	
    @OneToOne
    private TeamResource teamResource;
		
	public TeamResource getTeamResource() {
		return teamResource;
	}
	public void setTeamResource(TeamResource teamResource) {
		this.teamResource = teamResource;
	}

	public Set<TeamThreshold> getTeamThresholds() {
		return teamThresholds;
	}
	public void setTeamThresholds(Set<TeamThreshold> teamThresholds) {
		this.teamThresholds = teamThresholds;
	}


	/**
	 * 组创建者/拥有者(拥有者和管理员可以修改组（添加/邀请其他用户）)
	 */
	private String ownerId;
	
	/**
	 * 配置管理员
	 */
	@ManyToMany(fetch=FetchType.EAGER)
    @NotFound(action=NotFoundAction.IGNORE)
	@Fetch(FetchMode.SUBSELECT)
	private List<User> configers;

	//是否允许多次提测（true不允许，false允许）
	private Boolean sprintMultiTest;

	//提测时是否校验门禁
	private Boolean checkReleasePipeline;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<User> getMembers() {
		return members;
	}
	public void setMembers(List<User> members) {
		this.members = members;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public List<User> getConfigers() {
		return configers;
	}
	public void setConfigers(List<User> configers) {
		this.configers = configers;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getDevopsTeamId() {
		return devopsTeamId;
	}

	public void setDevopsTeamId(String devopsTeamId) {
		this.devopsTeamId = devopsTeamId;
	}

	public List<Guard> getGuards() {
		return guards;
	}

	public void setGuards(List<Guard> guards) {
		this.guards = guards;
	}

	public Boolean getSprintMultiTest() {
		return sprintMultiTest;
	}

	public void setSprintMultiTest(Boolean sprintMultiTest) {
		this.sprintMultiTest = sprintMultiTest;
	}

	public Boolean getCheckReleasePipeline() {
		return checkReleasePipeline;
	}

	public void setCheckReleasePipeline(Boolean checkReleasePipeline) {
		this.checkReleasePipeline = checkReleasePipeline;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
}
