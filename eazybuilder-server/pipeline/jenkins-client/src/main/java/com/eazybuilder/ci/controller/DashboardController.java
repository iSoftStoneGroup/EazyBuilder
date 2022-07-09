package com.eazybuilder.ci.controller;

import com.google.common.collect.Lists;
import com.eazybuilder.ci.service.ProjectService;
import com.eazybuilder.ci.service.TeamServiceImpl;
import com.eazybuilder.ci.util.AuthUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    ProjectService projectService;

    @Autowired
    JdbcTemplate template;

    @Autowired
    TeamServiceImpl teamService;

    private static String  SQL_GET_TOP10_BUG_ORDER_TEAM = getSqlByPath("/sql/top10BugOrderTeam.sql");

    private static String  SQL_GET_TOP10_BUG_ORDER_USER_TEAM = getSqlByPath("/sql/top10BugOrderUserTeam.sql");




    @GetMapping(value="/top10BugOrderTeam")
    public List top10BugOrderTeam(){
        String bugType = "'bug_blocker','bug_critical'";
        return teamDashboardTemplate(bugType);
    }


    private static String getSqlByPath(String path){
        try(InputStream input=
                    DashboardChartController.class.getResourceAsStream(path)){
            return IOUtils.toString(input, "utf-8");
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }



    public List teamDashboardTemplate(String bugType){
        if(AuthUtils.isAuditOrAdmin()){
            return template.queryForList(String.format(SQL_GET_TOP10_BUG_ORDER_TEAM,bugType));
        }
        Collection<String> projectIds=projectService.getMyProjectIds();
        if(projectIds.isEmpty()) {
            return Lists.newArrayList();
        }
        return template.queryForList(String.format(SQL_GET_TOP10_BUG_ORDER_USER_TEAM,bugType,StringUtils.join(projectIds,",")));
    }


    @RequestMapping(method=RequestMethod.GET,value="/top10Vulner")
    public List getTop10Vulner(){
        String bugType = "'vulner_blocker','vulner_critical'";
        return teamDashboardTemplate(bugType);
    }


    @RequestMapping(method=RequestMethod.GET,value="/top10CodeSmell")
    public List getTop10CodeSmell(){
        String bugType = "'code_smell_blocker','code_smell_critical'";
        return teamDashboardTemplate(bugType);
    }

    @RequestMapping(method=RequestMethod.GET,value="/top10DC")
    public List getTop10DCResult(){
        String bugType = "'dc_high'";
        return teamDashboardTemplate(bugType);
    }
}


