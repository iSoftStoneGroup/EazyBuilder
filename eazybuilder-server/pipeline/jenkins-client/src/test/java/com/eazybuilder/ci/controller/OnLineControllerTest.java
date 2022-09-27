package com.eazybuilder.ci.controller;

import com.eazybuilder.ci.Application;
import com.eazybuilder.ci.auth.AccessFilter;
import com.eazybuilder.ci.entity.Pipeline;
import com.eazybuilder.ci.entity.devops.Release;
import com.eazybuilder.ci.entity.report.Stage;
import com.eazybuilder.ci.entity.report.Status;
import com.eazybuilder.ci.service.*;
import com.eazybuilder.ci.util.AuthUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
class OnLineControllerTest {


    //被测controller类
    @InjectMocks
    private OnLineController controller;



    @Mock
    private ReleaseService mockReleaseService;
    @Mock
    private DockerDigestService mockDockerDigestService;
    @Mock
    private TeamServiceImpl mockTeamService;
    @Mock
    private PipelineServiceImpl mockPipelineService;
    @Mock
    private UserService mockUserService;

    private MockMvc mvc;

    /**
     * 每次测试前设置好环境
     */
    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        mvc= MockMvcBuilders.standaloneSetup(controller)
//                .addFilters(new AccessFilter())
                .build();
    }



    @Test
    void testUpdateRelease() throws Exception {
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
//        when(mockReleaseService.findOne("id")).thenReturn(release);

        // Run the test
        final MockHttpServletResponse response = mvc.perform(post("/api/onLine/updateRelease")
                .content("content").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }
}
