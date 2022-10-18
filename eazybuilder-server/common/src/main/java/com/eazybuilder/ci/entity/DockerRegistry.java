package com.eazybuilder.ci.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.*;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name="CI_DOCKER_REGISTRY")
public class DockerRegistry {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",columnDefinition="int(5)")
	private String id;
	
	@Column(name="registry_schema")
	private String schema="http";
	private String url;
	private String user;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;
	
	@Transient
	@JsonProperty(access=Access.READ_WRITE)
	private boolean changePwd;
	
	private String email;
	
	/**
	 * 所属团队
	 */
	@Column(name="team_id")
	private String teamId;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isChangePwd() {
		return changePwd;
	}
	public void setChangePwd(boolean changePwd) {
		this.changePwd = changePwd;
	}
	public String getTeamId() {
		return teamId;
	}
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
}
