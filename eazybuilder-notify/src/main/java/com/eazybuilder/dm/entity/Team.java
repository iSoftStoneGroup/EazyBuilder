package com.eazybuilder.dm.entity;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;


import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @date
 */
@Entity
@Table(name="TEAM")
public class Team  implements Serializable{

	@Id
	private String id;

	@OneToOne(cascade=CascadeType.ALL)
	private DingMsgProfile dingMsgProfile;


	@OneToOne(cascade=CascadeType.ALL)
	private MailMsgProfile mailMsgProfile;

	@ManyToMany(fetch=FetchType.EAGER,cascade=CascadeType.ALL)
	@NotFound(action= NotFoundAction.IGNORE)
	private List<User> devopsUsers;
	//钉钉机器人验签
	private String dingSecret;

	//钉钉群机器人链接
	private String dingWebHookUrl;

	/*
	 * 对应 用户平台中的群组id
	 */
	private Long groupId;

	/**
	 * 对应 用户平台中的租户id
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

	public List<User> getDevopsUsers() {
		return devopsUsers;
	}

	public void setDevopsUsers(List<User> devopsUsers) {
		this.devopsUsers = devopsUsers;
	}
}
