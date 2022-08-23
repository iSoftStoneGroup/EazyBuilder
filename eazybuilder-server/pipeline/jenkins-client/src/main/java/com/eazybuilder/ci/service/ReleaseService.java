package com.eazybuilder.ci.service;

import com.alibaba.fastjson.JSONObject;
import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.base.PageSearch;
import com.eazybuilder.ci.controller.vo.ProjectBuildVo;
import com.eazybuilder.ci.entity.*;
import com.eazybuilder.ci.entity.devops.*;
import com.eazybuilder.ci.rabbitMq.SendRabbitMq;
import com.eazybuilder.ci.repository.ReleaseDao;
import com.eazybuilder.ci.util.AuthUtils;
import com.eazybuilder.ci.util.DingtalkWebHookUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@PageSearch(value = QRelease.class, fields = {"title","imageTag"})
@Service
public class ReleaseService extends AbstractCommonServiceImpl<ReleaseDao, Release> implements CommonService<Release> {

    private static Logger logger = LoggerFactory.getLogger(ReleaseService.class);

    @Resource
    RedmineService redmineService;
    @Resource
    ProjectService projectService;
    @Resource
    PipelineServiceImpl pipelineService;
    @Resource
    EventService eventService;
    @Autowired
    SendRabbitMq sendRabbitMq;
    @Resource
    ReleaseProjectService releaseProjectService;
    @Resource
    PipelineProfileService pipelineProfileService;
    @Resource
    UserService userService;
    @Resource
    TeamServiceImpl teamService;
    





    public Map<Project,List<ProjectBuildVo>>  getProjects(Release release) throws Exception {
        String sprintsId= release.getSprintId();
        List<String> gitPathList = Arrays.asList(release.getGitPath().split(","));
        Set<String> gitPathSet = new HashSet<>();
        gitPathSet.addAll(gitPathList);
        String issuesId= release.getIssuesId();
        String tagVersion = "";
        String branchVersion = "";
        List<Project> projectList = new ArrayList<>();
        //通过需求号在redmine中查出对应的项目名
        List<RedmineIssues> issuesBySprintId = redmineService.getIssuesBySprintId(sprintsId);
        List<RedmineIssues> batchIssues = redmineService.getBatchIssues(issuesBySprintId, issuesId);
        StringBuilder tagDetail =null;
        List<Event>  events= null;
        Map<Project,List<ProjectBuildVo>> projectProfileMap = new HashedMap();
        String commonStr = "";
        for (String gitPath : gitPathSet) {
            //拼装git tag描述 循环所有需求，将同一个项目下的需求描述整合到一起
            tagDetail = new StringBuilder();
        	Project project = null;
            List<Project> projects = projectService.findByScmUrl(gitPath);
            if(!projects.isEmpty()&&projects.size()>0) {
                project=projects.get(0);
                this.getTagDetail(batchIssues, project.getName(), tagDetail);
            }
            if (null == project) {
                logger.error("项目：+{}不存在,请核对项目名是否和ci里的一致",gitPath);
                continue;
            }
            //tag 命名规范 项目名_冲刺看板名_时间戳
            //如IPAAS_20210903_20210809090212
            //FIXME 看板名称可能带中文或者特殊字符，需要处理一下
            JSONObject sprintsById = redmineService.getSprintsById(sprintsId);
            String sprintName = sprintsById.getString("name").replaceAll("-", "");
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
            if(StringUtils.isBlank(commonStr)) {
                commonStr = "_" + sprintName + "_" + df.format(new Date());
            }
            if(events==null) {
                events = eventService.findByTeamIdAndEventType(project.getTeam().getId(), EventType.applyOntestAllowed);
            }
            //事件设定
            List<ProjectBuildVo> projectBuildVos = eventService.getPipelineProfile(project,events);
            if(!projectBuildVos.isEmpty()) {
                projectProfileMap.put(project, projectBuildVos);
                for(ProjectBuildVo projectBuildVo:projectBuildVos){
                    if(projectBuildVo.getProfile()!=null){
                        PipelineProfile profile = pipelineProfileService.findOne(projectBuildVo.getProfile());
                        release.setNameSpace(profile.getNameSpace());
                        if(profile.isAddTag()) {
                            if (StringUtils.isNotBlank(profile.getTagPrefix())) {
                                tagVersion = profile.getTagPrefix() + commonStr;
                            }
                        }
                        if(profile.isCreateBranch()) {
                            if (StringUtils.isNotBlank(profile.getReleasePrefix())) {
                                branchVersion = profile.getReleasePrefix() + commonStr;
                            }
                        }
                        projectBuildVo.setCreteBranchVersion(branchVersion);
                        projectBuildVo.setReleaseDockerVersion(tagVersion);
                        projectBuildVo.setCreateTagVersion(tagVersion);
                        projectBuildVo.setCreateTagDetail(tagDetail.toString());
                        projectBuildVo.setProjectId(project.getId());

                        ReleaseProject releaseProject = new ReleaseProject();
                        releaseProject.setReleaseId(release.getId());
                        releaseProject.setCreteBranchVersion(branchVersion);
                        releaseProject.setReleaseDockerVersion(tagVersion);
                        releaseProject.setCreateTagVersion(tagVersion);
                        releaseProject.setCreateTagDetail(tagDetail.toString());
                        releaseProject.setProjectId(project.getId());
                        releaseProject.setProjectGitUrl(project.getScm().getUrl());
                        //在这里保存一下releaseProject
                        releaseProjectService.save(releaseProject);
                        projectBuildVo.setReleaseProjectId(releaseProject.getId());
                        release.getReleaseProjects().add(releaseProject);
                    }
                }
            }
            //release里的版本信息等只用来页面展示
            release.setImageTag(tagVersion);
            release.setTagDetail(tagDetail.toString());
            projectList.add(project);
        }
        release.setProjectList(projectList);
        logger.info("生产的tag标签：{}",tagVersion);
        dao.save(release);
        return projectProfileMap;
    }

    //上线时查询之前提测生成的tag，tag状态为提测中或已完成提测。
    public List<Release> findBySprintId(String sprintId) {
        return (List<Release>)dao.findAll(QRelease.release.sprintId.eq(sprintId).and(QRelease.release.releaseStatus.eq(Status.IN_PROGRESS)).or(QRelease.release.releaseStatus.eq(Status.SUCCESS)));
    }

    public Release findByPipeline(Pipeline pipeline) {
        return (Release)dao.findOne(QRelease.release.pipelineList.contains(pipeline)).orElse(null);
    }


    




    private void getConfigEdit(List<RedmineIssues> batchIssues,Project project) throws Exception {
        //取出一个
        for (int i = 0; i < batchIssues.size(); i++) {
            RedmineIssues redmineIssues = batchIssues.get(i);
            String gitlabPath=redmineIssues.getGitlabPath();
            if(StringUtils.isNotBlank(gitlabPath)) {
                logger.info("需求对应的git地址:{}",gitlabPath);
            }else {
                logger.error("需求对应的git地址为空，垃圾数据，过滤掉:{}",redmineIssues.getProjectName());
                continue;
            }
            //在这个地方判断是否修改了配置文件
            if (project.getName().equals(redmineIssues.getSubProjectIdentifier())) {
                JSONObject jsonObject = redmineService.getIssueDetailById(redmineIssues.getId().toString());
                if(jsonObject.containsKey("configEdit")){
                    project.setConfigEdit(jsonObject.getString("configEdit"));
                    if(project.getConfigEdit().equals("1")){
                        logger.info("该项目下有需求需要更新配置文件");
                        break;
                    }
                }
            }
        }
    }

    private void getTagDetail( List<RedmineIssues> batchIssues,String name,StringBuilder tagDetailBuf) {
        //取出一个
    	int total=1;
        for (int i = 0; i < batchIssues.size(); i++) {
            RedmineIssues redmineIssues = batchIssues.get(i);
            String gitlabPath=redmineIssues.getGitlabPath();
           
            if(StringUtils.isNotBlank(gitlabPath)) {
            	 logger.info("需求对应的git地址:{}",gitlabPath);
            }else {
            	logger.error("需求对应的git地址为空，垃圾数据，过滤掉:{}",redmineIssues.getProjectName());
            	continue;
            }
            if (name.equals(redmineIssues.getSubProjectIdentifier())) {
                if (redmineIssues.getTrackerName().equals("bug")) {
                	tagDetailBuf.append((total++) + "- (BUGFIX-" + redmineIssues.getId() + ")  " + redmineIssues.getSubject() + "\\n");
                } else if (redmineIssues.getTrackerName().equals("功能")) {
                	tagDetailBuf.append((total++) + "- (FEATURE-" + redmineIssues.getId() + ")  " + redmineIssues.getSubject() + "\\n");
                }
            }
        }
    }

    public void updateRelease(Release release,Map<Project,List<ProjectBuildVo>> projectProfileMap) {
        List<Pipeline> pipelineList = new ArrayList<>();
        try {
            for (Map.Entry<Project,List<ProjectBuildVo>> entry: projectProfileMap.entrySet()) {
                Project project = entry.getKey();
                List<ProjectBuildVo> pipelines = entry.getValue();
                //通过渲染freemarker脚本，在jenkins中执行相关操作。
                for(ProjectBuildVo projectBuildVo: pipelines) {
                    projectBuildVo.setPipelineType(PipelineType.release);
                    Pipeline pipeline = pipelineService.triggerPipeline(project.getId(), projectBuildVo);
                    //在这里将pipelineId set到releaseProject.historyId。
                    ReleaseProject releaseProject = releaseProjectService.findOne(projectBuildVo.getReleaseProjectId());
                    pipelineList.add(pipeline);
                    releaseProject.setHistoryId(pipeline.getId());
                    releaseProjectService.save(releaseProject);

                }
                logger.info("项目 {}-{}开始打标签: ", project.getName(), project.getTagVersion());
            }
        } catch (Exception e) {
            logger.error("上线自动化流程出现异常" + e.getMessage(), e);
        } finally {
            release.setPipelineList(pipelineList);
            logger.info("保存提测表与历史记录表的关联关系");
            dao.save(release);
        }
    }

    @Override
    public void save(Release entity) {
        //申请时间
        if (StringUtils.isBlank(entity.getId())) {
            entity.setCreateDate((new Date()));
            sendRabbitMq.sendReleaseStatusMq(entity.getIssuesId(),IssuesStatus.toBeReviewed);

        }
        User user = AuthUtils.getCurrentUser();
        if(null!=user) {
            //审批人
            if (StringUtils.isNotBlank(entity.getReleaseUserName())) {
                entity.setBatchUserName(user.getName());
            }
            //申请人
            if (StringUtils.isBlank(entity.getReleaseUserName())) {
                entity.setReleaseUserName(user.getName());
            }
        }
//        if (null == entity.getBatchStatus()) {
//            entity.setBatchStatus(Status.NOT_EXECUTED);
//        }
        if (null == entity.getReleaseStatus()) {
            entity.setReleaseStatus(Status.NOT_EXECUTED);
        }
        if (Status.SUCCESS == entity.getBatchStatus()) {
            entity.setReleaseStatus(Status.IN_PROGRESS);
            sendRabbitMq.sendReleaseStatusMq(entity.getIssuesId(),IssuesStatus.testing);
        }
        if (Status.FAILED == entity.getBatchStatus()) {
            entity.setReleaseStatus(Status.ABORTED);
        }
        dao.save(entity);
    }

    public  void  sendDingTalkAfterPassedRease(Release entity) {
        List<User> byUserName = userService.findByUserName(entity.getReleaseUserName());
        Assert.notEmpty(byUserName,"根据提测申请人姓名【 "+entity.getReleaseUserName()+" 】查询用户信息未找到！");
        User user = byUserName.get(0);
        List<String> emails = Arrays.asList(user.getEmail());
        StringBuilder sb=new StringBuilder();
        sb.append("**").append("提测标题:").append("**").append(entity.getTitle()).append("\n");
        sb.append("\n");
        sb.append("**").append("提测申请号:").append("**").append(null!=entity.getReleaseCode()?entity.getReleaseCode():"获取失败！").append("\n");
        sb.append("\n");
        sb.append("**").append("审批人:").append("**").append(entity.getBatchUserName()).append("\n");
        sb.append("\n");
        String title=null;
        MsgProfileType msgProfileType=null;
        if(Status.SUCCESS == entity.getBatchStatus()){
            sb.append("**").append("审批状态:").append("**").append("通过！");
            title="申请提测已通过";
            sb.append(title).append("请及时处理后续流程！").append("\n");
            msgProfileType=MsgProfileType.releasePass;
        }else {
            sb.append("**").append("审批状态:").append("**").append("未通过！").append("\n");
            title="申请提测未通过";
            sb.append("\n");
            sb.append("**").append("原因如下:").append("**").append( StringUtils.isNotBlank(entity.getBatchDetail()) ? entity.getBatchDetail():"无！" );
            msgProfileType=MsgProfileType.releaseRefused;
        }
        Team team = teamService.findOne(entity.getTeamId() + "");
        DingtalkWebHookUtil.sendDingtalkPrivateMsgBymq(title,sb.toString(),team.getCode(),emails,msgProfileType,sendRabbitMq);
    }



    public static void main(String[] args) {
        System.out.println(IssuesStatus.testing);
        System.out.println(IssuesStatus.testing.getName());
    }
}
