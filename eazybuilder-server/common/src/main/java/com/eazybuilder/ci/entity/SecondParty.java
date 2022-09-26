package com.eazybuilder.ci.entity;

import javax.persistence.*;

/**
 * 二方包自动上传配置
 */

@Entity
@Table(name="CI_SECOND_PARTY",uniqueConstraints=@UniqueConstraint(columnNames="secondPartyName"))
public class SecondParty {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",columnDefinition="int(8)")
	private String id;

	private String secondPartyName;

	private String secondPartyType;

	private String secondPartyPath;

	private String secondPartyUser;


	private  String secondPartyPass;


	private String secondPartyKey;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSecondPartyName() {
		return secondPartyName;
	}

	public void setSecondPartyName(String secondPartyName) {
		this.secondPartyName = secondPartyName;
	}

	public String getSecondPartyType() {
		return secondPartyType;
	}

	public void setSecondPartyType(String secondPartyType) {
		this.secondPartyType = secondPartyType;
	}

	public String getSecondPartyPath() {
		return secondPartyPath;
	}

	public void setSecondPartyPath(String secondPartyPath) {
		this.secondPartyPath = secondPartyPath;
	}

	public String getSecondPartyUser() {
		return secondPartyUser;
	}

	public void setSecondPartyUser(String secondPartyUser) {
		this.secondPartyUser = secondPartyUser;
	}

	public String getSecondPartyPass() {
		return secondPartyPass;
	}

	public void setSecondPartyPass(String secondPartyPass) {
		this.secondPartyPass = secondPartyPass;
	}

	public String getSecondPartyKey() {
		return secondPartyKey;
	}

	public void setSecondPartyKey(String secondPartyKey) {
		this.secondPartyKey = secondPartyKey;
	}
}
