package com.eazybuilder.ga.service;

import com.eazybuilder.ga.pojo.gitlab.GitlabBranch;
import com.eazybuilder.ga.pojo.gitlab.GitlabGroup;
import com.eazybuilder.ga.pojo.gitlab.GitlabProject;

import java.util.List;

public interface GitlabService {

    List<GitlabGroup> findGroup() throws Exception;

    List<GitlabProject> findProjectsByGroup(String group) throws Exception;

    List<GitlabBranch> findProjectBranch(int projectId) throws Exception;

    String createMR(Integer id, String source_branch, String target_branch, String title);
    
    String createMRNote(Integer projectId,Integer mergeRequestId, String body);

    String createMRDiscussion(Integer projectId,Integer mergeRequestId, String body);

    String updateMRState(Integer projectId,Integer mergeRequestId, String state_event);


}
