package com.eazybuilder.ci.controller.vo;

import com.eazybuilder.ci.entity.Deploy;
import com.eazybuilder.ci.entity.ProjectType;
/**
 * 用于批量导入工程信息的VO
 *
 */
public class ProjectVO {
	private String id;
    private String name;
    private String description;
    private ScmVO scm;
	private boolean legacyProject;//ANT 项目
	private ProjectType projectType;
	private String pomPath;
	private boolean eazybuilderEjbProject;//eazybuilder4/5 project;
	private boolean eazybuilderStyleProject;
	private String codeCharset;
	private String srcPath;
	private String libPath;
	private String jdk;
	private Deploy deployInfo;
	private String imageSchema;
	private String sonarKey;
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
	public ScmVO getScm() {
		return scm;
	}
	public void setScm(ScmVO scm) {
		this.scm = scm;
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
	public boolean isEazybuilderEjbProject() {
		return eazybuilderEjbProject;
	}
	public void setEazybuilderEjbProject(boolean eazybuilderEjbProject) {
		this.eazybuilderEjbProject = eazybuilderEjbProject;
	}
	public boolean iseazybuilderStyleProject() {
		return eazybuilderStyleProject;
	}
	public void seteazybuilderStyleProject(boolean eazybuilderStyleProject) {
		this.eazybuilderStyleProject = eazybuilderStyleProject;
	}
	public String getCodeCharset() {
		return codeCharset;
	}
	public void setCodeCharset(String codeCharset) {
		this.codeCharset = codeCharset;
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
	public String getJdk() {
		return jdk;
	}
	public void setJdk(String jdk) {
		this.jdk = jdk;
	}
	public Deploy getDeployInfo() {
		return deployInfo;
	}
	public void setDeployInfo(Deploy deployInfo) {
		this.deployInfo = deployInfo;
	}
	public String getImageSchema() {
		return imageSchema;
	}
	public void setImageSchema(String imageSchema) {
		this.imageSchema = imageSchema;
	}
	public String getSonarKey() {
		return sonarKey;
	}
	public void setSonarKey(String sonarKey) {
		this.sonarKey = sonarKey;
	}
}
