package com.eazybuilder.ga.pojo.gitlab;

import lombok.Data;

@Data
public class GitlabBranch {

    private String name;

    private String web_url;

    private GitlabCommit commit;

}
