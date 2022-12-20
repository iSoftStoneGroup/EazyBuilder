package com.eazybuilder.ga.pojo.merge;

import lombok.Data;

@Data
public class Object_attributes {

    private String assignee_id;
    private int author_id;
    private String created_at;
    private String description;
    private String head_pipeline_id;
    private int id;
    private int iid;
    private String last_edited_at;
    private String last_edited_by_id;
    private String merge_commit_sha;
    private String merge_error;
    private Merge_params merge_params;
    private String merge_status;
    private String merge_user_id;
    private boolean merge_when_pipeline_succeeds;
    private String milestone_id;
    private String source_branch;
    private Integer source_project_id;
    private String state;
    private String target_branch;
    private Integer target_project_id;
    private Integer time_estimate;
    private String title;
    private String updated_at;
    private String updated_by_id;
    private String url;
    private Source source;
    private Target target;
    private Last_commit last_commit;
    private boolean work_in_progress;
    private Integer total_time_spent;
    private String human_total_time_spent;
    private String human_time_estimate;
    private String action;

}
