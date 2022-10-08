package com.eazybuilder.ci.controller;

import com.eazybuilder.ci.entity.SecondParty;
import com.eazybuilder.ci.service.SecondPartyService;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class SecondPartyControllerTest {

    //被测controller类
    @InjectMocks
    private SecondPartyController controller;

    @Mock
    private SecondPartyService service;

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

        final SecondParty secondParty = new SecondParty();

        secondParty.setSecondPartyName(String.valueOf(System.currentTimeMillis()));
        secondParty.setSecondPartyType("mavenSnapshot");

        secondParty.setSecondPartyUser("root");
        secondParty.setSecondPartyName("root");


        JSONObject object = JSONObject.fromObject(secondParty);
        String entity=object.toString();

        // Run the test
        final MockHttpServletResponse response = mvc.perform(post("/api/secondParty")
                        .content(entity).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains("mavenSnapshot");
    }
}