package com.eazybuilder.ci.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.*;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
/**
 * 工程分组，跟项目组区分开，主要用于统计分析
 * 
 *
 *
 */
@Entity
@Table(name="CI_PROJECT_GROUP")
public class ProjectGroup {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",columnDefinition="int")
	private String id;
	private String name;
	private String leader;
	/**
	 * 关联工程
	 */
	@ManyToMany(fetch=FetchType.EAGER)
	@NotFound(action=NotFoundAction.IGNORE)
	private Set<Project> projects;
	
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
	public String getLeader() {
		return leader;
	}
	public void setLeader(String leader) {
		this.leader = leader;
	}
	public Set<Project> getProjects() {
		return projects;
	}
	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}
}
