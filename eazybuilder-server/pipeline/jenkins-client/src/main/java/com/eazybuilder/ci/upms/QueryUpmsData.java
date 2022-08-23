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
	 *                                   nickName=, email=zzz@eazybuilder.com}]}
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
