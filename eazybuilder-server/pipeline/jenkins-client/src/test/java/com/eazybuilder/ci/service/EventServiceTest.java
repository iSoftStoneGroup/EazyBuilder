package com.eazybuilder.ci.service;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eazybuilder.ci.constant.RoleEnum;
import com.eazybuilder.ci.controller.vo.ProjectBuildVo;
import com.eazybuilder.ci.entity.*;
import com.eazybuilder.ci.entity.devops.Event;
import com.eazybuilder.ci.entity.devops.EventType;
import com.eazybuilder.ci.entity.devops.ReleaseProject;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.util.Assert;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(PowerMockRunner.class)
public class EventServiceTest {

    @InjectMocks
    @Spy
    EventService eventService=new EventService();


    @Mock
    TeamServiceImpl teamServiceImpl;

    @Mock
    ProjectService projectService;

    @Mock
    ReleaseProjectService releaseProjectService;

    @Mock
    PipelineProfileService pipelineProfileService;

    @Mock
    UserService userService;

    @Mock
    BuildJobService buildJobService;

    @Mock
    SystemPropertyService propService;
    

    @Before
    public  void  init(){
        MockitoAnnotations.initMocks(this);
    }


    @Test
  public   void testGetEventProject() throws Exception {
        String  mesJson=
                "{\"assigneeName\":\"qiujin\",\"authorName\":\"gangwangn\",\"code\":\"2660\",\"customFields\":{\"工程标识\":\"nacos-config\",\"仓库地址\":\"http://gitlab.eazybuild-devops..cn/devops/nacos-config.git\",\"是否更新配置文件\":\"0\",\"配置文件内容\":\"\",\"指派测试人员\":\"\",\"镜像名称/版本\":\"registry.eazybuild-devops..cn/devops/devops-config/2651-2660-202209221420\",\"SP\":\"\",\"是否更新sql\":\"0\"},\"gitPath\":\"http://gitlab.eazybuild-devops..cn/devops/nacos-config.git\",\"imageTag\":\"\",\"profileType\":\"merge\",\"projectName\":\"Nacos Config\",\"sourceBranchName\":\"bugfix-2660\",\"tagName\":\"bugfix-2660\",\"targetBranchName\":\"master\",\"topCode\":\"2651\",\"userName\":\"gangwangn\"}";
        JSONObject jsonObject = JSON.parseObject(mesJson);
        final Project project = new Project();
        project.setImageTag("imageTag");
        project.setDevopsProjectId("devopsProjectId");
        final DeployConfig deployConfig = new DeployConfig();
        deployConfig.setAppType(AppType.deployment);
        deployConfig.setHostname("hostname");
        deployConfig.setLimitsCpu("limitsCpu");
        deployConfig.setName("name");
        deployConfig.setReplicas("replicas");
        deployConfig.setId("id");
        deployConfig.setIngressHost("ingressHost");
        deployConfig.setContainerPort("containerPort");
        deployConfig.setImageTag("imageTag");
        deployConfig.setYamlId("yamlId");
        project.setDeployConfigList(Arrays.asList(deployConfig));
        project.setDeployType(DeployType.k8s);
        project.setId("id");
        project.setName("name");
        final Scm scm = new Scm();
        scm.setType(ScmType.svn);
        scm.setUrl("url");
        scm.setUser("user");
        scm.setId("id");
        scm.setName("name");
        scm.setPassword("password");
        scm.setTagName("tagName");
        scm.setChangePwd(false);
        scm.setSourceBranch("sourceBranch");
        scm.setTargetBranch("targetBranch");
        project.setScm(scm);
        final Deploy deployInfo = new Deploy();
        deployInfo.setId("id");
        deployInfo.setHostId("hostId");
        deployInfo.setUrl("url");
        deployInfo.setPath("path");
        final HostInfo host = new HostInfo();
        host.setId("id");
        host.setIp("ip");
        host.setPort("port");
        host.setUser("user");
        host.setPass("pass");
        host.setName("name");
        host.setTeamId("teamId");
        deployInfo.setHost(host);
        project.setDeployInfo(deployInfo);
        final DockerRegistry registry = new DockerRegistry();
        registry.setUrl("url");
        registry.setUser("user");
        registry.setPassword("password");
        registry.setSchema("schema");
        registry.setId("id");
        registry.setEmail("email");
        registry.setChangePwd(false);
        registry.setTeamId("teamId");
        project.setRegistry(registry);
        project.setDescription("description");

        Team team=new Team();
        team.setId(UUID.randomUUID().toString());

        project.setTeam(team);
        ArrayList<Object> projects = Lists.newArrayList(project);
        String gitPath = jsonObject.getString("gitPath");
        System.out.println(gitPath);
        PowerMockito.when(projectService,"findByRepo",gitPath).thenReturn(projects);

        Event event=new Event();
        event.setEventType(EventType.merge);
        List<Event>  events=Lists.newArrayList(event);

        PowerMockito.doReturn(events) .when(eventService,"findByTeamIdAndEventType",team.getId(),EventType.merge);
        ProjectBuildVo projectBuildVo=new ProjectBuildVo();
        projectBuildVo.setProfile("profile");
        List<ProjectBuildVo> projectBuildVos =Lists.newArrayList(projectBuildVo);
        PowerMockito.doReturn(projectBuildVos) .when(eventService,"getPipelineProfile",project,events,jsonObject.getString("targetBranchName"),jsonObject.getString("sourceBranchName"));

        PipelineProfile pipelineProfile=new PipelineProfile();

        PowerMockito.doReturn(pipelineProfile) .when(pipelineProfileService,"findOne",projectBuildVo.getProfile());

        final Map<Project, List<ProjectBuildVo>> result = eventService.getEventProject(jsonObject);

        Assert.isTrue(!CollectionUtil.isEmpty(result),"testGetEventProject方法测试失败");

    }

    @Test
 public    void testGetEventProject_ProjectServiceReturnsNoItems() throws Exception {
        // Setup
        final JSONObject rabbitmqData = new JSONObject(0, false);
        //when(eventService.projectService.findByRepo("repoUrl")).thenReturn(Collections.emptyList());

        // Configure PipelineProfileService.findOne(...).
        final PipelineProfile pipelineProfile = new PipelineProfile();
        pipelineProfile.setDel(false);
        pipelineProfile.setUpdateTag(false);
        pipelineProfile.setUpdateConfig(false);
        pipelineProfile.setConfigPath("configPath");
        pipelineProfile.setFocusRedlightRepair(false);
        pipelineProfile.setReleasePrefix("releasePrefix");
        pipelineProfile.setAddTag(false);
        pipelineProfile.setTagPrefix("tagPrefix");
        pipelineProfile.setId("id");
        pipelineProfile.setSkipUnitTest(false);
        //when(eventService.pipelineProfileService.findOne("id")).thenReturn(pipelineProfile);

        // Configure ReleaseProjectService.findByLikeBranchVersionAndProjectUrl(...).
        final ReleaseProject releaseProject = new ReleaseProject();
        releaseProject.setId("id");
        releaseProject.setProjectId("projectId");
        releaseProject.setHistoryId("historyId");
        releaseProject.setCreateTagVersion("createTagVersion");
        releaseProject.setCreateTagDetail("createTagDetail");
        releaseProject.setCreteBranchVersion("creteBranchVersion");
        releaseProject.setReleaseDockerVersion("releaseDockerVersion");
        releaseProject.setOnlineDockerVersion("onlineDockerVersion");
        releaseProject.setProjectGitUrl("projectGitUrl");
        releaseProject.setReleaseId("releaseId");
        final List<ReleaseProject> releaseProjects = Arrays.asList(releaseProject);
        //when(eventService.releaseProjectService.findByLikeBranchVersionAndProjectUrl("branchVersion", "projectUrl")).thenReturn(releaseProjects);

        // Configure ReleaseProjectService.findByBranchVersionAndProjectUrl(...).
        final ReleaseProject releaseProject1 = new ReleaseProject();
        releaseProject1.setId("id");
        releaseProject1.setProjectId("projectId");
        releaseProject1.setHistoryId("historyId");
        releaseProject1.setCreateTagVersion("createTagVersion");
        releaseProject1.setCreateTagDetail("createTagDetail");
        releaseProject1.setCreteBranchVersion("creteBranchVersion");
        releaseProject1.setReleaseDockerVersion("releaseDockerVersion");
        releaseProject1.setOnlineDockerVersion("onlineDockerVersion");
        releaseProject1.setProjectGitUrl("projectGitUrl");
        releaseProject1.setReleaseId("releaseId");
        //when(eventService.releaseProjectService.findByBranchVersionAndProjectUrl("branchVersion", "projectUrl")).thenReturn(releaseProject1);

        // Configure UserService.findByUserName(...).
        final User user = new User();
        user.setId("id");
        user.setName("name");
        user.setEmail("email");
        user.setPhone("phone");
        user.setRoles(Arrays.asList(new Role(RoleEnum.developer)));
        user.setPassword("password");
        user.setDepartment("department");
        user.setTitle("title");
        user.setEmployeeId(0);
        final List<User> users = Arrays.asList(user);
        //when(eventService.userService.findByUserName("userName")).thenReturn(users);

        // Run the test
        final Map<Project, List<ProjectBuildVo>> result = eventService.getEventProject(rabbitmqData);

        // Verify the results
        //verify(eventService.releaseProjectService).save(any(ReleaseProject.class));
    }

    @Test
 public    void testGetEventProject_ReleaseProjectServiceFindByLikeBranchVersionAndProjectUrlReturnsNoItems() throws Exception {
        // Setup
        final JSONObject rabbitmqData = new JSONObject(0, false);

        // Configure ProjectService.findByRepo(...).
        final Project project = new Project();
        project.setImageTag("imageTag");
        project.setDevopsProjectId("devopsProjectId");
        final DeployConfig deployConfig = new DeployConfig();
        deployConfig.setAppType(AppType.deployment);
        deployConfig.setHostname("hostname");
        deployConfig.setLimitsCpu("limitsCpu");
        deployConfig.setName("name");
        deployConfig.setReplicas("replicas");
        deployConfig.setId("id");
        deployConfig.setIngressHost("ingressHost");
        deployConfig.setContainerPort("containerPort");
        deployConfig.setImageTag("imageTag");
        deployConfig.setYamlId("yamlId");
        project.setDeployConfigList(Arrays.asList(deployConfig));
        project.setDeployType(DeployType.k8s);
        project.setId("id");
        project.setName("name");
        final Scm scm = new Scm();
        scm.setType(ScmType.svn);
        scm.setUrl("url");
        scm.setUser("user");
        scm.setId("id");
        scm.setName("name");
        scm.setPassword("password");
        scm.setTagName("tagName");
        scm.setChangePwd(false);
        scm.setSourceBranch("sourceBranch");
        scm.setTargetBranch("targetBranch");
        project.setScm(scm);
        final Deploy deployInfo = new Deploy();
        deployInfo.setId("id");
        deployInfo.setHostId("hostId");
        deployInfo.setUrl("url");
        deployInfo.setPath("path");
        final HostInfo host = new HostInfo();
        host.setId("id");
        host.setIp("ip");
        host.setPort("port");
        host.setUser("user");
        host.setPass("pass");
        host.setName("name");
        host.setTeamId("teamId");
        deployInfo.setHost(host);
        project.setDeployInfo(deployInfo);
        final DockerRegistry registry = new DockerRegistry();
        registry.setUrl("url");
        registry.setUser("user");
        registry.setPassword("password");
        registry.setSchema("schema");
        registry.setId("id");
        registry.setEmail("email");
        registry.setChangePwd(false);
        registry.setTeamId("teamId");
        project.setRegistry(registry);
        project.setDescription("description");
        final List<Project> projects = Arrays.asList(project);
        //when(eventService.projectService.findByRepo("repoUrl")).thenReturn(projects);

        // Configure PipelineProfileService.findOne(...).
        final PipelineProfile pipelineProfile = new PipelineProfile();
        pipelineProfile.setDel(false);
        pipelineProfile.setUpdateTag(false);
        pipelineProfile.setUpdateConfig(false);
        pipelineProfile.setConfigPath("configPath");
        pipelineProfile.setFocusRedlightRepair(false);
        pipelineProfile.setReleasePrefix("releasePrefix");
        pipelineProfile.setAddTag(false);
        pipelineProfile.setTagPrefix("tagPrefix");
        pipelineProfile.setId("id");
        pipelineProfile.setSkipUnitTest(false);
        //when(eventService.pipelineProfileService.findOne("id")).thenReturn(pipelineProfile);

        //when(eventService.releaseProjectService.findByLikeBranchVersionAndProjectUrl("branchVersion", "projectUrl")).thenReturn(Collections.emptyList());

        // Configure ReleaseProjectService.findByBranchVersionAndProjectUrl(...).
        final ReleaseProject releaseProject = new ReleaseProject();
        releaseProject.setId("id");
        releaseProject.setProjectId("projectId");
        releaseProject.setHistoryId("historyId");
        releaseProject.setCreateTagVersion("createTagVersion");
        releaseProject.setCreateTagDetail("createTagDetail");
        releaseProject.setCreteBranchVersion("creteBranchVersion");
        releaseProject.setReleaseDockerVersion("releaseDockerVersion");
        releaseProject.setOnlineDockerVersion("onlineDockerVersion");
        releaseProject.setProjectGitUrl("projectGitUrl");
        releaseProject.setReleaseId("releaseId");
        //when(eventService.releaseProjectService.findByBranchVersionAndProjectUrl("branchVersion", "projectUrl")).thenReturn(releaseProject);

        // Configure UserService.findByUserName(...).
        final User user = new User();
        user.setId("id");
        user.setName("name");
        user.setEmail("email");
        user.setPhone("phone");
        user.setRoles(Arrays.asList(new Role(RoleEnum.developer)));
        user.setPassword("password");
        user.setDepartment("department");
        user.setTitle("title");
        user.setEmployeeId(0);
        final List<User> users = Arrays.asList(user);
        //when(eventService.userService.findByUserName("userName")).thenReturn(users);

        // Run the test
        final Map<Project, List<ProjectBuildVo>> result = eventService.getEventProject(rabbitmqData);

        // Verify the results
        //verify(eventService.releaseProjectService).save(any(ReleaseProject.class));
    }

    @Test
public     void testGetEventProject_UserServiceReturnsNoItems() throws Exception {
        // Setup
        final JSONObject rabbitmqData = new JSONObject(0, false);

        // Configure ProjectService.findByRepo(...).
        final Project project = new Project();
        project.setImageTag("imageTag");
        project.setDevopsProjectId("devopsProjectId");
        final DeployConfig deployConfig = new DeployConfig();
        deployConfig.setAppType(AppType.deployment);
        deployConfig.setHostname("hostname");
        deployConfig.setLimitsCpu("limitsCpu");
        deployConfig.setName("name");
        deployConfig.setReplicas("replicas");
        deployConfig.setId("id");
        deployConfig.setIngressHost("ingressHost");
        deployConfig.setContainerPort("containerPort");
        deployConfig.setImageTag("imageTag");
        deployConfig.setYamlId("yamlId");
        project.setDeployConfigList(Arrays.asList(deployConfig));
        project.setDeployType(DeployType.k8s);
        project.setId("id");
        project.setName("name");
        final Scm scm = new Scm();
        scm.setType(ScmType.svn);
        scm.setUrl("url");
        scm.setUser("user");
        scm.setId("id");
        scm.setName("name");
        scm.setPassword("password");
        scm.setTagName("tagName");
        scm.setChangePwd(false);
        scm.setSourceBranch("sourceBranch");
        scm.setTargetBranch("targetBranch");
        project.setScm(scm);
        final Deploy deployInfo = new Deploy();
        deployInfo.setId("id");
        deployInfo.setHostId("hostId");
        deployInfo.setUrl("url");
        deployInfo.setPath("path");
        final HostInfo host = new HostInfo();
        host.setId("id");
        host.setIp("ip");
        host.setPort("port");
        host.setUser("user");
        host.setPass("pass");
        host.setName("name");
        host.setTeamId("teamId");
        deployInfo.setHost(host);
        project.setDeployInfo(deployInfo);
        final DockerRegistry registry = new DockerRegistry();
        registry.setUrl("url");
        registry.setUser("user");
        registry.setPassword("password");
        registry.setSchema("schema");
        registry.setId("id");
        registry.setEmail("email");
        registry.setChangePwd(false);
        registry.setTeamId("teamId");
        project.setRegistry(registry);
        project.setDescription("description");
        final List<Project> projects = Arrays.asList(project);
        //when(eventService.projectService.findByRepo("repoUrl")).thenReturn(projects);

        // Configure PipelineProfileService.findOne(...).
        final PipelineProfile pipelineProfile = new PipelineProfile();
        pipelineProfile.setDel(false);
        pipelineProfile.setUpdateTag(false);
        pipelineProfile.setUpdateConfig(false);
        pipelineProfile.setConfigPath("configPath");
        pipelineProfile.setFocusRedlightRepair(false);
        pipelineProfile.setReleasePrefix("releasePrefix");
        pipelineProfile.setAddTag(false);
        pipelineProfile.setTagPrefix("tagPrefix");
        pipelineProfile.setId("id");
        pipelineProfile.setSkipUnitTest(false);
        //when(eventService.pipelineProfileService.findOne("id")).thenReturn(pipelineProfile);

        // Configure ReleaseProjectService.findByLikeBranchVersionAndProjectUrl(...).
        final ReleaseProject releaseProject = new ReleaseProject();
        releaseProject.setId("id");
        releaseProject.setProjectId("projectId");
        releaseProject.setHistoryId("historyId");
        releaseProject.setCreateTagVersion("createTagVersion");
        releaseProject.setCreateTagDetail("createTagDetail");
        releaseProject.setCreteBranchVersion("creteBranchVersion");
        releaseProject.setReleaseDockerVersion("releaseDockerVersion");
        releaseProject.setOnlineDockerVersion("onlineDockerVersion");
        releaseProject.setProjectGitUrl("projectGitUrl");
        releaseProject.setReleaseId("releaseId");
        final List<ReleaseProject> releaseProjects = Arrays.asList(releaseProject);
        //when(eventService.releaseProjectService.findByLikeBranchVersionAndProjectUrl("branchVersion", "projectUrl")).thenReturn(releaseProjects);

        // Configure ReleaseProjectService.findByBranchVersionAndProjectUrl(...).
        final ReleaseProject releaseProject1 = new ReleaseProject();
        releaseProject1.setId("id");
        releaseProject1.setProjectId("projectId");
        releaseProject1.setHistoryId("historyId");
        releaseProject1.setCreateTagVersion("createTagVersion");
        releaseProject1.setCreateTagDetail("createTagDetail");
        releaseProject1.setCreteBranchVersion("creteBranchVersion");
        releaseProject1.setReleaseDockerVersion("releaseDockerVersion");
        releaseProject1.setOnlineDockerVersion("onlineDockerVersion");
        releaseProject1.setProjectGitUrl("projectGitUrl");
        releaseProject1.setReleaseId("releaseId");
        //when(eventService.releaseProjectService.findByBranchVersionAndProjectUrl("branchVersion", "projectUrl")).thenReturn(releaseProject1);

        //when(eventService.userService.findByUserName("userName")).thenReturn(Collections.emptyList());

        // Run the test
        final Map<Project, List<ProjectBuildVo>> result = eventService.getEventProject(rabbitmqData);

        // Verify the results
        //verify(eventService.releaseProjectService).save(any(ReleaseProject.class));
    }

    @Test
 public    void testGetEventProject_ThrowsException() {
        // Setup
        final JSONObject rabbitmqData = new JSONObject(0, false);

        // Configure ProjectService.findByRepo(...).
        final Project project = new Project();
        project.setImageTag("imageTag");
        project.setDevopsProjectId("devopsProjectId");
        final DeployConfig deployConfig = new DeployConfig();
        deployConfig.setAppType(AppType.deployment);
        deployConfig.setHostname("hostname");
        deployConfig.setLimitsCpu("limitsCpu");
        deployConfig.setName("name");
        deployConfig.setReplicas("replicas");
        deployConfig.setId("id");
        deployConfig.setIngressHost("ingressHost");
        deployConfig.setContainerPort("containerPort");
        deployConfig.setImageTag("imageTag");
        deployConfig.setYamlId("yamlId");
        project.setDeployConfigList(Arrays.asList(deployConfig));
        project.setDeployType(DeployType.k8s);
        project.setId("id");
        project.setName("name");
        final Scm scm = new Scm();
        scm.setType(ScmType.svn);
        scm.setUrl("url");
        scm.setUser("user");
        scm.setId("id");
        scm.setName("name");
        scm.setPassword("password");
        scm.setTagName("tagName");
        scm.setChangePwd(false);
        scm.setSourceBranch("sourceBranch");
        scm.setTargetBranch("targetBranch");
        project.setScm(scm);
        final Deploy deployInfo = new Deploy();
        deployInfo.setId("id");
        deployInfo.setHostId("hostId");
        deployInfo.setUrl("url");
        deployInfo.setPath("path");
        final HostInfo host = new HostInfo();
        host.setId("id");
        host.setIp("ip");
        host.setPort("port");
        host.setUser("user");
        host.setPass("pass");
        host.setName("name");
        host.setTeamId("teamId");
        deployInfo.setHost(host);
        project.setDeployInfo(deployInfo);
        final DockerRegistry registry = new DockerRegistry();
        registry.setUrl("url");
        registry.setUser("user");
        registry.setPassword("password");
        registry.setSchema("schema");
        registry.setId("id");
        registry.setEmail("email");
        registry.setChangePwd(false);
        registry.setTeamId("teamId");
        project.setRegistry(registry);
        project.setDescription("description");
        final List<Project> projects = Arrays.asList(project);
        //when(eventService.projectService.findByRepo("repoUrl")).thenReturn(projects);

        // Configure PipelineProfileService.findOne(...).
        final PipelineProfile pipelineProfile = new PipelineProfile();
        pipelineProfile.setDel(false);
        pipelineProfile.setUpdateTag(false);
        pipelineProfile.setUpdateConfig(false);
        pipelineProfile.setConfigPath("configPath");
        pipelineProfile.setFocusRedlightRepair(false);
        pipelineProfile.setReleasePrefix("releasePrefix");
        pipelineProfile.setAddTag(false);
        pipelineProfile.setTagPrefix("tagPrefix");
        pipelineProfile.setId("id");
        pipelineProfile.setSkipUnitTest(false);
        //when(eventService.pipelineProfileService.findOne("id")).thenReturn(pipelineProfile);

        // Configure ReleaseProjectService.findByLikeBranchVersionAndProjectUrl(...).
        final ReleaseProject releaseProject = new ReleaseProject();
        releaseProject.setId("id");
        releaseProject.setProjectId("projectId");
        releaseProject.setHistoryId("historyId");
        releaseProject.setCreateTagVersion("createTagVersion");
        releaseProject.setCreateTagDetail("createTagDetail");
        releaseProject.setCreteBranchVersion("creteBranchVersion");
        releaseProject.setReleaseDockerVersion("releaseDockerVersion");
        releaseProject.setOnlineDockerVersion("onlineDockerVersion");
        releaseProject.setProjectGitUrl("projectGitUrl");
        releaseProject.setReleaseId("releaseId");
        final List<ReleaseProject> releaseProjects = Arrays.asList(releaseProject);
        //when(eventService.releaseProjectService.findByLikeBranchVersionAndProjectUrl("branchVersion", "projectUrl")).thenReturn(releaseProjects);

        // Configure ReleaseProjectService.findByBranchVersionAndProjectUrl(...).
        final ReleaseProject releaseProject1 = new ReleaseProject();
        releaseProject1.setId("id");
        releaseProject1.setProjectId("projectId");
        releaseProject1.setHistoryId("historyId");
        releaseProject1.setCreateTagVersion("createTagVersion");
        releaseProject1.setCreateTagDetail("createTagDetail");
        releaseProject1.setCreteBranchVersion("creteBranchVersion");
        releaseProject1.setReleaseDockerVersion("releaseDockerVersion");
        releaseProject1.setOnlineDockerVersion("onlineDockerVersion");
        releaseProject1.setProjectGitUrl("projectGitUrl");
        releaseProject1.setReleaseId("releaseId");
        //when(eventService.releaseProjectService.findByBranchVersionAndProjectUrl("branchVersion", "projectUrl")).thenReturn(releaseProject1);

        // Configure UserService.findByUserName(...).
        final User user = new User();
        user.setId("id");
        user.setName("name");
        user.setEmail("email");
        user.setPhone("phone");
        user.setRoles(Arrays.asList(new Role(RoleEnum.developer)));
        user.setPassword("password");
        user.setDepartment("department");
        user.setTitle("title");
        user.setEmployeeId(0);
        final List<User> users = Arrays.asList(user);
        //when(eventService.userService.findByUserName("userName")).thenReturn(users);

        // Run the test
        //assertThatThrownBy(() -> eventService.getEventProject(rabbitmqData)).isInstanceOf(Exception.class);
        //verify(eventService.releaseProjectService).save(any(ReleaseProject.class));
    }

    @Test
public     void testGetPipelineProfile1() throws Exception {
        // Setup
        final Project project = new Project();
        project.setImageTag("imageTag");
        project.setDevopsProjectId("devopsProjectId");
        final DeployConfig deployConfig = new DeployConfig();
        deployConfig.setAppType(AppType.deployment);
        deployConfig.setHostname("hostname");
        deployConfig.setLimitsCpu("limitsCpu");
        deployConfig.setName("name");
        deployConfig.setReplicas("replicas");
        deployConfig.setId("id");
        deployConfig.setIngressHost("ingressHost");
        deployConfig.setContainerPort("containerPort");
        deployConfig.setImageTag("imageTag");
        deployConfig.setYamlId("yamlId");
        project.setDeployConfigList(Arrays.asList(deployConfig));
        project.setDeployType(DeployType.k8s);
        project.setId("id");
        project.setName("name");
        final Scm scm = new Scm();
        scm.setType(ScmType.svn);
        scm.setUrl("url");
        scm.setUser("user");
        scm.setId("id");
        scm.setName("name");
        scm.setPassword("password");
        scm.setTagName("tagName");
        scm.setChangePwd(false);
        scm.setSourceBranch("sourceBranch");
        scm.setTargetBranch("targetBranch");
        project.setScm(scm);
        final Deploy deployInfo = new Deploy();
        deployInfo.setId("id");
        deployInfo.setHostId("hostId");
        deployInfo.setUrl("url");
        deployInfo.setPath("path");
        final HostInfo host = new HostInfo();
        host.setId("id");
        host.setIp("ip");
        host.setPort("port");
        host.setUser("user");
        host.setPass("pass");
        host.setName("name");
        host.setTeamId("teamId");
        deployInfo.setHost(host);
        project.setDeployInfo(deployInfo);
        final DockerRegistry registry = new DockerRegistry();
        registry.setUrl("url");
        registry.setUser("user");
        registry.setPassword("password");
        registry.setSchema("schema");
        registry.setId("id");
        registry.setEmail("email");
        registry.setChangePwd(false);
        registry.setTeamId("teamId");
        project.setRegistry(registry);
        project.setDescription("description");

        final Event event = new Event();
        event.setId("id");
        event.setEventType(EventType.AUTO_DEPLOYMENT);
        event.setUserId(0L);
        event.setProjectType(ProjectType.java);
        event.setDefault(false);
        event.setDetail("detail");
        event.setSourceBranch("sourceBranch");
        event.setTargetBranch("targetBranch");
        event.setCreateDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        event.setUserName("userName");
        final List<Event> events = Arrays.asList(event);

        // Run the test
        final List<ProjectBuildVo> result = eventService.getPipelineProfile(project, events, "targetBranchName", "sourceBranchName");

        // Verify the results
    }

    @Test
 public    void testGetPipelineProfile1_ThrowsException() {
        // Setup
        final Project project = new Project();
        project.setImageTag("imageTag");
        project.setDevopsProjectId("devopsProjectId");
        final DeployConfig deployConfig = new DeployConfig();
        deployConfig.setAppType(AppType.deployment);
        deployConfig.setHostname("hostname");
        deployConfig.setLimitsCpu("limitsCpu");
        deployConfig.setName("name");
        deployConfig.setReplicas("replicas");
        deployConfig.setId("id");
        deployConfig.setIngressHost("ingressHost");
        deployConfig.setContainerPort("containerPort");
        deployConfig.setImageTag("imageTag");
        deployConfig.setYamlId("yamlId");
        project.setDeployConfigList(Arrays.asList(deployConfig));
        project.setDeployType(DeployType.k8s);
        project.setId("id");
        project.setName("name");
        final Scm scm = new Scm();
        scm.setType(ScmType.svn);
        scm.setUrl("url");
        scm.setUser("user");
        scm.setId("id");
        scm.setName("name");
        scm.setPassword("password");
        scm.setTagName("tagName");
        scm.setChangePwd(false);
        scm.setSourceBranch("sourceBranch");
        scm.setTargetBranch("targetBranch");
        project.setScm(scm);
        final Deploy deployInfo = new Deploy();
        deployInfo.setId("id");
        deployInfo.setHostId("hostId");
        deployInfo.setUrl("url");
        deployInfo.setPath("path");
        final HostInfo host = new HostInfo();
        host.setId("id");
        host.setIp("ip");
        host.setPort("port");
        host.setUser("user");
        host.setPass("pass");
        host.setName("name");
        host.setTeamId("teamId");
        deployInfo.setHost(host);
        project.setDeployInfo(deployInfo);
        final DockerRegistry registry = new DockerRegistry();
        registry.setUrl("url");
        registry.setUser("user");
        registry.setPassword("password");
        registry.setSchema("schema");
        registry.setId("id");
        registry.setEmail("email");
        registry.setChangePwd(false);
        registry.setTeamId("teamId");
        project.setRegistry(registry);
        project.setDescription("description");

        final Event event = new Event();
        event.setId("id");
        event.setEventType(EventType.AUTO_DEPLOYMENT);
        event.setUserId(0L);
        event.setProjectType(ProjectType.java);
        event.setDefault(false);
        event.setDetail("detail");
        event.setSourceBranch("sourceBranch");
        event.setTargetBranch("targetBranch");
        event.setCreateDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        event.setUserName("userName");
        final List<Event> events = Arrays.asList(event);

        // Run the test
//        //assertThatThrownBy(() -> eventService.getPipelineProfile(project, events, "targetBranchName", "sourceBranchName")).isInstanceOf(Exception.class);
    }

    @Test
  public   void testIsEventBranchRules() {
        // Setup
        final Project project = new Project();
        project.setImageTag("imageTag");
        project.setDevopsProjectId("devopsProjectId");
        final DeployConfig deployConfig = new DeployConfig();
        deployConfig.setAppType(AppType.deployment);
        deployConfig.setHostname("hostname");
        deployConfig.setLimitsCpu("limitsCpu");
        deployConfig.setName("name");
        deployConfig.setReplicas("replicas");
        deployConfig.setId("id");
        deployConfig.setIngressHost("ingressHost");
        deployConfig.setContainerPort("containerPort");
        deployConfig.setImageTag("imageTag");
        deployConfig.setYamlId("yamlId");
        project.setDeployConfigList(Arrays.asList(deployConfig));
        project.setDeployType(DeployType.k8s);
        project.setId("id");
        project.setName("name");
        final Scm scm = new Scm();
        scm.setType(ScmType.svn);
        scm.setUrl("url");
        scm.setUser("user");
        scm.setId("id");
        scm.setName("name");
        scm.setPassword("password");
        scm.setTagName("tagName");
        scm.setChangePwd(false);
        scm.setSourceBranch("sourceBranch");
        scm.setTargetBranch("targetBranch");
        project.setScm(scm);
        final Deploy deployInfo = new Deploy();
        deployInfo.setId("id");
        deployInfo.setHostId("hostId");
        deployInfo.setUrl("url");
        deployInfo.setPath("path");
        final HostInfo host = new HostInfo();
        host.setId("id");
        host.setIp("ip");
        host.setPort("port");
        host.setUser("user");
        host.setPass("pass");
        host.setName("name");
        host.setTeamId("teamId");
        deployInfo.setHost(host);
        project.setDeployInfo(deployInfo);
        final DockerRegistry registry = new DockerRegistry();
        registry.setUrl("url");
        registry.setUser("user");
        registry.setPassword("password");
        registry.setSchema("schema");
        registry.setId("id");
        registry.setEmail("email");
        registry.setChangePwd(false);
        registry.setTeamId("teamId");
        project.setRegistry(registry);
        project.setDescription("description");

        final Event eventSetting = new Event();
        eventSetting.setId("id");
        eventSetting.setEventType(EventType.AUTO_DEPLOYMENT);
        eventSetting.setUserId(0L);
        eventSetting.setProjectType(ProjectType.java);
        eventSetting.setDefault(false);
        eventSetting.setDetail("detail");
        eventSetting.setSourceBranch("sourceBranch");
        eventSetting.setTargetBranch("targetBranch");
        eventSetting.setCreateDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        eventSetting.setUserName("userName");

        // Run the test
        final boolean result = eventService.isEventBranchRules(project, eventSetting, "targetBranchName", "sourceBranchName");

        // Verify the results
        assertThat(result).isTrue();
    }

    @Test
    public   void testGetPipelineProfile2() throws Exception {
        String projectJSON="{\"autoBuild\":false,\"deployConfigList\":[{\"appType\":\"deployment\",\"containerPort\":\"containerPort\",\"hostname\":\"hostname\",\"id\":\"id\",\"imageTag\":\"imageTag\",\"ingressHost\":\"ingressHost\",\"limitsCpu\":\"limitsCpu\",\"name\":\"name\",\"replicas\":\"replicas\",\"yamlId\":\"yamlId\"}],\"deployInfo\":{\"host\":{\"id\":\"id\",\"ip\":\"ip\",\"name\":\"name\",\"pass\":\"pass\",\"port\":\"port\",\"teamId\":\"teamId\",\"user\":\"user\"},\"hostId\":\"hostId\",\"id\":\"id\",\"path\":\"path\",\"url\":\"url\"},\"deployType\":\"k8s\",\"description\":\"description\",\"devopsProjectId\":\"devopsProjectId\",\"eazybuilderEjbProject\":false,\"eazybuilderStyleProject\":false,\"id\":\"id\",\"imageTag\":\"imageTag\",\"isDel\":0,\"legacyProject\":false,\"name\":\"name\",\"piplineEventProjectId\":\"devopsProjectId\",\"registry\":{\"changePwd\":false,\"email\":\"email\",\"id\":\"id\",\"password\":\"password\",\"schema\":\"schema\",\"teamId\":\"teamId\",\"url\":\"url\",\"user\":\"user\"},\"scm\":{\"changePwd\":false,\"id\":\"id\",\"name\":\"name\",\"password\":\"password\",\"sourceBranch\":\"sourceBranch\",\"tagName\":\"tagName\",\"targetBranch\":\"targetBranch\",\"type\":\"svn\",\"url\":\"url\",\"user\":\"user\"},\"team\":{\"id\":\"d8b12a48-3dae-4067-bb8f-bb1a631bb267\",\"isDel\":0}}";
        Project project = JSON.parseObject(projectJSON, Project.class);
        PipelineProfile pipelineProfile=new PipelineProfile();
        pipelineProfile.setId(UUID.randomUUID().toString());
        project.setDefaultProfile(pipelineProfile);
        Event event=new Event();
        event.setEventType(EventType.merge);
        event.setProjectType(ProjectType.java);
        List<Event>  events=Lists.newArrayList(event);
        String targetBranchName="master";
        String sourceBranchName="bugfix-1113";

        try {
            eventService.getPipelineProfile(project,events,targetBranchName,sourceBranchName);
        }catch (Exception e){
            Assert.isTrue(e!=null);
        }



    }

    @Test
  public   void testGetPipelineProfile2_ThrowsException() {
        // Setup
        final Project project = new Project();
        project.setImageTag("imageTag");
        project.setDevopsProjectId("devopsProjectId");
        final DeployConfig deployConfig = new DeployConfig();
        deployConfig.setAppType(AppType.deployment);
        deployConfig.setHostname("hostname");
        deployConfig.setLimitsCpu("limitsCpu");
        deployConfig.setName("name");
        deployConfig.setReplicas("replicas");
        deployConfig.setId("id");
        deployConfig.setIngressHost("ingressHost");
        deployConfig.setContainerPort("containerPort");
        deployConfig.setImageTag("imageTag");
        deployConfig.setYamlId("yamlId");
        project.setDeployConfigList(Arrays.asList(deployConfig));
        project.setDeployType(DeployType.k8s);
        project.setId("id");
        project.setName("name");
        final Scm scm = new Scm();
        scm.setType(ScmType.svn);
        scm.setUrl("url");
        scm.setUser("user");
        scm.setId("id");
        scm.setName("name");
        scm.setPassword("password");
        scm.setTagName("tagName");
        scm.setChangePwd(false);
        scm.setSourceBranch("sourceBranch");
        scm.setTargetBranch("targetBranch");
        project.setScm(scm);
        final Deploy deployInfo = new Deploy();
        deployInfo.setId("id");
        deployInfo.setHostId("hostId");
        deployInfo.setUrl("url");
        deployInfo.setPath("path");
        final HostInfo host = new HostInfo();
        host.setId("id");
        host.setIp("ip");
        host.setPort("port");
        host.setUser("user");
        host.setPass("pass");
        host.setName("name");
        host.setTeamId("teamId");
        deployInfo.setHost(host);
        project.setDeployInfo(deployInfo);
        final DockerRegistry registry = new DockerRegistry();
        registry.setUrl("url");
        registry.setUser("user");
        registry.setPassword("password");
        registry.setSchema("schema");
        registry.setId("id");
        registry.setEmail("email");
        registry.setChangePwd(false);
        registry.setTeamId("teamId");
        project.setRegistry(registry);
        project.setDescription("description");

        final Event event = new Event();
        event.setId("id");
        event.setEventType(EventType.AUTO_DEPLOYMENT);
        event.setUserId(0L);
        event.setProjectType(ProjectType.java);
        event.setDefault(false);
        event.setDetail("detail");
        event.setSourceBranch("sourceBranch");
        event.setTargetBranch("targetBranch");
        event.setCreateDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        event.setUserName("userName");
        final List<Event> events = Arrays.asList(event);

        // Run the test
        //assertThatThrownBy(() -> eventService.getPipelineProfile(project, events)).isInstanceOf(Exception.class);
    }


    @Test
  public   void testIsEnglish() {
        assertThat(EventService.isEnglish("charaString")).isTrue();
    }


    @Test
    public  void testListSort() {
        // Setup
        ReleaseProject releaseProject = new ReleaseProject();
        releaseProject.setId("id1");
        releaseProject.setProjectId("projectId");
        releaseProject.setHistoryId("historyId");
        releaseProject.setCreateTagVersion("createTagVersion");
        releaseProject.setCreateTagDetail("createTagDetail");
        releaseProject.setCreteBranchVersion("test-9-18-202209131605");
        releaseProject.setReleaseDockerVersion("releaseDockerVersion");
        releaseProject.setOnlineDockerVersion("onlineDockerVersion");
        releaseProject.setProjectGitUrl("projectGitUrl");
        releaseProject.setReleaseId("releaseId");
        ReleaseProject releaseProject2 = new ReleaseProject();
        releaseProject2.setId("id2");
        releaseProject2.setProjectId("projectId");
        releaseProject2.setHistoryId("historyId");
        releaseProject2.setCreateTagVersion("createTagVersion");
        releaseProject2.setCreateTagDetail("createTagDetail");
        releaseProject2.setCreteBranchVersion("test-9-18-202209091403");
        releaseProject2.setReleaseDockerVersion("releaseDockerVersion");
        releaseProject2.setOnlineDockerVersion("onlineDockerVersion");
        releaseProject2.setProjectGitUrl("projectGitUrl");
        releaseProject2.setReleaseId("releaseId");
         List<ReleaseProject> releaseProjects = new ArrayList<>();
        releaseProjects.add(releaseProject);
        releaseProjects.add(releaseProject2);

        // Run the test
        final List<ReleaseProject> result = eventService.listSort(releaseProjects);

        // Verify the results
    }
}
