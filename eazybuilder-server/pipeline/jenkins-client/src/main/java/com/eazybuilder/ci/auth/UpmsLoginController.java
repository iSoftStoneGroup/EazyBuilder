package com.eazybuilder.ci.auth;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.eazybuilder.ci.controller.vo.UserVo;
import com.eazybuilder.ci.entity.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RefreshScope //配置文件自动刷新
@RestController
public class UpmsLoginController {
    Logger logger=LoggerFactory.getLogger(this.getClass());
             
    
    @Autowired
    AccessTokenService accessService;    
  
    @Autowired
    UpmsLoginService upmsLoginService;


    @Value("${portal.used:false}")
    private Boolean used;

    @Value("${portal.loginUrl}")
    private String loginUrl;

    @Value("${portal.credentialsUrl}")
    private String credentialsUrl;

    @Value("${portal.getUserInfoUrl}")
    private String getUserInfoUrl;

    @Value("${portal.getMenusForCurrentUser}")
    private String getMenusForCurrentUser;


    
    




    
    @GetMapping(value = "/getPortalInfo")
    public Map<String,Object> getPortalInfo(){
        Map<String,Object> map= Maps.newHashMap();
        map.put("used",used);
        map.put("loginUrl",loginUrl);
        map.put("credentialsUrl",credentialsUrl);
        map.put("getUserInfoUrl",getUserInfoUrl);
        map.put("getMenusForCurrentUser",getMenusForCurrentUser);
        return map;
    }

    /**
     * 验签，使用签名去校验统一登录门户网站接口
     * @return
     */
    @PostMapping(value = "/signatureVerification")
    @ResponseBody
    public  Map  signatureVerification(@RequestBody Map<String,Object> map) throws Exception {
        boolean debugEnabled = logger.isDebugEnabled();
        String credentials = (String) map.get("credentials");
        String token = upmsLoginService.getTokenByCredentials(credentials);
        if(debugEnabled){
            logger.info ("获取的upms——token信息为--->{}",token);
        }
        String userInfoJsonStr = upmsLoginService.getUserInfoByToken(token);
        if(debugEnabled){
            logger.debug ("获取的用户信息:\n{}",userInfoJsonStr);
        }
        JSONObject upmsObject = upmsLoginService.getUpmsObject(userInfoJsonStr, token);
        UserVo user = upmsLoginService.getUerInfo(upmsObject);
        Map tokenMap=Maps.newHashMap();
        tokenMap.put("access_token", token);
        tokenMap.put("user",user);
        //用户是否被指定为某个项目的配置管理员 这里默认为false
        tokenMap.put("isCM", false);
        if(debugEnabled){
            logger.info ("返回到前端的值:\n{}", JSON.toJSONString(tokenMap));
        }
        //存储全局凭证信息
        upmsLoginService.accessToken(tokenMap);
        upmsLoginService.saveCurrentAccessUser(tokenMap);
        return tokenMap;
    }
    
    @PostMapping(value = "/getTeamFromUpms")
    @ResponseBody
    public  List<Team>  getTeamFromUpms(@RequestParam String token) throws Exception {
        return upmsLoginService.getTeamList(token);
    }
    


    
}
