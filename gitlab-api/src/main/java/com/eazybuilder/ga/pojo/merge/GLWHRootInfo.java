package com.eazybuilder.ga.pojo.merge;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GLWHRootInfo {

    private String object_kind;
    private User user;
    private Project project;
    private Object_attributes object_attributes;
    List<String> labels = new ArrayList<String>();
    private Changes changes;
    private Repository repository;
    private List<Assignees> assignees;

}
