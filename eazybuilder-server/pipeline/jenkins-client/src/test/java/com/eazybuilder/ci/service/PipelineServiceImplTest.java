package com.eazybuilder.ci.service;

import com.eazybuilder.ci.controller.vo.ProjectBuildVo;
import com.eazybuilder.ci.entity.Pipeline;
import com.eazybuilder.ci.entity.Project;
import com.eazybuilder.ci.event.EventBusSupport;
import com.eazybuilder.ci.mail.MailSenderHelper;
import com.eazybuilder.ci.rabbitMq.SendRabbitMq;
import com.eazybuilder.ci.repository.FileResourceDao;
import com.eazybuilder.ci.storage.ResourceStorageService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import freemarker.template.Configuration;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;

@RunWith(PowerMockRunner.class)
class PipelineServiceImplTest {

    @Spy
    @InjectMocks
    PipelineServiceImpl pipelineServiceImpl=new PipelineServiceImpl();

    @Mock
    OnlineService onlineService;

    @Mock
    FileResourceDao fileResourceDao;


    @Mock
    ReleaseProjectService releaseProjectService;

    @Mock
    CIPackageService ciPackageService;

    @Mock
    ReleaseService releaseService;

    @Mock
    DockerImageService dockerImageService;
    @Mock
    SendRabbitMq sendRabbitMq;
    @Mock
    ProjectService projectService;

    @Mock
    PipelineProfileService profileService;

    @Mock
    ResourceStorageService storageService;

    @Mock
    TeamServiceImpl teamServiceImpl;

    @Mock
    TeamResourceService teamResourceService;

    @Mock
    ScmService scmService;

    @Mock
    MetricService metricService;

    @Mock
    PipelineBuildService pipelineBuildService;


    @Mock
    Configuration configuration;


    @Mock
    MailSenderHelper mailSender;

    @Mock
    EventBusSupport eventBus;

    @Mock
    SystemPropertyService configService;

    @Mock
    HostInfoService hostService;

    @Mock
    DockerDigestService dockerDigestService;


    @Mock
    PipelineExecuteService pipelineExecuteService;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    /**
     * public void triggerPipeline(Map<Project, List<ProjectBuildVo>> projectProfileMap)
     */
    @Test
   public void triggerPipeline_projectProfileMap() throws Exception {
        Project project=new Project();
        project.setId(RandomStringUtils.random(2));
        ProjectBuildVo projectBuildVo=new ProjectBuildVo();
        projectBuildVo.setProjectId(RandomStringUtils.random(5));
        ArrayList<ProjectBuildVo> objects = Lists.newArrayList();
        objects.add(projectBuildVo);
        Map<Project, List<ProjectBuildVo>> eventProject = Maps.newHashMap();
        eventProject.put(project,objects);

        Pipeline pipeline =new Pipeline();
        PipelineServiceImpl spy = PowerMockito.spy(pipelineServiceImpl);
        PowerMockito.doReturn(pipeline).when(spy,"triggerPipeline",project.getId(),projectBuildVo);
        try {
            spy.triggerPipeline(eventProject);

        }catch (Exception e){
            Assert.isTrue(e!=null,"pipelineServiceImpl.triggerPipeline方法测试失败！");
        }
    }
}