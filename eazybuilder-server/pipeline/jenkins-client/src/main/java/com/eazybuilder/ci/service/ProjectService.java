package com.eazybuilder.ci.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.conf.UpmsLoginConf;
import com.eazybuilder.ci.constant.RoleEnum;
import com.eazybuilder.ci.controller.vo.UserVo;
import com.eazybuilder.ci.entity.*;
import com.eazybuilder.ci.rabbitMq.SendRabbitMq;
import com.eazybuilder.ci.repository.MetricDao;
import com.eazybuilder.ci.repository.PipelineDao;
import com.eazybuilder.ci.repository.ProjectDao;
import com.eazybuilder.ci.repository.ProjectHistoryDao;
import com.eazybuilder.ci.util.AuthUtils;
import com.eazybuilder.ci.util.JsonMapper;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class ProjectService extends AbstractCommonServiceImpl<ProjectDao, Project>
        implements CommonService<Project> {

    @Autowired
    TeamServiceImpl teamServiceImpl;

    @Autowired
    PipelineProfileService profileService;

    @Resource
    ProjectHistoryDao projectHistoryDao;

    @Autowired
    MetricDao metricDao;

    @Autowired
    PipelineDao pipelineDao;

    @Autowired
    SendRabbitMq sendRabbitMq;

    @Autowired
    DevopsInitServiceImpl devopsInitService;

    @Value("${message.updateProject}")
    String updateProjectExchange;

    @Autowired
    UpmsLoginConf upmsLoginConf;


    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public void recover(ProjectHistory projectHistory) {
        //????????????????????????????????????????????????
        saveHistory(dao.findById(projectHistory.getProjectId()).get());
        //?????????????????????????????????
        String jsonData = projectHistory.getJsonData();
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(jsonData);
        Project project = (Project) net.sf.json.JSONObject.toBean(jsonObject, Project.class);
		List<DeployConfig> deployConfigs = (List<DeployConfig>) JSONArray.parseArray(jsonObject.getJSONArray("deployConfigList").toString(), DeployConfig.class);
        project.setDeployConfigList(deployConfigs);
        dao.save(project);
    }


    public Project findByName(String name) {
        return dao.findOne(QProject.project.name.eq(name)).orElse(null);
    }
    public Project findByNameAndRepo(String name, String repoUrl) {
        return dao.findOne(QProject.project.name.eq(name).and(QProject.project.scm.url.eq(repoUrl))).get();
    }
    public List<Project> findByScmUrl(String scmUrl) {
        return (List<Project>)dao.findAll((QProject.project.scm.url.eq(scmUrl)));
    }
    public List<Project> findByRepo(String repoUrl) {
        return (List<Project>)dao.findAll(QProject.project.scm.url.eq(repoUrl));
    }

    public List<Project> findByUser(User user) {
        Collection<Team> teams = teamServiceImpl.findByUser(user);
        if (teams == null || teams.size() == 0) {
            return Lists.newArrayList();
        }
        return (List<Project>) dao.findAll(QProject.project.team.in(teams));
    }

    public Collection<String> getMyProjectIds() {
        User user = AuthUtils.getCurrentUser();
        List<Role> roles = user.getRoles();
        List<String> projectIds = Lists.newArrayList();
        if (Role.existRole(roles,RoleEnum.admin)) {
            super.findAll().forEach(project -> {
                projectIds.add(project.getId());
            });
        } else {
            findByUser(user).forEach(project -> {
                projectIds.add(project.getId());
            });
        }
        return projectIds;
    }

    public Page<Project> pageSearchByGitpaths(Pageable pageable, Collection<String> gitPaths) {
        return dao.findAll(QProject.project.scm.url.in(gitPaths), pageable);
    }
    @Override
    public Page<Project> pageSearch(Pageable pageable, String searchText) {
        UserVo currentUser = (UserVo) AuthUtils.getCurrentUser();
        BooleanExpression condition = null;
        if(currentUser != null
                &&  !currentUser.isAuditReader()){
            logger.info("??? admin/audit ????????? ????????????id??????,??????id???{}",JsonMapper.nonDefaultMapper().toJson(currentUser));
            condition = QProject.project.team.in(currentUser.getTeamList());
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
        return condition == null ? dao.findAll(pageable) : dao.findAll(condition, pageable);
    }

    @Override
    public Iterable<Project> findAll() {
        User currentUser = AuthUtils.getCurrentUser();
        List<Role> roles = currentUser.getRoles();
        if (currentUser != null
                && Role.existRole(roles,RoleEnum.admin)) {
            //filted by current user team
            return dao.findAll(QProject.project.team.in(teamServiceImpl.findByUser(currentUser)));
        }
        return super.findAll();
    }

    public List<Project> findAllByTeamId(String teamId) {
        return (List<Project>) dao.findAll(QProject.project.team.id.eq(teamId));
    }
    public List<Project> findAllByTeamName(String teamName) {
        return (List<Project>) dao.findAll(QProject.project.team.name.eq(teamName));
    }
    public Page<Project> pageSearchWithExcludes(Pageable pageable, String searchText, ArrayList<String> excludeIds) {
        BooleanExpression condition = null;

        if (StringUtils.isNotBlank(searchText)) {
            condition = QProject.project.name.like("%" + searchText + "%");
        }
        User currentUser = AuthUtils.getCurrentUser();
        List<Role> roles = currentUser.getRoles();
        if (currentUser != null
                && !Role.existRole(roles,RoleEnum.admin) && !Role.existRole(roles,RoleEnum.audit)) {
            //filted by current user team
            if (condition != null) {
                condition = condition.and(QProject.project.team.in(teamServiceImpl.findByUser(currentUser)));
            } else {
                condition = QProject.project.team.in(teamServiceImpl.findByUser(currentUser));
            }
        }
        if (excludeIds != null && excludeIds.size() > 0) {
            if (condition != null) {
                condition = condition.and(QProject.project.id.notIn(excludeIds));
            } else {
                condition = QProject.project.id.notIn(excludeIds);
            }
        }

        return condition == null ? dao.findAll(pageable) : dao.findAll(condition, pageable);
    }

    @Override
    public void save(Project entity) {
        this.saveWithoutMsg(entity);
        sendRabbitMq.sendMsg(JsonMapper.nonDefaultMapper().toJson(devopsInitService.genDevopsInit(entity)),updateProjectExchange,"");
    }

    public void saveWithoutMsg(List<Project> entitys){
        entitys.stream().forEach(this::saveWithoutMsg);
    }

    /**
     * ???????????????????????????mq
     */
    public void saveWithoutMsg(Project entity){
        if (entity.getDefaultProfile() != null) {
            entity.setDefaultProfile(profileService.findOne(entity.getDefaultProfile().getId()));
        }
        if (entity.getId() != null && entity.getScm() != null && !entity.getScm().isChangePwd()) {
            Project dbProject = dao.findById(entity.getId()).get();
            if(entity.getScm()!=null) {
                entity.getScm().setPassword(dbProject.getScm().getPassword());
            }else{
                Scm scm = new Scm();
                scm.setPassword(dbProject.getScm().getPassword());
                entity.setScm(scm);
            }
        }

        if(StringUtils.isNotBlank(entity.getScm().getUrl())) {
            String url = entity.getScm().getUrl();
            logger.info("????????????project.scm.url???{}",url);
            url = url.replaceAll("//", "/");
            url = url.replaceFirst("/", "//");
            entity.getScm().setUrl(url);
            logger.info("????????????project.scm.url???{}",url);
        }
        //???????????????????????????????????????????????????????????????
        if (StringUtils.isNotBlank(entity.getId())) {
            //??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            saveHistory(dao.findById(entity.getId()).get());
            super.save(entity);
        }else {
            super.save(entity);
            saveHistory(entity);
        }
        entity.setUpdateTime(new Date());
    }

    @Override
    public void save(List<Project> projects) {
        super.save(projects);
        for (Project project : projects) {
            //???????????????????????????????????????????????????????????????
            if (StringUtils.isNotBlank(project.getId())) {
                //??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                saveHistory(dao.findById(project.getId()).get());
            }
            project.setUpdateTime(new Date());
        }
    }

    /**
     *
     * @param project
     */
    public void saveHistory(Project project) {
        ProjectHistory projectHistory = new ProjectHistory();
        projectHistory.setProjectId(project.getId());
        projectHistory.setJsonData(JSONObject.toJSONString(project));
        projectHistory.setProjectName(project.getDescription());
        projectHistory.setTeamName(project.getTeam().getName());
        if(null!=AuthUtils.getCurrentUser()) {
        	 projectHistory.setUpdateUser(AuthUtils.getCurrentUser().getName());	
        }       
        projectHistory.setUpdateTime(new Date());
        projectHistoryDao.save(projectHistory);
    }

    @Override
    @Transactional
    public void delete(String id) {
        //????????????metric
        Iterable<Pipeline> pipelineHistorys = pipelineDao.findAll(QPipeline.pipeline.project.id.eq(id));
        if (pipelineHistorys != null) {
            for (Pipeline pp : pipelineHistorys) {
                //????????????????????? ????????????
                dao.deleteReleasePipeline(pp.getId());
                Iterable<Metric> metrics = metricDao.findAll(QMetric.metric.pipeline.id.eq(pp.getId()));
                if (metrics != null) {
                    metricDao.deleteAll(metrics);
                }
            }
            //????????????????????? ????????????
            dao.deleteReleaseProject(id);

            //??????pipeline history??????
            pipelineDao.deleteAll(pipelineHistorys);
        }
        //?????????????????????
        dao.deleteJobProject(id);
        //?????????????????????(??????)
        dao.deleteGroupProject(id);
        //??????
        super.delete(id);
    }

    @Override
    @Transactional
    public void deleteBatch(List<String> ids) {
        for (String id : ids) {
            delete(id);
        }
    }

}
