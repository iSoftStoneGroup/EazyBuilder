package com.eazybuilder.ci.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.eazybuilder.ci.constant.RoleEnum;
import org.springframework.util.Assert;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name="CI_ROLE")
public class  Role {

	/**
	 * 这个值来源于角色枚举里面的ordinal
	 */
	@Id
	private Integer id;


	@JsonProperty
	private RoleEnum roleEnum;

	/**
	 * 持久化类必须要求有不带参的构造方法（也是JavaBean的规范）
	 * 这里只是为了初始化，不建议使用，请使用下面的有参构造
	 */
	public Role() {		
	}
	
	public Role(RoleEnum roleEnum) {
		this.id = roleEnum.ordinal();
		this.roleEnum = roleEnum;
	}

	/**
	 * 判断当前角色是否包含在某个具体角色中
	 * @param roleList
	 * @return
	 */
	public static boolean existRole(List<Role> roleList ,RoleEnum roleEnum){
		if(roleList==null){return false;}
		Assert.isTrue(roleEnum!=null,"没有获取当前角色的角色枚举属性信息【roleEnum】！");
		return roleList.stream().anyMatch(role -> role.getRoleEnum() == roleEnum);
	}
	
		

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public RoleEnum getRoleEnum() {
		return roleEnum;
	}

	public void setRoleEnum(RoleEnum roleEnum) {
		this.roleEnum = roleEnum;
	}


	public boolean isAdmin(){
		return this.roleEnum == RoleEnum.admin;
	}

	public boolean isAuditReader(){
		return isAdmin() || this.roleEnum == RoleEnum.audit;
	}
}
