package com.eazybuilder.ga.component.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.http.client.response.HttpClientResponse;
import com.eazybuilder.ga.component.RequestComponent;
import com.eazybuilder.ga.component.config.GitLabComponentConfig;
import com.eazybuilder.ga.pojo.GitLabUserPojo;
import com.eazybuilder.ga.untils.CurrencyUntil;
import com.eazybuilder.ga.untils.OkHttp3Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RequestComponentImpl implements RequestComponent {

    @Autowired
    private GitLabComponentConfig gitLabComponentConfig; //导入gitlab配置

    /**
     * GitLab创建分组
     *
     * @param groupName 分组名称
     * @param groupPath 分组路径
     * @return
     */
    @Override
    public String gitLabCreateGroup(String groupName, String groupPath) {
        String url = gitLabComponentConfig.getUrl()+"groups?name="+groupName+"&path="+groupPath;
        HashMap<String, String>  bodyMap= null;
        Map<String,String> mapHead = new HashMap<>();
        mapHead.put("PRIVATE-TOKEN",gitLabComponentConfig.getToken());
        try {
            String postResult = OkHttp3Util.post(url, bodyMap, mapHead);
            return postResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String gitLabCreateSubGroup(String groupName, String groupPath, String parentId) {
        String url = gitLabComponentConfig.getUrl()+"groups?name="+groupName+"&path="+groupPath+"&parentId="+parentId;
        HashMap<String, String>  bodyMap= null;
        Map<String,String> mapHead = new HashMap<>();
        mapHead.put("PRIVATE-TOKEN",gitLabComponentConfig.getToken());
        try {
            String postResult = OkHttp3Util.post(url, bodyMap, mapHead);
            return postResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * GitLab创建项目
     *
     * @param projectName 项目名称
     * @param groupId 组ID
     * @return
     */
    @Override
    public String gitLabCreateProject(String projectName, String groupId) {
        String url = gitLabComponentConfig.getUrl()+"projects?name="+projectName+"&namespace_id="+groupId;
        HashMap<String, String>  bodyMap= null;
        Map<String,String> mapHead = new HashMap<>();
        mapHead.put("PRIVATE-TOKEN",gitLabComponentConfig.getToken());
        try {
            String postResult = OkHttp3Util.post(url, bodyMap,mapHead);
            return postResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * GitLab创建用户
     *
     * @param gitLabUserPojo
     * @return
     */
    @Override
    public String gitLabCreateUser(GitLabUserPojo gitLabUserPojo) {
        String url = gitLabComponentConfig.getUrl()+"users";
        Map<String, String> convertBeanToMapResult = CurrencyUntil.convertBeanToMap(gitLabUserPojo);
        convertBeanToMapResult.put("name",gitLabUserPojo.getUsername());
        convertBeanToMapResult.put("username",gitLabUserPojo.getUsername());
        convertBeanToMapResult.put("email",gitLabUserPojo.getEmail());
        Map<String,String> mapHead = new HashMap<>();
        mapHead.put("PRIVATE-TOKEN",gitLabComponentConfig.getToken());
        try {
            String postResult = OkHttp3Util.post(url, convertBeanToMapResult, mapHead);
            return postResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 用户添加到组里
     *
     * @param userIds 用户ID
     * @param accessLevel 用户权限
     * @param groupId 组ID
     * @return
     */
    @Override
    public String gitLabUserAddGroup(String userIds,String accessLevel,String groupId) {
        String url = gitLabComponentConfig.getUrl()+"groups/"+groupId+"/members";
        Map<String, String> map = new HashMap<>();
        map.put("user_id",userIds);
        map.put("access_level",accessLevel);
        Map<String,String> mapHead = new HashMap<>();
        mapHead.put("PRIVATE-TOKEN",gitLabComponentConfig.getToken());
        try {
            String postResult = OkHttp3Util.post(url, map, mapHead);
            return postResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 用户添加到项目里
     *
     * @param userIds 用户ID
     * @param accessLevel 用户权限
     * @param projectId 项目ID
     * @return
     */
    @Override
    public String gitLabUserAddProject(String userIds,String accessLevel,String projectId) {
        String url = gitLabComponentConfig.getUrl()+"projects/"+projectId+"/members";
        Map<String, String> map = new HashMap<>();
        map.put("user_id",userIds);
        map.put("access_level",accessLevel);
        Map<String,String> mapHead = new HashMap<>();
        mapHead.put("PRIVATE-TOKEN",gitLabComponentConfig.getToken());
        try {
            String postResult = OkHttp3Util.post(url, map, mapHead);
            return postResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询gitLab所有用户
     *
     * @return
     */
    @Override
    public String gitLabUserQuery() {
        String url = gitLabComponentConfig.getUrl()+"users";
        Map<String,String> mapHead = new HashMap<>();
        mapHead.put("PRIVATE-TOKEN",gitLabComponentConfig.getToken());
        try {
            String getResult = OkHttp3Util.get(url, mapHead);
            return getResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据组Path查询组信息
     *
     * @param groupPath 组Path
     * @return
     */
    @Override
    public String gitLabGroupQuery(String groupPath) {
        String url = gitLabComponentConfig.getUrl()+"/groups/"+groupPath;
        Map<String,String> mapHead = new HashMap<>();
        mapHead.put("PRIVATE-TOKEN",gitLabComponentConfig.getToken());
        try {
            String getResult = OkHttp3Util.get(url, mapHead);
            return getResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
