package com.eazybuilder.ci.entity.devops;



import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name="CI_DOCKER_IMAGE")
public class DockerImage {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",columnDefinition="int(5)")
	private String id;

	private String pullUrl;

	private String projectName;

	private String projectId;

	private String imageTag;

	private Date createDate;

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

	public String getPullUrl() {
		return pullUrl;
	}

	public void setPullUrl(String pullUrl) {
		this.pullUrl = pullUrl;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
}
