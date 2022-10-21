package com.eazybuilder.ci.service;

import com.alibaba.fastjson.JSONObject;
import com.eazybuilder.ci.constant.ActionScope;
import com.eazybuilder.ci.constant.RoleEnum;
import com.eazybuilder.ci.controller.vo.ProjectBuildVo;
import com.eazybuilder.ci.entity.*;
import com.eazybuilder.ci.entity.devops.*;
import com.eazybuilder.ci.entity.docker.DockerDigest;
import com.eazybuilder.ci.entity.report.Stage;
import com.eazybuilder.ci.mail.MailSenderHelper;
import com.eazybuilder.ci.rabbitMq.SendRabbitMq;
import freemarker.core.ParseException;
import freemarker.template.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class OnlineServiceTest {

    private OnlineService onlineServiceUnderTest;

    @BeforeEach
    void setUp() {
        onlineServiceUnderTest = new OnlineService();
        onlineServiceUnderTest.propService = mock(SystemPropertyService.class);
        onlineServiceUnderTest.buildJobService = mock(BuildJobService.class);
        onlineServiceUnderTest.teamService = mock(TeamServiceImpl.class);
        onlineServiceUnderTest.redmineService = mock(RedmineService.class);
        onlineServiceUnderTest.pipelineService = mock(PipelineServiceImpl.class);
        onlineServiceUnderTest.configuration = mock(Configuration.class);
        onlineServiceUnderTest.userService = mock(UserService.class);
        onlineServiceUnderTest.mailSender = mock(MailSenderHelper.class);
        onlineServiceUnderTest.releaseService = mock(ReleaseService.class);
        onlineServiceUnderTest.sendRabbitMq = mock(SendRabbitMq.class);
        onlineServiceUnderTest.eventService = mock(EventService.class);
        onlineServiceUnderTest.pipelineProfileService = mock(PipelineProfileService.class);
        onlineServiceUnderTest.releaseProjectService = mock(ReleaseProjectService.class);
        onlineServiceUnderTest.dockerDigestService = mock(DockerDigestService.class);
    }

    @Test
    void testFindDockerDigest1() {
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

        // Configure TeamServiceImpl.getTeamNameSpace(...).
        final TeamNamespace teamNamespace = new TeamNamespace();
        teamNamespace.setName("name");
        teamNamespace.setGitlabApiDomain("gitlabApiDomain");
        teamNamespace.setNamespaceType(NamespaceType.dev);
        teamNamespace.setCode("code");
        teamNamespace.setRemark1("remark1");
        teamNamespace.setRemark2("remark2");
        teamNamespace.setRemark3("remark3");
        final Set<TeamNamespace> teamNamespaces = new HashSet<>(Arrays.asList(teamNamespace));
        //when(onlineServiceUnderTest.teamService.getTeamNameSpace(any(Team.class))).thenReturn(teamNamespaces);

        // Configure DockerDigestService.findDockerByProjectIdAndNamespaceAndTag(...).
        final DockerDigest dockerDigest = new DockerDigest();
        dockerDigest.setId("id");
        dockerDigest.setPipelineId("pipelineId");
        dockerDigest.setProjectId("projectId");
        dockerDigest.setProjectName("projectName");
        dockerDigest.setDigest("digest");
        dockerDigest.setUrl("url");
        dockerDigest.setImageName("imageName");
        dockerDigest.setNamespace("namespace");
        dockerDigest.setTag("tag");
        final Iterable<DockerDigest> dockerDigests = Arrays.asList(dockerDigest);
        //when(onlineServiceUnderTest.dockerDigestService.findDockerByProjectIdAndNamespaceAndTag("projectId", Arrays.asList("value"), "imageTag")).thenReturn(dockerDigests);

        // Run the test
        final List<DockerDigest> result = onlineServiceUnderTest.findDockerDigest(project);

        // Verify the results
    }

    @Test
    void testFindDockerDigest1_TeamServiceImplReturnsNoItems() {
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

        //when(onlineServiceUnderTest.teamService.getTeamNameSpace(any(Team.class))).thenReturn(Collections.emptySet());

        // Configure DockerDigestService.findDockerByProjectIdAndNamespaceAndTag(...).
        final DockerDigest dockerDigest = new DockerDigest();
        dockerDigest.setId("id");
        dockerDigest.setPipelineId("pipelineId");
        dockerDigest.setProjectId("projectId");
        dockerDigest.setProjectName("projectName");
        dockerDigest.setDigest("digest");
        dockerDigest.setUrl("url");
        dockerDigest.setImageName("imageName");
        dockerDigest.setNamespace("namespace");
        dockerDigest.setTag("tag");
        final Iterable<DockerDigest> dockerDigests = Arrays.asList(dockerDigest);
        //when(onlineServiceUnderTest.dockerDigestService.findDockerByProjectIdAndNamespaceAndTag("projectId", Arrays.asList("value"), "imageTag")).thenReturn(dockerDigests);

        // Run the test
        final List<DockerDigest> result = onlineServiceUnderTest.findDockerDigest(project);

        // Verify the results
    }

    @Test
    void testFindDockerDigest1_DockerDigestServiceReturnsNoItems() {
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

        // Configure TeamServiceImpl.getTeamNameSpace(...).
        final TeamNamespace teamNamespace = new TeamNamespace();
        teamNamespace.setName("name");
        teamNamespace.setGitlabApiDomain("gitlabApiDomain");
        teamNamespace.setNamespaceType(NamespaceType.dev);
        teamNamespace.setCode("code");
        teamNamespace.setRemark1("remark1");
        teamNamespace.setRemark2("remark2");
        teamNamespace.setRemark3("remark3");
        final Set<TeamNamespace> teamNamespaces = new HashSet<>(Arrays.asList(teamNamespace));
        //when(onlineServiceUnderTest.teamService.getTeamNameSpace(any(Team.class))).thenReturn(teamNamespaces);

        //when(onlineServiceUnderTest.dockerDigestService.findDockerByProjectIdAndNamespaceAndTag("projectId", Arrays.asList("value"), "imageTag")).thenReturn(Collections.emptyList());

        // Run the test
        final List<DockerDigest> result = onlineServiceUnderTest.findDockerDigest(project);

        // Verify the results
    }

    @Test
    void testFindDockerDigest2() {
        // Setup
        // Configure ReleaseService.findOne(...).
        final Release release = new Release();
        release.setBatchDdvice("batchDdvice");
        release.setReleaseCode("releaseCode");
        release.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        release.setTitle("title");
        release.setId("id");
        release.setImageTag("imageTag");
        release.setCreateDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        release.setSprintId("sprintId");
        release.setIssuesId("issuesId");
        release.setReleaseDetail("releaseDetail");
        //when(onlineServiceUnderTest.releaseService.findOne("id")).thenReturn(release);

        // Configure TeamServiceImpl.findByName(...).
        final Team team = new Team();
        final TeamResource teamResource = new TeamResource();
        teamResource.setJenkinsUrl("jenkinsUrl");
        teamResource.setJenkinsWorkPath("jenkinsWorkPath");
        teamResource.setJenkinsWorkType("jenkinsWorkType");
        teamResource.setSonarUrl("sonarUrl");
        teamResource.setId("id");
        teamResource.setTeamId("teamId");
        teamResource.setTeamName("teamName");
        teamResource.setJenkinsK8sSupport(false);
        teamResource.setReferenceSource("referenceSource");
        teamResource.setK8sYmlPath("k8sYmlPath");
        team.setTeamResource(teamResource);
        final TeamThreshold teamThreshold = new TeamThreshold();
        teamThreshold.setId("id");
        teamThreshold.setTeamId("teamId");
        teamThreshold.setThreSholdType(ThresholdType.bug_blocker);
        teamThreshold.setBlockerId("blockerId");
        teamThreshold.setInputTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        teamThreshold.setUpdateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        teamThreshold.setRemark1("remark1");
        teamThreshold.setRemark2("remark2");
        teamThreshold.setRemark3("remark3");
        teamThreshold.setActionScope(ActionScope.CODE_PUSH);
        team.setTeamThresholds(new HashSet<>(Arrays.asList(teamThreshold)));
        team.setName("name");
        team.setId("id");
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
        team.setMembers(Arrays.asList(user));
        team.setOwnerId("ownerId");
        team.setCode("code");
        final User user1 = new User();
        user1.setId("id");
        user1.setName("name");
        user1.setEmail("email");
        user1.setPhone("phone");
        user1.setRoles(Arrays.asList(new Role(RoleEnum.developer)));
        user1.setPassword("password");
        user1.setDepartment("department");
        user1.setTitle("title");
        user1.setEmployeeId(0);
        team.setConfigers(Arrays.asList(user1));
        team.setDevopsTeamId("devopsTeamId");
        final Guard guard = new Guard();
        guard.setId("id");
        guard.setThresholdMin(0.0);
        guard.setThresholdMax(0.0);
        guard.setGuardType(ThresholdType.bug_blocker);
        guard.setName("name");
        guard.setLevel("level");
        team.setGuards(Arrays.asList(guard));
        //when(onlineServiceUnderTest.teamService.findByName("name")).thenReturn(team);

        // Configure TeamServiceImpl.getTeamNameSpace(...).
        final TeamNamespace teamNamespace = new TeamNamespace();
        teamNamespace.setName("name");
        teamNamespace.setGitlabApiDomain("gitlabApiDomain");
        teamNamespace.setNamespaceType(NamespaceType.dev);
        teamNamespace.setCode("code");
        teamNamespace.setRemark1("remark1");
        teamNamespace.setRemark2("remark2");
        teamNamespace.setRemark3("remark3");
        final Set<TeamNamespace> teamNamespaces = new HashSet<>(Arrays.asList(teamNamespace));
        //when(onlineServiceUnderTest.teamService.getTeamNameSpace(any(Team.class))).thenReturn(teamNamespaces);

        // Configure DockerDigestService.findDockerByProjectIdAndNamespaceAndTag(...).
        final DockerDigest dockerDigest = new DockerDigest();
        dockerDigest.setId("id");
        dockerDigest.setPipelineId("pipelineId");
        dockerDigest.setProjectId("projectId");
        dockerDigest.setProjectName("projectName");
        dockerDigest.setDigest("digest");
        dockerDigest.setUrl("url");
        dockerDigest.setImageName("imageName");
        dockerDigest.setNamespace("namespace");
        dockerDigest.setTag("tag");
        final Iterable<DockerDigest> dockerDigests = Arrays.asList(dockerDigest);
        //when(onlineServiceUnderTest.dockerDigestService.findDockerByProjectIdAndNamespaceAndTag(Arrays.asList("value"), Arrays.asList("value"), "imageTag")).thenReturn(dockerDigests);

        // Run the test
        final List<DockerDigest> result = onlineServiceUnderTest.findDockerDigest("releaseId", "teamName");

        // Verify the results
    }

    @Test
    void testFindDockerDigest2_TeamServiceImplGetTeamNameSpaceReturnsNoItems() {
        // Setup
        // Configure ReleaseService.findOne(...).
        final Release release = new Release();
        release.setBatchDdvice("batchDdvice");
        release.setReleaseCode("releaseCode");
        release.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        release.setTitle("title");
        release.setId("id");
        release.setImageTag("imageTag");
        release.setCreateDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        release.setSprintId("sprintId");
        release.setIssuesId("issuesId");
        release.setReleaseDetail("releaseDetail");
        //when(onlineServiceUnderTest.releaseService.findOne("id")).thenReturn(release);

        // Configure TeamServiceImpl.findByName(...).
        final Team team = new Team();
        final TeamResource teamResource = new TeamResource();
        teamResource.setJenkinsUrl("jenkinsUrl");
        teamResource.setJenkinsWorkPath("jenkinsWorkPath");
        teamResource.setJenkinsWorkType("jenkinsWorkType");
        teamResource.setSonarUrl("sonarUrl");
        teamResource.setId("id");
        teamResource.setTeamId("teamId");
        teamResource.setTeamName("teamName");
        teamResource.setJenkinsK8sSupport(false);
        teamResource.setReferenceSource("referenceSource");
        teamResource.setK8sYmlPath("k8sYmlPath");
        team.setTeamResource(teamResource);
        final TeamThreshold teamThreshold = new TeamThreshold();
        teamThreshold.setId("id");
        teamThreshold.setTeamId("teamId");
        teamThreshold.setThreSholdType(ThresholdType.bug_blocker);
        teamThreshold.setBlockerId("blockerId");
        teamThreshold.setInputTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        teamThreshold.setUpdateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        teamThreshold.setRemark1("remark1");
        teamThreshold.setRemark2("remark2");
        teamThreshold.setRemark3("remark3");
        teamThreshold.setActionScope(ActionScope.CODE_PUSH);
        team.setTeamThresholds(new HashSet<>(Arrays.asList(teamThreshold)));
        team.setName("name");
        team.setId("id");
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
        team.setMembers(Arrays.asList(user));
        team.setOwnerId("ownerId");
        team.setCode("code");
        final User user1 = new User();
        user1.setId("id");
        user1.setName("name");
        user1.setEmail("email");
        user1.setPhone("phone");
        user1.setRoles(Arrays.asList(new Role(RoleEnum.developer)));
        user1.setPassword("password");
        user1.setDepartment("department");
        user1.setTitle("title");
        user1.setEmployeeId(0);
        team.setConfigers(Arrays.asList(user1));
        team.setDevopsTeamId("devopsTeamId");
        final Guard guard = new Guard();
        guard.setId("id");
        guard.setThresholdMin(0.0);
        guard.setThresholdMax(0.0);
        guard.setGuardType(ThresholdType.bug_blocker);
        guard.setName("name");
        guard.setLevel("level");
        team.setGuards(Arrays.asList(guard));
        //when(onlineServiceUnderTest.teamService.findByName("name")).thenReturn(team);

        //when(onlineServiceUnderTest.teamService.getTeamNameSpace(any(Team.class))).thenReturn(Collections.emptySet());

        // Configure DockerDigestService.findDockerByProjectIdAndNamespaceAndTag(...).
        final DockerDigest dockerDigest = new DockerDigest();
        dockerDigest.setId("id");
        dockerDigest.setPipelineId("pipelineId");
        dockerDigest.setProjectId("projectId");
        dockerDigest.setProjectName("projectName");
        dockerDigest.setDigest("digest");
        dockerDigest.setUrl("url");
        dockerDigest.setImageName("imageName");
        dockerDigest.setNamespace("namespace");
        dockerDigest.setTag("tag");
        final Iterable<DockerDigest> dockerDigests = Arrays.asList(dockerDigest);
        //when(onlineServiceUnderTest.dockerDigestService.findDockerByProjectIdAndNamespaceAndTag(Arrays.asList("value"), Arrays.asList("value"), "imageTag")).thenReturn(dockerDigests);

        // Run the test
        final List<DockerDigest> result = onlineServiceUnderTest.findDockerDigest("releaseId", "teamName");

        // Verify the results
    }

    @Test
    void testFindDockerDigest2_DockerDigestServiceReturnsNoItems() {
        // Setup
        // Configure ReleaseService.findOne(...).
        final Release release = new Release();
        release.setBatchDdvice("batchDdvice");
        release.setReleaseCode("releaseCode");
        release.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        release.setTitle("title");
        release.setId("id");
        release.setImageTag("imageTag");
        release.setCreateDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        release.setSprintId("sprintId");
        release.setIssuesId("issuesId");
        release.setReleaseDetail("releaseDetail");
        //when(onlineServiceUnderTest.releaseService.findOne("id")).thenReturn(release);

        // Configure TeamServiceImpl.findByName(...).
        final Team team = new Team();
        final TeamResource teamResource = new TeamResource();
        teamResource.setJenkinsUrl("jenkinsUrl");
        teamResource.setJenkinsWorkPath("jenkinsWorkPath");
        teamResource.setJenkinsWorkType("jenkinsWorkType");
        teamResource.setSonarUrl("sonarUrl");
        teamResource.setId("id");
        teamResource.setTeamId("teamId");
        teamResource.setTeamName("teamName");
        teamResource.setJenkinsK8sSupport(false);
        teamResource.setReferenceSource("referenceSource");
        teamResource.setK8sYmlPath("k8sYmlPath");
        team.setTeamResource(teamResource);
        final TeamThreshold teamThreshold = new TeamThreshold();
        teamThreshold.setId("id");
        teamThreshold.setTeamId("teamId");
        teamThreshold.setThreSholdType(ThresholdType.bug_blocker);
        teamThreshold.setBlockerId("blockerId");
        teamThreshold.setInputTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        teamThreshold.setUpdateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        teamThreshold.setRemark1("remark1");
        teamThreshold.setRemark2("remark2");
        teamThreshold.setRemark3("remark3");
        teamThreshold.setActionScope(ActionScope.CODE_PUSH);
        team.setTeamThresholds(new HashSet<>(Arrays.asList(teamThreshold)));
        team.setName("name");
        team.setId("id");
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
        team.setMembers(Arrays.asList(user));
        team.setOwnerId("ownerId");
        team.setCode("code");
        final User user1 = new User();
        user1.setId("id");
        user1.setName("name");
        user1.setEmail("email");
        user1.setPhone("phone");
        user1.setRoles(Arrays.asList(new Role(RoleEnum.developer)));
        user1.setPassword("password");
        user1.setDepartment("department");
        user1.setTitle("title");
        user1.setEmployeeId(0);
        team.setConfigers(Arrays.asList(user1));
        team.setDevopsTeamId("devopsTeamId");
        final Guard guard = new Guard();
        guard.setId("id");
        guard.setThresholdMin(0.0);
        guard.setThresholdMax(0.0);
        guard.setGuardType(ThresholdType.bug_blocker);
        guard.setName("name");
        guard.setLevel("level");
        team.setGuards(Arrays.asList(guard));
        //when(onlineServiceUnderTest.teamService.findByName("name")).thenReturn(team);

        // Configure TeamServiceImpl.getTeamNameSpace(...).
        final TeamNamespace teamNamespace = new TeamNamespace();
        teamNamespace.setName("name");
        teamNamespace.setGitlabApiDomain("gitlabApiDomain");
        teamNamespace.setNamespaceType(NamespaceType.dev);
        teamNamespace.setCode("code");
        teamNamespace.setRemark1("remark1");
        teamNamespace.setRemark2("remark2");
        teamNamespace.setRemark3("remark3");
        final Set<TeamNamespace> teamNamespaces = new HashSet<>(Arrays.asList(teamNamespace));
        //when(onlineServiceUnderTest.teamService.getTeamNameSpace(any(Team.class))).thenReturn(teamNamespaces);

        //when(onlineServiceUnderTest.dockerDigestService.findDockerByProjectIdAndNamespaceAndTag(Arrays.asList("value"), Arrays.asList("value"), "imageTag")).thenReturn(Collections.emptyList());

        // Run the test
        final List<DockerDigest> result = onlineServiceUnderTest.findDockerDigest("releaseId", "teamName");

        // Verify the results
    }

    @Test
    void testPageSearch() {
        // Setup
        // Configure UserService.findOne(...).
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
        //when(onlineServiceUnderTest.userService.findOne("id")).thenReturn(user);

        // Run the test
        final Page<Online> result = onlineServiceUnderTest.pageSearch(PageRequest.of(0, 1), "searchText");

        // Verify the results
    }

    @Test
    void testGetApplyOnlineProjects() throws Exception {
        // Setup
        final Online online = new Online();
        online.setBatchDdvice("batchDdvice");
        online.setReleaseCode("releaseCode");
        online.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        online.setTitle("title");
        online.setId("id");
        online.setCreateDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        online.setSprintId("sprintId");
        online.setReleaseDetail("releaseDetail");
        online.setReleaseUserName("releaseUserName");
        online.setBatchStatus(Status.NOT_EXECUTED);

        // Configure ReleaseService.findOne(...).
        final Release release = new Release();
        release.setBatchDdvice("batchDdvice");
        release.setReleaseCode("releaseCode");
        release.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        release.setTitle("title");
        release.setId("id");
        release.setImageTag("imageTag");
        release.setCreateDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        release.setSprintId("sprintId");
        release.setIssuesId("issuesId");
        release.setReleaseDetail("releaseDetail");
        //when(onlineServiceUnderTest.releaseService.findOne("id")).thenReturn(release);

        //when(onlineServiceUnderTest.redmineService.getSprintsById("springId")).thenReturn(new JSONObject(0, false));

        // Configure EventService.findByTeamIdAndEventType(...).
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
        //when(onlineServiceUnderTest.eventService.findByTeamIdAndEventType("teamId", EventType.AUTO_DEPLOYMENT)).thenReturn(events);

        // Configure EventService.getPipelineProfile(...).
        final ProjectBuildVo projectBuildVo = new ProjectBuildVo();
        projectBuildVo.setNameSpace("nameSpace");
        projectBuildVo.setProfile("profile");
        projectBuildVo.setProjects(Arrays.asList("value"));
        projectBuildVo.setArriveTagName("arriveTagName");
        projectBuildVo.setRolloutVersion("rolloutVersion");
        projectBuildVo.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        projectBuildVo.setDbUrl("dbUrl");
        projectBuildVo.setDbUserName("dbUserName");
        projectBuildVo.setDbPassword("dbPassword");
        projectBuildVo.setDockerImageTag("dockerImageTag");
        final List<ProjectBuildVo> projectBuildVos = Arrays.asList(projectBuildVo);
        //when(onlineServiceUnderTest.eventService.getPipelineProfile(any(Project.class), eq(Arrays.asList(new Event())))).thenReturn(projectBuildVos);

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
        //when(onlineServiceUnderTest.pipelineProfileService.findOne("id")).thenReturn(pipelineProfile);

        // Configure ReleaseProjectService.findByReleaseIdAndProjectId(...).
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
        //when(onlineServiceUnderTest.releaseProjectService.findByReleaseIdAndProjectId("releaseId", "projectId")).thenReturn(releaseProject);

        // Configure UserService.findOne(...).
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
        //when(onlineServiceUnderTest.userService.findOne("id")).thenReturn(user);

        // Configure TeamServiceImpl.findByName(...).
        final Team team = new Team();
        final TeamResource teamResource = new TeamResource();
        teamResource.setJenkinsUrl("jenkinsUrl");
        teamResource.setJenkinsWorkPath("jenkinsWorkPath");
        teamResource.setJenkinsWorkType("jenkinsWorkType");
        teamResource.setSonarUrl("sonarUrl");
        teamResource.setId("id");
        teamResource.setTeamId("teamId");
        teamResource.setTeamName("teamName");
        teamResource.setJenkinsK8sSupport(false);
        teamResource.setReferenceSource("referenceSource");
        teamResource.setK8sYmlPath("k8sYmlPath");
        team.setTeamResource(teamResource);
        final TeamThreshold teamThreshold = new TeamThreshold();
        teamThreshold.setId("id");
        teamThreshold.setTeamId("teamId");
        teamThreshold.setThreSholdType(ThresholdType.bug_blocker);
        teamThreshold.setBlockerId("blockerId");
        teamThreshold.setInputTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        teamThreshold.setUpdateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        teamThreshold.setRemark1("remark1");
        teamThreshold.setRemark2("remark2");
        teamThreshold.setRemark3("remark3");
        teamThreshold.setActionScope(ActionScope.CODE_PUSH);
        team.setTeamThresholds(new HashSet<>(Arrays.asList(teamThreshold)));
        team.setName("name");
        team.setId("id");
        final User user1 = new User();
        user1.setId("id");
        user1.setName("name");
        user1.setEmail("email");
        user1.setPhone("phone");
        user1.setRoles(Arrays.asList(new Role(RoleEnum.developer)));
        user1.setPassword("password");
        user1.setDepartment("department");
        user1.setTitle("title");
        user1.setEmployeeId(0);
        team.setMembers(Arrays.asList(user1));
        team.setOwnerId("ownerId");
        team.setCode("code");
        final User user2 = new User();
        user2.setId("id");
        user2.setName("name");
        user2.setEmail("email");
        user2.setPhone("phone");
        user2.setRoles(Arrays.asList(new Role(RoleEnum.developer)));
        user2.setPassword("password");
        user2.setDepartment("department");
        user2.setTitle("title");
        user2.setEmployeeId(0);
        team.setConfigers(Arrays.asList(user2));
        team.setDevopsTeamId("devopsTeamId");
        final Guard guard = new Guard();
        guard.setId("id");
        guard.setThresholdMin(0.0);
        guard.setThresholdMax(0.0);
        guard.setGuardType(ThresholdType.bug_blocker);
        guard.setName("name");
        guard.setLevel("level");
        team.setGuards(Arrays.asList(guard));
        //when(onlineServiceUnderTest.teamService.findByName("name")).thenReturn(team);

        // Run the test
        final Map<Project, List<ProjectBuildVo>> result = onlineServiceUnderTest.getApplyOnlineProjects(online);

        // Verify the results
        //verify(onlineServiceUnderTest.sendRabbitMq).sendReleaseStatusMq("issuesIds", IssuesStatus.toBeClaimed);
    }

    @Test
    void testGetApplyOnlineProjects_RedmineServiceThrowsException() throws Exception {
        // Setup
        final Online online = new Online();
        online.setBatchDdvice("batchDdvice");
        online.setReleaseCode("releaseCode");
        online.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        online.setTitle("title");
        online.setId("id");
        online.setCreateDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        online.setSprintId("sprintId");
        online.setReleaseDetail("releaseDetail");
        online.setReleaseUserName("releaseUserName");
        online.setBatchStatus(Status.NOT_EXECUTED);

        // Configure ReleaseService.findOne(...).
        final Release release = new Release();
        release.setBatchDdvice("batchDdvice");
        release.setReleaseCode("releaseCode");
        release.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        release.setTitle("title");
        release.setId("id");
        release.setImageTag("imageTag");
        release.setCreateDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        release.setSprintId("sprintId");
        release.setIssuesId("issuesId");
        release.setReleaseDetail("releaseDetail");
        //when(onlineServiceUnderTest.releaseService.findOne("id")).thenReturn(release);

        //when(onlineServiceUnderTest.redmineService.getSprintsById("springId")).thenThrow(Exception.class);

        // Configure EventService.findByTeamIdAndEventType(...).
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
        //when(onlineServiceUnderTest.eventService.findByTeamIdAndEventType("teamId", EventType.AUTO_DEPLOYMENT)).thenReturn(events);

        // Configure EventService.getPipelineProfile(...).
        final ProjectBuildVo projectBuildVo = new ProjectBuildVo();
        projectBuildVo.setNameSpace("nameSpace");
        projectBuildVo.setProfile("profile");
        projectBuildVo.setProjects(Arrays.asList("value"));
        projectBuildVo.setArriveTagName("arriveTagName");
        projectBuildVo.setRolloutVersion("rolloutVersion");
        projectBuildVo.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        projectBuildVo.setDbUrl("dbUrl");
        projectBuildVo.setDbUserName("dbUserName");
        projectBuildVo.setDbPassword("dbPassword");
        projectBuildVo.setDockerImageTag("dockerImageTag");
        final List<ProjectBuildVo> projectBuildVos = Arrays.asList(projectBuildVo);
        //when(onlineServiceUnderTest.eventService.getPipelineProfile(any(Project.class), eq(Arrays.asList(new Event())))).thenReturn(projectBuildVos);

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
        //when(onlineServiceUnderTest.pipelineProfileService.findOne("id")).thenReturn(pipelineProfile);

        // Configure ReleaseProjectService.findByReleaseIdAndProjectId(...).
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
        //when(onlineServiceUnderTest.releaseProjectService.findByReleaseIdAndProjectId("releaseId", "projectId")).thenReturn(releaseProject);

        // Configure UserService.findOne(...).
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
        //when(onlineServiceUnderTest.userService.findOne("id")).thenReturn(user);

        // Configure TeamServiceImpl.findByName(...).
        final Team team = new Team();
        final TeamResource teamResource = new TeamResource();
        teamResource.setJenkinsUrl("jenkinsUrl");
        teamResource.setJenkinsWorkPath("jenkinsWorkPath");
        teamResource.setJenkinsWorkType("jenkinsWorkType");
        teamResource.setSonarUrl("sonarUrl");
        teamResource.setId("id");
        teamResource.setTeamId("teamId");
        teamResource.setTeamName("teamName");
        teamResource.setJenkinsK8sSupport(false);
        teamResource.setReferenceSource("referenceSource");
        teamResource.setK8sYmlPath("k8sYmlPath");
        team.setTeamResource(teamResource);
        final TeamThreshold teamThreshold = new TeamThreshold();
        teamThreshold.setId("id");
        teamThreshold.setTeamId("teamId");
        teamThreshold.setThreSholdType(ThresholdType.bug_blocker);
        teamThreshold.setBlockerId("blockerId");
        teamThreshold.setInputTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        teamThreshold.setUpdateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        teamThreshold.setRemark1("remark1");
        teamThreshold.setRemark2("remark2");
        teamThreshold.setRemark3("remark3");
        teamThreshold.setActionScope(ActionScope.CODE_PUSH);
        team.setTeamThresholds(new HashSet<>(Arrays.asList(teamThreshold)));
        team.setName("name");
        team.setId("id");
        final User user1 = new User();
        user1.setId("id");
        user1.setName("name");
        user1.setEmail("email");
        user1.setPhone("phone");
        user1.setRoles(Arrays.asList(new Role(RoleEnum.developer)));
        user1.setPassword("password");
        user1.setDepartment("department");
        user1.setTitle("title");
        user1.setEmployeeId(0);
        team.setMembers(Arrays.asList(user1));
        team.setOwnerId("ownerId");
        team.setCode("code");
        final User user2 = new User();
        user2.setId("id");
        user2.setName("name");
        user2.setEmail("email");
        user2.setPhone("phone");
        user2.setRoles(Arrays.asList(new Role(RoleEnum.developer)));
        user2.setPassword("password");
        user2.setDepartment("department");
        user2.setTitle("title");
        user2.setEmployeeId(0);
        team.setConfigers(Arrays.asList(user2));
        team.setDevopsTeamId("devopsTeamId");
        final Guard guard = new Guard();
        guard.setId("id");
        guard.setThresholdMin(0.0);
        guard.setThresholdMax(0.0);
        guard.setGuardType(ThresholdType.bug_blocker);
        guard.setName("name");
        guard.setLevel("level");
        team.setGuards(Arrays.asList(guard));
        //when(onlineServiceUnderTest.teamService.findByName("name")).thenReturn(team);

        // Run the test
        //assertThatThrownBy(() -> onlineServiceUnderTest.getApplyOnlineProjects(online)).isInstanceOf(Exception.class);
        //verify(onlineServiceUnderTest.sendRabbitMq).sendReleaseStatusMq("issuesIds", IssuesStatus.toBeClaimed);
    }

    @Test
    void testGetApplyOnlineProjects_EventServiceFindByTeamIdAndEventTypeReturnsNoItems() throws Exception {
        // Setup
        final Online online = new Online();
        online.setBatchDdvice("batchDdvice");
        online.setReleaseCode("releaseCode");
        online.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        online.setTitle("title");
        online.setId("id");
        online.setCreateDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        online.setSprintId("sprintId");
        online.setReleaseDetail("releaseDetail");
        online.setReleaseUserName("releaseUserName");
        online.setBatchStatus(Status.NOT_EXECUTED);

        // Configure ReleaseService.findOne(...).
        final Release release = new Release();
        release.setBatchDdvice("batchDdvice");
        release.setReleaseCode("releaseCode");
        release.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        release.setTitle("title");
        release.setId("id");
        release.setImageTag("imageTag");
        release.setCreateDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        release.setSprintId("sprintId");
        release.setIssuesId("issuesId");
        release.setReleaseDetail("releaseDetail");
        //when(onlineServiceUnderTest.releaseService.findOne("id")).thenReturn(release);

        //when(onlineServiceUnderTest.redmineService.getSprintsById("springId")).thenReturn(new JSONObject(0, false));
        //when(onlineServiceUnderTest.eventService.findByTeamIdAndEventType("teamId", EventType.AUTO_DEPLOYMENT)).thenReturn(Collections.emptyList());

        // Configure EventService.getPipelineProfile(...).
        final ProjectBuildVo projectBuildVo = new ProjectBuildVo();
        projectBuildVo.setNameSpace("nameSpace");
        projectBuildVo.setProfile("profile");
        projectBuildVo.setProjects(Arrays.asList("value"));
        projectBuildVo.setArriveTagName("arriveTagName");
        projectBuildVo.setRolloutVersion("rolloutVersion");
        projectBuildVo.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        projectBuildVo.setDbUrl("dbUrl");
        projectBuildVo.setDbUserName("dbUserName");
        projectBuildVo.setDbPassword("dbPassword");
        projectBuildVo.setDockerImageTag("dockerImageTag");
        final List<ProjectBuildVo> projectBuildVos = Arrays.asList(projectBuildVo);
        //when(onlineServiceUnderTest.eventService.getPipelineProfile(any(Project.class), eq(Arrays.asList(new Event())))).thenReturn(projectBuildVos);

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
        //when(onlineServiceUnderTest.pipelineProfileService.findOne("id")).thenReturn(pipelineProfile);

        // Configure ReleaseProjectService.findByReleaseIdAndProjectId(...).
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
        //when(onlineServiceUnderTest.releaseProjectService.findByReleaseIdAndProjectId("releaseId", "projectId")).thenReturn(releaseProject);

        // Configure UserService.findOne(...).
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
        //when(onlineServiceUnderTest.userService.findOne("id")).thenReturn(user);

        // Configure TeamServiceImpl.findByName(...).
        final Team team = new Team();
        final TeamResource teamResource = new TeamResource();
        teamResource.setJenkinsUrl("jenkinsUrl");
        teamResource.setJenkinsWorkPath("jenkinsWorkPath");
        teamResource.setJenkinsWorkType("jenkinsWorkType");
        teamResource.setSonarUrl("sonarUrl");
        teamResource.setId("id");
        teamResource.setTeamId("teamId");
        teamResource.setTeamName("teamName");
        teamResource.setJenkinsK8sSupport(false);
        teamResource.setReferenceSource("referenceSource");
        teamResource.setK8sYmlPath("k8sYmlPath");
        team.setTeamResource(teamResource);
        final TeamThreshold teamThreshold = new TeamThreshold();
        teamThreshold.setId("id");
        teamThreshold.setTeamId("teamId");
        teamThreshold.setThreSholdType(ThresholdType.bug_blocker);
        teamThreshold.setBlockerId("blockerId");
        teamThreshold.setInputTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        teamThreshold.setUpdateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        teamThreshold.setRemark1("remark1");
        teamThreshold.setRemark2("remark2");
        teamThreshold.setRemark3("remark3");
        teamThreshold.setActionScope(ActionScope.CODE_PUSH);
        team.setTeamThresholds(new HashSet<>(Arrays.asList(teamThreshold)));
        team.setName("name");
        team.setId("id");
        final User user1 = new User();
        user1.setId("id");
        user1.setName("name");
        user1.setEmail("email");
        user1.setPhone("phone");
        user1.setRoles(Arrays.asList(new Role(RoleEnum.developer)));
        user1.setPassword("password");
        user1.setDepartment("department");
        user1.setTitle("title");
        user1.setEmployeeId(0);
        team.setMembers(Arrays.asList(user1));
        team.setOwnerId("ownerId");
        team.setCode("code");
        final User user2 = new User();
        user2.setId("id");
        user2.setName("name");
        user2.setEmail("email");
        user2.setPhone("phone");
        user2.setRoles(Arrays.asList(new Role(RoleEnum.developer)));
        user2.setPassword("password");
        user2.setDepartment("department");
        user2.setTitle("title");
        user2.setEmployeeId(0);
        team.setConfigers(Arrays.asList(user2));
        team.setDevopsTeamId("devopsTeamId");
        final Guard guard = new Guard();
        guard.setId("id");
        guard.setThresholdMin(0.0);
        guard.setThresholdMax(0.0);
        guard.setGuardType(ThresholdType.bug_blocker);
        guard.setName("name");
        guard.setLevel("level");
        team.setGuards(Arrays.asList(guard));
        //when(onlineServiceUnderTest.teamService.findByName("name")).thenReturn(team);

        // Run the test
        final Map<Project, List<ProjectBuildVo>> result = onlineServiceUnderTest.getApplyOnlineProjects(online);

        // Verify the results
        //verify(onlineServiceUnderTest.sendRabbitMq).sendReleaseStatusMq("issuesIds", IssuesStatus.toBeClaimed);
    }

    @Test
    void testGetApplyOnlineProjects_EventServiceGetPipelineProfileReturnsNoItems() throws Exception {
        // Setup
        final Online online = new Online();
        online.setBatchDdvice("batchDdvice");
        online.setReleaseCode("releaseCode");
        online.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        online.setTitle("title");
        online.setId("id");
        online.setCreateDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        online.setSprintId("sprintId");
        online.setReleaseDetail("releaseDetail");
        online.setReleaseUserName("releaseUserName");
        online.setBatchStatus(Status.NOT_EXECUTED);

        // Configure ReleaseService.findOne(...).
        final Release release = new Release();
        release.setBatchDdvice("batchDdvice");
        release.setReleaseCode("releaseCode");
        release.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        release.setTitle("title");
        release.setId("id");
        release.setImageTag("imageTag");
        release.setCreateDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        release.setSprintId("sprintId");
        release.setIssuesId("issuesId");
        release.setReleaseDetail("releaseDetail");
        //when(onlineServiceUnderTest.releaseService.findOne("id")).thenReturn(release);

        //when(onlineServiceUnderTest.redmineService.getSprintsById("springId")).thenReturn(new JSONObject(0, false));

        // Configure EventService.findByTeamIdAndEventType(...).
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
        //when(onlineServiceUnderTest.eventService.findByTeamIdAndEventType("teamId", EventType.AUTO_DEPLOYMENT)).thenReturn(events);

        //when(onlineServiceUnderTest.eventService.getPipelineProfile(any(Project.class), eq(Arrays.asList(new Event())))).thenReturn(Collections.emptyList());

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
        //when(onlineServiceUnderTest.pipelineProfileService.findOne("id")).thenReturn(pipelineProfile);

        // Configure ReleaseProjectService.findByReleaseIdAndProjectId(...).
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
        //when(onlineServiceUnderTest.releaseProjectService.findByReleaseIdAndProjectId("releaseId", "projectId")).thenReturn(releaseProject);

        // Configure UserService.findOne(...).
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
        //when(onlineServiceUnderTest.userService.findOne("id")).thenReturn(user);

        // Configure TeamServiceImpl.findByName(...).
        final Team team = new Team();
        final TeamResource teamResource = new TeamResource();
        teamResource.setJenkinsUrl("jenkinsUrl");
        teamResource.setJenkinsWorkPath("jenkinsWorkPath");
        teamResource.setJenkinsWorkType("jenkinsWorkType");
        teamResource.setSonarUrl("sonarUrl");
        teamResource.setId("id");
        teamResource.setTeamId("teamId");
        teamResource.setTeamName("teamName");
        teamResource.setJenkinsK8sSupport(false);
        teamResource.setReferenceSource("referenceSource");
        teamResource.setK8sYmlPath("k8sYmlPath");
        team.setTeamResource(teamResource);
        final TeamThreshold teamThreshold = new TeamThreshold();
        teamThreshold.setId("id");
        teamThreshold.setTeamId("teamId");
        teamThreshold.setThreSholdType(ThresholdType.bug_blocker);
        teamThreshold.setBlockerId("blockerId");
        teamThreshold.setInputTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        teamThreshold.setUpdateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        teamThreshold.setRemark1("remark1");
        teamThreshold.setRemark2("remark2");
        teamThreshold.setRemark3("remark3");
        teamThreshold.setActionScope(ActionScope.CODE_PUSH);
        team.setTeamThresholds(new HashSet<>(Arrays.asList(teamThreshold)));
        team.setName("name");
        team.setId("id");
        final User user1 = new User();
        user1.setId("id");
        user1.setName("name");
        user1.setEmail("email");
        user1.setPhone("phone");
        user1.setRoles(Arrays.asList(new Role(RoleEnum.developer)));
        user1.setPassword("password");
        user1.setDepartment("department");
        user1.setTitle("title");
        user1.setEmployeeId(0);
        team.setMembers(Arrays.asList(user1));
        team.setOwnerId("ownerId");
        team.setCode("code");
        final User user2 = new User();
        user2.setId("id");
        user2.setName("name");
        user2.setEmail("email");
        user2.setPhone("phone");
        user2.setRoles(Arrays.asList(new Role(RoleEnum.developer)));
        user2.setPassword("password");
        user2.setDepartment("department");
        user2.setTitle("title");
        user2.setEmployeeId(0);
        team.setConfigers(Arrays.asList(user2));
        team.setDevopsTeamId("devopsTeamId");
        final Guard guard = new Guard();
        guard.setId("id");
        guard.setThresholdMin(0.0);
        guard.setThresholdMax(0.0);
        guard.setGuardType(ThresholdType.bug_blocker);
        guard.setName("name");
        guard.setLevel("level");
        team.setGuards(Arrays.asList(guard));
        //when(onlineServiceUnderTest.teamService.findByName("name")).thenReturn(team);

        // Run the test
        final Map<Project, List<ProjectBuildVo>> result = onlineServiceUnderTest.getApplyOnlineProjects(online);

        // Verify the results
        //verify(onlineServiceUnderTest.sendRabbitMq).sendReleaseStatusMq("issuesIds", IssuesStatus.toBeClaimed);
    }

    @Test
    void testGetApplyOnlineProjects_EventServiceGetPipelineProfileThrowsException() throws Exception {
        // Setup
        final Online online = new Online();
        online.setBatchDdvice("batchDdvice");
        online.setReleaseCode("releaseCode");
        online.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        online.setTitle("title");
        online.setId("id");
        online.setCreateDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        online.setSprintId("sprintId");
        online.setReleaseDetail("releaseDetail");
        online.setReleaseUserName("releaseUserName");
        online.setBatchStatus(Status.NOT_EXECUTED);

        // Configure ReleaseService.findOne(...).
        final Release release = new Release();
        release.setBatchDdvice("batchDdvice");
        release.setReleaseCode("releaseCode");
        release.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        release.setTitle("title");
        release.setId("id");
        release.setImageTag("imageTag");
        release.setCreateDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        release.setSprintId("sprintId");
        release.setIssuesId("issuesId");
        release.setReleaseDetail("releaseDetail");
        //when(onlineServiceUnderTest.releaseService.findOne("id")).thenReturn(release);

        //when(onlineServiceUnderTest.redmineService.getSprintsById("springId")).thenReturn(new JSONObject(0, false));

        // Configure EventService.findByTeamIdAndEventType(...).
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
        //when(onlineServiceUnderTest.eventService.findByTeamIdAndEventType("teamId", EventType.AUTO_DEPLOYMENT)).thenReturn(events);

        //when(onlineServiceUnderTest.eventService.getPipelineProfile(any(Project.class), eq(Arrays.asList(new Event())))).thenThrow(Exception.class);

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
        //when(onlineServiceUnderTest.pipelineProfileService.findOne("id")).thenReturn(pipelineProfile);

        // Configure ReleaseProjectService.findByReleaseIdAndProjectId(...).
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
        //when(onlineServiceUnderTest.releaseProjectService.findByReleaseIdAndProjectId("releaseId", "projectId")).thenReturn(releaseProject);

        // Configure UserService.findOne(...).
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
        //when(onlineServiceUnderTest.userService.findOne("id")).thenReturn(user);

        // Configure TeamServiceImpl.findByName(...).
        final Team team = new Team();
        final TeamResource teamResource = new TeamResource();
        teamResource.setJenkinsUrl("jenkinsUrl");
        teamResource.setJenkinsWorkPath("jenkinsWorkPath");
        teamResource.setJenkinsWorkType("jenkinsWorkType");
        teamResource.setSonarUrl("sonarUrl");
        teamResource.setId("id");
        teamResource.setTeamId("teamId");
        teamResource.setTeamName("teamName");
        teamResource.setJenkinsK8sSupport(false);
        teamResource.setReferenceSource("referenceSource");
        teamResource.setK8sYmlPath("k8sYmlPath");
        team.setTeamResource(teamResource);
        final TeamThreshold teamThreshold = new TeamThreshold();
        teamThreshold.setId("id");
        teamThreshold.setTeamId("teamId");
        teamThreshold.setThreSholdType(ThresholdType.bug_blocker);
        teamThreshold.setBlockerId("blockerId");
        teamThreshold.setInputTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        teamThreshold.setUpdateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        teamThreshold.setRemark1("remark1");
        teamThreshold.setRemark2("remark2");
        teamThreshold.setRemark3("remark3");
        teamThreshold.setActionScope(ActionScope.CODE_PUSH);
        team.setTeamThresholds(new HashSet<>(Arrays.asList(teamThreshold)));
        team.setName("name");
        team.setId("id");
        final User user1 = new User();
        user1.setId("id");
        user1.setName("name");
        user1.setEmail("email");
        user1.setPhone("phone");
        user1.setRoles(Arrays.asList(new Role(RoleEnum.developer)));
        user1.setPassword("password");
        user1.setDepartment("department");
        user1.setTitle("title");
        user1.setEmployeeId(0);
        team.setMembers(Arrays.asList(user1));
        team.setOwnerId("ownerId");
        team.setCode("code");
        final User user2 = new User();
        user2.setId("id");
        user2.setName("name");
        user2.setEmail("email");
        user2.setPhone("phone");
        user2.setRoles(Arrays.asList(new Role(RoleEnum.developer)));
        user2.setPassword("password");
        user2.setDepartment("department");
        user2.setTitle("title");
        user2.setEmployeeId(0);
        team.setConfigers(Arrays.asList(user2));
        team.setDevopsTeamId("devopsTeamId");
        final Guard guard = new Guard();
        guard.setId("id");
        guard.setThresholdMin(0.0);
        guard.setThresholdMax(0.0);
        guard.setGuardType(ThresholdType.bug_blocker);
        guard.setName("name");
        guard.setLevel("level");
        team.setGuards(Arrays.asList(guard));
        //when(onlineServiceUnderTest.teamService.findByName("name")).thenReturn(team);

        // Run the test
        //assertThatThrownBy(() -> onlineServiceUnderTest.getApplyOnlineProjects(online)).isInstanceOf(Exception.class);
        //verify(onlineServiceUnderTest.sendRabbitMq).sendReleaseStatusMq("issuesIds", IssuesStatus.toBeClaimed);
    }

    @Test
    void testGetOnlineTags() {
        // Setup
        // Run the test
        final List<Online> result = onlineServiceUnderTest.getOnlineTags();

        // Verify the results
    }

    @Test
    void testSendOnLineMail() throws Exception {
        // Setup
        final Online online = new Online();
        online.setBatchDdvice("batchDdvice");
        online.setReleaseCode("releaseCode");
        online.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        online.setTitle("title");
        online.setId("id");
        online.setCreateDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        online.setSprintId("sprintId");
        online.setReleaseDetail("releaseDetail");
        online.setReleaseUserName("releaseUserName");
        online.setBatchStatus(Status.NOT_EXECUTED);

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

        // Configure Configuration.getTemplate(...).
        final Template template = new Template("name", new StringReader("content"), new Configuration(new Version(0, 0, 0, "extraInfo", false, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())));
        //when(onlineServiceUnderTest.configuration.getTemplate("name")).thenReturn(template);

        // Configure UserService.findOne(...).
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
        //when(onlineServiceUnderTest.userService.findOne("id")).thenReturn(user);

        // Run the test
        onlineServiceUnderTest.sendOnLineMail(online, projects);

        // Verify the results
    }

    @Test
    void testSendOnLineMail_ConfigurationThrowsTemplateNotFoundException() throws Exception {
        // Setup
        final Online online = new Online();
        online.setBatchDdvice("batchDdvice");
        online.setReleaseCode("releaseCode");
        online.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        online.setTitle("title");
        online.setId("id");
        online.setCreateDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        online.setSprintId("sprintId");
        online.setReleaseDetail("releaseDetail");
        online.setReleaseUserName("releaseUserName");
        online.setBatchStatus(Status.NOT_EXECUTED);

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
        //when(onlineServiceUnderTest.configuration.getTemplate("name")).thenThrow(TemplateNotFoundException.class);

        // Configure UserService.findOne(...).
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
        //when(onlineServiceUnderTest.userService.findOne("id")).thenReturn(user);

        // Run the test
        //assertThatThrownBy(() -> onlineServiceUnderTest.sendOnLineMail(online, projects)).isInstanceOf(TemplateNotFoundException.class);
    }

    @Test
    void testSendOnLineMail_ConfigurationThrowsMalformedTemplateNameException() throws Exception {
        // Setup
        final Online online = new Online();
        online.setBatchDdvice("batchDdvice");
        online.setReleaseCode("releaseCode");
        online.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        online.setTitle("title");
        online.setId("id");
        online.setCreateDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        online.setSprintId("sprintId");
        online.setReleaseDetail("releaseDetail");
        online.setReleaseUserName("releaseUserName");
        online.setBatchStatus(Status.NOT_EXECUTED);

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
        //when(onlineServiceUnderTest.configuration.getTemplate("name")).thenThrow(MalformedTemplateNameException.class);

        // Configure UserService.findOne(...).
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
        //when(onlineServiceUnderTest.userService.findOne("id")).thenReturn(user);

        // Run the test
        //assertThatThrownBy(() -> onlineServiceUnderTest.sendOnLineMail(online, projects)).isInstanceOf(MalformedTemplateNameException.class);
    }

    @Test
    void testSendOnLineMail_ConfigurationThrowsParseException() throws Exception {
        // Setup
        final Online online = new Online();
        online.setBatchDdvice("batchDdvice");
        online.setReleaseCode("releaseCode");
        online.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        online.setTitle("title");
        online.setId("id");
        online.setCreateDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        online.setSprintId("sprintId");
        online.setReleaseDetail("releaseDetail");
        online.setReleaseUserName("releaseUserName");
        online.setBatchStatus(Status.NOT_EXECUTED);

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
        //when(onlineServiceUnderTest.configuration.getTemplate("name")).thenThrow(ParseException.class);

        // Configure UserService.findOne(...).
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
        //when(onlineServiceUnderTest.userService.findOne("id")).thenReturn(user);

        // Run the test
        //assertThatThrownBy(() -> onlineServiceUnderTest.sendOnLineMail(online, projects)).isInstanceOf(ParseException.class);
    }

    @Test
    void testSendOnLineMail_ConfigurationThrowsIOException() throws Exception {
        // Setup
        final Online online = new Online();
        online.setBatchDdvice("batchDdvice");
        online.setReleaseCode("releaseCode");
        online.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        online.setTitle("title");
        online.setId("id");
        online.setCreateDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        online.setSprintId("sprintId");
        online.setReleaseDetail("releaseDetail");
        online.setReleaseUserName("releaseUserName");
        online.setBatchStatus(Status.NOT_EXECUTED);

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
        //when(onlineServiceUnderTest.configuration.getTemplate("name")).thenThrow(IOException.class);

        // Configure UserService.findOne(...).
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
        //when(onlineServiceUnderTest.userService.findOne("id")).thenReturn(user);

        // Run the test
        //assertThatThrownBy(() -> onlineServiceUnderTest.sendOnLineMail(online, projects)).isInstanceOf(IOException.class);
    }

    @Test
    void testUpdateOnline() throws Exception {
        // Setup
        final Map<Project, List<ProjectBuildVo>> projectProfileMap = new HashMap<>();

        // Configure PipelineServiceImpl.triggerPipeline(...).
        final Pipeline pipeline = new Pipeline();
        pipeline.setProfileId("profileId");
        pipeline.setRunBranch("runBranch");
        pipeline.setProfileName("profileName");
        pipeline.setId("id");
        pipeline.setName("name");
        pipeline.setStartTimeMillis(0L);
        pipeline.setEndTimeMillis(0L);
        pipeline.setDurationMillis(0L);
        final Stage stage = new Stage();
        stage.setId("id");
        stage.setName("name");
        stage.setStatus("status");
        stage.setStartTimeMillis(0L);
        stage.setDurationMillis(0L);
        pipeline.setStages(Arrays.asList(stage));
        pipeline.setStatus(com.eazybuilder.ci.entity.report.Status.NOT_EXECUTED);
        //when(onlineServiceUnderTest.pipelineService.triggerPipeline(eq("projectId"), any(ProjectBuildVo.class))).thenReturn(pipeline);

        // Run the test
        onlineServiceUnderTest.updateOnline(projectProfileMap);

        // Verify the results
    }

    @Test
    void testUpdateOnline_PipelineServiceImplThrowsException() throws Exception {
        // Setup
        final Map<Project, List<ProjectBuildVo>> projectProfileMap = new HashMap<>();
        //when(onlineServiceUnderTest.pipelineService.triggerPipeline(eq("projectId"), any(ProjectBuildVo.class))).thenThrow(Exception.class);

        // Run the test
        onlineServiceUnderTest.updateOnline(projectProfileMap);

        // Verify the results
    }

    @Test
    void testSave() {
        // Setup
        final Online entity = new Online();
        entity.setBatchDdvice("batchDdvice");
        entity.setReleaseCode("releaseCode");
        entity.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        entity.setTitle("title");
        entity.setId("id");
        entity.setCreateDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        entity.setSprintId("sprintId");
        entity.setReleaseDetail("releaseDetail");
        entity.setReleaseUserName("releaseUserName");
        entity.setBatchStatus(Status.NOT_EXECUTED);

        // Configure ReleaseService.findOne(...).
        final Release release = new Release();
        release.setBatchDdvice("batchDdvice");
        release.setReleaseCode("releaseCode");
        release.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        release.setTitle("title");
        release.setId("id");
        release.setImageTag("imageTag");
        release.setCreateDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        release.setSprintId("sprintId");
        release.setIssuesId("issuesId");
        release.setReleaseDetail("releaseDetail");
        //when(onlineServiceUnderTest.releaseService.findOne("id")).thenReturn(release);

        // Configure UserService.findOne(...).
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
        //when(onlineServiceUnderTest.userService.findOne("id")).thenReturn(user);

        // Configure TeamServiceImpl.findByName(...).
        final Team team = new Team();
        final TeamResource teamResource = new TeamResource();
        teamResource.setJenkinsUrl("jenkinsUrl");
        teamResource.setJenkinsWorkPath("jenkinsWorkPath");
        teamResource.setJenkinsWorkType("jenkinsWorkType");
        teamResource.setSonarUrl("sonarUrl");
        teamResource.setId("id");
        teamResource.setTeamId("teamId");
        teamResource.setTeamName("teamName");
        teamResource.setJenkinsK8sSupport(false);
        teamResource.setReferenceSource("referenceSource");
        teamResource.setK8sYmlPath("k8sYmlPath");
        team.setTeamResource(teamResource);
        final TeamThreshold teamThreshold = new TeamThreshold();
        teamThreshold.setId("id");
        teamThreshold.setTeamId("teamId");
        teamThreshold.setThreSholdType(ThresholdType.bug_blocker);
        teamThreshold.setBlockerId("blockerId");
        teamThreshold.setInputTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        teamThreshold.setUpdateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        teamThreshold.setRemark1("remark1");
        teamThreshold.setRemark2("remark2");
        teamThreshold.setRemark3("remark3");
        teamThreshold.setActionScope(ActionScope.CODE_PUSH);
        team.setTeamThresholds(new HashSet<>(Arrays.asList(teamThreshold)));
        team.setName("name");
        team.setId("id");
        final User user1 = new User();
        user1.setId("id");
        user1.setName("name");
        user1.setEmail("email");
        user1.setPhone("phone");
        user1.setRoles(Arrays.asList(new Role(RoleEnum.developer)));
        user1.setPassword("password");
        user1.setDepartment("department");
        user1.setTitle("title");
        user1.setEmployeeId(0);
        team.setMembers(Arrays.asList(user1));
        team.setOwnerId("ownerId");
        team.setCode("code");
        final User user2 = new User();
        user2.setId("id");
        user2.setName("name");
        user2.setEmail("email");
        user2.setPhone("phone");
        user2.setRoles(Arrays.asList(new Role(RoleEnum.developer)));
        user2.setPassword("password");
        user2.setDepartment("department");
        user2.setTitle("title");
        user2.setEmployeeId(0);
        team.setConfigers(Arrays.asList(user2));
        team.setDevopsTeamId("devopsTeamId");
        final Guard guard = new Guard();
        guard.setId("id");
        guard.setThresholdMin(0.0);
        guard.setThresholdMax(0.0);
        guard.setGuardType(ThresholdType.bug_blocker);
        guard.setName("name");
        guard.setLevel("level");
        team.setGuards(Arrays.asList(guard));
        //when(onlineServiceUnderTest.teamService.findByName("name")).thenReturn(team);

        // Run the test
        onlineServiceUnderTest.save(entity);

        // Verify the results
        //verify(onlineServiceUnderTest.sendRabbitMq).sendReleaseStatusMq("issuesIds", IssuesStatus.toBeClaimed);
    }

    @Test
    void testSaveJobOnlineEntity() throws Exception {
        // Setup
        final Online entity = new Online();
        entity.setBatchDdvice("batchDdvice");
        entity.setReleaseCode("releaseCode");
        entity.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        entity.setTitle("title");
        entity.setId("id");
        entity.setCreateDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        entity.setSprintId("sprintId");
        entity.setReleaseDetail("releaseDetail");
        entity.setReleaseUserName("releaseUserName");
        entity.setBatchStatus(Status.NOT_EXECUTED);

        final Release release = new Release();
        release.setBatchDdvice("batchDdvice");
        release.setReleaseCode("releaseCode");
        release.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        release.setTitle("title");
        release.setId("id");
        release.setImageTag("imageTag");
        release.setCreateDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        release.setSprintId("sprintId");
        release.setIssuesId("issuesId");
        release.setReleaseDetail("releaseDetail");

        // Configure PipelineProfileService.findByName(...).
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
        //when(onlineServiceUnderTest.pipelineProfileService.findByName("name")).thenReturn(pipelineProfile);

        //when(onlineServiceUnderTest.propService.getValue("key", "defaultVal")).thenReturn("result");

        // Configure RedmineService.getRedmineTeams(...).
        final RedmineProject redmineProject = new RedmineProject();
        redmineProject.setId(0);
        redmineProject.setCreateOn(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        redmineProject.setDescription("description");
        redmineProject.setHomePage("homePage");
        redmineProject.setIdentifier("identifier");
        redmineProject.setInheritMembers(false);
        redmineProject.setName("name");
        redmineProject.setParentId(0);
        redmineProject.setStatus(0);
        redmineProject.setUpdateOn(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        final List<RedmineProject> redmineProjects = Arrays.asList(redmineProject);
        //when(onlineServiceUnderTest.redmineService.getRedmineTeams()).thenReturn(redmineProjects);

        // Configure TeamServiceImpl.findByName(...).
        final Team team = new Team();
        final TeamResource teamResource = new TeamResource();
        teamResource.setJenkinsUrl("jenkinsUrl");
        teamResource.setJenkinsWorkPath("jenkinsWorkPath");
        teamResource.setJenkinsWorkType("jenkinsWorkType");
        teamResource.setSonarUrl("sonarUrl");
        teamResource.setId("id");
        teamResource.setTeamId("teamId");
        teamResource.setTeamName("teamName");
        teamResource.setJenkinsK8sSupport(false);
        teamResource.setReferenceSource("referenceSource");
        teamResource.setK8sYmlPath("k8sYmlPath");
        team.setTeamResource(teamResource);
        final TeamThreshold teamThreshold = new TeamThreshold();
        teamThreshold.setId("id");
        teamThreshold.setTeamId("teamId");
        teamThreshold.setThreSholdType(ThresholdType.bug_blocker);
        teamThreshold.setBlockerId("blockerId");
        teamThreshold.setInputTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        teamThreshold.setUpdateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        teamThreshold.setRemark1("remark1");
        teamThreshold.setRemark2("remark2");
        teamThreshold.setRemark3("remark3");
        teamThreshold.setActionScope(ActionScope.CODE_PUSH);
        team.setTeamThresholds(new HashSet<>(Arrays.asList(teamThreshold)));
        team.setName("name");
        team.setId("id");
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
        team.setMembers(Arrays.asList(user));
        team.setOwnerId("ownerId");
        team.setCode("code");
        final User user1 = new User();
        user1.setId("id");
        user1.setName("name");
        user1.setEmail("email");
        user1.setPhone("phone");
        user1.setRoles(Arrays.asList(new Role(RoleEnum.developer)));
        user1.setPassword("password");
        user1.setDepartment("department");
        user1.setTitle("title");
        user1.setEmployeeId(0);
        team.setConfigers(Arrays.asList(user1));
        team.setDevopsTeamId("devopsTeamId");
        final Guard guard = new Guard();
        guard.setId("id");
        guard.setThresholdMin(0.0);
        guard.setThresholdMax(0.0);
        guard.setGuardType(ThresholdType.bug_blocker);
        guard.setName("name");
        guard.setLevel("level");
        team.setGuards(Arrays.asList(guard));
        //when(onlineServiceUnderTest.teamService.findByName("name")).thenReturn(team);

        // Run the test
        onlineServiceUnderTest.saveJobOnlineEntity(entity, release);

        // Verify the results
        //verify(onlineServiceUnderTest.buildJobService).save(any(BuildJob.class));
    }

    @Test
    void testSaveJobOnlineEntity_RedmineServiceReturnsNoItems() throws Exception {
        // Setup
        final Online entity = new Online();
        entity.setBatchDdvice("batchDdvice");
        entity.setReleaseCode("releaseCode");
        entity.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        entity.setTitle("title");
        entity.setId("id");
        entity.setCreateDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        entity.setSprintId("sprintId");
        entity.setReleaseDetail("releaseDetail");
        entity.setReleaseUserName("releaseUserName");
        entity.setBatchStatus(Status.NOT_EXECUTED);

        final Release release = new Release();
        release.setBatchDdvice("batchDdvice");
        release.setReleaseCode("releaseCode");
        release.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        release.setTitle("title");
        release.setId("id");
        release.setImageTag("imageTag");
        release.setCreateDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        release.setSprintId("sprintId");
        release.setIssuesId("issuesId");
        release.setReleaseDetail("releaseDetail");

        // Configure PipelineProfileService.findByName(...).
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
        //when(onlineServiceUnderTest.pipelineProfileService.findByName("name")).thenReturn(pipelineProfile);

        //when(onlineServiceUnderTest.propService.getValue("key", "defaultVal")).thenReturn("result");
        //when(onlineServiceUnderTest.redmineService.getRedmineTeams()).thenReturn(Collections.emptyList());

        // Configure TeamServiceImpl.findByName(...).
        final Team team = new Team();
        final TeamResource teamResource = new TeamResource();
        teamResource.setJenkinsUrl("jenkinsUrl");
        teamResource.setJenkinsWorkPath("jenkinsWorkPath");
        teamResource.setJenkinsWorkType("jenkinsWorkType");
        teamResource.setSonarUrl("sonarUrl");
        teamResource.setId("id");
        teamResource.setTeamId("teamId");
        teamResource.setTeamName("teamName");
        teamResource.setJenkinsK8sSupport(false);
        teamResource.setReferenceSource("referenceSource");
        teamResource.setK8sYmlPath("k8sYmlPath");
        team.setTeamResource(teamResource);
        final TeamThreshold teamThreshold = new TeamThreshold();
        teamThreshold.setId("id");
        teamThreshold.setTeamId("teamId");
        teamThreshold.setThreSholdType(ThresholdType.bug_blocker);
        teamThreshold.setBlockerId("blockerId");
        teamThreshold.setInputTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        teamThreshold.setUpdateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        teamThreshold.setRemark1("remark1");
        teamThreshold.setRemark2("remark2");
        teamThreshold.setRemark3("remark3");
        teamThreshold.setActionScope(ActionScope.CODE_PUSH);
        team.setTeamThresholds(new HashSet<>(Arrays.asList(teamThreshold)));
        team.setName("name");
        team.setId("id");
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
        team.setMembers(Arrays.asList(user));
        team.setOwnerId("ownerId");
        team.setCode("code");
        final User user1 = new User();
        user1.setId("id");
        user1.setName("name");
        user1.setEmail("email");
        user1.setPhone("phone");
        user1.setRoles(Arrays.asList(new Role(RoleEnum.developer)));
        user1.setPassword("password");
        user1.setDepartment("department");
        user1.setTitle("title");
        user1.setEmployeeId(0);
        team.setConfigers(Arrays.asList(user1));
        team.setDevopsTeamId("devopsTeamId");
        final Guard guard = new Guard();
        guard.setId("id");
        guard.setThresholdMin(0.0);
        guard.setThresholdMax(0.0);
        guard.setGuardType(ThresholdType.bug_blocker);
        guard.setName("name");
        guard.setLevel("level");
        team.setGuards(Arrays.asList(guard));
        //when(onlineServiceUnderTest.teamService.findByName("name")).thenReturn(team);

        // Run the test
        onlineServiceUnderTest.saveJobOnlineEntity(entity, release);

        // Verify the results
        //verify(onlineServiceUnderTest.buildJobService).save(any(BuildJob.class));
    }

    @Test
    void testSaveJobOnlineEntity_RedmineServiceThrowsException() throws Exception {
        // Setup
        final Online entity = new Online();
        entity.setBatchDdvice("batchDdvice");
        entity.setReleaseCode("releaseCode");
        entity.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        entity.setTitle("title");
        entity.setId("id");
        entity.setCreateDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        entity.setSprintId("sprintId");
        entity.setReleaseDetail("releaseDetail");
        entity.setReleaseUserName("releaseUserName");
        entity.setBatchStatus(Status.NOT_EXECUTED);

        final Release release = new Release();
        release.setBatchDdvice("batchDdvice");
        release.setReleaseCode("releaseCode");
        release.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        release.setTitle("title");
        release.setId("id");
        release.setImageTag("imageTag");
        release.setCreateDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        release.setSprintId("sprintId");
        release.setIssuesId("issuesId");
        release.setReleaseDetail("releaseDetail");

        // Configure PipelineProfileService.findByName(...).
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
        //when(onlineServiceUnderTest.pipelineProfileService.findByName("name")).thenReturn(pipelineProfile);

        //when(onlineServiceUnderTest.propService.getValue("key", "defaultVal")).thenReturn("result");
        //when(onlineServiceUnderTest.redmineService.getRedmineTeams()).thenThrow(Exception.class);

        // Configure TeamServiceImpl.findByName(...).
        final Team team = new Team();
        final TeamResource teamResource = new TeamResource();
        teamResource.setJenkinsUrl("jenkinsUrl");
        teamResource.setJenkinsWorkPath("jenkinsWorkPath");
        teamResource.setJenkinsWorkType("jenkinsWorkType");
        teamResource.setSonarUrl("sonarUrl");
        teamResource.setId("id");
        teamResource.setTeamId("teamId");
        teamResource.setTeamName("teamName");
        teamResource.setJenkinsK8sSupport(false);
        teamResource.setReferenceSource("referenceSource");
        teamResource.setK8sYmlPath("k8sYmlPath");
        team.setTeamResource(teamResource);
        final TeamThreshold teamThreshold = new TeamThreshold();
        teamThreshold.setId("id");
        teamThreshold.setTeamId("teamId");
        teamThreshold.setThreSholdType(ThresholdType.bug_blocker);
        teamThreshold.setBlockerId("blockerId");
        teamThreshold.setInputTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        teamThreshold.setUpdateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        teamThreshold.setRemark1("remark1");
        teamThreshold.setRemark2("remark2");
        teamThreshold.setRemark3("remark3");
        teamThreshold.setActionScope(ActionScope.CODE_PUSH);
        team.setTeamThresholds(new HashSet<>(Arrays.asList(teamThreshold)));
        team.setName("name");
        team.setId("id");
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
        team.setMembers(Arrays.asList(user));
        team.setOwnerId("ownerId");
        team.setCode("code");
        final User user1 = new User();
        user1.setId("id");
        user1.setName("name");
        user1.setEmail("email");
        user1.setPhone("phone");
        user1.setRoles(Arrays.asList(new Role(RoleEnum.developer)));
        user1.setPassword("password");
        user1.setDepartment("department");
        user1.setTitle("title");
        user1.setEmployeeId(0);
        team.setConfigers(Arrays.asList(user1));
        team.setDevopsTeamId("devopsTeamId");
        final Guard guard = new Guard();
        guard.setId("id");
        guard.setThresholdMin(0.0);
        guard.setThresholdMax(0.0);
        guard.setGuardType(ThresholdType.bug_blocker);
        guard.setName("name");
        guard.setLevel("level");
        team.setGuards(Arrays.asList(guard));
        //when(onlineServiceUnderTest.teamService.findByName("name")).thenReturn(team);

        // Run the test
        //assertThatThrownBy(() -> onlineServiceUnderTest.saveJobOnlineEntity(entity, release)).isInstanceOf(Exception.class);
        //verify(onlineServiceUnderTest.buildJobService).save(any(BuildJob.class));
    }

    @Test
    void testGetJobOnlineProfile() {
        // Setup
        //when(onlineServiceUnderTest.propService.getValue("key", "defaultVal")).thenReturn("result");

        // Run the test
        final String result = onlineServiceUnderTest.getJobOnlineProfile();

        // Verify the results
        assertThat(result).isEqualTo("result");
    }

    @Test
    void testSendDingTalkAfterPassedRease() {
        // Setup
        final Online entity = new Online();
        entity.setBatchDdvice("batchDdvice");
        entity.setReleaseCode("releaseCode");
        entity.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        entity.setTitle("title");
        entity.setId("id");
        entity.setCreateDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        entity.setSprintId("sprintId");
        entity.setReleaseDetail("releaseDetail");
        entity.setReleaseUserName("releaseUserName");
        entity.setBatchStatus(Status.NOT_EXECUTED);

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
        //when(onlineServiceUnderTest.userService.findByUserName("userName")).thenReturn(users);

        // Configure TeamServiceImpl.findOne(...).
        final Team team = new Team();
        final TeamResource teamResource = new TeamResource();
        teamResource.setJenkinsUrl("jenkinsUrl");
        teamResource.setJenkinsWorkPath("jenkinsWorkPath");
        teamResource.setJenkinsWorkType("jenkinsWorkType");
        teamResource.setSonarUrl("sonarUrl");
        teamResource.setId("id");
        teamResource.setTeamId("teamId");
        teamResource.setTeamName("teamName");
        teamResource.setJenkinsK8sSupport(false);
        teamResource.setReferenceSource("referenceSource");
        teamResource.setK8sYmlPath("k8sYmlPath");
        team.setTeamResource(teamResource);
        final TeamThreshold teamThreshold = new TeamThreshold();
        teamThreshold.setId("id");
        teamThreshold.setTeamId("teamId");
        teamThreshold.setThreSholdType(ThresholdType.bug_blocker);
        teamThreshold.setBlockerId("blockerId");
        teamThreshold.setInputTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        teamThreshold.setUpdateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        teamThreshold.setRemark1("remark1");
        teamThreshold.setRemark2("remark2");
        teamThreshold.setRemark3("remark3");
        teamThreshold.setActionScope(ActionScope.CODE_PUSH);
        team.setTeamThresholds(new HashSet<>(Arrays.asList(teamThreshold)));
        team.setName("name");
        team.setId("id");
        final User user1 = new User();
        user1.setId("id");
        user1.setName("name");
        user1.setEmail("email");
        user1.setPhone("phone");
        user1.setRoles(Arrays.asList(new Role(RoleEnum.developer)));
        user1.setPassword("password");
        user1.setDepartment("department");
        user1.setTitle("title");
        user1.setEmployeeId(0);
        team.setMembers(Arrays.asList(user1));
        team.setOwnerId("ownerId");
        team.setCode("code");
        final User user2 = new User();
        user2.setId("id");
        user2.setName("name");
        user2.setEmail("email");
        user2.setPhone("phone");
        user2.setRoles(Arrays.asList(new Role(RoleEnum.developer)));
        user2.setPassword("password");
        user2.setDepartment("department");
        user2.setTitle("title");
        user2.setEmployeeId(0);
        team.setConfigers(Arrays.asList(user2));
        team.setDevopsTeamId("devopsTeamId");
        final Guard guard = new Guard();
        guard.setId("id");
        guard.setThresholdMin(0.0);
        guard.setThresholdMax(0.0);
        guard.setGuardType(ThresholdType.bug_blocker);
        guard.setName("name");
        guard.setLevel("level");
        team.setGuards(Arrays.asList(guard));
        //when(onlineServiceUnderTest.teamService.findOne("id")).thenReturn(team);

        // Run the test
        onlineServiceUnderTest.sendDingTalkAfterPassedRease(entity);

        // Verify the results
    }

    @Test
    void testSendDingTalkAfterPassedRease_UserServiceReturnsNoItems() {
        // Setup
        final Online entity = new Online();
        entity.setBatchDdvice("batchDdvice");
        entity.setReleaseCode("releaseCode");
        entity.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        entity.setTitle("title");
        entity.setId("id");
        entity.setCreateDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        entity.setSprintId("sprintId");
        entity.setReleaseDetail("releaseDetail");
        entity.setReleaseUserName("releaseUserName");
        entity.setBatchStatus(Status.NOT_EXECUTED);

        //when(onlineServiceUnderTest.userService.findByUserName("userName")).thenReturn(Collections.emptyList());

        // Configure TeamServiceImpl.findOne(...).
        final Team team = new Team();
        final TeamResource teamResource = new TeamResource();
        teamResource.setJenkinsUrl("jenkinsUrl");
        teamResource.setJenkinsWorkPath("jenkinsWorkPath");
        teamResource.setJenkinsWorkType("jenkinsWorkType");
        teamResource.setSonarUrl("sonarUrl");
        teamResource.setId("id");
        teamResource.setTeamId("teamId");
        teamResource.setTeamName("teamName");
        teamResource.setJenkinsK8sSupport(false);
        teamResource.setReferenceSource("referenceSource");
        teamResource.setK8sYmlPath("k8sYmlPath");
        team.setTeamResource(teamResource);
        final TeamThreshold teamThreshold = new TeamThreshold();
        teamThreshold.setId("id");
        teamThreshold.setTeamId("teamId");
        teamThreshold.setThreSholdType(ThresholdType.bug_blocker);
        teamThreshold.setBlockerId("blockerId");
        teamThreshold.setInputTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        teamThreshold.setUpdateTime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        teamThreshold.setRemark1("remark1");
        teamThreshold.setRemark2("remark2");
        teamThreshold.setRemark3("remark3");
        teamThreshold.setActionScope(ActionScope.CODE_PUSH);
        team.setTeamThresholds(new HashSet<>(Arrays.asList(teamThreshold)));
        team.setName("name");
        team.setId("id");
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
        team.setMembers(Arrays.asList(user));
        team.setOwnerId("ownerId");
        team.setCode("code");
        final User user1 = new User();
        user1.setId("id");
        user1.setName("name");
        user1.setEmail("email");
        user1.setPhone("phone");
        user1.setRoles(Arrays.asList(new Role(RoleEnum.developer)));
        user1.setPassword("password");
        user1.setDepartment("department");
        user1.setTitle("title");
        user1.setEmployeeId(0);
        team.setConfigers(Arrays.asList(user1));
        team.setDevopsTeamId("devopsTeamId");
        final Guard guard = new Guard();
        guard.setId("id");
        guard.setThresholdMin(0.0);
        guard.setThresholdMax(0.0);
        guard.setGuardType(ThresholdType.bug_blocker);
        guard.setName("name");
        guard.setLevel("level");
        team.setGuards(Arrays.asList(guard));
        //when(onlineServiceUnderTest.teamService.findOne("id")).thenReturn(team);

        // Run the test
        onlineServiceUnderTest.sendDingTalkAfterPassedRease(entity);

        // Verify the results
    }
}
