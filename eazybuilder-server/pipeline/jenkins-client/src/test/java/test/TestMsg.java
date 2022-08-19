package test;

import java.io.UnsupportedEncodingException;

import com.alibaba.fastjson.JSONObject;
import com.eazybuilder.ci.entity.MsgProfileType;
import com.eazybuilder.ci.entity.MsgType;
import com.eazybuilder.ci.entity.devops.DevopsInit;
import com.eazybuilder.ci.service.DevopsInitServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.eazybuilder.ci.Application;
import com.eazybuilder.ci.rabbitMq.SendRabbitMq;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestMsg {
	private static Logger logger = LoggerFactory.getLogger(TestMsg.class);


	@Resource
	DevopsInitServiceImpl devopsInitService;

	@Autowired
	SendRabbitMq sendRabbitMq;

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public  void sendDingtalkMsgBymq() {

		JSONObject msgJson = new JSONObject();
		msgJson.put("title", "标题");
		msgJson.put("content", "正文");
//		msgJson.put("dingtalkSecret", dingtalkSecret);
//		msgJson.put("access_token", accessToken);
		msgJson.put("msgType", MsgType.ding);
		msgJson.put("chatMethods", "private");
		msgJson.put("msgProfile", MsgProfileType.issuesStatusUpdate);
		String[] emailArray = new String[]{"kunyangu@eazybuilder.com"};
		msgJson.put("emailArray",emailArray);
		logger.info("发送钉钉消息：{}", msgJson.toJSONString());

		sendRabbitMq.publish(msgJson.toJSONString());

	}

	@Test
	public void findOne() throws UnsupportedEncodingException {
		Iterable<DevopsInit> all = devopsInitService.findAll();
		DevopsInit one = devopsInitService.findOne("43");
		System.out.println(one.getDevopsProjects().size());
	}

	@Test
	public void publish() throws UnsupportedEncodingException {
		/**
		 * 发送消息 参数一：交换机名称 参数二：路由key: rancher.#,符合路由rancher.#规则即可 参数三：发送的消息
		 */

	 
		String msg = "{\r\n" + "\"title\":\"ISS钉钉\",\r\n" + "\"markdownContent\":\"测试发送消息\",\r\n"
				+ "\"access_token\":\"35be4fbc1d460ae42f88aa3e434c254e14dcfcade649634759067632bee5d970\",\r\n"
				+ "\"dingtalkSecret\":\"SEC5b9a6099b4dc945f97392d3d9a9c07d98ba79e0450b9efa287bd74c9e0f31da7\"\r\n"
				+ "}";
		sendRabbitMq.publish(msg);
		 
		logger.info("消息发送成功");
	}

}
