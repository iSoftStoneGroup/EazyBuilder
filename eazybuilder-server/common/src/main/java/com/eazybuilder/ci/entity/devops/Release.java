package com.eazybuilder.ci.entity.devops;

import com.eazybuilder.ci.entity.Pipeline;
import com.eazybuilder.ci.entity.Project;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="CI_RELEASE")
public class Release {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",columnDefinition="int(5)")
	private String id;

	@OneToMany
	private List<ReleaseProject> releaseProjects;

	@OneToMany
	private List<Pipeline> pipelineList;

	/**
	 * 分支版本
	 */
//	private String creteBranchVersion;

	/**
	 * 关联的ci项目
	 */
	@ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private List<Project> projectList;
	/**
	 * 标题
	 */
	private String title;

	/**
	 * 项目路径
	 */
	private String gitPath;
	/**
	 * redmine team
	 */
	private Integer teamId;

	/**
	 * redmine sprint
	 */
	private String sprintId;


	/**
	 * redmine 相关信息需求号 以逗号隔开
	 */
	//FIXME 最好调整一下字段名，不然容易引起误会
	private String issuesId;

	/**
	 * 上线镜像版本、tag版本
	 */
	private String imageTag;

	/**
	 * 镜像命名空间-从构建过程中获取的。
	 */
	private String nameSpace;

	/**
	 * 申请日期
	 */
	private Date createDate;

	/**
	 * 预计上线时间
	 */
	private Date releaseDate;

	/**
	 * 上线申请号 自动生成
	 */
	private String releaseCode;

	/**
	 * 上线描述
	 */
    private String releaseDetail;

	/**
	 * 申请上线人
	 * @return
	 */
	 private String releaseUserName;

	/**
	 * 审批负责人
	 * @return
	 */
	private String batchUserName;
	/**
	 *审批状态
	 */
	private Status batchStatus;
	/**
	 * 审批建议
	 */
	private String batchDetail;
	/**
	 *上线状态
	 */
	private Status releaseStatus;

	/**
	 *审批建议
	 */
	private String batchDdvice;

	@Lob
	private String issuesTreeJson;


	@Lob
	private String tagDetail;

	public String getBatchDdvice() {
		return batchDdvice;
	}

	public void setBatchDdvice(String batchDdvice) {
		this.batchDdvice = batchDdvice;
	}

	public String getReleaseCode() {
		return releaseCode;
	}

	public void setReleaseCode(String releaseCode) {
		this.releaseCode = releaseCode;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getImageTag() {
		return imageTag;
	}

	public void setImageTag(String imageTag) {
		this.imageTag = imageTag;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getSprintId() {
		return sprintId;
	}

	public void setSprintId(String sprintId) {
		this.sprintId = sprintId;
	}

	public String getIssuesId() {
		return issuesId;
	}

	public void setIssuesId(String issuesId) {
		this.issuesId = issuesId;
	}

	public String getReleaseDetail() {
		return releaseDetail;
	}

	public void setReleaseDetail(String releaseDetail) {
		this.releaseDetail = releaseDetail;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public String getReleaseUserName() {
		return releaseUserName;
	}

	public void setReleaseUserName(String releaseUserName) {
		this.releaseUserName = releaseUserName;
	}

	public Status getBatchStatus() {
		return batchStatus;
	}

	public void setBatchStatus(Status batchStatus) {
		this.batchStatus = batchStatus;
	}

	public Status getReleaseStatus() {
		return releaseStatus;
	}

	public void setReleaseStatus(Status releaseStatus) {
		this.releaseStatus = releaseStatus;
	}

	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}

	public String getBatchUserName() {
		return batchUserName;
	}

	public void setBatchUserName(String batchUserName) {
		this.batchUserName = batchUserName;
	}

	public String getBatchDetail() {
		return batchDetail;
	}

	public void setBatchDetail(String batchDetail) {
		this.batchDetail = batchDetail;
	}

	public List<Project> getProjectList() {
		return projectList;
	}

	public void setProjectList(List<Project> projectList) {
		this.projectList = projectList;
	}

	public List<Pipeline> getPipelineList() {
		return pipelineList;
	}

	public void setPipelineList(List<Pipeline> pipelineList) {
		this.pipelineList = pipelineList;
	}

	public String getGitPath() {
		return gitPath;
	}

	public void setGitPath(String gitPath) {
		this.gitPath = gitPath;
	}

	public String getIssuesTreeJson() {
		return issuesTreeJson;
	}

	public void setIssuesTreeJson(String issuesTreeJson) {
		this.issuesTreeJson = issuesTreeJson;
	}

	public String getTagDetail() {
		return tagDetail;
	}

	public void setTagDetail(String tagDetail) {
		this.tagDetail = tagDetail;
	}

	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}

//	public String getCreteBranchVersion() {
//		return creteBranchVersion;
//	}
//
//	public void setCreteBranchVersion(String creteBranchVersion) {
//		this.creteBranchVersion = creteBranchVersion;
//	}

	public List<ReleaseProject> getReleaseProjects() {
		if(releaseProjects==null){
			return new ArrayList<ReleaseProject>();
		}
		return releaseProjects;
	}

	public void setReleaseProjects(List<ReleaseProject> releaseProjects) {
		this.releaseProjects = releaseProjects;
	}

	@Override
	public String toString() {
		return "Release{" +
				"id='" + id + '\'' +
				", pipelineList=" + pipelineList +
				", projectList=" + projectList +
				", title='" + title + '\'' +
				", gitPath='" + gitPath + '\'' +
				", teamId=" + teamId +
				", sprintId='" + sprintId + '\'' +
				", issuesId='" + issuesId + '\'' +
				", imageTag='" + imageTag + '\'' +
				", nameSpace='" + nameSpace + '\'' +
				", createDate=" + createDate +
				", releaseDate=" + releaseDate +
				", releaseCode='" + releaseCode + '\'' +
				", releaseDetail='" + releaseDetail + '\'' +
				", releaseUserName='" + releaseUserName + '\'' +
				", batchUserName='" + batchUserName + '\'' +
				", batchStatus=" + batchStatus +
				", batchDetail='" + batchDetail + '\'' +
				", releaseStatus=" + releaseStatus +
				", batchDdvice='" + batchDdvice + '\'' +
				", issuesTreeJson='" + issuesTreeJson + '\'' +
				", tagDetail='" + tagDetail + '\'' +
				'}';
	}
}
