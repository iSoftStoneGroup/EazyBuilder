package com.eazybuilder.ci.job;

import com.eazybuilder.ci.entity.Project;
import com.eazybuilder.ci.entity.ProjectGroup;
import com.eazybuilder.ci.mail.MailSenderHelper;
import com.eazybuilder.ci.service.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import freemarker.template.Configuration;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author: mlxuef
 * @createTime: 2022/8/29
 * @description:
 **/
@RunWith(PowerMockRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ReportJobTest {

    @InjectMocks
    ReportJob reportJob;


    @Mock
    ProjectService projectService;

    @Mock
    ProjectGroupService projectGroupService;
    @Mock
    PipelineServiceImpl pipelineServiceImpl;
    @Mock
    MetricService metricService;
    @Mock
    WarnService warnService;
    @Mock
    private Configuration configuration;
    @Mock
    private MailSenderHelper mailService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void searchWarnTeamReportByWarnRule() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ProjectGroup projectGroup = new ProjectGroup();
        Project project = new Project();
        Set<Project> set = Sets.newHashSet();
        set.add(project);
        projectGroup.setProjects(set);
        Set<ProjectGroup> groups = Sets.newHashSet();
        Method method =  ReportJob.class.getDeclaredMethod("searchWarnTeamReportByWarnRule",Set.class,List.class);
        method.setAccessible(true);
        method.invoke(groups, Lists.newArrayList());
    }

}