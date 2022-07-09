package com.eazybuilder.ci.openApi.vo;

import com.eazybuilder.ci.entity.ProjectType;

public class ProjectVO {
	private String id;
	private String name;
	private String description;
	private String scmUrl;
	private String scmUser;
	private String scmPassword;
	private String scmTagName;
	
	private ProjectType projectType;
	
	private DockerRegistryVO registry;
	private String registryNamespace;

	public DockerRegistryVO getRegistry() {
		return registry;
	}

	public void setRegistry(DockerRegistryVO registry) {
		this.registry = registry;
	}

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getScmUrl() {
		return scmUrl;
	}

	public void setScmUrl(String scmUrl) {
		this.scmUrl = scmUrl;
	}

	public String getScmUser() {
		return scmUser;
	}

	public void setScmUser(String scmUser) {
		this.scmUser = scmUser;
	}

	public String getScmPassword() {
		return scmPassword;
	}

	public void setScmPassword(String scmPassword) {
		this.scmPassword = scmPassword;
	}

	public String getScmTagName() {
		return scmTagName;
	}

	public void setScmTagName(String scmTagName) {
		this.scmTagName = scmTagName;
	}

	public ProjectType getProjectType() {
		return projectType;
	}

	public void setProjectType(ProjectType projectType) {
		this.projectType = projectType;
	}

	public String getRegistryNamespace() {
		return registryNamespace;
	}

	public void setRegistryNamespace(String registryNamespace) {
		this.registryNamespace = registryNamespace;
	}
	
}
