package com.eazybuilder.ci.entity.devops;

import com.eazybuilder.ci.entity.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name="CI_DEVEOPS_TEAM")
@Where(clause = "IS_DEL = 0 or IS_DEL is null")
public class DevopsInit extends BaseEntry implements Serializable{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(columnDefinition="int(8)")
	private String id;

	@OneToOne(cascade=CascadeType.ALL)
	private DingMsgProfile dingMsgProfile;

	@OneToOne(cascade=CascadeType.ALL)
	private MailMsgProfile mailMsgProfile;

	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@NotFound(action= NotFoundAction.IGNORE)
	private Set<ProjectInitStatus> projectInitStatuses;

	//钉钉机器人验签
	private String dingSecret;

	//钉钉群机器人链接
	private String dingWebHookUrl;

	/*
	 * 对应 upms中的群组id
	 */
	private Long groupId;

	/**
	 * 对应 upms中的租户id
	 */
	private Long tenantId;
	/**
	 * 对应 本次操作人的id
	 */
	private Long userId;

	private String teamBeginDate;

	private String teamEndDate;

	/**
	 * 项目组名称
	 */
	private String teamName;
	/**
	 * 项目组编号
	 */
	private  String teamCode;

	/**
	 * 需求管理平台
	 */
	private String projectManage;

	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@NotFound(action= NotFoundAction.IGNORE)
	private Set<TeamNamespace> teamNamespaces;


	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	private List<DevopsProject> devopsProjects;

	public String getTeamBeginDate() {
		return teamBeginDate;
	}

	public void setTeamBeginDate(String teamBeginDate) {
		this.teamBeginDate = teamBeginDate;
	}

	public String getTeamEndDate() {
		return teamEndDate;
	}

	public void setTeamEndDate(String teamEndDate) {
		this.teamEndDate = teamEndDate;
	}


	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getTeamCode() {
		return teamCode;
	}

	public void setTeamCode(String teamCode) {
		this.teamCode = teamCode;
	}

	public String getProjectManage() {
		return projectManage;
	}

	public void setProjectManage(String projectManage) {
		this.projectManage = projectManage;
	}

	public List<DevopsProject> getDevopsProjects() {
		return devopsProjects;
	}

	public void setDevopsProjects(List<DevopsProject> devopsProjects) {
		this.devopsProjects = devopsProjects;
	}


	public String getDingSecret() {
		return dingSecret;
	}

	public void setDingSecret(String dingSecret) {
		this.dingSecret = dingSecret;
	}

	public String getDingWebHookUrl() {
		return dingWebHookUrl;
	}

	public void setDingWebHookUrl(String dingWebHookUrl) {
		this.dingWebHookUrl = dingWebHookUrl;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public String getId() {
		return id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public Set<TeamNamespace> getTeamNamespaces() {
		return teamNamespaces;
	}

	public void setTeamNamespaces(Set<TeamNamespace> teamNamespaces) {
		this.teamNamespaces = teamNamespaces;
	}

	public DingMsgProfile getDingMsgProfile() {
		return dingMsgProfile;
	}

	public void setDingMsgProfile(DingMsgProfile dingMsgProfile) {
		this.dingMsgProfile = dingMsgProfile;
	}

	public MailMsgProfile getMailMsgProfile() {
		return mailMsgProfile;
	}

	public void setMailMsgProfile(MailMsgProfile mailMsgProfile) {
		this.mailMsgProfile = mailMsgProfile;
	}

	public Set<ProjectInitStatus> getProjectInitStatuses() {
		if(projectInitStatuses==null||projectInitStatuses.isEmpty()){
			projectInitStatuses = new HashSet<>();
		}
		return projectInitStatuses;
	}

	public void setProjectInitStatuses(Set<ProjectInitStatus> projectInitStatuses) {
		this.projectInitStatuses = projectInitStatuses;
	}
}
