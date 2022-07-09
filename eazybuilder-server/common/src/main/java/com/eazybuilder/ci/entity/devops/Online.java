package com.eazybuilder.ci.entity.devops;

import com.eazybuilder.ci.entity.DtpReport;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="CI_ONLINE")
public class Online {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",columnDefinition="int(5)")
	private String id;

	/**
	 * 是否立即上线
	 */
	private boolean immedIatelyOnline;

	/**
	 * 标题
	 */
	private String title;

   /**
    * 自动化测试报告文件路径
    */
	private String dtpReportUrl;
	/**
	 * 自动化测试报告
	 */
	@Transient
	private  List<DtpReport> dtpReports;
	/**
	 * redmine team
	 */
	private Integer teamId;

	/**
	 * redmine sprint
	 */
	private String sprintId;

	/**
	 * 提测数据 id
	 */
	private String releaseId;

	/**
	 * 提测时镜像版本、tag版本、分支号
	 */
	private String imageTag;

	/**
	 * 上线后的镜像版本
	 */
	private String onLineImageTag;

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

	/**
	 * 指定上线人id
	 * @return
	 */
	private String memberId;

	/**
	 * 指定上线人名称
	 */
    private String memberName;
	/**
	 * 是否更新nacos
	 * @return
	 */
	private Boolean updateNacos;

	/**
	 * 是否更新sql
	 * @return
	 */
	private Boolean excuteSQL;

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

	public String getReleaseId() {
		return releaseId;
	}

	public void setReleaseId(String releaseId) {
		this.releaseId = releaseId;
	}

	public String getImageTag() {
		return imageTag;
	}

	public void setImageTag(String imageTag) {
		this.imageTag = imageTag;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getOnLineImageTag() {
		return onLineImageTag;
	}

	public void setOnLineImageTag(String onLineImageTag) {
		this.onLineImageTag = onLineImageTag;
	}

	public String getDtpReportUrl() {
		return dtpReportUrl;
	}

	public void setDtpReportUrl(String dtpReportUrl) {
		this.dtpReportUrl = dtpReportUrl;
	}

	public List<DtpReport> getDtpReports() {
		return dtpReports;
	}

	public void setDtpReports(List<DtpReport> dtpReports) {
		this.dtpReports = dtpReports;
	}

	public Boolean getUpdateNacos() {
		return updateNacos;
	}

	public void setUpdateNacos(Boolean updateNacos) {
		this.updateNacos = updateNacos;
	}

	public Boolean getExcuteSQL() {
		return excuteSQL;
	}

	public void setExcuteSQL(Boolean excuteSQL) {
		this.excuteSQL = excuteSQL;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public boolean isImmedIatelyOnline() {
		return immedIatelyOnline;
	}

	public void setImmedIatelyOnline(boolean immedIatelyOnline) {
		this.immedIatelyOnline = immedIatelyOnline;
	}
}
