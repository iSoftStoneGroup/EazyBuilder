package com.eazybuilder.ga.component;

import com.eazybuilder.ga.pojo.GitLabUserPojo;

public interface RequestComponent {

    String gitLabCreateGroup(String groupName, String groupPath);

    String gitLabCreateSubGroup(String groupName, String groupPath, String parentId);

    String gitLabCreateProject(String projectName, String groupId);

    String gitLabCreateUser(GitLabUserPojo gitLabUserPojo);

    String gitLabUserAddGroup(String userIds,String accessLevel,String groupId);

    String gitLabUserAddProject(String userIds,String accessLevel,String projectId);

    String gitLabUserQuery();

    String gitLabGroupQuery(String groupPath);

}
