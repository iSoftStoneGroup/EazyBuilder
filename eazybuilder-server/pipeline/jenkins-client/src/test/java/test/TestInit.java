package test;

import com.eazybuilder.ci.controller.DevopsInitController;
import com.eazybuilder.ci.entity.devops.DevopsInit;
import com.eazybuilder.ci.rabbitMq.SendRabbitMq;
import com.eazybuilder.ci.service.DevopsInitServiceImpl;
import com.eazybuilder.ci.service.ProjectService;
import com.eazybuilder.ci.upms.QueryUpmsData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(PowerMockRunner.class)
public class TestInit {

    //被测controller类
    @InjectMocks
    private DevopsInitController controller;

    @Mock
    private DevopsInitServiceImpl devopsInitServiceImpl;

    @Mock
    private SendRabbitMq sendRabbitMq;

    @Mock
    private QueryUpmsData queryUpmsData;

    @Mock
    private ProjectService projectService;

    private CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter("utf-8", true, true);

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInit() throws Exception{
        String url = "/api/deveops";
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post(url);
        mockHttpServletRequestBuilder.contentType(MediaType.APPLICATION_JSON_VALUE);
        //mockHttpServletRequestBuilder.content("[]");
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addFilter(characterEncodingFilter).build();
        ResultActions resultActions = mockMvc.perform(mockHttpServletRequestBuilder);

        resultActions.andDo(print());
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void testSave() throws Exception{
        String url = "/api/deveops";
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post(url);
        mockHttpServletRequestBuilder.contentType(MediaType.APPLICATION_JSON_VALUE);
        //mockHttpServletRequestBuilder.content();
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addFilter(characterEncodingFilter).build();
        ResultActions resultActions = mockMvc.perform(mockHttpServletRequestBuilder);

        resultActions.andDo(print());
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void testGetDevopsPage() throws Exception {

        List<DevopsInit> devopsInitList = new ArrayList<DevopsInit>();
        Page<DevopsInit> page = new PageImpl<DevopsInit>(devopsInitList);

        PowerMockito.when(devopsInitServiceImpl.pageSearch(any(),any())).thenReturn(page);

        String url = "/api/deveops/getDeveopsPage?limit=10&offset=0&search=test";
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get(url);
        mockHttpServletRequestBuilder.contentType(MediaType.APPLICATION_JSON_VALUE);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addFilter(characterEncodingFilter).build();
        ResultActions resultActions = mockMvc.perform(mockHttpServletRequestBuilder);

        resultActions.andDo(print());
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void testDelete() throws Exception {
        String url = "/api/deveops/delete";
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post(url);
        mockHttpServletRequestBuilder.contentType(MediaType.APPLICATION_JSON_VALUE);
        mockHttpServletRequestBuilder.content("[]");
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addFilter(characterEncodingFilter).build();
        ResultActions resultActions = mockMvc.perform(mockHttpServletRequestBuilder);

        resultActions.andDo(print());
        resultActions.andExpect(status().isOk());
    }
}
