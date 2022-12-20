package com.eazybuilder.ga.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eazybuilder.ga.component.GitlabApiComponent;
import com.eazybuilder.ga.pojo.gitlab.GitlabBranch;
import com.eazybuilder.ga.pojo.gitlab.GitlabCommit;
import com.eazybuilder.ga.pojo.gitlab.GitlabGroup;
import com.eazybuilder.ga.pojo.gitlab.GitlabProject;
import com.eazybuilder.ga.service.GitlabService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GitlabServiceImpl implements GitlabService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private GitlabApiComponent gitlabApiComponent;

    @Override
    public List<GitlabGroup> findGroup() throws Exception {
        String gitlabGroupResult = gitlabApiComponent.findGroup();

        if(gitlabGroupResult == null){
            throw new Exception("无法请求GitLab服务器");
        }

        Object json = JSON.parse(gitlabGroupResult);
        if(json instanceof JSONObject){
            JSONObject gitlabGroupJson = (JSONObject) json;
            if (gitlabGroupJson.getString("error") != null) {
                throw new Exception(gitlabGroupJson.getString("error"));
            }else if(gitlabGroupJson.getString("message") != null){
                throw new Exception(gitlabGroupJson.getString("message"));
            }
        }else if(json instanceof JSONArray){
            JSONArray gitlabGroupJson = (JSONArray) json;
            List<GitlabGroup> gitlabGroupList = new ArrayList<GitlabGroup>();
            for(int i=0;i<gitlabGroupJson.size();i++){
                JSONObject groupObject = gitlabGroupJson.getJSONObject(i);
                GitlabGroup gitlabGroup = new GitlabGroup();
                gitlabGroup.setId(groupObject.getInteger("id"));
                gitlabGroup.setName(groupObject.getString("name"));
                gitlabGroup.setPath(groupObject.getString("path"));
                gitlabGroup.setFull_name(groupObject.getString("full_name"));
                gitlabGroup.setFull_path(groupObject.getString("full_path"));
                gitlabGroup.setParent_id(groupObject.getInteger("parent_id"));
                gitlabGroupList.add(gitlabGroup);
            }
            return gitlabGroupList;
        }
        throw new Exception("未知错误");
    }

    @Override
    public List<GitlabProject> findProjectsByGroup(String group) throws Exception {
        String gitProjectResult = gitlabApiComponent.findProjectsByGroup(group);
        if(gitProjectResult == null){
            throw new Exception("无法请求GitLab服务器");
        }

        Object json = JSON.parse(gitProjectResult);
        if(json instanceof JSONObject){
            JSONObject gitlabProjectJson = (JSONObject) json;
            if (gitlabProjectJson.getString("error") != null) {
                throw new Exception(gitlabProjectJson.getString("error"));
            }else if(gitlabProjectJson.getString("message") != null){
                throw new Exception(gitlabProjectJson.getString("message"));
            }else{
                JSONArray gitlabProjectArray = gitlabProjectJson.getJSONArray("projects");
                List<GitlabProject> gitlabProjectList = new ArrayList<GitlabProject>();
                for(int i=0;i<gitlabProjectArray.size();i++){
                    JSONObject projectObject = gitlabProjectArray.getJSONObject(i);
                    GitlabProject gitlabProject = new GitlabProject();
                    gitlabProject.setId(projectObject.getInteger("id"));
                    gitlabProject.setDescription(projectObject.getString("description"));
                    gitlabProject.setName(projectObject.getString("name"));
                    gitlabProject.setPath(projectObject.getString("path"));
                    gitlabProject.setName_with_namespace(projectObject.getString("name_with_namespace"));
                    gitlabProject.setPath_with_namespace(projectObject.getString("path_with_namespace"));
                    gitlabProjectList.add(gitlabProject);
                }
                return gitlabProjectList;
            }
        }
        throw new Exception("未知错误");
    }

    @Override
    public List<GitlabBranch> findProjectBranch(int projectId) throws Exception {
        String gitBranchResult = gitlabApiComponent.findProjectBranch(projectId);
        if(gitBranchResult == null){
            throw new Exception("无法请求GitLab服务器");
        }

        Object json = JSON.parse(gitBranchResult);
        if(json instanceof JSONObject){
            JSONObject gitlabBranchJson = (JSONObject) json;
            if (gitlabBranchJson.getString("error") != null) {
                throw new Exception(gitlabBranchJson.getString("error"));
            }else if(gitlabBranchJson.getString("message") != null){
                throw new Exception(gitlabBranchJson.getString("message"));
            }
        }else if(json instanceof JSONArray){
            JSONArray gitlabBranchJson = (JSONArray) json;
            List<GitlabBranch> gitlabBranchList = new ArrayList<GitlabBranch>();
            for(int i=0;i<gitlabBranchJson.size();i++){
                JSONObject branchObject = gitlabBranchJson.getJSONObject(i);
                GitlabBranch gitlabBranch = new GitlabBranch();
                gitlabBranch.setName(branchObject.getString("name"));
                gitlabBranch.setWeb_url(branchObject.getString("web_url"));
                GitlabCommit gitlabCommit = new GitlabCommit();
                JSONObject commitObject = branchObject.getJSONObject("commit");
                gitlabCommit.setId(commitObject.getString("id"));
                gitlabCommit.setAuthor_email(commitObject.getString("author_email"));
                gitlabCommit.setAuthor_name(commitObject.getString("author_name"));
                gitlabCommit.setCommitter_email(commitObject.getString("committer_email"));
                gitlabCommit.setCommitter_name(commitObject.getString("committer_name"));
                gitlabCommit.setMessage(commitObject.getString("message"));
                gitlabCommit.setWeb_url(commitObject.getString("web_url"));
                gitlabCommit.setTitle(commitObject.getString("title"));
                gitlabCommit.setShort_id(commitObject.getString("short_id"));
                gitlabBranch.setCommit(gitlabCommit);
                gitlabBranchList.add(gitlabBranch);
            }
            return gitlabBranchList;
        }
        throw new Exception("未知错误");
    }

    @Override
    public String createMR(Integer id, String source_branch, String target_branch, String title) {
        String createMRResult = gitlabApiComponent.createMR(id, source_branch, target_branch, title);
        if(createMRResult == null){
            return "无法请求GitLab服务器";
        }
        Object json = JSON.parse(createMRResult);
        if(json instanceof JSONObject){
            JSONObject createMRJson = (JSONObject) json;
            if (createMRJson.getString("error") != null) {
                return createMRJson.getString("error");
            }else if(createMRJson.getString("message") != null){
                return createMRJson.getString("message");
            }else{
                return null;
            }
        }
        return "未知错误";
    }

	@Override
	public String createMRNote(Integer projectId,Integer mergeRequestId, String body) {
        String createMRNoteResult = gitlabApiComponent.createMRNote(projectId, mergeRequestId, body);
        if(createMRNoteResult == null){
            return "无法请求GitLab服务器";
        }
        Object json = JSON.parse(createMRNoteResult);
        if(json instanceof JSONObject){
            JSONObject createMRJson = (JSONObject) json;
            if (createMRJson.getString("error") != null) {
                return createMRJson.getString("error");
            }else if(createMRJson.getString("message") != null){
                return createMRJson.getString("message");
            }else{
                return null;
            }
        }
        return "未知错误";
	}

    @Override
    public String createMRDiscussion(Integer projectId, Integer mergeRequestId, String body) {
        String createMRNoteResult = gitlabApiComponent.createMRDiscussion(projectId, mergeRequestId, body);
        if(createMRNoteResult == null){
            return "无法请求GitLab服务器";
        }
        Object json = JSON.parse(createMRNoteResult);
        if(json instanceof JSONObject){
            JSONObject createMRJson = (JSONObject) json;
            if (createMRJson.getString("error") != null) {
                return createMRJson.getString("error");
            }else if(createMRJson.getString("message") != null){
                return createMRJson.getString("message");
            }else{
                return null;
            }
        }
        return "未知错误";
    }

    @Override
    public String updateMRState(Integer projectId, Integer mergeRequestId, String state_event) {
        String createMRNoteResult = gitlabApiComponent.updateMRState(projectId, mergeRequestId, state_event);
        if(createMRNoteResult == null){
            return "无法请求GitLab服务器";
        }
        Object json = JSON.parse(createMRNoteResult);
        if(json instanceof JSONObject){
            JSONObject createMRJson = (JSONObject) json;
            if (createMRJson.getString("error") != null) {
                return createMRJson.getString("error");
            }else if(createMRJson.getString("message") != null){
                return createMRJson.getString("message");
            }else{
                return null;
            }
        }
        return "未知错误";
    }
}
