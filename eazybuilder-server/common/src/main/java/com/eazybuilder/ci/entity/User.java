package com.eazybuilder.ci.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.*;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.google.common.collect.Lists;
import com.eazybuilder.ci.constant.RoleEnum;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

@Entity
@Table(name="CI_USER")
public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7075751223784841382L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",columnDefinition="int(8)")
	private String id;
	private String name;
	@JsonProperty(access=Access.WRITE_ONLY)
	private String password;
	private String email;
	private String phone;
	
	private String department;
	private String title;
	private Integer employeeId;
	
	@ManyToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	private List<Role> roles;
	
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

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	@JSONField(serialize = false)
	@JsonIgnore
	public List<Role> getRoleByTitle() {
		List<Role> list=new ArrayList();
		if(title.contains("主管")||title.contains("经理")||title.contains("架构")
				||title.contains("总监")||title.contains("总裁")){
			list.add(new Role(RoleEnum.teamleader));
			return list;
		}
		if(title.indexOf("测试")!=-1){
			list.add(new Role(RoleEnum.tester));
			return list;
		}
		list.add(new Role(RoleEnum.developer));
		return list;
	}
	
	public  void setRolesByRoleEnum(RoleEnum roleEnum){
		Assert.isTrue(roleEnum!=null,"枚举值不能为null");
		List<Role> objects = Lists.newArrayListWithCapacity(1);
		objects.add(new Role(roleEnum));
		this.roles=objects;
	}

	@Override
	public String toString() {
		
		return "User{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", password='" + password + '\'' +
				", email='" + email + '\'' +
				", phone='" + phone + '\'' +
				", department='" + department + '\'' +
				", title='" + title + '\'' +
				'}';
	}
}
