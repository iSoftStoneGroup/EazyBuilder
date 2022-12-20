package com.eazybuilder.ga.component.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eazybuilder.ga.component.GitlabApiComponent;
import com.eazybuilder.ga.component.config.GitLabComponentConfig;
import com.eazybuilder.ga.untils.OkHttp3Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GitlabApiComponentImpl implements GitlabApiComponent {

    @Autowired
    private GitLabComponentConfig gitLabComponentConfig; //导入gitlab配置

    @Override
    public String findGroup() {
        String url = gitLabComponentConfig.getUrl()+"groups";
        Map<String,String> mapHead = new HashMap<>();
        mapHead.put("PRIVATE-TOKEN",gitLabComponentConfig.getToken());
        try {
            String postResult = OkHttp3Util.get(url,mapHead);
            return postResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String findProjectsByGroup(String group) {
        String url = gitLabComponentConfig.getUrl()+"groups/"+group;
        Map<String,String> mapHead = new HashMap<>();
        mapHead.put("PRIVATE-TOKEN",gitLabComponentConfig.getToken());
        try {
            String postResult = OkHttp3Util.get(url,mapHead);
            return postResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String findProjectBranch(int projectId) {
        String url = gitLabComponentConfig.getUrl()+"projects/"+projectId+"/repository/branches";
        Map<String,String> mapHead = new HashMap<>();
        mapHead.put("PRIVATE-TOKEN",gitLabComponentConfig.getToken());
        try {
            String postResult = OkHttp3Util.get(url,mapHead);
            return postResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String createMR(Integer id, String source_branch, String target_branch, String title) {
        String url = gitLabComponentConfig.getUrl()+"/projects/"+id+"/merge_requests";
        Map<String, String> map = new HashMap<>();
        map.put("source_branch",source_branch);
        map.put("target_branch",target_branch);
        map.put("title",title);
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

	@Override
	public String createMRNote(Integer projectId,Integer mergeRequestId, String body) {
		 String url = gitLabComponentConfig.getUrl()+"/projects/"+projectId+"/merge_requests/"+mergeRequestId+"/notes";
		 Map<String, String> map = new HashMap<>();
		 map.put("body",body);
		 
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

    @Override
    public String createMRDiscussion(Integer projectId, Integer mergeRequestId, String body) {
        String url = gitLabComponentConfig.getUrl()+"/projects/"+projectId+"/merge_requests/"+mergeRequestId+"/discussions";
        Map<String, String> map = new HashMap<>();
        map.put("body",body);

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

    @Override
    public String updateMRState(Integer projectId, Integer mergeRequestId, String state_event) {
        String url = gitLabComponentConfig.getUrl()+"/projects/"+projectId+"/merge_requests/"+mergeRequestId;
        Map<String, String> map = new HashMap<>();
        map.put("state_event","close");

        Map<String,String> mapHead = new HashMap<>();
        mapHead.put("PRIVATE-TOKEN",gitLabComponentConfig.getToken());
        try {
            String postResult = OkHttp3Util.put(url, JSON.toJSONString(map), mapHead);
            return postResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<String> getUserEmail(String userName) {
        String url = gitLabComponentConfig.getUrl()+"users?username="+userName;
        Map<String,String> mapHead = new HashMap<>();
        mapHead.put("PRIVATE-TOKEN",gitLabComponentConfig.getToken());
        try {
            String postResult = OkHttp3Util.get(url,mapHead);
            JSONArray jsonResult = JSON.parseArray(postResult);
            List<String> emailList = new ArrayList<String>();
            if(null!=jsonResult&&jsonResult.size()>0){
                for(int i=0;i<jsonResult.size();i++){
                   JSONObject userJson = jsonResult.getJSONObject(i);
                   String email = userJson.getString("email");
                    emailList.add(email);
                }
            }
            return emailList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
