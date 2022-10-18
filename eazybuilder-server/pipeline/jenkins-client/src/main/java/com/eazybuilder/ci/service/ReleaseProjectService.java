package com.eazybuilder.ci.service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.entity.devops.*;

import com.eazybuilder.ci.repository.ReleaseProjectDao;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ReleaseProjectService extends AbstractCommonServiceImpl<ReleaseProjectDao, ReleaseProject> implements CommonService<ReleaseProject> {


    public List<ReleaseProject> findByLikeBranchVersionAndProjectUrl(String branchVersion, String projectUrl) {
        return (List<ReleaseProject>) dao.findAll(QReleaseProject.releaseProject.creteBranchVersion.like(branchVersion).and(QReleaseProject.releaseProject.projectGitUrl.eq(projectUrl)));
    }


    public ReleaseProject findByBranchVersionAndProjectUrl(String branchVersion,String projectUrl) {
        return dao.findOne(QReleaseProject.releaseProject.creteBranchVersion.eq(branchVersion).and(QReleaseProject.releaseProject.projectGitUrl.eq(projectUrl))).orElse(null);
    }
    public ReleaseProject findByPipelineId(String pipelineId) {
        return dao.findOne(QReleaseProject.releaseProject.historyId.eq(pipelineId)).orElse(null);
    }
    public ReleaseProject findByReleaseIdAndProjectId(String releaseId,String projectId) {
        return dao.findOne(QReleaseProject.releaseProject.releaseId.eq(releaseId).and(QReleaseProject.releaseProject.projectId.eq(projectId))).orElse(null);
    }

}
