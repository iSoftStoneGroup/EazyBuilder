package com.eazybuilder.ga.pojo.gitlab;

import lombok.Data;

@Data
public class GitlabCommit {

    private String id;

    private String short_id;

    private String title;

    private String message;

    private String author_name;

    private String author_email;

    private String committer_name;

    private String committer_email;

    private String web_url;

}
