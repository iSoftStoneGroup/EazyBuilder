package com.eazybuilder.ci.controller;

import com.eazybuilder.ci.base.CRUDRestController;
import com.eazybuilder.ci.controller.vo.ProjectBuildVo;
import com.eazybuilder.ci.entity.Pipeline;
import com.eazybuilder.ci.entity.Project;
import com.eazybuilder.ci.entity.Team;
import com.eazybuilder.ci.entity.devops.Release;
import com.eazybuilder.ci.entity.devops.Status;
import com.eazybuilder.ci.entity.report.StatusGuard;
import com.eazybuilder.ci.service.DockerDigestService;
import com.eazybuilder.ci.service.PipelineServiceImpl;
import com.eazybuilder.ci.service.ReleaseService;
import com.eazybuilder.ci.service.TeamServiceImpl;
import com.wordnik.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/release")
public class ReleaseController extends CRUDRestController<ReleaseService, Release> {

    private static Logger logger = LoggerFactory.getLogger(ReleaseController.class);

    @Resource
    ReleaseService releaseService;

    @Resource
    DockerDigestService dockerDigestService;

    @Resource
    TeamServiceImpl teamService;

    @Resource
    PipelineServiceImpl pipelineService;

    @RequestMapping(value = "/getPipelineByGitPath", method = RequestMethod.POST)
    public ReleaseVO getPipelineByGitPath(@RequestBody List<String> gitPathList){
        List<Pipeline> pipelineList = new ArrayList<Pipeline>();
        ReleaseVO releaseVO = new ReleaseVO();
        Boolean checkFlag = Boolean.TRUE;
        releaseVO.setCheckFlag(checkFlag);
        releaseVO.setPipelineList(pipelineList);
        try {
            pipelineList = pipelineService.getPipelineByGitPath(gitPathList);
            if(null!=pipelineList&&pipelineList.size()>0){
                for(Pipeline pipeline:pipelineList){
                    if(pipeline.getStatusGuard()== StatusGuard.FAILED||!pipeline.getStatus().isSuccess()){
                        checkFlag=Boolean.FALSE;
                    }
                }
                releaseVO.setCheckFlag(checkFlag);
                releaseVO.setPipelineList(pipelineList);
            }
            return releaseVO;
        } catch (Exception e) {
            e.printStackTrace();
            releaseVO.setCheckFlag(Boolean.FALSE);
            return releaseVO;
        }
    }

    @RequestMapping(value = "/getReleaseById", method = RequestMethod.GET)
    public Release getReleaseById(@RequestParam(value = "releaseId") String releaseId) throws Exception {
        Release release = releaseService.findOne(releaseId);
        Hibernate.initialize(release.getPipelineList());
        return release;
    }

    @RequestMapping(value = "/checkReleaseSprints", method = RequestMethod.GET)
    public Boolean checkReleaseSprint(@RequestParam(value = "sprintId") String sprintId,
                                      @RequestParam(value = "teamName") String teamName) throws Exception {
        List<Release> releaseList = releaseService.findBySprintId(sprintId);
        Team team = teamService.findByName(teamName);
        if(team.getSprintMultiTest()!=null && team.getSprintMultiTest()  && CollectionUtils.isNotEmpty(releaseList)){
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/updateRelease", method = {RequestMethod.POST})
    @ApiOperation("更新发版页面审核状态，并且进行一系列自动化操作")
    public void updateRelease(@RequestBody Release release) throws Exception {
        //更新数据
        releaseService.save(release);
        //1.判断上线申请是否通过审核
        if (Status.SUCCESS == release.getBatchStatus()) {
            //2.获取项目信息。拼装tag 具体描述
            Map<Project,List<ProjectBuildVo>> projectProfileMap = null;
            try {
                projectProfileMap = releaseService.getProjects(release);
            } catch (Exception e) {
                logger.info("匹配提测对应的流水线项目信息 出现异常" + e.getMessage(), e);
                throw new Exception("匹配提测对应的流水线项目信息 出现异常: " + e);
            }
            //3.拉取需求代码，更新pom版本并且提交到master分支、创建tag标签
            releaseService.updateRelease(release, projectProfileMap);
        }
    }

    @RequestMapping(value = "/getApplyOnlineTags", method = {RequestMethod.GET})
    @ApiOperation("上线时是查看板下的 提测tag")
    public List<Release> getApplyOnlineTags(@RequestParam(value = "sprintId") String sprintId) throws Exception {
        return releaseService.findBySprintId(sprintId);
    }

    class ReleaseVO {

        private List<Pipeline> pipelineList;

        private Boolean checkFlag;

        public List<Pipeline> getPipelineList() {
            return pipelineList;
        }

        public void setPipelineList(List<Pipeline> pipelineList) {
            this.pipelineList = pipelineList;
        }

        public Boolean getCheckFlag() {
            return checkFlag;
        }

        public void setCheckFlag(Boolean checkFlag) {
            this.checkFlag = checkFlag;
        }
    }


}