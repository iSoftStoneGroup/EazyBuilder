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

//	@Test
//	public void queryPipelineHistory() throws Exception {
//
////		 http://ci-ingress.eazybuild-devops..cn/ci/api/pipeline/projectPage?sort=startTimeMillis&order=asc&offset=10&limit=10&token=b7bfa3c4-746d-431b-89f8-de5427e8dedf&_=1645078111349
//		List<String> projectIds = Lists.newArrayList();
//		projectIds.add("75");
//		projectIds.add("76");
//		projectIds.add("77");
//		projectIds.add("78");
//		projectIds.add("81");
//		projectIds.add("82");
//		projectIds.add("134");
//		projectIds.add("182");
//		projectIds.add("193");
//		Pageable pageable = PageRequest.of(Math.floorDiv(10, 10), 10, Direction.DESC, "endTimeMillis");
////		Page<Pipeline> page=service.pageSearch(pageable, "");
//		Date date = null;
//		LocalDateTime startDate=null;
//		for (int i=0; i < 10; i++) {
//			startDate = LocalDateTime.now();
////			logger.error("查询前时间:{}", startDate);
//			Page<Pipeline> page = service.pageSearch(pageable, projectIds, "", "", "", "", date);
//			LocalDateTime endDate = LocalDateTime.now();
////			logger.error("查询前时间:{},查询消耗时间:{}秒", endDate, Duration.between(startDate, endDate).getSeconds());
//			System.err.println("查询消耗："+Duration.between(startDate, endDate).getSeconds());
////		PageResult<Pipeline> result=PageResult.create(page.getTotalElements(), page.getContent());
//
//		}
//
//		logger.info("查询成功");
//	}

	@Test
	public void split() throws Exception {
		
		String url="http://localhost:8080/ci/queue/item/123/";
		logger.info("查询成功:{}",url.lastIndexOf("queue/item"));
	}
	
	
}
