package com.eazybuilder.ci.entity.devops;

import javax.persistence.*;

@Entity
@Table(name="CI_RELEASE_PROJECT")
public class ReleaseProject {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",columnDefinition="int(5)")
	private String id;

	private String nameSpace;


	private String releaseId;

	/**
	 * 因为项目表关联关联的太多，所以这里不用jpa关联了。
	 */
	private String projectId;

	/**
	 * mq推过来的数据没有projectId，所以需要加一个gitUrl
	 */
    private String projectGitUrl;

	/**
	 * 历史记录表id 如果有重试的场景，这里需要记录最后一次的id
	 */
	private String historyId;

	/**
	 * tag 命名规范 项目名_冲刺看板名_时间戳
	 * 如 IPAAS_20210903_20210809090212
	 */
	private String createTagVersion;

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
	 * docker镜像版本 上线的时候生成的镜像版本
	 */
	private String onlineDockerVersion;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getHistoryId() {
		return historyId;
	}

	public void setHistoryId(String historyId) {
		this.historyId = historyId;
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

	public String getProjectGitUrl() {
		return projectGitUrl;
	}

	public void setProjectGitUrl(String projectGitUrl) {
		this.projectGitUrl = projectGitUrl;
	}

	public String getReleaseId() {
		return releaseId;
	}

	public void setReleaseId(String releaseId) {
		this.releaseId = releaseId;
	}

	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}
}
