package com.eazybuilder.ga.pojo.gitlab;

import lombok.Data;

@Data
public class GitlabProject {

    private Integer id;

    private String description;

    private String name;

    private String name_with_namespace;

    private String path;

    private String path_with_namespace;

}
