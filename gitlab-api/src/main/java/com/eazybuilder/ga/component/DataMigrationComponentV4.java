package com.eazybuilder.ga.component;

public interface DataMigrationComponentV4 {

    String findGroups(String oldGitAddress,String oldGitlabToken);

    String findProjects(String oldGitAddress,String oldGitlabToken);

    String findProjectsByGroup(String oldGitAddress,String oldGitlabToken, String group);

    String gitLabCreateGroup(String newGitAddress,String newGitlabToken, String groupName, String groupPath);

    String gitLabCreateProject(String newGitAddress,String newGitlabToken, String projectName, String groupId);

}
