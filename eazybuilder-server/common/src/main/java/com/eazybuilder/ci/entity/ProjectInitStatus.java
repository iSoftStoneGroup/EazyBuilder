package com.eazybuilder.ci.entity;

import com.eazybuilder.ci.entity.devops.ProjectCode;
import com.eazybuilder.ci.entity.devops.Status;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;


@Entity

@Table(name = "CI_PROJECT_INIT_STATUS")
public class ProjectInitStatus extends BaseEntry implements Serializable {

	private Status status;

	private ProjectCode projectCode;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public ProjectCode getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(ProjectCode projectCode) {
		this.projectCode = projectCode;
	}
}
