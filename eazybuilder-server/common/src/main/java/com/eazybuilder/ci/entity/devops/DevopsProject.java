package com.eazybuilder.ci.entity.devops;

import com.eazybuilder.ci.entity.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
@Entity
@Table(name="CI_DEVOPS_PROJECT")
public class DevopsProject implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",columnDefinition="int(5)")
	private String id;

	/**
	 * 源码仓库地址。
	 */
	private String scmUrl;

	private String projectCode;

	private String description;

	/**
	 * 	ANT 项目
	 */
	@Column(name="legacy_project",columnDefinition="int(1) default 0")
	private boolean legacyProject;



	@Column(name="project_type",columnDefinition="int(1) default 0")
	private ProjectType projectType;
	private String pomPath;

	private String netPath;

	private String netSlnPath;

	private String netTestPath;

	private NetType netType;

	/**
	 * 镜像仓库中的命名空间(例如0.0.0.0:5000/ci/xxx  命名空间即为ci)
	 */
	private String imageSchema;

	/**
	 * k8s配置 一个项目可以有多个配置
	 */
	@OneToMany(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
	private List<DeployConfig> deployConfigList;


	private String projectName;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<DeployConfig> getDeployConfigList() {
		return deployConfigList;
	}

	public void setDeployConfigList(List<DeployConfig> deployConfigList) {
		this.deployConfigList = deployConfigList;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isLegacyProject() {
		return legacyProject;
	}

	public void setLegacyProject(boolean legacyProject) {
		this.legacyProject = legacyProject;
	}

	public ProjectType getProjectType() {
		return projectType;
	}

	public void setProjectType(ProjectType projectType) {
		this.projectType = projectType;
	}

	public String getPomPath() {
		return pomPath;
	}

	public void setPomPath(String pomPath) {
		this.pomPath = pomPath;
	}

	public String getImageSchema() {
		return imageSchema;
	}

	public void setImageSchema(String imageSchema) {
		this.imageSchema = imageSchema;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}



	public String getScmUrl() {
		return scmUrl;
	}

	public void setScmUrl(String scmUrl) {
		this.scmUrl = scmUrl;
	}

	public String getNetPath() {
		return netPath;
	}

	public void setNetPath(String netPath) {
		this.netPath = netPath;
	}

	public String getNetSlnPath() {
		return netSlnPath;
	}

	public void setNetSlnPath(String netSlnPath) {
		this.netSlnPath = netSlnPath;
	}

	public String getNetTestPath() {
		return netTestPath;
	}

	public void setNetTestPath(String netTestPath) {
		this.netTestPath = netTestPath;
	}

	public NetType getNetType() {
		return netType;
	}

	public void setNetType(NetType netType) {
		this.netType = netType;
	}

	//TODO 待修改，如果都是用Project的话，代码可删除
	public void update(Project project){
		this.scmUrl = project.getScm().getUrl();
		this.projectName = project.getDescription();
		this.projectType = project.getProjectType() == ProjectType.npm ? ProjectType.npm:ProjectType.java;
		this.legacyProject = this.projectType == ProjectType.npm?false: project.getProjectType() == ProjectType.java;
		this.pomPath = project.getPomPath();
		this.netPath = project.getNetPath();
		this.netSlnPath = project.getNetSlnPath();
		this.netTestPath = project.getNetTestPath();
		this.netType = project.getNetType();
	}
}
