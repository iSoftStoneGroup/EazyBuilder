package com.eazybuilder.ci.controller.vo;

import com.eazybuilder.ci.entity.PipelineType;
import com.eazybuilder.ci.entity.devops.EventType;

import javax.persistence.Lob;
import java.util.Date;
import java.util.List;

public class ProjectBuildVo {

	private String redmineCode;
	private String redmineUser;

	private String dbUrl;
	private String dbUserName;
	private String dbPassword;
	//应用回滚版本： 当构建过程中包含自动回滚的时候展示
	private String rolloutVersion;

	//数据库回滚日期： 当构建过程中包含数据库回滚的时候展示
	private Date releaseDate;

	//手工运行流水线指定构建分支
	private String arriveTagName;
	//手工运行流水线指定镜像
	private String dockerImageTag;
	private String gitlabApiUrl;

	private String profile;//profile id
	private List<String> projects;//: "65", projects ids: ["1"]}

	/**
	 * 提测时用的命名空间
	 */
	private String nameSpace;
	/**
	 * 因为项目表关联关联的太多，所以这里不用jpa关联了。
	 */
	private String projectId;

	/**
	 * 历史记录表id 如果有重试的场景，这里需要记录最后一次的id
	 */
	private String releaseProjectId;

	/**
	 * tag 命名规范 项目名_冲刺看板名_时间戳
	 * 如 IPAAS_20210903_20210809090212
	 */
	private String createTagVersion;

	/**
	 * 触发类型
	 */
	private PipelineType pipelineType;

	@Lob
	private String createTagDetail;

	/**
	 * 分支版本
	 */
	private String creteBranchVersion;

	/**
	 * docker镜像版本 提测的时候生成的镜像版本
	 */
	private String releaseDockerVersion;

	/**
	 * 来源分支、
	 */
	private String sourceBranch;

	/**
	 * 目标分支、
	 */
	private String targetBranch;

	/**
	 * docker镜像版本 上线的时候生成的镜像版本
	 */
	private String onlineDockerVersion;

	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}

	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	public List<String> getProjects() {
		return projects;
	}
	public void setProjects(List<String> projects) {
		this.projects = projects;
	}



	public String getArriveTagName() {
		return arriveTagName;
	}

	public void setArriveTagName(String arriveTagName) {
		this.arriveTagName = arriveTagName;
	}

	public String getRolloutVersion() {
		return rolloutVersion;
	}

	public void setRolloutVersion(String rolloutVersion) {
		this.rolloutVersion = rolloutVersion;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public String getDbUserName() {
		return dbUserName;
	}

	public void setDbUserName(String dbUserName) {
		this.dbUserName = dbUserName;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public String getDockerImageTag() {
		return dockerImageTag;
	}

	public void setDockerImageTag(String dockerImageTag) {
		this.dockerImageTag = dockerImageTag;
	}

	public String getGitlabApiUrl() {
		return gitlabApiUrl;
	}

	public void setGitlabApiUrl(String gitlabApiUrl) {
		this.gitlabApiUrl = gitlabApiUrl;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getReleaseProjectId() {
		return releaseProjectId;
	}

	public void setReleaseProjectId(String releaseProjectId) {
		this.releaseProjectId = releaseProjectId;
	}

	public String getCreateTagVersion() {
		return createTagVersion;
	}

	public void setCreateTagVersion(String createTagVersion) {
		this.createTagVersion = createTagVersion;
	}

	public String getCreateTagDetail() {
		return createTagDetail;
	}

	public void setCreateTagDetail(String createTagDetail) {
		this.createTagDetail = createTagDetail;
	}

	public String getCreteBranchVersion() {
		return creteBranchVersion;
	}

	public void setCreteBranchVersion(String creteBranchVersion) {
		this.creteBranchVersion = creteBranchVersion;
	}

	public String getReleaseDockerVersion() {
		return releaseDockerVersion;
	}

	public void setReleaseDockerVersion(String releaseDockerVersion) {
		this.releaseDockerVersion = releaseDockerVersion;
	}

	public String getOnlineDockerVersion() {
		return onlineDockerVersion;
	}

	public void setOnlineDockerVersion(String onlineDockerVersion) {
		this.onlineDockerVersion = onlineDockerVersion;
	}


	public PipelineType getPipelineType() {
		return pipelineType;
	}

	public void setPipelineType(PipelineType pipelineType) {
		this.pipelineType = pipelineType;
	}

	public String getSourceBranch() {
		return sourceBranch;
	}

	public void setSourceBranch(String sourceBranch) {
		this.sourceBranch = sourceBranch;
	}

	public String getTargetBranch() {
		return targetBranch;
	}

	public void setTargetBranch(String targetBranch) {
		this.targetBranch = targetBranch;
	}

	public String getRedmineCode() {
		return redmineCode;
	}

	public void setRedmineCode(String redmineCode) {
		this.redmineCode = redmineCode;
	}

	public String getRedmineUser() {
		return redmineUser;
	}

	public void setRedmineUser(String redmineUser) {
		this.redmineUser = redmineUser;
	}
}
