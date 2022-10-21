package com.eazybuilder.ci.entity;

import javax.persistence.*;

@Entity
@Table(name="CI_PROJECT_MANAGE",uniqueConstraints=@UniqueConstraint(columnNames="NAME"))
public class ProjectManage {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",columnDefinition="int(8)")
	private String id;

	private String createDate;


	/**
	 * 需求管理平台配置名称
	 */
	private String name;

	/**
	 * 需求管理平台配置类型
	 */
	private String type;

	/**
	 * 需求管理平台配置地址
	 */
	private String url;

	/**
	 * 需求管理平台配置用户
	 */
	private  String userName;

	private String password;

	/**
	 * 数据库连接地址
	 */
	private String dbUrl;

	private String dbName;

	private String dbPassword;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
}
