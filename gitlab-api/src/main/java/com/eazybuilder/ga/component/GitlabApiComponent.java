package com.eazybuilder.ga.component;

import java.util.List;

public interface GitlabApiComponent {

    String findGroup();

    String findProjectsByGroup(String group);

    String findProjectBranch(int projectId);

    String createMR(Integer id, String source_branch, String target_branch, String title);
    
    String createMRNote(Integer projectId,Integer mergeRequestId, String body);

    String createMRDiscussion(Integer projectId,Integer mergeRequestId, String body);

    String updateMRState(Integer projectId,Integer mergeRequestId, String state_event);

    List<String> getUserEmail(String userName);

}
