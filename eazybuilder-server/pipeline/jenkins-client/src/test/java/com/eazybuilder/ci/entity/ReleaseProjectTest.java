package com.eazybuilder.ci.entity;

import com.eazybuilder.ci.entity.devops.ReleaseProject;
import org.junit.Assert;

import javax.persistence.*;

public class ReleaseProjectTest {

	public void releaseProjectTest(){
		ReleaseProject releaseProject = new ReleaseProject();
		releaseProject.setId("");
		releaseProject.setProjectId("");
		releaseProject.setHistoryId("");
		releaseProject.setCreateTagVersion("");
		releaseProject.setCreateTagDetail("");
		releaseProject.setCreteBranchVersion("");
		releaseProject.setReleaseDockerVersion("");
		releaseProject.setOnlineDockerVersion("");
		releaseProject.setProjectGitUrl("");
		releaseProject.setReleaseId("");
		releaseProject.setNameSpace("");

		Assert.assertNotNull(releaseProject.getId());
		Assert.assertNotNull(releaseProject.getProjectId());
		Assert.assertNotNull(releaseProject.getHistoryId());
		Assert.assertNotNull(releaseProject.getCreateTagVersion());
		Assert.assertNotNull(releaseProject.getCreateTagDetail());
		Assert.assertNotNull(releaseProject.getCreteBranchVersion());
		Assert.assertNotNull(releaseProject.getReleaseDockerVersion());
		Assert.assertNotNull(releaseProject.getOnlineDockerVersion());
		Assert.assertNotNull(releaseProject.getProjectGitUrl());
		Assert.assertNotNull(releaseProject.getReleaseId());
		Assert.assertNotNull(releaseProject.getNameSpace());
	}
}
