package com.eazybuilder.ci.service;

import com.eazybuilder.ci.entity.AppType;
import com.eazybuilder.ci.entity.DeployConfig;
import com.eazybuilder.ci.entity.ProjectManage;
import com.eazybuilder.ci.entity.devops.DevopsInit;
import com.eazybuilder.ci.entity.devops.DevopsProject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProjectManageServiceTest {

    @Mock
    private StringRedisTemplate mockRedisTemplate;
    @Mock
    private DevopsInitServiceImpl mockDevopsInitService;

    @InjectMocks
    private ProjectManageService projectManageServiceUnderTest;

    @Test
    public void testSave() {
        // Setup
        final ProjectManage entity = new ProjectManage();

        entity.setId("1");
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
        entity.setCreateDate(df.format(new Date()));

        entity.setName(String.valueOf(System.currentTimeMillis()));
        entity.setType("redmine");

        entity.setUrl("sadfasf");
        entity.setUserName("root");
        entity.setPassword("root");

        entity.setDbUrl("jdbc:mysql://0.0.0.0:3506/ci?useUnicode=true&useSSL=false&characterEncoding=utf-8");
        entity.setDbName("root");
        entity.setDbPassword("root");

        // Configure DevopsInitServiceImpl.findByProjectManageId(...).
        final DevopsInit devopsInit = new DevopsInit();
        devopsInit.setTeamBeginDate(df.format(new Date()));
        devopsInit.setTeamEndDate(df.format(new Date()));
        devopsInit.setTeamName("teamName");
        devopsInit.setTeamCode("teamCode");
        devopsInit.setProjectManageId("projectManageId");
        devopsInit.setProjectManageName("projectManageName");
        final DevopsProject devopsProject = new DevopsProject();
        devopsProject.setId("1");
        final DeployConfig deployConfig = new DeployConfig();
        deployConfig.setAppType(AppType.deployment);
        deployConfig.setHostname("hostname");
        deployConfig.setLimitsCpu("limitsCpu");
        deployConfig.setName("name");
        deployConfig.setReplicas("replicas");
        deployConfig.setId("1");
        devopsProject.setDeployConfigList(Arrays.asList(deployConfig));
        devopsInit.setDevopsProjects(Arrays.asList(devopsProject));
        final List<DevopsInit> devopsInits = Arrays.asList(devopsInit);
        when(mockDevopsInitService.findByProjectManageId("1")).thenReturn(devopsInits);

        when(mockRedisTemplate.opsForHash()).thenReturn(null);
        when(mockRedisTemplate.opsForValue()).thenReturn(null);

        // Run the test
       // projectManageServiceUnderTest.save(entity);

        // Verify the results
    }

    @Test
    public void testSave_DevopsInitServiceImplReturnsNull() {
        // Setup
        final ProjectManage entity = new ProjectManage();
        entity.setId("id");
        entity.setName("name");
        entity.setType("type");
        entity.setUrl("url");
        entity.setUserName("userName");
        entity.setPassword("password");
        entity.setDbUrl("dbUrl");
        entity.setDbName("dbName");
        entity.setDbPassword("dbPassword");
        entity.setCreateDate("createDate");

        when(mockDevopsInitService.findByProjectManageId("id")).thenReturn(null);
        when(mockRedisTemplate.opsForHash()).thenReturn(null);
        when(mockRedisTemplate.opsForValue()).thenReturn(null);

        // Run the test
       // projectManageServiceUnderTest.save(entity);

        // Verify the results
    }

    @Test
    public void testSave_DevopsInitServiceImplReturnsNoItems() {
        // Setup
        final ProjectManage entity = new ProjectManage();
        entity.setId("id");
        entity.setName("name");
        entity.setType("type");
        entity.setUrl("url");
        entity.setUserName("userName");
        entity.setPassword("password");
        entity.setDbUrl("dbUrl");
        entity.setDbName("dbName");
        entity.setDbPassword("dbPassword");
        entity.setCreateDate("createDate");

        when(mockDevopsInitService.findByProjectManageId("id")).thenReturn(Collections.emptyList());
        when(mockRedisTemplate.opsForHash()).thenReturn(null);
        when(mockRedisTemplate.opsForValue()).thenReturn(null);

        // Run the test
        //projectManageServiceUnderTest.save(entity);

        // Verify the results
    }
}
