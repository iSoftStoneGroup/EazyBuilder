package com.eazybuilder.ci.svn;

import com.eazybuilder.ci.entity.ProjectType;

public class ProjectSourceInfo {
	private String name;
	private boolean maven;
	private ProjectType projectType;
	private String srcPath;
	private String libPath;
	private String projectPath;
	
	public ProjectSourceInfo(String name, boolean maven, String srcPath, String libPath, String projectPath,ProjectType type) {
		super();
		this.name = safe(name);
		this.maven = maven;
		this.srcPath = srcPath;
		this.libPath = libPath;
		this.projectPath = projectPath;
		this.projectType=type;
	}

	private String safe(String name) {
		if(name!=null){
			return name.replaceAll("/", "_");
		}
		return name;
	}

	public ProjectSourceInfo() {
	}
	
	public boolean isMaven() {
		return maven;
	}
	public void setMaven(boolean maven) {
		this.maven = maven;
	}
	public String getSrcPath() {
		return srcPath;
	}
	public void setSrcPath(String srcPath) {
		this.srcPath = srcPath;
	}
	public String getLibPath() {
		return libPath;
	}
	public void setLibPath(String libPath) {
		this.libPath = libPath;
	}

	public String getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}

	@Override
	public String toString() {
		return "ProjectSourceInfo [maven=" + maven + ", srcPath=" + srcPath + ", libPath=" + libPath + ", projectPath="
				+ projectPath + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = safe(name);
	}

	public ProjectType getProjectType() {
		return projectType;
	}

	public void setProjectType(ProjectType projectType) {
		this.projectType = projectType;
	}
}
