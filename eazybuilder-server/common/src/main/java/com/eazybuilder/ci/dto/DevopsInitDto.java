package com.eazybuilder.ci.dto;

import com.eazybuilder.ci.entity.Upms.UpmsUserVo;
import com.eazybuilder.ci.entity.devops.DevopsInit;

import java.io.Serializable;
import java.util.List;
import java.util.Set;


public class DevopsInitDto extends DevopsInit implements Serializable {

	private String password;
	private String email;
	private String phone;
	private String userName;
	private List<UpmsUserVo> devopsUsers;

	public List<UpmsUserVo> getDevopsUsers() {
		return devopsUsers;
	}

	public void setDevopsUsers(List<UpmsUserVo> devopsUsers) {
		this.devopsUsers = devopsUsers;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
