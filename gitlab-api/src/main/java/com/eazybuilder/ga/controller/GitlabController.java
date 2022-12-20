package com.eazybuilder.ga.controller;

import com.eazybuilder.ga.pojo.Result;
import com.eazybuilder.ga.pojo.gitlab.GitlabBranch;
import com.eazybuilder.ga.pojo.gitlab.GitlabGroup;
import com.eazybuilder.ga.pojo.gitlab.GitlabProject;
import com.eazybuilder.ga.service.GitlabService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/gitlab")
public class GitlabController {

    private static Logger logger = LoggerFactory.getLogger(GitlabController.class);

    @Autowired
    GitlabService gitlabService;

    @ApiOperation(value = "查询群组")
    @GetMapping("/findGroup")
    public List<GitlabGroup> findGroup(){
        try {
            return gitlabService.findGroup();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @ApiOperation(value = "查询群组下的项目")
    @GetMapping("/findProjectsByGroup")
    public List<GitlabProject> findProjectsByGroup(String group){
        try {
            return gitlabService.findProjectsByGroup(group);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @ApiOperation(value = "查询项目下的分支")
    @GetMapping("/findProjectBranch")
    public List<GitlabBranch> findProjectBranch(int projectId){
        try {
            return gitlabService.findProjectBranch(projectId);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @ApiOperation(value = "创建合并请求")
    @GetMapping("/createMR")
    public Result createMR(Integer id, String source_branch, String target_branch, String title){
        String result = gitlabService.createMR(id, source_branch, target_branch, title);
        if(null==result){
            return Result.ok().message("创建成功");
        }else{
            return Result.no().message(result);
        }
    }


}
