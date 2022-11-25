package com.eazybuilder.dm.controller;


import com.eazybuilder.dm.NotifyApplication;
import com.eazybuilder.dm.controller.ReceiveMsg;
import com.eazybuilder.dm.util.HttpUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NotifyApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReceiveMsgTest {


	@Test
	public void testGetNamespaces() throws Exception {
		String s = HttpUtil.postJson("https://oapi.dingtalk.com/gettoken?appkey=xxx", null);
		System.out.println(s);
	}
	//被测controller�?
	@InjectMocks
	private ReceiveMsg receiveMsg;
	//controller的依赖，通过mock注入


}
