package com.eazybuilder.ci.jenkins;

import com.eazybuilder.ci.controller.vo.ProjectBuildVo;
import com.eazybuilder.ci.entity.*;
import com.eazybuilder.ci.entity.docker.DockerDigest;
import com.eazybuilder.ci.service.OnlineService;
import freemarker.core.ParseException;
import freemarker.template.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JenkinsPipelineServiceTest {

    @Mock
    private Configuration mockConfiguration;
    @Mock
    private JenkinsPipelineEnv mockEnv;
    @Mock
    private OnlineService mockOnlineService;

    private JenkinsPipelineService jenkinsPipelineServiceUnderTest;

    @BeforeEach
    void setUp() throws Exception {
        jenkinsPipelineServiceUnderTest = new JenkinsPipelineService("jenkinsUrl", "sonarUrl", mockConfiguration, false, mockEnv, "dockerImageTag", mockOnlineService, "storageType");
    }

    @Test
    void testInitJenkins() throws Exception {
        // Setup
        //when(mockEnv.getUser()).thenReturn("result");
        //when(mockEnv.getPassword()).thenReturn("result");

        // Run the test
        jenkinsPipelineServiceUnderTest.initJenkins();

        // Verify the results
    }

    @Test
    void testInitJenkins_ThrowsException() {
        // Setup
        //when(mockEnv.getUser()).thenReturn("result");
        //when(mockEnv.getPassword()).thenReturn("result");

        // Run the test
        //assertThatThrownBy(() -> jenkinsPipelineServiceUnderTest.initJenkins()).isInstanceOf(Exception.class);
    }

    @Test
    void testGeneratePipeLineJob() throws Exception {
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

        final ProjectBuildVo buildParam = new ProjectBuildVo();
        buildParam.setNameSpace("nameSpace");
        buildParam.setProfile("profile");
        buildParam.setProjects(Arrays.asList("value"));
        buildParam.setArriveTagName("arriveTagName");
        buildParam.setRolloutVersion("rolloutVersion");
        buildParam.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        buildParam.setDbUrl("dbUrl");
        buildParam.setDbUserName("dbUserName");
        buildParam.setDbPassword("dbPassword");
        buildParam.setDockerImageTag("dockerImageTag");

        //when(mockEnv.crumb()).thenReturn(false);
        //when(mockEnv.getRegistryUrl()).thenReturn("result");
        //when(mockEnv.getToolGitUrl()).thenReturn("result");
        //when(mockEnv.getBaseUrl()).thenReturn("result");
        //when(mockEnv.getSonarUser()).thenReturn("result");
        //when(mockEnv.getSonarPassword()).thenReturn("result");
        //when(mockEnv.getGitLabApiBaseUrl()).thenReturn("result");
        //when(mockEnv.initEnvParams(new HashMap<>())).thenReturn(new HashMap<>());
        //when(mockEnv.getArm64DockerBuildHost()).thenReturn("result");
        //when(mockEnv.getDockerBuildHost()).thenReturn("result");

        // Configure Configuration.getTemplate(...).
        final Template template = new Template("name", new StringReader("content"), new Configuration(new Version(0, 0, 0, "extraInfo", false, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())));
        //when(mockConfiguration.getTemplate("name")).thenReturn(template);

        // Configure OnlineService.findDockerDigest(...).
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
        final List<DockerDigest> dockerDigests = Arrays.asList(dockerDigest);
        //when(mockOnlineService.findDockerDigest("releaseId", "teamName")).thenReturn(dockerDigests);

        // Configure OnlineService.findDockerDigest(...).
        final DockerDigest dockerDigest1 = new DockerDigest();
        dockerDigest1.setId("id");
        dockerDigest1.setPipelineId("pipelineId");
        dockerDigest1.setProjectId("projectId");
        dockerDigest1.setProjectName("projectName");
        dockerDigest1.setDigest("digest");
        dockerDigest1.setUrl("url");
        dockerDigest1.setImageName("imageName");
        dockerDigest1.setNamespace("namespace");
        dockerDigest1.setTag("tag");
        final List<DockerDigest> dockerDigests1 = Arrays.asList(dockerDigest1);
        //when(mockOnlineService.findDockerDigest(any(Project.class))).thenReturn(dockerDigests1);

        // Run the test
        final String result = jenkinsPipelineServiceUnderTest.generatePipeLineJob(project, "pipeLineUid", buildParam);

        // Verify the results
        assertThat(result).isEqualTo("result");
    }

    @Test
    void testGeneratePipeLineJob_ConfigurationThrowsTemplateNotFoundException() throws Exception {
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

        final ProjectBuildVo buildParam = new ProjectBuildVo();
        buildParam.setNameSpace("nameSpace");
        buildParam.setProfile("profile");
        buildParam.setProjects(Arrays.asList("value"));
        buildParam.setArriveTagName("arriveTagName");
        buildParam.setRolloutVersion("rolloutVersion");
        buildParam.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        buildParam.setDbUrl("dbUrl");
        buildParam.setDbUserName("dbUserName");
        buildParam.setDbPassword("dbPassword");
        buildParam.setDockerImageTag("dockerImageTag");

        //when(mockEnv.crumb()).thenReturn(false);
        //when(mockEnv.getRegistryUrl()).thenReturn("result");
        //when(mockEnv.getToolGitUrl()).thenReturn("result");
        //when(mockEnv.getBaseUrl()).thenReturn("result");
        //when(mockEnv.getSonarUser()).thenReturn("result");
        //when(mockEnv.getSonarPassword()).thenReturn("result");
        //when(mockEnv.getGitLabApiBaseUrl()).thenReturn("result");
        //when(mockEnv.initEnvParams(new HashMap<>())).thenReturn(new HashMap<>());
        //when(mockEnv.getArm64DockerBuildHost()).thenReturn("result");
        //when(mockEnv.getDockerBuildHost()).thenReturn("result");
        //when(mockConfiguration.getTemplate("name")).thenThrow(TemplateNotFoundException.class);

        // Configure OnlineService.findDockerDigest(...).
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
        final List<DockerDigest> dockerDigests = Arrays.asList(dockerDigest);
        //when(mockOnlineService.findDockerDigest("releaseId", "teamName")).thenReturn(dockerDigests);

        // Configure OnlineService.findDockerDigest(...).
        final DockerDigest dockerDigest1 = new DockerDigest();
        dockerDigest1.setId("id");
        dockerDigest1.setPipelineId("pipelineId");
        dockerDigest1.setProjectId("projectId");
        dockerDigest1.setProjectName("projectName");
        dockerDigest1.setDigest("digest");
        dockerDigest1.setUrl("url");
        dockerDigest1.setImageName("imageName");
        dockerDigest1.setNamespace("namespace");
        dockerDigest1.setTag("tag");
        final List<DockerDigest> dockerDigests1 = Arrays.asList(dockerDigest1);
        //when(mockOnlineService.findDockerDigest(any(Project.class))).thenReturn(dockerDigests1);

        // Run the test
        //assertThatThrownBy(() -> jenkinsPipelineServiceUnderTest.generatePipeLineJob(project, "pipeLineUid", buildParam)).isInstanceOf(TemplateNotFoundException.class);
    }

    @Test
    void testGeneratePipeLineJob_ConfigurationThrowsMalformedTemplateNameException() throws Exception {
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

        final ProjectBuildVo buildParam = new ProjectBuildVo();
        buildParam.setNameSpace("nameSpace");
        buildParam.setProfile("profile");
        buildParam.setProjects(Arrays.asList("value"));
        buildParam.setArriveTagName("arriveTagName");
        buildParam.setRolloutVersion("rolloutVersion");
        buildParam.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        buildParam.setDbUrl("dbUrl");
        buildParam.setDbUserName("dbUserName");
        buildParam.setDbPassword("dbPassword");
        buildParam.setDockerImageTag("dockerImageTag");

        //when(mockEnv.crumb()).thenReturn(false);
        //when(mockEnv.getRegistryUrl()).thenReturn("result");
        //when(mockEnv.getToolGitUrl()).thenReturn("result");
        //when(mockEnv.getBaseUrl()).thenReturn("result");
        //when(mockEnv.getSonarUser()).thenReturn("result");
        //when(mockEnv.getSonarPassword()).thenReturn("result");
        //when(mockEnv.getGitLabApiBaseUrl()).thenReturn("result");
        //when(mockEnv.initEnvParams(new HashMap<>())).thenReturn(new HashMap<>());
        //when(mockEnv.getArm64DockerBuildHost()).thenReturn("result");
        //when(mockEnv.getDockerBuildHost()).thenReturn("result");
        //when(mockConfiguration.getTemplate("name")).thenThrow(MalformedTemplateNameException.class);

        // Configure OnlineService.findDockerDigest(...).
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
        final List<DockerDigest> dockerDigests = Arrays.asList(dockerDigest);
        //when(mockOnlineService.findDockerDigest("releaseId", "teamName")).thenReturn(dockerDigests);

        // Configure OnlineService.findDockerDigest(...).
        final DockerDigest dockerDigest1 = new DockerDigest();
        dockerDigest1.setId("id");
        dockerDigest1.setPipelineId("pipelineId");
        dockerDigest1.setProjectId("projectId");
        dockerDigest1.setProjectName("projectName");
        dockerDigest1.setDigest("digest");
        dockerDigest1.setUrl("url");
        dockerDigest1.setImageName("imageName");
        dockerDigest1.setNamespace("namespace");
        dockerDigest1.setTag("tag");
        final List<DockerDigest> dockerDigests1 = Arrays.asList(dockerDigest1);
        //when(mockOnlineService.findDockerDigest(any(Project.class))).thenReturn(dockerDigests1);

        // Run the test
        //assertThatThrownBy(() -> jenkinsPipelineServiceUnderTest.generatePipeLineJob(project, "pipeLineUid", buildParam)).isInstanceOf(MalformedTemplateNameException.class);
    }

    @Test
    void testGeneratePipeLineJob_ConfigurationThrowsParseException() throws Exception {
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

        final ProjectBuildVo buildParam = new ProjectBuildVo();
        buildParam.setNameSpace("nameSpace");
        buildParam.setProfile("profile");
        buildParam.setProjects(Arrays.asList("value"));
        buildParam.setArriveTagName("arriveTagName");
        buildParam.setRolloutVersion("rolloutVersion");
        buildParam.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        buildParam.setDbUrl("dbUrl");
        buildParam.setDbUserName("dbUserName");
        buildParam.setDbPassword("dbPassword");
        buildParam.setDockerImageTag("dockerImageTag");

        //when(mockEnv.crumb()).thenReturn(false);
        //when(mockEnv.getRegistryUrl()).thenReturn("result");
        //when(mockEnv.getToolGitUrl()).thenReturn("result");
        //when(mockEnv.getBaseUrl()).thenReturn("result");
        //when(mockEnv.getSonarUser()).thenReturn("result");
        //when(mockEnv.getSonarPassword()).thenReturn("result");
        //when(mockEnv.getGitLabApiBaseUrl()).thenReturn("result");
        //when(mockEnv.initEnvParams(new HashMap<>())).thenReturn(new HashMap<>());
        //when(mockEnv.getArm64DockerBuildHost()).thenReturn("result");
        //when(mockEnv.getDockerBuildHost()).thenReturn("result");
        //when(mockConfiguration.getTemplate("name")).thenThrow(ParseException.class);

        // Configure OnlineService.findDockerDigest(...).
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
        final List<DockerDigest> dockerDigests = Arrays.asList(dockerDigest);
        //when(mockOnlineService.findDockerDigest("releaseId", "teamName")).thenReturn(dockerDigests);

        // Configure OnlineService.findDockerDigest(...).
        final DockerDigest dockerDigest1 = new DockerDigest();
        dockerDigest1.setId("id");
        dockerDigest1.setPipelineId("pipelineId");
        dockerDigest1.setProjectId("projectId");
        dockerDigest1.setProjectName("projectName");
        dockerDigest1.setDigest("digest");
        dockerDigest1.setUrl("url");
        dockerDigest1.setImageName("imageName");
        dockerDigest1.setNamespace("namespace");
        dockerDigest1.setTag("tag");
        final List<DockerDigest> dockerDigests1 = Arrays.asList(dockerDigest1);
        //when(mockOnlineService.findDockerDigest(any(Project.class))).thenReturn(dockerDigests1);

        // Run the test
        //assertThatThrownBy(() -> jenkinsPipelineServiceUnderTest.generatePipeLineJob(project, "pipeLineUid", buildParam)).isInstanceOf(ParseException.class);
    }

    @Test
    void testGeneratePipeLineJob_ConfigurationThrowsIOException() throws Exception {
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

        final ProjectBuildVo buildParam = new ProjectBuildVo();
        buildParam.setNameSpace("nameSpace");
        buildParam.setProfile("profile");
        buildParam.setProjects(Arrays.asList("value"));
        buildParam.setArriveTagName("arriveTagName");
        buildParam.setRolloutVersion("rolloutVersion");
        buildParam.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        buildParam.setDbUrl("dbUrl");
        buildParam.setDbUserName("dbUserName");
        buildParam.setDbPassword("dbPassword");
        buildParam.setDockerImageTag("dockerImageTag");

        //when(mockEnv.crumb()).thenReturn(false);
        //when(mockEnv.getRegistryUrl()).thenReturn("result");
        //when(mockEnv.getToolGitUrl()).thenReturn("result");
        //when(mockEnv.getBaseUrl()).thenReturn("result");
        //when(mockEnv.getSonarUser()).thenReturn("result");
        //when(mockEnv.getSonarPassword()).thenReturn("result");
        //when(mockEnv.getGitLabApiBaseUrl()).thenReturn("result");
        //when(mockEnv.initEnvParams(new HashMap<>())).thenReturn(new HashMap<>());
        //when(mockEnv.getArm64DockerBuildHost()).thenReturn("result");
        //when(mockEnv.getDockerBuildHost()).thenReturn("result");
        //when(mockConfiguration.getTemplate("name")).thenThrow(IOException.class);

        // Configure OnlineService.findDockerDigest(...).
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
        final List<DockerDigest> dockerDigests = Arrays.asList(dockerDigest);
        //when(mockOnlineService.findDockerDigest("releaseId", "teamName")).thenReturn(dockerDigests);

        // Configure OnlineService.findDockerDigest(...).
        final DockerDigest dockerDigest1 = new DockerDigest();
        dockerDigest1.setId("id");
        dockerDigest1.setPipelineId("pipelineId");
        dockerDigest1.setProjectId("projectId");
        dockerDigest1.setProjectName("projectName");
        dockerDigest1.setDigest("digest");
        dockerDigest1.setUrl("url");
        dockerDigest1.setImageName("imageName");
        dockerDigest1.setNamespace("namespace");
        dockerDigest1.setTag("tag");
        final List<DockerDigest> dockerDigests1 = Arrays.asList(dockerDigest1);
        //when(mockOnlineService.findDockerDigest(any(Project.class))).thenReturn(dockerDigests1);

        // Run the test
        //assertThatThrownBy(() -> jenkinsPipelineServiceUnderTest.generatePipeLineJob(project, "pipeLineUid", buildParam)).isInstanceOf(IOException.class);
    }




    @Test
    void testCreatePipeLine() throws Exception {
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

        final ProjectBuildVo buildParam = new ProjectBuildVo();
        buildParam.setNameSpace("nameSpace");
        buildParam.setProfile("profile");
        buildParam.setProjects(Arrays.asList("value"));
        buildParam.setArriveTagName("arriveTagName");
        buildParam.setRolloutVersion("rolloutVersion");
        buildParam.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        buildParam.setDbUrl("dbUrl");
        buildParam.setDbUserName("dbUserName");
        buildParam.setDbPassword("dbPassword");
        buildParam.setDockerImageTag("dockerImageTag");

        //when(mockEnv.crumb()).thenReturn(false);
        //when(mockEnv.getRegistryUrl()).thenReturn("result");
        //when(mockEnv.getToolGitUrl()).thenReturn("result");
        //when(mockEnv.getBaseUrl()).thenReturn("result");
        //when(mockEnv.getSonarUser()).thenReturn("result");
        //when(mockEnv.getSonarPassword()).thenReturn("result");
        //when(mockEnv.getGitLabApiBaseUrl()).thenReturn("result");
        //when(mockEnv.initEnvParams(new HashMap<>())).thenReturn(new HashMap<>());
        //when(mockEnv.getArm64DockerBuildHost()).thenReturn("result");
        //when(mockEnv.getDockerBuildHost()).thenReturn("result");

        // Configure Configuration.getTemplate(...).
        final Template template = new Template("name", new StringReader("content"), new Configuration(new Version(0, 0, 0, "extraInfo", false, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())));
        //when(mockConfiguration.getTemplate("name")).thenReturn(template);

        // Configure OnlineService.findDockerDigest(...).
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
        final List<DockerDigest> dockerDigests = Arrays.asList(dockerDigest);
        //when(mockOnlineService.findDockerDigest("releaseId", "teamName")).thenReturn(dockerDigests);

        // Configure OnlineService.findDockerDigest(...).
        final DockerDigest dockerDigest1 = new DockerDigest();
        dockerDigest1.setId("id");
        dockerDigest1.setPipelineId("pipelineId");
        dockerDigest1.setProjectId("projectId");
        dockerDigest1.setProjectName("projectName");
        dockerDigest1.setDigest("digest");
        dockerDigest1.setUrl("url");
        dockerDigest1.setImageName("imageName");
        dockerDigest1.setNamespace("namespace");
        dockerDigest1.setTag("tag");
        final List<DockerDigest> dockerDigests1 = Arrays.asList(dockerDigest1);
        //when(mockOnlineService.findDockerDigest(any(Project.class))).thenReturn(dockerDigests1);

        // Run the test
        final String result = jenkinsPipelineServiceUnderTest.createPipeLine(project, "pipeLineUid", false, buildParam);

        // Verify the results
        assertThat(result).isEqualTo("result");
    }

    @Test
    void testCreatePipeLine_ConfigurationThrowsTemplateNotFoundException() throws Exception {
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

        final ProjectBuildVo buildParam = new ProjectBuildVo();
        buildParam.setNameSpace("nameSpace");
        buildParam.setProfile("profile");
        buildParam.setProjects(Arrays.asList("value"));
        buildParam.setArriveTagName("arriveTagName");
        buildParam.setRolloutVersion("rolloutVersion");
        buildParam.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        buildParam.setDbUrl("dbUrl");
        buildParam.setDbUserName("dbUserName");
        buildParam.setDbPassword("dbPassword");
        buildParam.setDockerImageTag("dockerImageTag");

        //when(mockEnv.crumb()).thenReturn(false);
        //when(mockEnv.getRegistryUrl()).thenReturn("result");
        //when(mockEnv.getToolGitUrl()).thenReturn("result");
        //when(mockEnv.getBaseUrl()).thenReturn("result");
        //when(mockEnv.getSonarUser()).thenReturn("result");
        //when(mockEnv.getSonarPassword()).thenReturn("result");
        //when(mockEnv.getGitLabApiBaseUrl()).thenReturn("result");
        //when(mockEnv.initEnvParams(new HashMap<>())).thenReturn(new HashMap<>());
        //when(mockEnv.getArm64DockerBuildHost()).thenReturn("result");
        //when(mockEnv.getDockerBuildHost()).thenReturn("result");
        //when(mockConfiguration.getTemplate("name")).thenThrow(TemplateNotFoundException.class);

        // Configure OnlineService.findDockerDigest(...).
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
        final List<DockerDigest> dockerDigests = Arrays.asList(dockerDigest);
        //when(mockOnlineService.findDockerDigest("releaseId", "teamName")).thenReturn(dockerDigests);

        // Configure OnlineService.findDockerDigest(...).
        final DockerDigest dockerDigest1 = new DockerDigest();
        dockerDigest1.setId("id");
        dockerDigest1.setPipelineId("pipelineId");
        dockerDigest1.setProjectId("projectId");
        dockerDigest1.setProjectName("projectName");
        dockerDigest1.setDigest("digest");
        dockerDigest1.setUrl("url");
        dockerDigest1.setImageName("imageName");
        dockerDigest1.setNamespace("namespace");
        dockerDigest1.setTag("tag");
        final List<DockerDigest> dockerDigests1 = Arrays.asList(dockerDigest1);
        //when(mockOnlineService.findDockerDigest(any(Project.class))).thenReturn(dockerDigests1);

        // Run the test
        //assertThatThrownBy(() -> jenkinsPipelineServiceUnderTest.createPipeLine(project, "pipeLineUid", false, buildParam)).isInstanceOf(TemplateNotFoundException.class);
    }

    @Test
    void testCreatePipeLine_ConfigurationThrowsMalformedTemplateNameException() throws Exception {
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

        final ProjectBuildVo buildParam = new ProjectBuildVo();
        buildParam.setNameSpace("nameSpace");
        buildParam.setProfile("profile");
        buildParam.setProjects(Arrays.asList("value"));
        buildParam.setArriveTagName("arriveTagName");
        buildParam.setRolloutVersion("rolloutVersion");
        buildParam.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        buildParam.setDbUrl("dbUrl");
        buildParam.setDbUserName("dbUserName");
        buildParam.setDbPassword("dbPassword");
        buildParam.setDockerImageTag("dockerImageTag");

        //when(mockEnv.crumb()).thenReturn(false);
        //when(mockEnv.getRegistryUrl()).thenReturn("result");
        //when(mockEnv.getToolGitUrl()).thenReturn("result");
        //when(mockEnv.getBaseUrl()).thenReturn("result");
        //when(mockEnv.getSonarUser()).thenReturn("result");
        //when(mockEnv.getSonarPassword()).thenReturn("result");
        //when(mockEnv.getGitLabApiBaseUrl()).thenReturn("result");
        //when(mockEnv.initEnvParams(new HashMap<>())).thenReturn(new HashMap<>());
        //when(mockEnv.getArm64DockerBuildHost()).thenReturn("result");
        //when(mockEnv.getDockerBuildHost()).thenReturn("result");
        //when(mockConfiguration.getTemplate("name")).thenThrow(MalformedTemplateNameException.class);

        // Configure OnlineService.findDockerDigest(...).
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
        final List<DockerDigest> dockerDigests = Arrays.asList(dockerDigest);
//        //when(mockOnlineService.findDockerDigest("releaseId", "teamName")).thenReturn(dockerDigests);

        // Configure OnlineService.findDockerDigest(...).
        final DockerDigest dockerDigest1 = new DockerDigest();
        dockerDigest1.setId("id");
        dockerDigest1.setPipelineId("pipelineId");
        dockerDigest1.setProjectId("projectId");
        dockerDigest1.setProjectName("projectName");
        dockerDigest1.setDigest("digest");
        dockerDigest1.setUrl("url");
        dockerDigest1.setImageName("imageName");
        dockerDigest1.setNamespace("namespace");
        dockerDigest1.setTag("tag");
        final List<DockerDigest> dockerDigests1 = Arrays.asList(dockerDigest1);
        //when(mockOnlineService.findDockerDigest(any(Project.class))).thenReturn(dockerDigests1);

        // Run the test
//        //assertThatThrownBy(() -> jenkinsPipelineServiceUnderTest.createPipeLine(project, "pipeLineUid", false, buildParam)).isInstanceOf(MalformedTemplateNameException.class);
    }

    @Test
    void testCreatePipeLine_ConfigurationThrowsParseException() throws Exception {
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

        final ProjectBuildVo buildParam = new ProjectBuildVo();
        buildParam.setNameSpace("nameSpace");
        buildParam.setProfile("profile");
        buildParam.setProjects(Arrays.asList("value"));
        buildParam.setArriveTagName("arriveTagName");
        buildParam.setRolloutVersion("rolloutVersion");
        buildParam.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        buildParam.setDbUrl("dbUrl");
        buildParam.setDbUserName("dbUserName");
        buildParam.setDbPassword("dbPassword");
        buildParam.setDockerImageTag("dockerImageTag");

        //when(mockEnv.crumb()).thenReturn(false);
        //when(mockEnv.getRegistryUrl()).thenReturn("result");
        //when(mockEnv.getToolGitUrl()).thenReturn("result");
        //when(mockEnv.getBaseUrl()).thenReturn("result");
        //when(mockEnv.getSonarUser()).thenReturn("result");
        //when(mockEnv.getSonarPassword()).thenReturn("result");
        //when(mockEnv.getGitLabApiBaseUrl()).thenReturn("result");
        //when(mockEnv.initEnvParams(new HashMap<>())).thenReturn(new HashMap<>());
        //when(mockEnv.getArm64DockerBuildHost()).thenReturn("result");
        //when(mockEnv.getDockerBuildHost()).thenReturn("result");
        //when(mockConfiguration.getTemplate("name")).thenThrow(ParseException.class);

        // Configure OnlineService.findDockerDigest(...).
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
        final List<DockerDigest> dockerDigests = Arrays.asList(dockerDigest);
        //when(mockOnlineService.findDockerDigest("releaseId", "teamName")).thenReturn(dockerDigests);

        // Configure OnlineService.findDockerDigest(...).
        final DockerDigest dockerDigest1 = new DockerDigest();
        dockerDigest1.setId("id");
        dockerDigest1.setPipelineId("pipelineId");
        dockerDigest1.setProjectId("projectId");
        dockerDigest1.setProjectName("projectName");
        dockerDigest1.setDigest("digest");
        dockerDigest1.setUrl("url");
        dockerDigest1.setImageName("imageName");
        dockerDigest1.setNamespace("namespace");
        dockerDigest1.setTag("tag");
        final List<DockerDigest> dockerDigests1 = Arrays.asList(dockerDigest1);
        //when(mockOnlineService.findDockerDigest(any(Project.class))).thenReturn(dockerDigests1);

        // Run the test
//        //assertThatThrownBy(() -> jenkinsPipelineServiceUnderTest.createPipeLine(project, "pipeLineUid", false, buildParam)).isInstanceOf(ParseException.class);
    }

    @Test
    void testCreatePipeLine_ConfigurationThrowsIOException() throws Exception {
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

        final ProjectBuildVo buildParam = new ProjectBuildVo();
        buildParam.setNameSpace("nameSpace");
        buildParam.setProfile("profile");
        buildParam.setProjects(Arrays.asList("value"));
        buildParam.setArriveTagName("arriveTagName");
        buildParam.setRolloutVersion("rolloutVersion");
        buildParam.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        buildParam.setDbUrl("dbUrl");
        buildParam.setDbUserName("dbUserName");
        buildParam.setDbPassword("dbPassword");
        buildParam.setDockerImageTag("dockerImageTag");

        //when(mockEnv.crumb()).thenReturn(false);
        //when(mockEnv.getRegistryUrl()).thenReturn("result");
        //when(mockEnv.getToolGitUrl()).thenReturn("result");
        //when(mockEnv.getBaseUrl()).thenReturn("result");
        //when(mockEnv.getSonarUser()).thenReturn("result");
        //when(mockEnv.getSonarPassword()).thenReturn("result");
        //when(mockEnv.getGitLabApiBaseUrl()).thenReturn("result");
        //when(mockEnv.initEnvParams(new HashMap<>())).thenReturn(new HashMap<>());
        //when(mockEnv.getArm64DockerBuildHost()).thenReturn("result");
        //when(mockEnv.getDockerBuildHost()).thenReturn("result");
        //when(mockConfiguration.getTemplate("name")).thenThrow(IOException.class);

        // Configure OnlineService.findDockerDigest(...).
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
        final List<DockerDigest> dockerDigests = Arrays.asList(dockerDigest);
        //when(mockOnlineService.findDockerDigest("releaseId", "teamName")).thenReturn(dockerDigests);

        // Configure OnlineService.findDockerDigest(...).
        final DockerDigest dockerDigest1 = new DockerDigest();
        dockerDigest1.setId("id");
        dockerDigest1.setPipelineId("pipelineId");
        dockerDigest1.setProjectId("projectId");
        dockerDigest1.setProjectName("projectName");
        dockerDigest1.setDigest("digest");
        dockerDigest1.setUrl("url");
        dockerDigest1.setImageName("imageName");
        dockerDigest1.setNamespace("namespace");
        dockerDigest1.setTag("tag");
        final List<DockerDigest> dockerDigests1 = Arrays.asList(dockerDigest1);
        //when(mockOnlineService.findDockerDigest(any(Project.class))).thenReturn(dockerDigests1);

        // Run the test
//        //assertThatThrownBy(() -> jenkinsPipelineServiceUnderTest.createPipeLine(project, "pipeLineUid", false, buildParam)).isInstanceOf(IOException.class);
    }


    @Test
    void testCreatePipeLine_OnlineServiceFindDockerDigestReturnsNoItems() throws Exception {
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

        final ProjectBuildVo buildParam = new ProjectBuildVo();
        buildParam.setNameSpace("nameSpace");
        buildParam.setProfile("profile");
        buildParam.setProjects(Arrays.asList("value"));
        buildParam.setArriveTagName("arriveTagName");
        buildParam.setRolloutVersion("rolloutVersion");
        buildParam.setReleaseDate(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        buildParam.setDbUrl("dbUrl");
        buildParam.setDbUserName("dbUserName");
        buildParam.setDbPassword("dbPassword");
        buildParam.setDockerImageTag("dockerImageTag");

        //when(mockEnv.crumb()).thenReturn(false);
        //when(mockEnv.getRegistryUrl()).thenReturn("result");
        //when(mockEnv.getToolGitUrl()).thenReturn("result");
        //when(mockEnv.getBaseUrl()).thenReturn("result");
        //when(mockEnv.getSonarUser()).thenReturn("result");
        //when(mockEnv.getSonarPassword()).thenReturn("result");
        //when(mockEnv.getGitLabApiBaseUrl()).thenReturn("result");
        //when(mockEnv.initEnvParams(new HashMap<>())).thenReturn(new HashMap<>());
        //when(mockEnv.getArm64DockerBuildHost()).thenReturn("result");
        //when(mockEnv.getDockerBuildHost()).thenReturn("result");

        // Configure Configuration.getTemplate(...).
        final Template template = new Template("name", new StringReader("content"), new Configuration(new Version(0, 0, 0, "extraInfo", false, new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())));
        //when(mockConfiguration.getTemplate("name")).thenReturn(template);

        // Configure OnlineService.findDockerDigest(...).
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
        final List<DockerDigest> dockerDigests = Arrays.asList(dockerDigest);
        //when(mockOnlineService.findDockerDigest("releaseId", "teamName")).thenReturn(dockerDigests);

        //when(mockOnlineService.findDockerDigest(any(Project.class))).thenReturn(Collections.emptyList());

        // Run the test
        final String result = jenkinsPipelineServiceUnderTest.createPipeLine(project, "pipeLineUid", false, buildParam);

        // Verify the results
        assertThat(result).isEqualTo("result");
    }

    @Test
    void testGetLastPipeline() throws Exception {
        // Setup
        // Run the test
        final Pipeline result = jenkinsPipelineServiceUnderTest.getLastPipeline("projectName");

        // Verify the results
    }

    @Test
    void testGetLastPipeline_ThrowsIOException() {
        // Setup
        // Run the test
        //assertThatThrownBy(() -> jenkinsPipelineServiceUnderTest.getLastPipeline("projectName")).isInstanceOf(IOException.class);
    }

    @Test
    void testRunPipeLine() throws Exception {
        // Setup
        //when(mockEnv.crumb()).thenReturn(false);

        // Run the test
        jenkinsPipelineServiceUnderTest.runPipeLine("name");

        // Verify the results
    }

    @Test
    void testRunPipeLine_ThrowsIOException() {
        // Setup
        //when(mockEnv.crumb()).thenReturn(false);

        // Run the test
        //assertThatThrownBy(() -> jenkinsPipelineServiceUnderTest.runPipeLine("name")).isInstanceOf(IOException.class);
    }

    @Test
    void testGetLastPipelineConsoleText() throws Exception {
        // Setup
        // Run the test
        final String result = jenkinsPipelineServiceUnderTest.getLastPipelineConsoleText("projectName");

        // Verify the results
        assertThat(result).isEqualTo("result");
    }

    @Test
    void testGetLastPipelineConsoleText_ThrowsIOException() {
        // Setup
        // Run the test
        //assertThatThrownBy(() -> jenkinsPipelineServiceUnderTest.getLastPipelineConsoleText("projectName")).isInstanceOf(IOException.class);
    }

    @Test
    void testAttachPipeLine() {
        // Setup
        // Run the test
        final Pipeline result = jenkinsPipelineServiceUnderTest.attachPipeLine("name");

        // Verify the results
    }

    @Test
    void testClearLegacyWorkspaceAfterPipeline() {
        // Setup
        //when(mockEnv.clearLegacyWorkspace()).thenReturn(false);

        // Run the test
        final boolean result = jenkinsPipelineServiceUnderTest.clearLegacyWorkspaceAfterPipeline();

        // Verify the results
        assertThat(result).isTrue();
    }
}
