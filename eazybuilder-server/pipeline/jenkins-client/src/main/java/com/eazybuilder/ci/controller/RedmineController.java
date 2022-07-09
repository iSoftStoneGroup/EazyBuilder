package com.eazybuilder.ci.controller;

import com.eazybuilder.ci.entity.devops.RedmineIssues;
import com.eazybuilder.ci.entity.devops.RedmineProject;
import com.eazybuilder.ci.entity.devops.RedmineSprint;
import com.eazybuilder.ci.service.RedmineService;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/api/redmine")
public class RedmineController  {

    @Resource
    RedmineService redmineService;

    @RequestMapping(value="/getRedmineTeams",method=RequestMethod.GET)
    @ApiOperation("查询redmine 当前用户拥有的项目")
    public List<RedmineProject> getRedmineTeams() throws Exception {
        return redmineService.getRedmineTeams();
    }
    @RequestMapping(value="/getRedmineSprintByTeam",method=RequestMethod.GET)
    @ApiOperation("根据项目组id查询 对应的sprint")
    public List<RedmineSprint> getRedmineProjects(@RequestParam(value = "teamId") String teamId) throws Exception {
        return redmineService.getRedmineSprintByTeam(teamId);
    }

    @RequestMapping(value="/getIssuesBySprintId",method=RequestMethod.GET)
    @ApiOperation("根据sprintId查询issues")
    public List<RedmineIssues> getIssuesUrl(@RequestParam(value = "sprintId") String sprintId,
                                            @RequestParam(value = "teamName") String teamName) throws Exception {

        List<RedmineIssues> issuesBySprintId = redmineService.getIssuesBySprintId(sprintId);

        //如果是审批状态的话，则展示需要上线的需求。
//        if(batch){
//            issuesBySprintId = redmineService.getBatchIssues(issuesBySprintId,issuesId);
//        }
        //将一级菜单改为以gitpath展示。如果一级菜单没有gitpath，则去掉一级菜单。
        //将在当前冲刺版没有活跃度的项目拼到树形结构里
        issuesBySprintId= redmineService.getZtreeByGitUrl(issuesBySprintId,teamName);
        return issuesBySprintId;
    }

    @RequestMapping(value="/refreshIssuesBySprintId",method=RequestMethod.GET)
    @ApiOperation("根据sprintId查询issues")
    public List<RedmineIssues> refreshIssuesBySprintId(@RequestParam(value = "sprintId") String sprintId,
                                                       @RequestParam(value = "teamName") String teamName) throws Exception {
        List<RedmineIssues> issuesBySprintId = redmineService.synIssuesBySprintId(sprintId);
        issuesBySprintId= redmineService.getZtreeByGitUrl(issuesBySprintId,teamName);
        return issuesBySprintId;
    }


    @RequestMapping(value="/getIssueDetailById",method=RequestMethod.GET)
    @ApiOperation("根据issuesId查询明细")
    public com.alibaba.fastjson.JSONObject getIssueDetailById(@RequestParam(value = "issueId") String issueId) throws Exception {
        return redmineService.getIssueDetailById(issueId);
    }

}
