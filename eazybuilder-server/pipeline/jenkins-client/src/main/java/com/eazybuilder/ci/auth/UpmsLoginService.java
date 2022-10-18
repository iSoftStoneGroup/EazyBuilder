package com.eazybuilder.ci.auth;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.eazybuilder.ci.constant.RoleEnum;
import com.eazybuilder.ci.controller.vo.UserVo;
import com.eazybuilder.ci.entity.QTeam;
import com.eazybuilder.ci.entity.Role;
import com.eazybuilder.ci.entity.Team;
import com.eazybuilder.ci.entity.User;
import com.eazybuilder.ci.entity.devops.QDevopsInit;
import com.eazybuilder.ci.service.UserService;
import com.eazybuilder.ci.util.AuthUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RefreshScope //配置文件自动刷新
@Service
public class UpmsLoginService {

    private Logger logger= LoggerFactory.getLogger(UpmsLoginService.class);

    public static final String ROLE_KEY_PREFIX = "eazybuilder";

    public static final String UPMS_TOKEN = "upmsToken";

    @Value("${portal.getUserInfoUrl}")
    private String getUserInfoUrl;

    @Value("${portal.credentialsUrl}")
    private String credentialsUrl;

    @Value("${portal.getProjectGroupUrl}")
    private String getProjectGroupUrl;

    @Value("${portal.getRolesById}")
    private String getRolesById;

    @Autowired
    AccessTokenService accessService;

    @Autowired
    private EntityManager entityManager;

    //查询工厂实体
    private JPAQueryFactory queryFactory;

    @Autowired
    private UserService userService;


    @PostConstruct
    public void initFactory(){
        queryFactory = new JPAQueryFactory(entityManager);
    }





    public String getTokenByCredentials(String credentials)   {
        //{"code":200,"msg":null,"data":"Admin-Expires-In=7200;Admin-Token=c785c90c-d9be-4a80-bbde-330ac5e42122"}
        HttpRequest get = HttpUtil.createGet(credentialsUrl+"?credentials=" + credentials);
        HttpResponse execute = get.execute();
        String json = execute.body();
        logger.debug("请求——>{}:\n{}",credentialsUrl,json);
        Assert.isTrue(StringUtils.isNoneBlank(json),"验签失败！");
        JSONObject res = JSON.parseObject(json);
        Long code = res.getLong("code");
        Assert.isTrue(code!=null || !code.equals(200L),res.getString("msg"));
        String data = res.getString("data");
        String afterAdminTokenLast = StringUtils.substringAfterLast(data, "Admin-Token=");
        String token = StringUtils.substringBefore(afterAdminTokenLast, ";");
        return token;
    }


    public String  getUserInfoByToken(String token) throws Exception {
        String jsonString = httpGetWithAuthorization(getUserInfoUrl,token);
        return jsonString;
    }





    //获取upms返回的对象
    public JSONObject getUpmsObject(String str,String token) {
        JSONObject jsonObject = JSON.parseObject(str);
        Assert.isTrue(jsonObject.getIntValue("code")==200,jsonObject.getString("msg"));
        JSONObject data = jsonObject.getJSONObject("data").getJSONObject("user");
        data.put(UPMS_TOKEN,token);
        return data;
    }


    public  String  accessToken(Map<String,Object> resultMap){
        User vo = (User)resultMap.get("user");
        String token = (String) resultMap.get("access_token");
        accessService.upmsSaveToken(vo,token);
        return token;
    }


    public void saveCurrentAccessUser( Map<String, Object> resultMap){
        AuthUtils.ACCESS_USER.set((UserVo) resultMap.get("user"));
    }



    public UserVo getUerInfo(JSONObject data) throws Exception {
        UserVo user=new UserVo();
        user.setId(data.getString("userId"));
        user.setName(data.getString("userName"));
        user.setPhone(data.getString("phoneNumber"));
        user.setEmail(data.getString("email"));
        User searchUser =  userService.findByEmail(user.getEmail());
        Assert.isTrue(searchUser!=null,"用户信息在CI系统没有找到对应的信息！");
        user.setId(searchUser.getId());
        user.setDepartment(data.getJSONObject("dept").getString("deptName"));
        user.setTitle("");
        List<Role> role=getRole(data);
        user.setRoles(role);
        user.setTeamList(getTeamList(data));
        user.setEmployeeId(data.getInteger("employeeId"));
//        user.setEnterprise(getEnterprise(data));//企业信息，没有设置            
        return user;
    }


    private List<Team>  getTeamList(JSONObject data) throws Exception {
        String json = httpGetWithAuthorization(getProjectGroupUrl, data.getString(UPMS_TOKEN));
        JSONObject objs = JSON.parseObject(json);
        Assert.isTrue(objs.getIntValue("code")==200,objs.getString("msg"));
        JSONArray groups = objs.getJSONArray("data");
        if(groups!=null && !groups.isEmpty()){
            List<Long> upmsGroupIdList =  groups.stream().map(jsonObject-> ((JSONObject)jsonObject).getLongValue("id")).collect(Collectors.toList());
            QDevopsInit qDevopsInit = QDevopsInit.devopsInit;
            QTeam qTeam = QTeam.team;
            List<Team> teams = queryFactory.select(qTeam).from(qDevopsInit,qTeam).
                    where(qDevopsInit.id.eq(qTeam.devopsTeamId).and(qDevopsInit.groupId.in(upmsGroupIdList))).fetch();
            return teams;
        }
        return Lists.newArrayList();
    }
    
    private  List<Role>  getRole(JSONObject data){
        List<Role> list=new ArrayList();
        JSONArray roles  =  data.getJSONArray("roles");
        if(roles==null || roles.size()==0){            
            return list;
        }
        //对所有角色进行筛选，筛选符合ci前缀的角色信息
        Set<Object> ciRoles = roles.stream().filter(role -> {
            String roleKey = ((JSONObject) role).getString("roleKey");
            if(StringUtils.isNotBlank(roleKey) && roleKey.contains(ROLE_KEY_PREFIX)){
                return true;
            }
            return false;            
        }).collect(Collectors.toSet());
        if(ciRoles==null || ciRoles.size()==0){
            list.add(new Role(RoleEnum.tester ));
            return list;
        }
        ciRoles.stream().map(e->judgeRole((JSONObject)e)).distinct().forEach(roleEnum -> list.add(new Role(roleEnum)));
        return list;
    }


    /**
     * 根据rolekey去推断映射到Role类
     * 规定rolekey定义为（eazybuilder:ci:RoleEnum类中定义的角色名）
     * 如果没有匹配到给与默认角色为audit,因为大部分代码判断有这个判断
     * @param roleObject
     * @return
     */
    private RoleEnum judgeRole(JSONObject roleObject){
        String roleKey = roleObject.getString("roleKey");
        String roleName = StringUtils.substringAfterLast(roleKey, ":");
        RoleEnum[] values = RoleEnum.values();
        for (RoleEnum r:values) {
            if(roleName.equalsIgnoreCase(r.name())){
                logger.info("推断出当前用户角色:{}",r.name());
                return r;
            }
        }
        logger.info("没有推断出当前用户角色信息，予以默认角色:{}",RoleEnum.audit.name());
        return RoleEnum.audit;    
    }

 

    /**
     * get请求请求头包含认证信息
     * @param url
     * @param token
     * @return
     * @throws IOException
     */
    private String httpGetWithAuthorization(String url,String token) throws IOException {
        HttpRequest get = HttpUtil.createGet(url);
        get.header("Authorization","Bearer "+ token);
        HttpResponse execute = get.execute();
        Assert.isTrue(execute.getStatus()==200,"请求[ "+url+" ]报错！");        
        String jsonString = execute.body();
        logger.debug("请求——>{}:\n{}",url,jsonString);
        JSONObject jsonObject = JSON.parseObject(jsonString);
        Assert.isTrue(jsonObject.getLongValue("code")==200L,jsonObject.getString("msg"));
        return jsonString;
    }

    public List<Team>  getTeamList(String token) throws Exception {
        String json = httpGetWithAuthorization(getProjectGroupUrl, token);
        JSONObject objs = JSON.parseObject(json);
        Assert.isTrue(objs.getIntValue("code")==200,objs.getString("msg"));
        List<Team> jsonArray=new LinkedList();
        JSONArray groups = objs.getJSONArray("data");
        if(groups!=null && !groups.isEmpty()){
            for (int i = 0; i < groups.size(); i++) {
                Team checkedTeam=new Team();
                JSONObject jsonObject= (JSONObject)groups.get(i);
                checkedTeam.setId(jsonObject.getString("id"));
                checkedTeam.setName(jsonObject.getString("groupName") );
                
                jsonArray.add(checkedTeam);
            }
        }
        return jsonArray;
    }











}
