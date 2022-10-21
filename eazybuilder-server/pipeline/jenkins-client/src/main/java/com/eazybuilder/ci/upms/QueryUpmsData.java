package com.eazybuilder.ci.upms;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eazybuilder.ci.entity.User;
import com.eazybuilder.ci.util.AuthUtils;

import static de.regnis.q.sequence.core.QSequenceAssert.assertNotNull;


@RefreshScope
@Component
public class QueryUpmsData {
	// upms网关地址
	@Value("${upms.gateway.url}")
	public String gatewayUrl;

	private static Logger logger = LoggerFactory.getLogger(QueryUpmsData.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private StringRedisTemplate redisTemplate;

	// upms请求头
	@Resource
	@Qualifier(value = "upmsHeader")
	private HttpHeaders upmsHeader;

	private static final String USERSCACHE = "upms:users";
	private static final String ROLECACHE = "upms:role";
	private static final String GROUPCACHE = "upms:group";
	private static final String GROUPNAMECACHE = "upms:groupname";
	private static final String CURRENTUSERROLECACHE = "upms:currentuser:role";

	
	
	
	
	
	//获取当前登录用户在upms的权限信息
	public String getCurrentUserRole() throws Exception {
		try {
			
			if (Boolean.TRUE.equals(redisTemplate.hasKey(CURRENTUSERROLECACHE))) {
				logger.info("缓存中存在upms user数据，直接返回");
				return redisTemplate.opsForValue().get(CURRENTUSERROLECACHE);
			}
			
			String userListStr=this.getUserList();
			JSONObject userList=JSON.parseObject(userListStr);
			JSONArray rows=userList.getJSONArray("rows");
			 User currentUser=AuthUtils.getCurrentUser();
			Long userId=0L;
			for(int i=0;i<rows.size();i++) {
				JSONObject user=rows.getJSONObject(i);
				if(currentUser.getEmail().equals(user.getString("email"))) {
					userId=user.getLong("userId");
					logger.info("从upms中成功获取当前登录用户信息:{},userid:{}",currentUser.getName(),userId);					
				}
								
			}			
			
			String api = "role/list";
			HttpEntity<JSONObject> jsonObject = restTemplate.exchange(gatewayUrl + api + "?userIds=" + userId, HttpMethod.GET, new HttpEntity<>(upmsHeader),
					JSONObject.class);

			logger.info("返回：{}", jsonObject.getBody());
			redisTemplate.opsForValue().set(CURRENTUSERROLECACHE, jsonObject.getBody().toJSONString());
			redisTemplate.expire(CURRENTUSERROLECACHE, 300, TimeUnit.SECONDS);


			return jsonObject.getBody().toJSONString();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}	
	
	

	//该接口支持批量查询功能，需要改为批量查询
	public JSONObject getRoleByUserId(Long userId) throws Exception {
		try {
			String api = "role/list";
			HttpEntity<JSONObject> jsonObject = restTemplate.exchange(gatewayUrl + api + "?userIds=" + userId, HttpMethod.GET, new HttpEntity<>(upmsHeader),
					JSONObject.class);

			assertNotNull(jsonObject.getBody());
			logger.info("返回：{}", jsonObject.getBody());

			return jsonObject.getBody();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 获取用户列表
	 *
	 * @return:{"msg":"查询成功","total":48,"code":200,"rows":[{"userId":2,"userName":"平台管理员","nickName":"","email":"admin"},{"userId":3,"userName":"企业管理员","nickName":"","email":"erpAdmin"}]}
	 *
	 */
	public String getUserList() {
		try {
			if (Boolean.TRUE.equals(redisTemplate.hasKey(USERSCACHE))) {
				logger.info("缓存中存在upms user数据，直接返回");
				return redisTemplate.opsForValue().get(USERSCACHE);
			}
			String api = "user";
			HttpEntity<JSONObject> jsonObject = restTemplate.exchange(gatewayUrl + api, HttpMethod.GET,
					new HttpEntity<>(upmsHeader), JSONObject.class);

			logger.info("upms用户查询返回结果：{}", jsonObject.getBody());

			redisTemplate.opsForValue().set(USERSCACHE, jsonObject.getBody().toJSONString());
			redisTemplate.expire(USERSCACHE, 300, TimeUnit.SECONDS);

			return jsonObject.getBody().toJSONString();

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;

	}

	/**
	 * 获取角色
	 *
	 * @return:{id=106665204514816, tenantId=null, roleName=ci-admin}
	 *
	 */
	public String getRole() {
		try {
			if (Boolean.TRUE.equals(redisTemplate.hasKey(ROLECACHE))) {
				logger.info("缓存中存在upms role数据，直接返回");
				return redisTemplate.opsForValue().get(ROLECACHE);
			}
			String api = "role";
			HttpEntity<JSONObject> jsonObject = restTemplate.exchange(gatewayUrl + api, HttpMethod.GET,
					new HttpEntity<>(upmsHeader), JSONObject.class);

			logger.info("upms角色查询返回结果：{}", jsonObject.getBody());

			redisTemplate.opsForValue().set(ROLECACHE, jsonObject.getBody().toJSONString());
			redisTemplate.expire(ROLECACHE, 300, TimeUnit.SECONDS);

			return jsonObject.getBody().toJSONString();

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;

	}

	/**
	 * 获取项目组列表
	 *
	 * @return:{"code":200,"data":[{"groupId":121846640934912,"tenantId":101781516320768,"groupName":"devops-2021","users":[{"userId":17760,"tenantId":101781516320768,"userName":"张三","nickName":" ","email":"mlxuef@eazybuilder.com"},{"userId":113203268812803,"tenantId":101781516320768,"userName":"张三","nickName":"张三","email":"eazybuilde@eazybuilder.com"},{"userId":113564717154314,"tenantId":101781516320768,"userName":"张三","nickName":"张三","email":"eazybuilde@eazybuilder.com"}]},{"groupId":107533985382400,"tenantId":101781516320768,"groupName":"DevOps项目组","users":[{"userId":17760,"tenantId":101781516320768,"userName":"张三","nickName":" ","email":"mlxuef@eazybuilder.com"},{"userId":113203266715655,"tenantId":101781516320768,"userName":"张三","nickName":"张三","email":"eazybuilde@eazybuilder.com"},{"userId":113203268812803,"tenantId":101781516320768,"userName":"张三","nickName":"张三","email":"eazybuilde@eazybuilder.com"},{"userId":113564717154314,"tenantId":101781516320768,"userName":"张三","nickName":"张三","email":"eazybuilde@eazybuilder.com"},{"userId":119313467965441,"tenantId":101781516320768,"userName":"张炳恒","nickName":"张炳恒","email":"bhzhange@eazybuilder.com"},{"userId":119700348469249,"tenantId":101781516320768,"userName":"肖志伟","nickName":"肖志伟","email":"zwxiao@eazybuilder.com"}]},{"groupId":119327858622464,"tenantId":101781516320768,"groupName":"eazybuilder","users":[{"userId":2,"tenantId":101781516320768,"userName":"平台管理员","nickName":"","email":"admin"},{"userId":3,"tenantId":101781516320768,"userName":"企业管理员","nickName":"","email":"erpAdmin"}]},{"groupId":119337216114688,"tenantId":101781516320768,"groupName":"idms","users":[{"userId":26327,"tenantId":101781516320768,"userName":"闫晗","nickName":"","email":"hanyanh@eazybuilder.com"},{"userId":113203266715650,"tenantId":101781516320768,"userName":"张三","nickName":"张三","email":"ccxuef@eazybuilder.com"},{"userId":113203266715652,"tenantId":101781516320768,"userName":"张宇飞","nickName":"张宇飞","email":"yfzhangu@eazybuilder.com"},{"userId":113203268812801,"tenantId":101781516320768,"userName":"司程闯","nickName":"司程闯","email":"ccsi@eazybuilder.com"},{"userId":113564478078981,"tenantId":101781516320768,"userName":"陆金鑫","nickName":"陆金鑫","email":"jxluf@eazybuilder.com"},{"userId":119313467965441,"tenantId":101781516320768,"userName":"张炳恒","nickName":"张炳恒","email":"bhzhange@eazybuilder.com"},{"userId":119700348469249,"tenantId":101781516320768,"userName":"肖志伟","nickName":"肖志伟","email":"zwxiao@eazybuilder.com"},{"userId":121537470398465,"tenantId":101781516320768,"userName":"郭林巧","nickName":"郭林巧","email":"lqguo@eazybuilder.com"},{"userId":121537472495616,"tenantId":101781516320768,"userName":"赵治民","nickName":"赵治民","email":"zmzhaoc@eazybuilder.com"},{"userId":121537472495618,"tenantId":101781516320768,"userName":"董超","nickName":"董超","email":"chaodong@eazybuilder.com"},{"userId":121537472495620,"tenantId":101781516320768,"userName":"陈洁(1)","nickName":"陈洁(1)","email":"jiecheni@eazybuilder.com"},{"userId":121537472495624,"tenantId":101781516320768,"userName":"蒋磊","nickName":"蒋磊","email":"leijiangj@eazybuilder.com"},{"userId":121537472495632,"tenantId":101781516320768,"userName":"水迪","nickName":"水迪","email":"dishuic@eazybuilder.com"},{"userId":121537472495636,"tenantId":101781516320768,"userName":"张凯龙","nickName":"张凯龙","email":"klzhanga@eazybuilder.com"},{"userId":121537474592773,"tenantId":101781516320768,"userName":"李振华","nickName":"李振华","email":"zhliq@eazybuilder.com"},{"userId":121537474592775,"tenantId":101781516320768,"userName":"黄橙","nickName":"黄橙","email":"chenghuangf@eazybuilder.com"},{"userId":121537474592781,"tenantId":101781516320768,"userName":"皇永伟","nickName":"皇永伟","email":"ywhuangl@eazybuilder.com"},{"userId":121537476689921,"tenantId":101781516320768,"userName":"陈理辉(1)","nickName":"陈理辉(1)","email":"lhchencf@eazybuilder.com"},{"userId":121537476689937,"tenantId":101781516320768,"userName":"王明阳","nickName":"王明阳","email":"mywangce@eazybuilder.com"},{"userId":121537478787075,"tenantId":101781516320768,"userName":"陈剑(1)","nickName":"陈剑(1)","email":"jianchency@eazybuilder.com"},{"userId":121537478787078,"tenantId":101781516320768,"userName":"陈龙","nickName":"陈龙","email":"longchende@eazybuilder.com"},{"userId":121537478787080,"tenantId":101781516320768,"userName":"郭浩","nickName":"郭浩","email":"haoguocc@eazybuilder.com"},{"userId":121537589936130,"tenantId":101781516320768,"userName":"崔桂彬","nickName":"崔桂彬","email":"gbcui@eazybuilder.com"},{"userId":121537675919361,"tenantId":101781516320768,"userName":"朱风松","nickName":"朱风松","email":"fszhu@eazybuilder.com"},{"userId":121537675919363,"tenantId":101781516320768,"userName":"李鑫(1)","nickName":"李鑫(1)","email":"xinliz@eazybuilder.com"},{"userId":121537675919379,"tenantId":101781516320768,"userName":"邵健","nickName":"邵健","email":"jianshaoa@eazybuilder.com"},{"userId":121537678016521,"tenantId":101781516320768,"userName":"马灵娟","nickName":"马灵娟","email":"ljmak@eazybuilder.com"},{"userId":121537678016523,"tenantId":101781516320768,"userName":"俞磊","nickName":"俞磊","email":"leiyut@eazybuilder.com"},{"userId":121537678016525,"tenantId":101781516320768,"userName":"秦士磊","nickName":"秦士磊","email":"slqind@eazybuilder.com"},{"userId":121537680113667,"tenantId":101781516320768,"userName":"刘德斌","nickName":"刘德斌","email":"dbliul@eazybuilder.com"},{"userId":121538028240897,"tenantId":101781516320768,"userName":"代伟龙","nickName":"代伟龙","email":"wldaif@eazybuilder.com"},{"userId":121538028240899,"tenantId":101781516320768,"userName":"苟壮壮","nickName":"苟壮壮","email":"zzgou@eazybuilder.com"}]},{"groupId":120208054288384,"tenantId":101781516320768,"groupName":"devops自动化测试组","users":[{"userId":17760,"tenantId":101781516320768,"userName":"张三","nickName":" ","email":"mlxuef@eazybuilder.com"}]},{"groupId":121712712613888,"tenantId":101781516320768,"groupName":"测试用户列表"},{"groupId":121716206469120,"tenantId":101781516320768,"groupName":"测试用户列表1"},{"groupId":121716531527680,"tenantId":101781516320768,"groupName":"测试用户列表2","users":[{"userId":2,"tenantId":101781516320768,"userName":"平台管理员","nickName":"","email":"admin"},{"userId":3,"tenantId":101781516320768,"userName":"企业管理员","nickName":"","email":"erpAdmin"},{"userId":8,"tenantId":101781516320768,"userName":"黄丽改","nickName":"","email":"lghuang@eazybuilder.com"},{"userId":9,"tenantId":101781516320768,"userName":"董春玲","nickName":"","email":"cldong@eazybuilder.com"},{"userId":17760,"tenantId":101781516320768,"userName":"张三","nickName":" ","email":"mlxuef@eazybuilder.com"},{"userId":26309,"tenantId":101781516320768,"userName":"刘明明","nickName":"","email":"mmliuh@eazybuilder.com"},{"userId":26322,"tenantId":101781516320768,"userName":"张智慧","nickName":"","email":"zhzhangx@eazybuilder.com"},{"userId":26323,"tenantId":101781516320768,"userName":"李向楠","nickName":"","email":"xnlif@eazybuilder.com"},{"userId":26325,"tenantId":101781516320768,"userName":"杨文君","nickName":"","email":"wjyangc@eazybuilder.com"},{"userId":26326,"tenantId":101781516320768,"userName":"刘郁","nickName":"","email":"yuliuc@eazybuilder.com"}]},{"groupId":121838866792448,"tenantId":101781516320768,"groupName":"临时演示","users":[{"userId":17760,"tenantId":101781516320768,"userName":"张三","nickName":" ","email":"mlxuef@eazybuilder.com"}]},{"groupId":107375264530432,"tenantId":101781516320768,"groupName":"new-group1","users":[{"userId":2,"tenantId":101781516320768,"userName":"平台管理员","nickName":"","email":"admin"},{"userId":3,"tenantId":101781516320768,"userName":"企业管理员","nickName":"","email":"erpAdmin"}]},{"groupId":121846708043776,"tenantId":101781516320768,"groupName":"devops2021","users":[{"userId":17760,"tenantId":101781516320768,"userName":"张三","nickName":" ","email":"mlxuef@eazybuilder.com"},{"userId":113203268812803,"tenantId":101781516320768,"userName":"张三","nickName":"张三","email":"eazybuilde@eazybuilder.com"},{"userId":113564717154314,"tenantId":101781516320768,"userName":"张三","nickName":"张三","email":"eazybuilde@eazybuilder.com"}]},{"groupId":121846968090624,"tenantId":101781516320768,"groupName":"devops-tool-group","users":[{"userId":17760,"tenantId":101781516320768,"userName":"张三","nickName":" ","email":"mlxuef@eazybuilder.com"},{"userId":113203268812803,"tenantId":101781516320768,"userName":"张三","nickName":"张三","email":"eazybuilde@eazybuilder.com"},{"userId":113564717154314,"tenantId":101781516320768,"userName":"张三","nickName":"张三","email":"eazybuilde@eazybuilder.com"}]},{"groupId":121847169417216,"tenantId":101781516320768,"groupName":"tool-group","users":[{"userId":17760,"tenantId":101781516320768,"userName":"张三","nickName":" ","email":"mlxuef@eazybuilder.com"},{"userId":113203268812803,"tenantId":101781516320768,"userName":"张三","nickName":"张三","email":"eazybuilde@eazybuilder.com"},{"userId":113564717154314,"tenantId":101781516320768,"userName":"张三","nickName":"张三","email":"eazybuilde@eazybuilder.com"}]},{"groupId":121847272177664,"tenantId":101781516320768,"groupName":"tool1234","users":[{"userId":17760,"tenantId":101781516320768,"userName":"张三","nickName":" ","email":"mlxuef@eazybuilder.com"},{"userId":113203268812803,"tenantId":101781516320768,"userName":"张三","nickName":"张三","email":"eazybuilde@eazybuilder.com"},{"userId":113564717154314,"tenantId":101781516320768,"userName":"张三","nickName":"张三","email":"eazybuilde@eazybuilder.com"}]},{"groupId":121847353966592,"tenantId":101781516320768,"groupName":"wwwwwwwwwwwwwww","users":[{"userId":17760,"tenantId":101781516320768,"userName":"张三","nickName":" ","email":"mlxuef@eazybuilder.com"},{"userId":113203268812803,"tenantId":101781516320768,"userName":"张三","nickName":"张三","email":"eazybuilde@eazybuilder.com"},{"userId":113564717154314,"tenantId":101781516320768,"userName":"张三","nickName":"张三","email":"eazybuilde@eazybuilder.com"}]},{"groupId":121856971505664,"tenantId":101781516320768,"groupName":"ipaas","users":[{"userId":113564478078977,"tenantId":101781516320768,"userName":"张三","nickName":"张三","email":"zpzhaoa@eazybuilder.com"}]},{"groupId":121867251744768,"tenantId":101781516320768,"groupName":"devops-tmp","users":[{"userId":8,"tenantId":101781516320768,"userName":"黄丽改","nickName":"","email":"lghuang@eazybuilder.com"},{"userId":17760,"tenantId":101781516320768,"userName":"张三","nickName":" ","email":"mlxuef@eazybuilder.com"}]},{"groupId":4,"tenantId":101781516320768,"groupName":"司库产品项目","users":[{"userId":2,"tenantId":101781516320768,"userName":"平台管理员","nickName":"","email":"admin"},{"userId":8,"tenantId":101781516320768,"userName":"黄丽改","nickName":"","email":"lghuang@eazybuilder.com"},{"userId":9,"tenantId":101781516320768,"userName":"董春玲","nickName":"","email":"cldong@eazybuilder.com"},{"userId":17760,"tenantId":101781516320768,"userName":"张三","nickName":" ","email":"mlxuef@eazybuilder.com"},{"userId":285718,"tenantId":101781516320768,"userName":"禅道用户","nickName":"","email":"zentao@eazybuilder.com"}]},{"groupId":26312,"tenantId":101781516320768,"groupName":"银企平台","users":[{"userId":2,"tenantId":101781516320768,"userName":"平台管理员","nickName":"","email":"admin"},{"userId":8,"tenantId":101781516320768,"userName":"黄丽改","nickName":"","email":"lghuang@eazybuilder.com"},{"userId":9,"tenantId":101781516320768,"userName":"董春玲","nickName":"","email":"cldong@eazybuilder.com"},{"userId":26309,"tenantId":101781516320768,"userName":"刘明明","nickName":"","email":"mmliuh@eazybuilder.com"},{"userId":1834251,"tenantId":101781516320768,"userName":"张晓","nickName":"","email":"xiaozhangp@eazybuilder.com"}]},{"groupId":26315,"tenantId":101781516320768,"groupName":"财务公司运营平台2.0","users":[{"userId":2,"tenantId":101781516320768,"userName":"平台管理员","nickName":"","email":"admin"},{"userId":3,"tenantId":101781516320768,"userName":"企业管理员","nickName":"","email":"erpAdmin"},{"userId":8,"tenantId":101781516320768,"userName":"黄丽改","nickName":"","email":"lghuang@eazybuilder.com"},{"userId":9,"tenantId":101781516320768,"userName":"董春玲","nickName":"","email":"cldong@eazybuilder.com"},{"userId":17760,"tenantId":101781516320768,"userName":"张三","nickName":" ","email":"mlxuef@eazybuilder.com"},{"userId":26322,"tenantId":101781516320768,"userName":"张智慧","nickName":"","email":"zhzhangx@eazybuilder.com"},{"userId":26323,"tenantId":101781516320768,"userName":"李向楠","nickName":"","email":"xnlif@eazybuilder.com"},{"userId":26325,"tenantId":101781516320768,"userName":"杨文君","nickName":"","email":"wjyangc@eazybuilder.com"},{"userId":26326,"tenantId":101781516320768,"userName":"刘郁","nickName":"","email":"yuliuc@eazybuilder.com"},{"userId":26327,"tenantId":101781516320768,"userName":"闫晗","nickName":"","email":"hanyanh@eazybuilder.com"},{"userId":26329,"tenantId":101781516320768,"userName":"陶彦强","nickName":"","email":"yqtaoc@eazybuilder.com"},{"userId":26331,"tenantId":101781516320768,"userName":"杨英","nickName":"","email":"yingyangh@eazybuilder.com"},{"userId":1142174,"tenantId":101781516320768,"userName":"郗文铖","nickName":"","email":"wcxi@eazybuilder.com"},{"userId":1834251,"tenantId":101781516320768,"userName":"张晓","nickName":"","email":"xiaozhangp@eazybuilder.com"},{"userId":2027951,"tenantId":101781516320768,"userName":"张溪倬","nickName":"","email":"xzzhangr@eazybuilder.com"}]},{"groupId":166468,"tenantId":101781516320768,"groupName":"财务公司产品2019年票据池项目","users":[{"userId":2,"tenantId":101781516320768,"userName":"平台管理员","nickName":"","email":"admin"},{"userId":8,"tenantId":101781516320768,"userName":"黄丽改","nickName":"","email":"lghuang@eazybuilder.com"},{"userId":9,"tenantId":101781516320768,"userName":"董春玲","nickName":"","email":"cldong@eazybuilder.com"},{"userId":17760,"tenantId":101781516320768,"userName":"张三","nickName":" ","email":"mlxuef@eazybuilder.com"},{"userId":166470,"tenantId":101781516320768,"userName":"王璞","nickName":"","email":"wangpu@eazybuilder.com"},{"userId":169979,"tenantId":101781516320768,"userName":"曹佳","nickName":"","email":"jiacao@eazybuilder.com"},{"userId":419516,"tenantId":101781516320768,"userName":"林雪文","nickName":"","email":"xwlinc@eazybuilder.com"},{"userId":419517,"tenantId":101781516320768,"userName":"孙亮","nickName":"","email":"liangsun@eazybuilder.com"},{"userId":1746655,"tenantId":101781516320768,"userName":"陈秘","nickName":"","email":"michen@eazybuilder.com"},{"userId":1746656,"tenantId":101781516320768,"userName":"朱茜","nickName":"","email":"qianzhuc@eazybuilder.com"},{"userId":1746657,"tenantId":101781516320768,"userName":"欧月","nickName":"","email":"yueouc@eazybuilder.com"},{"userId":1750780,"tenantId":101781516320768,"userName":"周玲(1)","nickName":"","email":"lingzhoud@eazybuilder.com"},{"userId":1750784,"tenantId":101781516320768,"userName":"姚丽苹","nickName":"","email":"lpyao@eazybuilder.com"},{"userId":1750786,"tenantId":101781516320768,"userName":"范慈英","nickName":"","email":"cyfana@eazybuilder.com"},{"userId":1750787,"tenantId":101781516320768,"userName":"昌思敏","nickName":"","email":"smchang@eazybuilder.com"},{"userId":2075140,"tenantId":101781516320768,"userName":"王瑞禹","nickName":"","email":"rywangg@eazybuilder.com"},{"userId":2075141,"tenantId":101781516320768,"userName":"宋明红","nickName":"","email":"mhsonga@eazybuilder.com"},{"userId":2075142,"tenantId":101781516320768,"userName":"张旭雯","nickName":"","email":"xwzhangap@eazybuilder.com"}]},{"groupId":316098,"tenantId":101781516320768,"groupName":"中广核项目-票据接口","users":[{"userId":2,"tenantId":101781516320768,"userName":"平台管理员","nickName":"","email":"admin"},{"userId":8,"tenantId":101781516320768,"userName":"黄丽改","nickName":"","email":"lghuang@eazybuilder.com"},{"userId":17760,"tenantId":101781516320768,"userName":"张三","nickName":" ","email":"mlxuef@eazybuilder.com"},{"userId":26326,"tenantId":101781516320768,"userName":"刘郁","nickName":"","email":"yuliuc@eazybuilder.com"},{"userId":405437,"tenantId":101781516320768,"userName":"孙轩","nickName":"","email":"xuansunc@eazybuilder.com"}]},{"groupId":679913,"tenantId":101781516320768,"groupName":"票据交易","users":[{"userId":2,"tenantId":101781516320768,"userName":"平台管理员","nickName":"","email":"admin"},{"userId":8,"tenantId":101781516320768,"userName":"黄丽改","nickName":"","email":"lghuang@eazybuilder.com"},{"userId":9,"tenantId":101781516320768,"userName":"董春玲","nickName":"","email":"cldong@eazybuilder.com"},{"userId":17760,"tenantId":101781516320768,"userName":"张三","nickName":" ","email":"mlxuef@eazybuilder.com"},{"userId":26309,"tenantId":101781516320768,"userName":"刘明明","nickName":"","email":"mmliuh@eazybuilder.com"},{"userId":45197,"tenantId":101781516320768,"userName":"陈霞","nickName":"","email":"xiachen@eazybuilder.com"},{"userId":123799,"tenantId":101781516320768,"userName":"周府林","nickName":"","email":"flzhou@eazybuilder.com"},{"userId":1834251,"tenantId":101781516320768,"userName":"张晓","nickName":"","email":"xiaozhangp@eazybuilder.com"}]},{"groupId":1200667,"tenantId":101781516320768,"groupName":"demo项目","users":[{"userId":1200666,"tenantId":101781516320768,"userName":"apitest","nickName":"","email":"apitest@163.com"},{"userId":1577534,"tenantId":101781516320768,"userName":"demo","nickName":"","email":"demo@eazybuilder.com"},{"userId":2076190,"tenantId":101781516320768,"userName":"代芷萁(1)","nickName":"","email":"zqdai@eazybuilder.com"}]},{"groupId":1302599,"tenantId":101781516320768,"groupName":"西安团队学习项目","users":[{"userId":45197,"tenantId":101781516320768,"userName":"陈霞","nickName":"","email":"xiachen@eazybuilder.com"},{"userId":123799,"tenantId":101781516320768,"userName":"周府林","nickName":"","email":"flzhou@eazybuilder.com"},{"userId":405437,"tenantId":101781516320768,"userName":"孙轩","nickName":"","email":"xuansunc@eazybuilder.com"},{"userId":1302189,"tenantId":101781516320768,"userName":"刘晓龙","nickName":"","email":"xlliu@eazybuilder.com"}]},{"groupId":1578860,"tenantId":101781516320768,"groupName":"中油项目","users":[{"userId":8,"tenantId":101781516320768,"userName":"黄丽改","nickName":"","email":"lghuang@eazybuilder.com"},{"userId":9,"tenantId":101781516320768,"userName":"董春玲","nickName":"","email":"cldong@eazybuilder.com"},{"userId":17760,"tenantId":101781516320768,"userName":"张三","nickName":" ","email":"mlxuef@eazybuilder.com"},{"userId":26309,"tenantId":101781516320768,"userName":"刘明明","nickName":"","email":"mmliuh@eazybuilder.com"},{"userId":26322,"tenantId":101781516320768,"userName":"张智慧","nickName":"","email":"zhzhangx@eazybuilder.com"},{"userId":26325,"tenantId":101781516320768,"userName":"杨文君","nickName":"","email":"wjyangc@eazybuilder.com"},{"userId":26326,"tenantId":101781516320768,"userName":"刘郁","nickName":"","email":"yuliuc@eazybuilder.com"},{"userId":166470,"tenantId":101781516320768,"userName":"王璞","nickName":"","email":"wangpu@eazybuilder.com"},{"userId":419517,"tenantId":101781516320768,"userName":"孙亮","nickName":"","email":"liangsun@eazybuilder.com"},{"userId":1834251,"tenantId":101781516320768,"userName":"张晓","nickName":"","email":"xiaozhangp@eazybuilder.com"},{"userId":1835527,"tenantId":101781516320768,"userName":"刘龙","nickName":"","email":"longliuf@eazybuilder.com"},{"userId":1835528,"tenantId":101781516320768,"userName":"王亚辉","nickName":"","email":"yhwangcu@eazybuilder.com"},{"userId":1835529,"tenantId":101781516320768,"userName":"任长明","nickName":"","email":"cmrenc@eazybuilder.com"}]},{"groupId":1849929,"tenantId":101781516320768,"groupName":"演示demo项目","users":[{"userId":8,"tenantId":101781516320768,"userName":"黄丽改","nickName":"","email":"lghuang@eazybuilder.com"}]},{"groupId":1914600,"tenantId":101781516320768,"groupName":"T6项目","users":[{"userId":8,"tenantId":101781516320768,"userName":"黄丽改","nickName":"","email":"lghuang@eazybuilder.com"},{"userId":1914599,"tenantId":101781516320768,"userName":"周晨宇","nickName":"","email":"cyzhouc@eazybuilder.com"}]},{"groupId":2042345,"tenantId":101781516320768,"groupName":"自动化测试项目组","users":[{"userId":17760,"tenantId":101781516320768,"userName":"张三","nickName":" ","email":"mlxuef@eazybuilder.com"}]},{"groupId":2047871,"tenantId":101781516320768,"groupName":"2020年西电财务小核心渠道开发项目","users":[{"userId":17760,"tenantId":101781516320768,"userName":"张三","nickName":" ","email":"mlxuef@eazybuilder.com"},{"userId":45197,"tenantId":101781516320768,"userName":"陈霞","nickName":"","email":"xiachen@eazybuilder.com"},{"userId":405437,"tenantId":101781516320768,"userName":"孙轩","nickName":"","email":"xuansunc@eazybuilder.com"},{"userId":2047870,"tenantId":101781516320768,"userName":"张兴昱","nickName":"","email":"xyzhanggr@eazybuilder.com"},{"userId":2222764,"tenantId":101781516320768,"userName":"王凯","nickName":"","email":"kaiwangdp@eazybuilder.com"}]},{"groupId":2048525,"tenantId":101781516320768,"groupName":"西安团队学习项目1","users":[{"userId":2,"tenantId":101781516320768,"userName":"平台管理员","nickName":"","email":"admin"},{"userId":3,"tenantId":101781516320768,"userName":"企业管理员","nickName":"","email":"erpAdmin"},{"userId":8,"tenantId":101781516320768,"userName":"黄丽改","nickName":"","email":"lghuang@eazybuilder.com"},{"userId":9,"tenantId":101781516320768,"userName":"董春玲","nickName":"","email":"cldong@eazybuilder.com"},{"userId":17760,"tenantId":101781516320768,"userName":"张三","nickName":" ","email":"mlxuef@eazybuilder.com"},{"userId":26309,"tenantId":101781516320768,"userName":"刘明明","nickName":"","email":"mmliuh@eazybuilder.com"},{"userId":26322,"tenantId":101781516320768,"userName":"张智慧","nickName":"","email":"zhzhangx@eazybuilder.com"},{"userId":26323,"tenantId":101781516320768,"userName":"李向楠","nickName":"","email":"xnlif@eazybuilder.com"},{"userId":26325,"tenantId":101781516320768,"userName":"杨文君","nickName":"","email":"wjyangc@eazybuilder.com"},{"userId":26326,"tenantId":101781516320768,"userName":"刘郁","nickName":"","email":"yuliuc@eazybuilder.com"}]},{"groupId":2077182,"tenantId":101781516320768,"groupName":"金融级项目","users":[{"userId":2,"tenantId":101781516320768,"userName":"平台管理员","nickName":"","email":"admin"},{"userId":8,"tenantId":101781516320768,"userName":"黄丽改","nickName":"","email":"lghuang@eazybuilder.com"},{"userId":9,"tenantId":101781516320768,"userName":"董春玲","nickName":"","email":"cldong@eazybuilder.com"},{"userId":17760,"tenantId":101781516320768,"userName":"张三","nickName":" ","email":"mlxuef@eazybuilder.com"},{"userId":26322,"tenantId":101781516320768,"userName":"张智慧","nickName":"","email":"zhzhangx@eazybuilder.com"},{"userId":26323,"tenantId":101781516320768,"userName":"李向楠","nickName":"","email":"xnlif@eazybuilder.com"},{"userId":26325,"tenantId":101781516320768,"userName":"杨文君","nickName":"","email":"wjyangc@eazybuilder.com"},{"userId":26326,"tenantId":101781516320768,"userName":"刘郁","nickName":"","email":"yuliuc@eazybuilder.com"},{"userId":26327,"tenantId":101781516320768,"userName":"闫晗","nickName":"","email":"hanyanh@eazybuilder.com"},{"userId":26329,"tenantId":101781516320768,"userName":"陶彦强","nickName":"","email":"yqtaoc@eazybuilder.com"},{"userId":26331,"tenantId":101781516320768,"userName":"杨英","nickName":"","email":"yingyangh@eazybuilder.com"}]},{"groupId":2377823,"tenantId":101781516320768,"groupName":"验证bug项目","users":[{"userId":26329,"tenantId":101781516320768,"userName":"陶彦强","nickName":"","email":"yqtaoc@eazybuilder.com"},{"userId":1750780,"tenantId":101781516320768,"userName":"周玲(1)","nickName":"","email":"lingzhoud@eazybuilder.com"}]}],"error":false,"message":{"code":"200","description":"操作成功"},"msg":"操作成功","success":true}

	 *
	 */
	public String getGroupList() {
		try {
			if (Boolean.TRUE.equals(redisTemplate.hasKey(GROUPCACHE))) {
				logger.info("缓存中存在upms group数据，直接返回");
				return redisTemplate.opsForValue().get(GROUPCACHE);
			}
			String api = "group";
			HttpEntity<JSONObject> jsonObject = restTemplate.exchange(gatewayUrl + api, HttpMethod.GET,
					new HttpEntity<>(upmsHeader), JSONObject.class);

			logger.info("upms项目组列表查询返回结果：{}", jsonObject.getBody());

			JSONArray array=jsonObject.getBody().getJSONArray("data");
			for(int i=0;i<array.size();i++) {
				redisTemplate.opsForValue().set(GROUPNAMECACHE+":"+array.getJSONObject(i).getString("groupName"),array.getJSONObject(i).toJSONString());
			}

			redisTemplate.opsForValue().set(GROUPCACHE, jsonObject.getBody().getJSONObject("data").toJSONString());
			redisTemplate.expire(GROUPCACHE, 300, TimeUnit.SECONDS);

			return jsonObject.getBody().toJSONString();

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;

	}

	public String  getGroupByName(String groupName) {
		if (null !=redisTemplate.opsForValue().get(GROUPNAMECACHE+":"+groupName)) {
			logger.info("缓存中存在upms group数据，直接返回:{}",redisTemplate.opsForValue().get(GROUPNAMECACHE+":"+groupName));
			return redisTemplate.opsForValue().get(GROUPNAMECACHE+":"+groupName);
		}else {
			getGroupList();
		}

		return redisTemplate.opsForValue().get(GROUPNAMECACHE+":"+groupName);
	}


	/**
	 * 查询一个项目组下的人员
	 *
	 * @return:{groupId=107533985382400, tenantId=101781516320768,
	 *                                   groupName=DevOps项目组, users=[{userId=17760,
	 *                                   tenantId=101781516320768, userName=张三,
	 *                                   nickName=, email=mlxuef@eazybuilder.com}]}
	 *
	 */
	public String getGroup(String groupId) {
		try {
//			String group = GROUPCACHE + ":" + groupId;
//			if (Boolean.TRUE.equals(redisTemplate.hasKey(group))) {
//				logger.info("缓存中存在upms group数据，直接返回");
//				return redisTemplate.opsForValue().get(group);
//			}
			String api = "group/" + groupId;
			HttpEntity<JSONObject> jsonObject = restTemplate.exchange(gatewayUrl + api, HttpMethod.GET,
					new HttpEntity<>(upmsHeader), JSONObject.class);

			logger.info("upms项目组查询返回结果：{}", jsonObject.getBody());

//			redisTemplate.opsForValue().set(group, jsonObject.getBody().toJSONString());
//			redisTemplate.expire(group, 300, TimeUnit.SECONDS);

			return jsonObject.getBody().toJSONString();

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;

	}

	/**
	 * 创建项目组
	 *
	 * @param groupName
	 * @return
	 * @throws Exception
	 */
	public long createGroup(String groupName) throws Exception {
		try {
			logger.info("查询项目组在upms是否存在：{}", this.getGroupByName(groupName));
			if(null!=this.getGroupByName(groupName)) {
				logger.info("upms已经存在这个项目组了，不需要创建，直接取出");
				return JSONObject.parseObject(this.getGroupByName(groupName)).getLong("groupId");
			}

			String api = "group";
			JSONObject requestParam = new JSONObject();
			requestParam.put("groupName", groupName);

			HttpEntity<JSONObject> jsonObject = restTemplate.postForEntity(gatewayUrl + api,
					new HttpEntity<>(requestParam, upmsHeader), JSONObject.class);
			JSONObject body = jsonObject.getBody();
			logger.info("upms创建项目组返回结果：{}", body);

			if(body.containsKey("code")&&!body.getString("code").equals("200")){
				throw new Exception(body.getString("msg"));
			}
			return body.getJSONObject("data").getLong("groupId");
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("调用upms创建项目组出现异常: "+ e.getMessage(),e);
		}
	}

	/**
	 * 将已有用户，添加到项目组中
	 *
	 * @param groupId
	 * @param useridArr
	 * @return
	 * @throws Exception
	 */
	public String bindUserToGroup(Long groupId, List<String> useridArr) {
		try {

			String api = "group/" + groupId.toString() + "/users";

			JSONArray requestParam = new JSONArray();
			for (int i = 0; i < useridArr.size(); i++) {
				JSONObject user = new JSONObject();
				user.put("userId", useridArr.get(i));
				requestParam.add(user);
			}

			HttpEntity<JSONObject> jsonObject = restTemplate.postForEntity(gatewayUrl + api,
					new HttpEntity<>(requestParam, upmsHeader), JSONObject.class);

			logger.info("upms项目组新增人员返回结果：{}", jsonObject.getBody());

			return jsonObject.getBody().toJSONString();

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

}
