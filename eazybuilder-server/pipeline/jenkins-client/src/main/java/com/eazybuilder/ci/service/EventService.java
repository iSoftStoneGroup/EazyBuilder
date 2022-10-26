package com.eazybuilder.ci.service;

import com.alibaba.fastjson.JSONObject;
import com.eazybuilder.ci.entity.devops.*;
import com.google.common.collect.Lists;
import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.constant.RoleEnum;
import com.eazybuilder.ci.controller.vo.ProjectBuildVo;
import com.eazybuilder.ci.controller.vo.UserVo;
import com.eazybuilder.ci.entity.*;
import com.eazybuilder.ci.repository.EventDao;
import com.eazybuilder.ci.util.AuthUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class EventService extends AbstractCommonServiceImpl<EventDao, Event> implements CommonService<Event>{

    private static Logger logger= LoggerFactory.getLogger(EventService.class);

    @Autowired
    TeamServiceImpl teamServiceImpl;

    @Resource
    ProjectService projectService;

    @Resource
    ReleaseProjectService releaseProjectService;

    @Resource
    PipelineProfileService pipelineProfileService;

    @Resource
    UserService userService;

    @Resource
    BuildJobService buildJobService;

    @Autowired
    SystemPropertyService propService;

    /**
     * 根据mq传过来的json进行事件判定
     */
    public Map<Project,List<ProjectBuildVo>> getEventProject(JSONObject rabbitmqData) throws Exception {
        //根据.git路径来匹配项目
        if (rabbitmqData.containsKey("gitPath") && StringUtils.isNotBlank("gitPath")) {
            String gitPath = rabbitmqData.getString("gitPath");
            if(!gitPath.endsWith(".git")){
                gitPath=gitPath+".git";
            }
            List<Project> projects = projectService.findByRepo(gitPath);
            List<Event>  events= null;
            if (projects != null && !projects.isEmpty()) {
                Map<Project,List<ProjectBuildVo>> projectProfileMap = new HashedMap();
                for (Project project : projects) {
                    //根据redmine传过来的参数，区分构建过程
                    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
                    String dockerImageTag = df.format(new Date());
                    //根据redmine传过来的参数，区分构建过程
                    if (rabbitmqData.getString("profileType") != null) {
                        //根据teamId和事件类型查询对应的事件设定
                        if(events==null) {
                            EventType eventType = EventType.valueOf(rabbitmqData.getString("profileType"));
                            events = findByTeamIdAndEventType(project.getTeam().getId(),eventType);
                            String targetBranchName = "";
                            String sourceBranchName = "";
                            if(rabbitmqData.containsKey("targetBranchName")){
                                targetBranchName = rabbitmqData.getString("targetBranchName");
                            }
                            if(rabbitmqData.containsKey("sourceBranchName")){
                                sourceBranchName = rabbitmqData.getString("sourceBranchName");
                            }
                            List<ProjectBuildVo> projectBuildVos = getPipelineProfile(project, events, targetBranchName, sourceBranchName);
                            if(!projectBuildVos.isEmpty()) {
                                projectProfileMap.put(project, projectBuildVos);
                                for(ProjectBuildVo projectBuildVo:projectBuildVos){
                                    if(projectBuildVo.getProfile()!=null){
                                        PipelineProfile profile = pipelineProfileService.findOne(projectBuildVo.getProfile());
                                        if(StringUtils.isBlank(projectBuildVo.getCreateTagVersion())) {
                                            //生成镜像版本号 如果打镜像的话，就会用这个版本
                                            if (StringUtils.isNotBlank(rabbitmqData.getString("topCode")) && StringUtils.isNotBlank(rabbitmqData.getString("code"))) {
                                                dockerImageTag = rabbitmqData.getString("topCode") + "-" + rabbitmqData.getString("code") + "-" + df.format(new Date());
                                            }
                                            projectBuildVo.setCreateTagVersion(dockerImageTag);
                                        }
                                        if(eventType!=null) {
                                            if(eventType==EventType.merge) {
                                                projectBuildVo.setPipelineType(PipelineType.merge);
                                            }else if(eventType==EventType.push){
                                                projectBuildVo.setPipelineType(PipelineType.push);
                                            }else if(eventType==EventType.applyOntestAllowed){
                                                projectBuildVo.setPipelineType(PipelineType.release);
                                                String tagVersion = "";
                                                String branchVersion = "";
                                                if(profile.isAddTag()) {
                                                    if (StringUtils.isNotBlank(profile.getTagPrefix())) {
                                                        tagVersion = profile.getTagPrefix()+"-" + dockerImageTag;
                                                    }
                                                }
                                                if(profile.isCreateBranch()) {
                                                    if (StringUtils.isNotBlank(profile.getReleasePrefix())) {
                                                        branchVersion = profile.getReleasePrefix()+"-" + dockerImageTag;
                                                    }
                                                }
                                                projectBuildVo.setCreteBranchVersion(branchVersion);
                                                projectBuildVo.setReleaseDockerVersion(tagVersion);
                                                projectBuildVo.setCreateTagVersion(tagVersion);

                                                ReleaseProject releaseProject = new ReleaseProject();
                                                releaseProject.setCreteBranchVersion(branchVersion);
                                                releaseProject.setReleaseDockerVersion(tagVersion);
                                                releaseProject.setCreateTagVersion(tagVersion);
                                                releaseProject.setProjectId(project.getId());
                                                releaseProject.setProjectGitUrl(project.getScm().getUrl());
                                                releaseProject.setNameSpace(profile.getNameSpace());
                                                //在这里保存一下releaseProject
                                                releaseProjectService.save(releaseProject);
                                            }else if(eventType==EventType.applyOnlineAllowed){
                                                if(profile.isUpgradeDocker()) {
                                                    logger.info("上线命中的构建过程开启了制品晋级");
                                                    projectBuildVo.setPipelineType(PipelineType.online);
                                                    //1.找到之前的提测版本信息
                                                    List<ReleaseProject> releaseProjects = releaseProjectService.
                                                            findByLikeBranchVersionAndProjectUrl("%" +
                                                                    StringUtils.substringBeforeLast(dockerImageTag,"-") + "%", project.getScm().getUrl());
                                                    logger.info("根据任务号和git路径找到的提测信息：{}",releaseProjects.size());

                                                    if(!releaseProjects.isEmpty()) {
                                                        if(releaseProjects.size()>1) {
                                                            listSort(releaseProjects);
                                                        }
                                                        ReleaseProject releaseProject = releaseProjects.get(0);
                                                        String onlineTagVersion =profile.getTagPrefix() +"-"+dockerImageTag+
                                                                "-T"+StringUtils.substringAfterLast(releaseProject.getCreateTagVersion(),"-");
                                                        project.setImageTag(onlineTagVersion);
                                                        project.setNameSpace(releaseProject.getNameSpace());
                                                        projectBuildVo.setCreateTagVersion(onlineTagVersion);
                                                        //如果是上线的话需要指定操作分支为提测的时候创建的test分支。
                                                        projectBuildVo.setArriveTagName(releaseProject.getCreteBranchVersion());
                                                        //提测的docker版本
                                                        projectBuildVo.setReleaseDockerVersion(releaseProject.getCreateTagVersion());
                                                        projectBuildVo.setNameSpace(releaseProject.getNameSpace());
                                                        logger.info("新增上线部署任务");
                                                        BuildJob buildJob = new BuildJob();
                                                        buildJob.setName(project.getDescription()+"-"+df.format(new Date()));
                                                        buildJob.setOnLine(true);
//                                                    buildJob.setOnLineId(entity.getId());
                                                        buildJob.setImmedIatelyOnline(true);
                                                        //从系统参数里面取值
                                                        buildJob.setProfileId(pipelineProfileService.findByName(propService.getValue("job.online.profile.name", "ipaas生产环境部署")).getId());
                                                        List<Project> projectList = new ArrayList<>();
                                                        projectList.add(project);
                                                        buildJob.setProjects(projectList);
                                                        buildJob.setTriggerType(JobTrigger.manual);
                                                        buildJob.setTeamId(project.getTeam().getId());
                                                        buildJob.setOnlineTag(onlineTagVersion);
                                                        buildJobService.save(buildJob);
                                                    }

                                                }


                                            }
                                        }
                                        if(profile.isUpdateTag()) {
                                            ReleaseProject releaseProject = releaseProjectService.findByBranchVersionAndProjectUrl(targetBranchName, project.getScm().getUrl());
                                            if (releaseProject!=null &&StringUtils.isNotBlank(releaseProject.getCreateTagVersion())) {
                                                String newTag = releaseProject.getCreateTagVersion().substring(0, releaseProject.getCreateTagVersion().lastIndexOf("_")) + "_" + df.format(new Date());
                                                projectBuildVo.setCreateTagVersion(newTag);
                                                releaseProject.setCreateTagVersion(newTag);
                                                releaseProjectService.save(releaseProject);
                                            }
                                            logger.info("构建过程中需要更新标签:{},{}", profile, project.getImageTag());
                                        }
                                        if(rabbitmqData.containsKey("code")){
                                            projectBuildVo.setRedmineCode(rabbitmqData.getString("code"));
                                        }
                                        projectBuildVo.setGitlabApiUrl(gitPath);
                                        projectBuildVo.setEncodedUrl(projectBuildVo.getGitlabApiUrl());
                                        if(rabbitmqData.containsKey("userName")){
                                            String redmineUser = rabbitmqData.getString("userName");
                                            //gitlab推过来的用户信息有时候是中文、有时候是英文。英文可以直接发送邮件，中文需要调接口查询
                                            //gitlab推过来的用户信息有时候是中文、有时候是英文。英文可以直接发送邮件，中文需要调接口查询
                                            if (!isEnglish(redmineUser)) {
                                                logger.info("gitlab推送的用户名是中文，ci根据中文名调用upms接口查询用户游戏");
                                                List<User> byUserName = userService.findByUserName(redmineUser);
                                                if (!byUserName.isEmpty()) {
                                                    String[] split = byUserName.get(0).getEmail().split("@");
                                                    redmineUser = split[0];
                                                }
                                            }
                                            projectBuildVo.setRedmineUser(redmineUser);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                return projectProfileMap;
            } else {
                throw new IllegalArgumentException("项目: " + gitPath + "不存在");
            }
        }
        return null;
    }

    public List<ReleaseProject> listSort(List<ReleaseProject> releaseProjects) {
        Collections.sort(releaseProjects, new Comparator<ReleaseProject>() {
            @Override
            public int compare(ReleaseProject o1, ReleaseProject o2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
                try {
                    Date date1 = format.parse(StringUtils.substringAfterLast(o1.getCreteBranchVersion(),"-"));
                    Date date2 = format.parse(StringUtils.substringAfterLast(o2.getCreteBranchVersion(),"-"));
                    if(date1.getTime()>date2.getTime()){
                        return -1;//大的放前面
                    }else{
                        return 1;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
        return releaseProjects;
    }

    /**
     * 是否是英文
     *
     * @param charaString
     * @return
     */

    public static boolean isEnglish(String charaString) {
        return charaString.matches("^[a-zA-Z]*");
    }
    //返回该工程应该执行的所有构建过程
    public List<ProjectBuildVo> getPipelineProfile(Project project,List<Event>  events,String targetBranchName,String sourceBranchName) throws Exception {
        //FIXME 查询事件设定的方法可以移到外面，for循环外面
        List<ProjectBuildVo> projectBuildVos = new ArrayList<>();
        if(!events.isEmpty()){
            logger.info("根据事件类型匹配到的事件设定数量：{}",events.size());
            for(Event event:events){
                if(event.getProjectType()== ProjectType.all || event.getProjectType()== project.getProjectType()){
                    //如果没有配置或者在前端页面上选的是默认 选默认的话值就是空的，则采用项目自带的默认流水线
                    //如果传了目标分支，则根据目标分支执行流水线，如果没用则用来源分支。
                    //判断一下分支条件k
                    if(isEventBranchRules(project,event,targetBranchName,sourceBranchName)) {
                        ProjectBuildVo projectBuildVo = new ProjectBuildVo();
                        if (StringUtils.isNotBlank(sourceBranchName)) {
                            projectBuildVo.setSourceBranch(sourceBranchName);
                            projectBuildVo.setArriveTagName(sourceBranchName);
                        }
                        if (StringUtils.isNotBlank(targetBranchName)) {
                            projectBuildVo.setTargetBranch(targetBranchName);
                            projectBuildVo.setArriveTagName(targetBranchName);
                        }
                        if(StringUtils.isBlank(projectBuildVo.getArriveTagName())){
                            projectBuildVo.setArriveTagName("master");
                        }
                        if (StringUtils.isNotBlank(event.getProfileId())) {
                            projectBuildVo.setProfile(event.getProfileId());
                        } else {
                            projectBuildVo.setProfile(project.getDefaultProfile().getId());
                            logger.info("事件设定-未配置构建过程，采用默认构建过程：{} ", project.toString());
                        }
                        projectBuildVos.add(projectBuildVo);
                        logger.info("事件设定-项目类型匹配成功：{}-{}", project.toString(), event.getProjectType().name());
                    }
                }
            }
            return projectBuildVos;
        }else {
            logger.error("项目组没有配置对应的构建过程,项目组：{} ",project.getTeam().getName());
            throw new Exception("项目组没有配置对应的构建过程("+project.getTeam().getName());
        }

    }

    public boolean isEventBranchRules(Project project,Event eventSetting,String targetBranchName,String sourceBranchName) {
        //判断事件设定中的 分支和客户实际操作的分支是否一致。这里支持通配符。
        String targetBranchSetting = eventSetting.getTargetBranch();
        String sourceBranchSetting = eventSetting.getSourceBranch();
        if (StringUtils.isNotBlank(sourceBranchSetting) && StringUtils.isNotBlank(sourceBranchName)) {
            //检查是否采用了逗号 中文
            if (sourceBranchSetting.contains(",")) {
                String[] split = sourceBranchSetting.split(",");
                boolean b = matchingBranchArr(sourceBranchName, split);
                if (!b) {
                    return false;
                }
                //检查是否采用了逗号 英文
            } else if (sourceBranchSetting.contains("，")) {
                String[] split = sourceBranchSetting.split("");
                boolean b = matchingBranchArr(sourceBranchName, split);
                if (!b) {
                    return false;
                }
            } else {
                boolean b = matchingBranch(sourceBranchName, sourceBranchSetting);
                if (!b) {
                    return false;
                }
            }
        }
        if (StringUtils.isNotBlank(targetBranchSetting) && StringUtils.isNotBlank(targetBranchName)) {
            //检查是否采用了逗号
            if (targetBranchSetting.contains(",")) {
                String[] split = targetBranchSetting.split(",");
                boolean b = matchingBranchArr(targetBranchName, split);
                //代表有匹配成功的 结束本次循环
                if (!b) {
                    return false;
                }
            } else if (targetBranchSetting.contains("，")) {
                String[] split = targetBranchSetting.split("，");
                boolean b = matchingBranchArr(targetBranchName, split);
                //代表有匹配成功的 结束本次循环
                if (!b) {
                    return false;
                }
            } else {
                boolean b = matchingBranch(targetBranchName, targetBranchSetting);
                if (!b) {
                    return false;
                }
            }
        }
        return true;

    }
    /**
     * @param sourceBranchName 客户实际操作的分支
     * @param branchName       事件设定里配置的分支
     * @return
     */
    private boolean matchingBranchArr(String sourceBranchName, String[] branchName) {
        //检查是否采了的通配符*
        for (String s : branchName) {
            boolean b = matchingBranch(sourceBranchName, s);
            //代表有匹配成功的 结束本次循环
            if (b) {
                return b;
            }
        }
        return false;
    }
    /**
     * @param branchName    客户实际操作的分支
     * @param branchSetting 事件设定里配置的分支
     * @return
     */
    private boolean matchingBranch(String branchName, String branchSetting) {
        //检查是否采了的通配符*
        logger.info("客户操作分支:{} 事件设定分支:{}", branchName, branchSetting);
        if (branchSetting.contains("*")) {
            String subTargetBranch = branchSetting.substring(0, branchSetting.indexOf("*"));
            if (branchName.contains(subTargetBranch)) {
                logger.info("事件设定分支匹配成功(通配符)");
                return true;
            } else {
                logger.info("事件设定分支匹配失败(通配符)");
                return false;
            }
        } else {
            if (branchName.equals(branchSetting)) {
                logger.info("事件设定分支匹配成功{}、{}", branchSetting, branchName);
                return true;
            } else {
                logger.info("事件设定分支匹配失败{}、{}", branchSetting, branchName);
                return false;
            }
        }
    }
    //返回该工程应该执行的所有构建过程
    public List<ProjectBuildVo> getPipelineProfile(Project project,List<Event>  events) throws Exception {
        //FIXME 查询事件设定的方法可以移到外面，for循环外面
        return getPipelineProfile(project,events,null,null);
    }


    @Override
    public Page<Event> pageSearch(Pageable pageable, String searchText) {
        BooleanExpression condition = null;
        UserVo currentUser = AuthUtils.getCurrentUser();
        if (currentUser != null
                && !Role.existRole(currentUser.getRoles(), RoleEnum.admin)) {
            //filted by current user team
            logger.info("事件设定查询条件进入了组装管理权限 :{}",currentUser);
            List<String> teamIds= Lists.newArrayList();
            currentUser.getTeamList().forEach(team->{
                teamIds.add(team.getId());
            });
            condition = (QEvent.event.teamId.in(teamIds));
        }
        if (StringUtils.isNotBlank(searchText)) {
            if (condition != null) {
                condition = condition.and(QProject.project.name.like("%" + searchText + "%")
                        .or(QProject.project.description.like("%" + searchText + "%")));
            } else {
                condition = QProject.project.name.like("%" + searchText + "%")
                        .or(QProject.project.description.like("%" + searchText + "%"));
            }
        }
        logger.info("事件设定查询条件：{}",condition);
        return condition == null ? dao.findAll(pageable) : dao.findAll(condition, pageable);
    }

    @Override
    public void save(Event entity) {
        if(null != AuthUtils.getCurrentUser()){
            entity.setUserId(Long.valueOf(AuthUtils.getCurrentUser().getId()));
            entity.setUserName(AuthUtils.getCurrentUser().getName());
        }
        entity.setCreateDate(new Date());
        super.save(entity);
    }
    public List<Event> findByTeamId(String teamId) {
        return (List<Event>)dao.findAll(QEvent.event.teamId.eq(teamId));
    }
    
    public List<Event> findByTeamIdAndEventType(String teamId,EventType eventType) {
    	 
        return (List<Event>)dao.findAll(QEvent.event.teamId.eq(teamId).and(QEvent.event.eventType.eq(eventType)));
    }

    public List<Event> findByTeamIdAndEventTypeAndProjectType(String teamId, EventType eventType, ProjectType projectType) {
        return (List<Event>)dao.findAll(QEvent.event.teamId.eq(teamId).and(QEvent.event.eventType.eq(eventType)).and(QEvent.event.projectType.eq(projectType)));
    }
}
