package com.eazybuilder.ci.service;


import com.eazybuilder.ci.entity.devops.RedmineSprint;
import com.eazybuilder.ci.util.HttpUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class SonarqubeService {


    @Value("${sonar.user}")
    private String user;

    @Value("${sonar.password}")
    private String password;

    @Value("${sonar.languagesListUrl}")
    private String languagesListUrl;

    @Value("${sonar.qualityprofilesSearchUrl}")
    private String qualityprofilesSearchUrl;

    @Value("${sonar.qualityprofilesAddProjectUrl}")
    private String qualityprofilesAddProjectUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String SONARQUBELANGUAGES = "SONARQUBE:LANGUAGESLIST";

    private static final String SONARQUBEQUALITYPROFILE = "SONARQUBE:QUALITYPROFILE";

    private static Logger logger = LoggerFactory.getLogger(SonarqubeService.class);


    public String getLanguagesList() throws Exception {
        logger.info("调用sonarqube查询支持的语言列表开始{}", languagesListUrl);
        String data = "";
//        if (Boolean.TRUE.equals(redisTemplate.hasKey(SONARQUBELANGUAGES))) {
//            logger.info("缓存中存在sonarqube 支持的语言数据，直接返回{}",data);
//            data = redisTemplate.opsForValue().get(SONARQUBELANGUAGES);
//        }else {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(user, password);
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(languagesListUrl, HttpMethod.GET, httpEntity, String.class);
        JSONObject jsonObject = JSONObject.fromObject(responseEntity.getBody());
        data = jsonObject.getJSONArray("languages").toString();
        logger.info("调用sonarqube查询支持的语言数据结束{}", data);
        redisTemplate.opsForValue().set(SONARQUBELANGUAGES, data);
        //一天。
        redisTemplate.expire(SONARQUBELANGUAGES, 24, TimeUnit.HOURS);
//        }
        return data;
    }


    public void qualityprofilesAddProject(String project,String language,String qualityProfile) throws Exception {
        logger.info("调用sonarqube给指定项目设置规则集开始{}", qualityprofilesAddProjectUrl);
        JSONObject jsonObject = new JSONObject();
        MultiValueMap<String,Object> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("language",language);
        multiValueMap.add("project",project);
        multiValueMap.add("qualityProfile",qualityProfile);
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(user, password);
        HttpEntity<MultiValueMap<String,Object>> httpEntity = new HttpEntity<>(multiValueMap,headers);
        try {
            String responseData = restTemplate.postForObject(qualityprofilesAddProjectUrl, httpEntity, String.class);
            logger.info("调用sonarqube给指定项目设置规则集响应结果：{}",responseData);
        } catch (RestClientException e) {
            logger.error("调用sonarqube给指定项目设置规则集出现异常{}",e.getMessage());
        }
        logger.info("调用sonarqube给指定项目设置规则集结束");
    }


    public Map<String, List<String>> getQualityprofiles() throws Exception {
        logger.info("调用sonarqube查询语言对应的规则集列表开始{}", qualityprofilesSearchUrl);
        String data = "";
//        if (Boolean.TRUE.equals(redisTemplate.hasKey(SONARQUBELANGUAGES))) {
//            logger.info("缓存中存在sonarqube 支持的语言数据，直接返回{}",data);
//            data = redisTemplate.opsForValue().get(SONARQUBELANGUAGES);
//        }else {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(user, password);
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        String url = qualityprofilesSearchUrl;
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        JSONObject jsonObject = JSONObject.fromObject(responseEntity.getBody());
        data = jsonObject.getJSONArray("profiles").toString();
        logger.info("调用sonarqube查询语言对应的规则集列表结束{}", data);
        redisTemplate.opsForValue().set(SONARQUBELANGUAGES, data);
        //一天。
        redisTemplate.expire(SONARQUBELANGUAGES, 24, TimeUnit.HOURS);

        JSONArray profiles = jsonObject.getJSONArray("profiles");
        Map<String,List<String>> map = new HashMap<>();
        profiles.forEach(profile->{
            JSONObject jsonProfile = JSONObject.fromObject(profile);
            String languageName = jsonProfile.getString("language");
            String name = jsonProfile.getString("name");
            if(map.containsKey(languageName)){
                map.get(languageName).add(name);
            }else{
                List<String> list = new ArrayList<>();
                list.add(name);
                map.put(languageName,list);
            }
        });
//        }
        return map;
    }


}
