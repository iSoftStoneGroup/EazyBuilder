package com.eazybuilder.ga.component.impl;

import com.eazybuilder.ga.component.DataMigrationComponentV4;

public class DataMigrationComponentV4Impl implements DataMigrationComponentV4 {
    @Override
    public String findGroups(String oldGitAddress, String oldGitlabToken) {
        return null;
    }

    @Override
    public String findProjects(String oldGitAddress, String oldGitlabToken) {
        return null;
    }

    @Override
    public String findProjectsByGroup(String oldGitAddress, String oldGitlabToken, String group) {
        return null;
    }

    @Override
    public String gitLabCreateGroup(String newGitAddress, String newGitlabToken, String groupName, String groupPath) {
        return null;
    }

    @Override
    public String gitLabCreateProject(String newGitAddress, String newGitlabToken, String projectName, String groupId) {
        return null;
    }
}
