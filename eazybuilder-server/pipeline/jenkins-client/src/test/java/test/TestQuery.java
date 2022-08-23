package test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.eazybuilder.ci.Application;
import com.eazybuilder.ci.service.PipelineServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestQuery {
	private static Logger logger = LoggerFactory.getLogger(TestQuery.class);

	@Autowired
	PipelineServiceImpl service;

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void split() throws Exception {
		
		String url="http://localhost:8080/ci/queue/item/123/";
		logger.info("查询成功:{}",url.lastIndexOf("queue/item"));
	}
	
	
}
