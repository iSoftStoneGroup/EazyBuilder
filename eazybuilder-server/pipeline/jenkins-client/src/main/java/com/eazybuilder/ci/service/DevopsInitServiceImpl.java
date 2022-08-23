package com.eazybuilder.ci.service;

import com.alibaba.fastjson.JSONObject;
import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.base.PageSearch;
import com.eazybuilder.ci.dto.DevopsInitDto;
import com.eazybuilder.ci.entity.*;
import com.eazybuilder.ci.entity.Upms.UpmsUserVo;
import com.eazybuilder.ci.entity.devops.*;
import com.eazybuilder.ci.repository.DevopsInitDao;
import com.eazybuilder.ci.repository.DevopsProjectDao;
import com.eazybuilder.ci.upms.QueryUpmsData;
import com.eazybuilder.ci.util.AuthUtils;
import com.eazybuilder.ci.util.RedisUtils;
import com.mysema.commons.lang.Assert;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;


@PageSearch(value = QDevopsInit.class, fields = {"teamName"})
@Service
public class DevopsInitServiceImpl extends AbstractCommonServiceImpl<DevopsInitDao, DevopsInit> implements CommonService<DevopsInit>{

    private static Logger logger = LoggerFactory.getLogger(DevopsInitServiceImpl.class);

    @Value("${ci.scm.user}")
    String scmUser;
    @Value("${ci.scm.password}")
    String scmPass;
    @Value("${ci.harbor.url}")
    String harborUrl;
    @Value("${ci.harbor.password}")
    String harborPassword;


    @Resource
    UserService userService;

    @Resource
    ProjectService projectService;

    @Resource
    QueryUpmsData queryUpmsData;

    @Resource
    DockerRegistryService dockerRegistryService;

    @Autowired
    DevopsProjectDao devopsProjectDao;
    @Resource
    TeamServiceImpl teamService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Resource
    RedisUtils redisUtils;
    private static final String CIMSGCONFIGEMAIL = "ci:msg:email:focusRedlightRepair";

    private static final String CIMSGCONFIGDING = "ci:msg:ding:focusRedlightRepair";

    public void  saveRedisFocusRedlightRepairConfig(DevopsInit devopsInit){
        try {

            if(StringUtils.isNotBlank(devopsInit.getMailMsgProfile().getFocusRedlightRepairConfig())) {
                logger.info("保存邮件红灯修复发送间隔时间");
                redisUtils.set(CIMSGCONFIGEMAIL, devopsInit.getMailMsgProfile().getFocusRedlightRepairConfig());
            }
            if(StringUtils.isNotBlank(devopsInit.getDingMsgProfile().getFocusRedlightRepairConfig())) {
                logger.info("保存钉钉红灯修复发送间隔时间");
                redisUtils.set(CIMSGCONFIGDING, devopsInit.getDingMsgProfile().getFocusRedlightRepairConfig());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    }

    public void ciDatasyncResult(JSONObject jsonObject) {
        DevopsInit devopsInit = findOne(jsonObject.getString("id"));
        ProjectInitStatus projectInitStatus = new ProjectInitStatus();
        projectInitStatus.setProjectCode(ProjectCode.valueOf(jsonObject.getString("projectCode")));
        projectInitStatus.setStatus(Status.valueOf(jsonObject.getString("status")));
        Set<ProjectInitStatus> projectInitStatuses = devopsInit.getProjectInitStatuses();
        for (Iterator<ProjectInitStatus> it = projectInitStatuses.iterator(); it.hasNext(); ) {
            ProjectInitStatus next = it.next();
            if (next.getProjectCode().equals(projectInitStatus.getProjectCode())) {
                it.remove();
            }
        }
        projectInitStatuses.add(projectInitStatus);
        dao.save(devopsInit);
    }

    public DevopsInit findByDevopsTeamId(String uid){
        return dao.findOne(QDevopsInit.devopsInit.groupId.eq(Long.valueOf(uid))).get();
    }

    public Team getCiTeam(DevopsInit devopsInit,List<User> users) {
        Team team = teamService.findByName(devopsInit.getTeamName());
        if(null ==team){
            team = new Team();
        }
        team.setGroupId(devopsInit.getGroupId());
        team.setDevopsTeamId(devopsInit.getId());
        team.setName(devopsInit.getTeamName());
        team.setCode(devopsInit.getTeamCode());
        team.setMembers(users);
        teamService.save(team);
        return team;
    }

    public List<User> getCiUser(DevopsInitDto deveopsInitDto) throws Exception {
        List<User> users = new ArrayList<>();
        List<User> userList = new ArrayList<>();
        for(UpmsUserVo upmsUserVo: deveopsInitDto.getDevopsUsers()){
            User byEmail = userService.findByEmail(upmsUserVo.getEmail());
            if(null == byEmail) {
                User user = new User();
                user.setEmail(upmsUserVo.getEmail());
                user.setPassword(null);
                user.setPassword(AuthUtils.encrypt(UserService.DEFAULT_TOKEN, user.getEmail()));
                user.setName(upmsUserVo.getUserName());
                user.setEmployeeId(upmsUserVo.getEmployeeId());
                userList.add(user);
            }else{
                byEmail.setEmployeeId(upmsUserVo.getEmployeeId());
                users.add(byEmail);
            }
        }
        userService.save(userList);
        users.addAll(userList);
        return users;
    }


    public DevopsInit genDevopsInit(Project project){
        if(StringUtils.isNotBlank(project.getTeam().getDevopsTeamId())) {
            DevopsInit devopsInit = findOne(project.getTeam().getDevopsTeamId());
            Assert.isTrue(devopsInit != null, "项目组初始化信息已经被删除，无法更新项目信息！");
            DevopsProject devopsProject = StringUtils.isNotEmpty(project.getDevopsProjectId()) ? devopsProjectDao.findById(project.getDevopsProjectId()).get() : new DevopsProject();
            devopsProject = devopsProject == null ? new DevopsProject() : devopsProject;
            devopsProject.update(project);
            devopsProject.setId(project.getPiplineEventProjectId());
            devopsInit.setDevopsProjects(Arrays.asList(devopsProject));
//            devopsProjectDao.save(devopsProject);
//            devopsInit.getDevopsProjects().add(devopsProject);
//            dao.save(devopsInit);
            return devopsInit;
        }else {
            logger.info("该项目所属的项目组没有经过初始化，暂时不推送消息。");
            return null;
        }
    }

    public List<Project> getCiProject(DevopsInit devopsInit, Team team) {
        List<DevopsProject> devopsProjects = devopsInit.getDevopsProjects();
        List<Project> projects = new ArrayList<>();
        List<DeployConfig> listTemp=null;
        for(DevopsProject devopsProject : devopsProjects){
        	listTemp=new ArrayList<DeployConfig>();
            Project project = projectService.findByName(devopsProject.getDescription());
            if(project==null){
                project = new Project();
                project.setDevopsProjectId(devopsProject.getId());
                project.setName(devopsProject.getDescription());
                project.setProjectType(devopsProject.getProjectType());
                if(devopsProject.getDeployConfigList() !=null &&devopsProject.getDeployConfigList().size()>0) {
                    for (DeployConfig deployConfig : devopsProject.getDeployConfigList()) {
                        DeployConfig deployConfigNew=new DeployConfig();
                        deployConfigNew.setContainerPort(deployConfig.getContainerPort());
                        deployConfigNew.setImageTag(deployConfig.getImageTag());
                        deployConfigNew.setIngressHost(deployConfig.getIngressHost());
                        deployConfigNew.setInitImageTag(deployConfig.getInitImageTag());
                        deployConfigNew.setLimitsCpu(deployConfig.getLimitsCpu());
                        deployConfigNew.setName(deployConfig.getName());
                        deployConfigNew.setNameSpace(deployConfig.getNameSpace());
                        deployConfigNew.setReplicas(deployConfig.getReplicas());
                        deployConfigNew.setTag(deployConfig.getTag());
                        deployConfigNew.setYamlId(deployConfig.getYamlId());
                        deployConfigNew.setAppType(deployConfig.getAppType());
                        listTemp.add(deployConfigNew);
                    }
                }
                project.setDeployConfigList(listTemp);
                project.setDescription(devopsProject.getProjectName());
                project.setPomPath(devopsProject.getPomPath());
                project.setNetPath(devopsProject.getNetPath());
                project.setNetSlnPath(devopsProject.getNetSlnPath());
                project.setNetTestPath(devopsProject.getNetTestPath());
                project.setNetType(devopsProject.getNetType());
                project.setSqlPath(devopsProject.getSqlPath());
                project.setSqlType(devopsProject.getSqlType());
                project.setLegacyProject(devopsProject.isLegacyProject());
                project.setTeam(team);
                project.setDeployType(DeployType.k8s);
                project.setImageSchema(team.getCode());
                //先查看所属docker仓库是否已经存在
                List<DockerRegistry> dockerRegistrys = dockerRegistryService.findByUrl(harborUrl);
                if(dockerRegistrys.isEmpty()){
                    DockerRegistry dockerRegistry = new DockerRegistry();
                    String email = AuthUtils.getCurrentUser().getEmail();
                    dockerRegistry.setEmail(email);
                    if(email.contains("@")) {
                        dockerRegistry.setUser(email.substring(0, email.indexOf("@")));
                    }else{
                        dockerRegistry.setUser(email);
                    }
                    dockerRegistry.setSchema("https");
                    dockerRegistry.setPassword(harborPassword);
                    dockerRegistry.setTeamId(team.getId());
                    dockerRegistry.setUrl(harborUrl);
                    dockerRegistryService.save(dockerRegistry);
                    project.setRegistry(dockerRegistry);
                }else {
                    project.setRegistry(dockerRegistrys.get(0));
                }
                Scm scm = new Scm();
                scm.setType(ScmType.git);
                scm.setTagName("master");
                scm.setUser(scmUser);
                scm.setPassword(scmPass);
                scm.setUrl(devopsProject.getScmUrl());
                project.setScm(scm);
                projects.add(project);
            }else{
                logger.info("项目-{} 已经初始化，不进行更新操作",project.getDescription());
            }
        }

        return projects;
    }

    public net.sf.json.JSONObject getSendInitData(DevopsInitDto deveopsInitDto) throws Exception {
        JSONArray jsonArray = new JSONArray();
        if(AuthUtils.getCurrentUser()!=null) {
            //harbor管理员账号
            deveopsInitDto.setUserId(Long.valueOf(AuthUtils.getCurrentUser().getId()));
            deveopsInitDto.setUserName(AuthUtils.getCurrentUser().getName());
            deveopsInitDto.setPassword(harborPassword);
            deveopsInitDto.setEmail(AuthUtils.getCurrentUser().getEmail());
        }
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(deveopsInitDto);
        //遍历用户数据，将数据格式转为之前和deveops各系统预定好的
        Map<Long,JSONObject> map = new HashMap<>();
        for(UpmsUserVo upmsUserVo: deveopsInitDto.getDevopsUsers()){
            JSONObject jsonUser = new JSONObject();
            jsonUser.put("userId", Long.valueOf(upmsUserVo.getUserId()));
            jsonUser.put("email", upmsUserVo.getEmail());
            jsonUser.put("phoneNumber", upmsUserVo.getPhoneNumber());
            jsonUser.put("userName", upmsUserVo.getUserName());
            jsonUser.put("surname", upmsUserVo.getUserName().substring(0, 1));
            jsonUser.put("name", upmsUserVo.getUserName().substring(1));
            jsonUser.put("employeeId", upmsUserVo.getEmployeeId());
            //根据userId调用upms接口查询角色
            jsonUser.put("role",queryUpmsData.getRoleByUserId(Long.valueOf(upmsUserVo.getUserId())));
            map.put(Long.valueOf(upmsUserVo.getUserId()),jsonUser);
            jsonArray.add(jsonUser);
        }
//        Set<Long> userIdSet = map.keySet();
//        List<Long> userIdList = new ArrayList<>(userIdSet);
//        queryUpmsData.getRoleByUserId(userIdList);
        jsonObject.put("devopsUsers",jsonArray);
        jsonObject.put("teamNamespaces",deveopsInitDto.getTeamNamespaces());
        return jsonObject;
    }

}
