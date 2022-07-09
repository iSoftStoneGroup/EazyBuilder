package com.eazybuilder.ci.service;

import com.alibaba.fastjson.JSONObject;
import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.base.PageSearch;
import com.eazybuilder.ci.constant.RoleEnum;
import com.eazybuilder.ci.controller.vo.ProjectBuildVo;
import com.eazybuilder.ci.entity.*;
import com.eazybuilder.ci.entity.devops.*;
import com.eazybuilder.ci.mail.MailSenderHelper;
import com.eazybuilder.ci.rabbitMq.SendRabbitMq;
import com.eazybuilder.ci.repository.OnlineDao;
import com.eazybuilder.ci.util.AuthUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@PageSearch(value = QRelease.class, fields = {"title"})
@Service
public class OnlineService extends AbstractCommonServiceImpl<OnlineDao, Online> implements CommonService<Online> {

    @Autowired
    SystemPropertyService propService;

    @Resource
    BuildJobService buildJobService;

    @Resource
    TeamServiceImpl teamService;

    @Resource
    RedmineService redmineService;

    @Resource
    PipelineServiceImpl pipelineService;
    @Autowired
    Configuration configuration;
    @Resource
    UserService userService;
    @Autowired
    MailSenderHelper mailSender;

    @Resource
    ReleaseService releaseService;

    @Resource
    SendRabbitMq sendRabbitMq;

    @Resource
    EventService eventService;

    @Resource
    PipelineProfileService pipelineProfileService;

    @Resource
    ReleaseProjectService releaseProjectService;

    private static Logger logger = LoggerFactory.getLogger(OnlineService.class);


    @Override
    public Page<Online> pageSearch(Pageable pageable, String searchText) {
        Page<Online> onlines = super.pageSearch(pageable, searchText);
        onlines.forEach(online -> {
                    if (StringUtils.isBlank(online.getMemberName())&&StringUtils.isNotBlank(online.getMemberId())) {
                        online.setMemberName(userService.findOne(online.getMemberId()).getName());
                    }
                }

        );
        return onlines;
    }

    /**
     * 上线申请通过，直接从数据库查出冲刺板下面，tag名下面对应的项目，执行镜像晋级
     * @param online
     * @return
     * @throws Exception
     */
    public Map<Project,List<ProjectBuildVo>> getApplyOnlineProjects(Online online) throws Exception {
        String releaseId = online.getReleaseId();
        //查询数据库，获取冲刺板下面，tag名下面对应的项目
        Release release = releaseService.findOne(releaseId);
        List<Project> projectList = release.getProjectList();
        JSONObject sprintsById = redmineService.getSprintsById(release.getSprintId());
        String sprintName = sprintsById.getString("name").replaceAll("-", "");
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
        String commonStr="_"+sprintName+"_"+ df.format(new Date());
        String onlineTagVersion = "";
        Map<Project,List<ProjectBuildVo>> projectProfileMap = new HashedMap();
        List<Event>  events= null;

        for(int i=0;i<projectList.size();i++) {
            Project project = projectList.get(i);
            if(events==null) {
                events = eventService.findByTeamIdAndEventType(project.getTeam().getId(), EventType.applyOnlineAllowed);
            }
            List<ProjectBuildVo> projectBuildVos = eventService.getPipelineProfile(project, events);
            if(!projectBuildVos.isEmpty()) {
                projectProfileMap.put(project, projectBuildVos);
                for(ProjectBuildVo projectBuildVo:projectBuildVos){
                    if(projectBuildVo.getProfile()!=null){
                        PipelineProfile profile = pipelineProfileService.findOne(projectBuildVo.getProfile());
                        ReleaseProject releaseProject = releaseProjectService.findByReleaseIdAndProjectId(releaseId, project.getId());
                        String createTagVersion = releaseProject.getCreateTagVersion();
                        if(StringUtils.isNotBlank(profile.getTagPrefix())){
                            onlineTagVersion= profile.getTagPrefix()+commonStr;
                            onlineTagVersion= onlineTagVersion+"_T"+createTagVersion.split("_")[2];
                        }
//                        ReleaseProject releaseProject = new ReleaseProject();
//                        releaseProject.setOnlineDockerVersion(onlineTagVersion);
//                        releaseProject.setProjectId(project.getId());
//                        releaseProject.setProjectGitUrl(project.getScm().getUrl());
//                        releaseProjectService.save(releaseProject);
                        projectBuildVo.setCreateTagVersion(onlineTagVersion);
                        //如果是上线的话需要指定操作分支为提测的时候创建的test分支。
                        projectBuildVo.setArriveTagName(online.getImageTag().toLowerCase());
                        //提测的docker版本
                        projectBuildVo.setReleaseDockerVersion(createTagVersion);
                        projectBuildVo.setNameSpace(release.getNameSpace());
                    }
                }
            }
            project.setImageTag(onlineTagVersion);
            project.setNameSpace(release.getNameSpace());
        }
        online.setOnLineImageTag(onlineTagVersion);
        save(online);
        return projectProfileMap;
    }

    //上线时查询之前提测生成的tag，tag状态为上线中。
    public List<Online> getOnlineTags() {
        //admin可以上线所有tag
        if(Role.existRole(AuthUtils.getCurrentUser().getRoles(), RoleEnum.admin)){
            return (List<Online>)dao.findAll(QOnline.online.releaseStatus.eq(Status.ON_LINE_IN));
        }else{
            return (List<Online>)dao.findAll(QOnline.online.releaseStatus.eq(Status.ON_LINE_IN).and(QOnline.online.memberId.eq(AuthUtils.getCurrentUser().getId())));
        }
    }


    public void sendOnLineMail(Online online,List<Project> projects) throws Exception {
        //渲染邮件
        Template mailTemplate = configuration.getTemplate("mail-onLine.ftl");
        Map<String, Object> context = new HashMap<>();
        context.put("onLine",online);
        context.put("projects",projects);


        String mailHtml = FreeMarkerTemplateUtils.processTemplateIntoString(mailTemplate, context);
        //发送邮件
        logger.info("ci发送邮件内容 {}", mailHtml);
        User user = userService.findOne(online.getMemberId());
        String[] email= {user.getEmail()};
//        mailSender.sendMail(email, null, mailHtml, null);
    }

    public void updateOnline(Map<Project,List<ProjectBuildVo>> projectProfileMap) {
        List<Pipeline> pipelineList = new ArrayList<>();
        for (Map.Entry<Project, List<ProjectBuildVo>> entry : projectProfileMap.entrySet()) {
            Project project = entry.getKey();
            List<ProjectBuildVo> pipelines = entry.getValue();
            Pipeline pipeline =null;
            //通过渲染freemarker脚本，在jenkins中执行相关操作。
            try {
                for (ProjectBuildVo projectBuildVo : pipelines) {
                    logger.info("项目 {}-{}开启上线流程", project.getName(), project.getImageTag());
                    projectBuildVo.setPipelineType(PipelineType.online);
                    pipeline= pipelineService.triggerPipeline(project.getId(), projectBuildVo);
                    pipelineList.add(pipeline);
                }
            } catch (Exception e) {
                logger.error("上线自动化流程出现异常" + e.getMessage(), e);
            }
        }
    }
    @Override
    public void save(Online entity) {
        Release release = releaseService.findOne(entity.getReleaseId());
        saveOnlineEntity(entity,release);
    }

    public void saveJobOnlineEntity(Online entity,Release release) throws Exception {
        BuildJob buildJob = new BuildJob();
        buildJob.setName(entity.getTitle()+(entity.isImmedIatelyOnline()? "(紧急)":""));
        buildJob.setOnLine(true);
        buildJob.setOnLineId(entity.getId());
        buildJob.setImmedIatelyOnline(true);
        //从系统参数里面取值
        PipelineProfile profile = pipelineProfileService.findByName(getJobOnlineProfile());
        buildJob.setProfileId(profile.getId());
        List<Project> projectList = new ArrayList<>(release.getProjectList().size());
        release.getProjectList().forEach(project -> projectList.add(project));
        buildJob.setProjects(projectList);
        buildJob.setTriggerType(JobTrigger.manual);
        List<RedmineProject> redmineTeams = redmineService.getRedmineTeams();
        redmineTeams.forEach(redmineProject -> {
                    if (redmineProject.getId().equals(entity.getTeamId())) {
                        Team team = teamService.findByName(redmineProject.getName());
                        if(team!=null){
                            buildJob.setTeamId(team.getId());
                        }
                    }
                }
        );
        buildJobService.save(buildJob);
    }

    private void saveOnlineEntity(Online entity,Release release) {
        //申请时间
        if (StringUtils.isBlank(entity.getId())) {
            entity.setCreateDate((new Date()));
            sendRabbitMq.sendReleaseStatusMq(release.getIssuesId(),IssuesStatus.toBeOnline);
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
        if (null == entity.getReleaseStatus()) {
            entity.setReleaseStatus(Status.NOT_EXECUTED);
        }
        if (Status.SUCCESS == entity.getBatchStatus()) {
            entity.setReleaseStatus(Status.ON_LINE_IN);
            sendRabbitMq.sendReleaseStatusMq(release.getIssuesId(),IssuesStatus.toDeployed);
        }
        if (Status.FAILED == entity.getBatchStatus()) {
            entity.setReleaseStatus(Status.ABORTED);
        }
        dao.save(entity);
    }
    public String getJobOnlineProfile() {
        return (propService.getValue("job.online.profile.name", "ipaas生产环境部署"));
    }
}
