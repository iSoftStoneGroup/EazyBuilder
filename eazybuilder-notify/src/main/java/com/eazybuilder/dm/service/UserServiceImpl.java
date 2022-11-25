package com.eazybuilder.dm.service;


import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eazybuilder.dm.base.AbstractCommonServiceImpl;
import com.eazybuilder.dm.base.CommonService;
import com.eazybuilder.dm.dao.TeamDao;
import com.eazybuilder.dm.dao.UserDao;
import com.eazybuilder.dm.entity.QTeam;
import com.eazybuilder.dm.entity.QUser;
import com.eazybuilder.dm.entity.Team;
import com.eazybuilder.dm.entity.User;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;


/**
 *  
 */
@RefreshScope
@Service
public class UserServiceImpl extends AbstractCommonServiceImpl<UserDao, User> implements CommonService<User> {
    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Value("${upms.gateway.url}")
    private  String url;

    @Value("${upms.gateway.token}")
    private  String token;

    public List<User> findByEmail(String[]  emailArray){
        return (List<User>) dao.findAll(QUser.user.email.in(emailArray));
    }

    public Integer  getEmployeeIdByEmail(String email){
        Optional<User> one = dao.findOne(QUser.user.email.eq(email));
        Assert.isTrue(one.isPresent(),"在数据库中没有找到邮箱[ "+email+" ]的用户信息！");
        User user = one.get();
        Integer employeeId =null;
        if(null !=user.getEmployeeId() ){
            employeeId=user.getEmployeeId();
        }else {
            employeeId= getEmployeeIdFromUpms(user);
            user.setEmployeeId(employeeId);
            dao.save(user);
        }
        return employeeId;

    }

    /**
        * @param user
     * @return
     */
    private Integer getEmployeeIdFromUpms(User user){
        UrlBuilder urlBuilder = UrlBuilder.of(url, Charsets.UTF_8);
        if( StringUtils.isNotBlank(user.getEmail())){
            urlBuilder.addQuery("email",user.getEmail());
        }else if( StringUtils.isNotBlank(user.getPhoneNumber())){
            urlBuilder.addQuery("phoneNumber",user.getPhoneNumber());
        }else if(StringUtils.isNotBlank(user.getUserId())){
            urlBuilder.addQuery("userId",user.getUserId());
        }
        Assert.isTrue(StringUtils.isNotBlank(urlBuilder.getQuery().toString()),"没有可用的用户邮箱，手机号，用户id去查询用户工号！【 "+ JSON.toJSONString(user)+" 】");
        HttpRequest get = HttpUtil.createGet(urlBuilder.build());
        get.timeout(3000);
        get.auth(token);
        get.header("Grant-Type","api_token");
        HttpResponse execute = get.execute();
        logger.info("请求upms信息:\n{}", get.toString());
        String body = execute.body();
        JSONObject jsonObject = JSON.parseObject(body);
        int status = jsonObject.getIntValue("code");
        logger.info("请求upms用户信息响应信息为:\n{}",execute.toString());
        Assert.isTrue( status==200 ,"请求upms查询用户工号失败！");
        return jsonObject.getJSONObject("data").getInteger("employeeId");
    }


}
