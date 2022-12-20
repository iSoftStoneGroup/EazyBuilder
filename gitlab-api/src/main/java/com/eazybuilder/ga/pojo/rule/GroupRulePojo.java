package com.eazybuilder.ga.pojo.rule;

import lombok.Data;

import java.util.List;

@Data
public class GroupRulePojo {

    private String name;

    private String code;

    List<GuardPojo> guards;

}