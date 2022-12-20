package com.eazybuilder.ga.pojo.gitlab;

import lombok.Data;

@Data
public class GitlabGroup {

    private Integer id;

    private String name;

    private String path;

    private String full_name;

    private String full_path;

    private Integer parent_id;
}
