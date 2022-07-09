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
import com.eazybuilder.ci.upms.QueryUpmsData;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestUpms {
	private static Logger logger = LoggerFactory.getLogger(TestUpms.class);

 
	@Autowired
	QueryUpmsData queryUpmsData;

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void getRoleByUserId() throws Exception {
		Long userId=137294000422912L;
		assertNotNull(queryUpmsData.getRoleByUserId(userId));
		 
		logger.info("查询成功");
	}
	
	
	@Test
	public void getUserList() throws Exception {
		
		assertNotNull(queryUpmsData.getUserList());
		 
		logger.info("查询成功");
	}
	
	@Test
	public void getRole() throws Exception {
		
		assertNotNull(queryUpmsData.getRole());
		 
		logger.info("查询成功");
	}

}
