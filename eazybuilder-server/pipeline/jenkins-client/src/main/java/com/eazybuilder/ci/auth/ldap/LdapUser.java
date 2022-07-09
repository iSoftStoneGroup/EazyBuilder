package com.eazybuilder.ci.auth.ldap;

public class LdapUser {
	/**
	 * 中文姓名
	 */
	private String cn;
	/**
	 * 职位
	 */
	private String title;
	/**
	 * 手机号
	 */
	private String mobile;
	/**
	 * 员工编号
	 */
	private String employeeID;
	/**
	 * 部门
	 */
	private String department;
	/**
	 * 邮箱
	 */
	private String userPrincipalName;
	/**
	 * 地址
	 */
	private String streetAddress;
	
	public String getCn() {
		return cn;
	}
	public void setCn(String cn) {
		this.cn = cn;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmployeeID() {
		return employeeID;
	}
	public void setEmployeeID(String employeeID) {
		this.employeeID = employeeID;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getUserPrincipalName() {
		return userPrincipalName;
	}
	public void setUserPrincipalName(String userPrincipalName) {
		this.userPrincipalName = userPrincipalName;
	}
	public String getStreetAddress() {
		return streetAddress;
	}
	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}
}
