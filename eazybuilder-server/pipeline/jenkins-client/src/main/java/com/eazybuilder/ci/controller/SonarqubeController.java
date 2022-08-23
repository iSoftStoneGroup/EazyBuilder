package com.eazybuilder.ci.controller;


import com.eazybuilder.ci.entity.Pipeline;
import com.eazybuilder.ci.entity.Project;
import com.eazybuilder.ci.service.ProjectService;
import com.eazybuilder.ci.service.SonarqubeService;

import com.eazybuilder.ci.service.TeamServiceImpl;
import com.wordnik.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/sonarqube")
public class SonarqubeController {

    @Resource
    SonarqubeService sonarqubeService;

    @Resource
    ProjectService projectService;

    @RequestMapping(value="/getLanguagesList",method=RequestMethod.GET)
    @ApiOperation("查询redmine 当前用户拥有的项目")
    public String getRedmineTeams() throws Exception {
        return sonarqubeService.getLanguagesList();
    }

    @RequestMapping(value="/getQualityprofiles",method=RequestMethod.GET)
    @ApiOperation("调用sonarqube查询语言对应的规则集列表")
    public Map<String, List<String>> getPipelineInfo() throws Exception {
        return sonarqubeService.getQualityprofiles();
    }

    @RequestMapping(value="/qualityprofilesAddProject",method=RequestMethod.POST)
    @ApiOperation("调用sonarqube 为指定项目设置默认规则集")
    public void qualityprofilesAddProject(@RequestParam String teamId,
                                          @RequestBody Map<String,Object> map) throws Exception {
        List<Project> projects = projectService.findAllByTeamId(teamId);
        for(Project project:projects){
            for(Map.Entry<String,Object> entry: map.entrySet()){
                sonarqubeService.qualityprofilesAddProject(project.getName()+":"+project.getId(),
                        entry.getKey(),
                        JSONObject.fromObject(entry.getValue()).getString("rule"));
            }
        }

//
    }

}
