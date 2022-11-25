package com.eazybuilder.dm.controller;

import java.io.File;
import java.net.URL;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import com.eazybuilder.dm.NotifyApplication;
import okhttp3.Response;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NotifyApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class K8sApiControllerTest {

	/**
	 * @LocalServerPort 提供�? @Value("${local.server.port}") 的代�?
	 */
	@LocalServerPort
	private int port;

	private URL base;

	@Autowired
	private TestRestTemplate restTemplate;

	private static Logger logger = LoggerFactory.getLogger(K8sApiControllerTest.class);

	@Before
	public void setUp() throws Exception {
		String url = String.format("http://localhost:%d/k8s/api", port);
		System.out.println(String.format("port is : [%d]", port));
		this.base = new URL(url);
	}

	@Test
	public void testGetNamespaces() {
//		ResponseEntity<V1NamespaceList> response = this.restTemplate.getForEntity(
//				this.base.toString() + "/get/namespaces", V1NamespaceList.class, "app=ats-selenium-agent");
//
//		logger.info("testGetNamespaces接口测试结果:{}", response);
	}

 

}
