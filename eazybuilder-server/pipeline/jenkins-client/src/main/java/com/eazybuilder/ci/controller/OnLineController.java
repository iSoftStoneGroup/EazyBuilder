package com.eazybuilder.ci.controller;

import com.alibaba.fastjson.JSON;
import com.eazybuilder.ci.OperLog;
import com.eazybuilder.ci.base.CRUDRestController;
import com.eazybuilder.ci.controller.vo.ProjectBuildVo;
import com.eazybuilder.ci.entity.*;
import com.eazybuilder.ci.entity.devops.Online;
import com.eazybuilder.ci.entity.devops.Release;
import com.eazybuilder.ci.entity.devops.Status;
import com.eazybuilder.ci.entity.docker.DockerDigest;
import com.eazybuilder.ci.service.*;
import com.wordnik.swagger.annotations.ApiOperation;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/onLine")
public class OnLineController extends CRUDRestController<OnlineService, Online> {

    private static Logger logger = LoggerFactory.getLogger(OnLineController.class);

    @Resource
    ReleaseService releaseService;

    @Resource
    DockerDigestService dockerDigestService;

    @Resource
    TeamServiceImpl teamService;

    @Resource
    PipelineServiceImpl pipelineService;

    @Resource
    UserService  userService;


    @Override
    @RequestMapping(method={RequestMethod.POST,RequestMethod.PUT})
    @ApiOperation("保存并发送上线申请给审批人")
    @OperLog(module = "persist",opType = "save",opDesc = "保存并发送上线申请给审批人")
    public Online save(@RequestBody Online entity){
        if(logger.isDebugEnabled()){
            logger.debug("保存并发送上线申请给审批人!");
            logger.debug(JSON.toJSONString(entity));
        }
        service.save(entity);
        return entity;
    }







    @RequestMapping(value = "/getProjectByOnLineTag", method = RequestMethod.GET)
    public List<Project> getProjectByOnLineTag(@RequestParam(value = "onLineId") String onLineId) {
        Online online = service.findOne(onLineId);
        Release release = releaseService.findOne(online.getReleaseId());
        return release.getProjectList();
    }


    @RequestMapping(value="/getOnlineTags",method=RequestMethod.GET)
    public List<Online> getOnlineTags() {
        return service.getOnlineTags();
    }


    @RequestMapping(value="/getIssuesAndReportByReleaseId",method=RequestMethod.GET)
    @ApiOperation("根据releaseId查询issues")
    public Map<String,Object> getIssuesUrl(@RequestParam(value = "releaseId") String releaseId) throws Exception {
        Map<String,Object> data = new HashMap<>();
        Release release = releaseService.findOne(releaseId);
        data.put("issuesTreeJson",release.getIssuesTreeJson());
        List<DtpReport> dtpReportList = new ArrayList<>();
        List<Project> projectList = release.getProjectList();
        //上线的时候展示的自动化测试报告是根据分支名和项目名在历史记录中匹配最新的结果
        Set<String> projectIdSet = projectList.stream().map(Project::getId).collect(Collectors.toSet());
        if (projectIdSet.size() <= 0) {
            return null;
        } else {
            //查询出该分支以及项目命中的所有自动化测试报告
            for(String projectId:projectIdSet) {
                List<Pipeline> pipelines = (List<Pipeline>) pipelineService.findPipelineByProjectIdAndTargetBranch(projectId, release);
                Map<String,Object> testTypeMap = new HashedMap();
                for(Pipeline pipeline:pipelines) {
                    for(DtpReport dtpReport:pipeline.getDtpReports()){
                        if (!testTypeMap.containsKey(dtpReport.getTestType())&&StringUtils.isNotBlank(dtpReport.getTestType())){
                            dtpReportList.add(dtpReport);
                            testTypeMap.put(dtpReport.getTestType(),"");
                        }
                    }
                }
                pipelines.clear();
            }
        }
        data.put("dtpReports",dtpReportList);
        return data;
    }

    @RequestMapping(value="/findDockerDigest",method=RequestMethod.GET)
    public List<DockerDigest> findDockerDigest(@RequestParam(value = "releaseId") String releaseId,@RequestParam(value = "teamName") String teamName) {
       return service.findDockerDigest(releaseId,teamName);
    }



    @RequestMapping(value = "/updateRelease",method = {RequestMethod.POST})
    @ApiOperation("更新发版页面审核状态，并且进行一系列自动化操作")
    public void updateRelease(@RequestBody Online entity) throws Exception {
        //更新数据
        service.save(entity);
        //1.判断上线申请是否通过审核
        if (Status.SUCCESS == entity.getBatchStatus()) {
            //2.获取项目信息。拼装tag 具体描述
            Map<Project,List<ProjectBuildVo>> projectProfileMap = null;
            try {
                projectProfileMap = service.getApplyOnlineProjects(entity);
                Release release = releaseService.findOne(entity.getReleaseId());
                service.saveJobOnlineEntity(entity, release);
            } catch (Exception e) {
                logger.info("上线审批通过后出现异常" + e.getMessage(), e);
                throw new Exception("上线审批通过后出现异常: " + e);
            }
            //3.拉取需求代码，更新pom版本并且提交到master分支、创建tag标签
            service.updateOnline(projectProfileMap);
            //4.给指定执行用户发送通知邮件。 上线哪些应用，对应的镜像名，是否涉及变更数据脚本，计划实施时间  、tag对应的测试报告
//            service.sendOnLineMail(entity,projects);
        }
        //审批成功或者失败都发送消息提醒
        try {
            service.sendDingTalkAfterPassedRease(entity);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
