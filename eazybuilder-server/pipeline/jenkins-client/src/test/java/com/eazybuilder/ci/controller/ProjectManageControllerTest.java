package com.eazybuilder.ci.controller;

import com.eazybuilder.ci.entity.ProjectManage;
import com.eazybuilder.ci.service.ProjectManageService;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
public class ProjectManageControllerTest {

    //被测controller类
    @InjectMocks
    private ProjectManageController controller;

    @Mock
    private ProjectManageService service;

    private MockMvc mvc;

    /**
     * 设置好环境
     */
    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        mvc= MockMvcBuilders.standaloneSetup(controller)
                .build();
    }

    @Test
    void testSave() throws Exception{

        final ProjectManage projectManage = new ProjectManage();

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
        projectManage.setCreateDate(df.format(new Date()));

        projectManage.setName(String.valueOf(System.currentTimeMillis()));
        projectManage.setType("redmine");

        projectManage.setUrl("sadfasf");
        projectManage.setUserName("root");
        projectManage.setPassword("root");

        projectManage.setDbUrl("jdbc:mysql://0.0.0.0:3506/ci?useUnicode=true&useSSL=false&characterEncoding=utf-8");
        projectManage.setDbName("root");
        projectManage.setDbPassword("root");

        JSONObject object = JSONObject.fromObject(projectManage);
        String entity=object.toString();

        // Run the test
        final MockHttpServletResponse response = mvc.perform(post("/api/projectManage")
                        .content(entity).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains("jdbc:mysql://0.0.0.0:3506/ci?useUnicode=true&useSSL=false&characterEncoding=utf-8");
    }


    @Test
    void testConnection() throws Exception{
        final ProjectManage projectManage = new ProjectManage();
        projectManage.setDbUrl("jdbc:mysql://0.0.0.0:3506/ci?useUnicode=true&useSSL=false&characterEncoding=utf-8");
        projectManage.setDbName("root");
        projectManage.setDbPassword("root");

        JSONObject object = JSONObject.fromObject(projectManage);
        String entity=object.toString();
        // Run the test

        final MockHttpServletResponse response = mvc.perform(post("/api/projectManage/testConnection")
                        .content(entity).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andReturn().getResponse();
        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("true");
    }


    @Test
    void testPage() throws Exception{

    }




}