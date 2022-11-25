package com.eazybuilder.dm.entity;


import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name="USER",uniqueConstraints = @UniqueConstraint(columnNames={"email"}))
public class User implements Serializable{
	@Id
	private String userId;

	//判断用户是否在项目中。
	private String phoneNumber;
	private String userName;
	private String nickName;
	private String email;
	private String deptName;
	private Integer employeeId;


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}
}
