package com.eazybuilder.ga.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eazybuilder.ga.component.RequestComponent;
import com.eazybuilder.ga.pojo.GitLabPojo;
import com.eazybuilder.ga.pojo.GitLabUserPojo;
import com.eazybuilder.ga.pojo.GitlabResultPojo;
import com.eazybuilder.ga.service.SettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


@Service
public class SettingServiceImpl implements SettingService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RequestComponent requestComponent;

    @Override
    public String gitLabSetting(GitLabPojo gitLabPojo) {
        String errInfo = "";//错误信息
        String groupId = null;//组ID
        String projectId = null;//项目ID
        String groupUserIds = "";
        String projectUserIds = "";
        String fullGroupName = "";
        List<String> projectIdList = new ArrayList<String>();

        String groupPath = gitLabPojo.getGroupPath();
        String[] groupArray = groupPath.split("/");

        JSONObject createGroupResult=null;
        for(int gIndex=0;gIndex<groupArray.length;gIndex++){
            String groupName = groupArray[gIndex];
            if(gIndex==0){
                fullGroupName=groupName;
                GitlabResultPojo gitlabResultPojo = createGroup(groupName);
                if(gitlabResultPojo.getSuccessFlag()){
                    createGroupResult=gitlabResultPojo.getResultJson();
                    groupId = createGroupResult.getString("id");
                }else{
                    return gitlabResultPojo.getResultMessage();
                }
            }else{
                fullGroupName = fullGroupName + "%2F" + groupName;
                GitlabResultPojo gitlabResultPojo = createSubGroup(groupName,groupId,fullGroupName);
                if(gitlabResultPojo.getSuccessFlag()){
                    createGroupResult=gitlabResultPojo.getResultJson();
                    groupId = createGroupResult.getString("id");
                }else{
                    return gitlabResultPojo.getResultMessage();
                }
            }
        }



        String projects = createGroupResult.getString("devopsProjects");
        JSONArray projectObjects = JSONObject.parseArray(projects);
        ListIterator<Object> projectObjectListIterator = projectObjects.listIterator();
        List<String> projectList = gitLabPojo.getProjectList();
        if(null!=projectList&&projectList.size()>0) {
            for (int i = 0; i < projectList.size(); i++) {
                String projectName = projectList.get(i);
                while (projectObjectListIterator.hasNext()) {
                    String next = projectObjectListIterator.next().toString();
                    JSONObject nextRes = JSONObject.parseObject(next);
                    if (projectName.equals(nextRes.getString("name"))) {
                        projectId = nextRes.getString("id");
                        projectIdList.add(projectId);
                        break;
                    }
                }
                //判断项目是否存在
                if (projectId == null) {//项目不存在
                    String gitLabCreateProjectResult = requestComponent.gitLabCreateProject(projectName, groupId);//创建项目
                    if (gitLabCreateProjectResult == null) {//请求失败
                        errInfo = errInfo + "项目创建失败,无法请求GitLab服务器;";
                    }
                    JSONObject createProjectRes = JSONObject.parseObject(gitLabCreateProjectResult);
                    if (createProjectRes.getString("error") != null || createProjectRes.getString("message") != null) {//请求出现问题
                        errInfo = errInfo + projectName+"项目创建失败;";
                    } else {
                        projectId = createProjectRes.getString("id");
                        projectIdList.add(projectId);
                    }
                }
                projectId = null;
            }
        }



        //查询添加用户
        //创建逻辑去掉
        String gitLabUserQuerryResult = requestComponent.gitLabUserQuery();//查询全部用户
        if (gitLabUserQuerryResult == null) {//请求失败
            errInfo = errInfo + "用户查询失败,无法请求GitLab服务器;";
        }
        JSONArray objects = JSONObject.parseArray(gitLabUserQuerryResult);
        ListIterator<Object> objectListIterator = objects.listIterator();
        for (GitLabUserPojo groupAddUserEmil : gitLabPojo.getGroupAddUserEmails()) {
            Boolean isExistence = false;
            while (objectListIterator.hasNext()) {
                String next = objectListIterator.next().toString();
                JSONObject nextRes = JSONObject.parseObject(next);
                if (groupAddUserEmil.getEmail().equals(nextRes.getString("commit_email"))) {//用户已经存在
                    String userId = nextRes.getString("id");
                    isExistence=true;
                    if (groupAddUserEmil.getIsAddProject()) {
                        projectUserIds = projectUserIds + userId + ",";
                    }
                    groupUserIds = groupUserIds + userId + ",";
                    break;
                }
            }
            if (!isExistence){//用户已存在
                if ( !(groupAddUserEmil.getUsername().isEmpty())) {
                    String gitLabCreateUserResult = requestComponent.gitLabCreateUser(groupAddUserEmil);
                    if (gitLabCreateUserResult == null) {//请求出现问题
                        errInfo = errInfo + "groupAddUserEmil.getEmail()此用户创建失败,无法请求GitLab服务器;";
                    }
                    JSONObject createUserRes = JSONObject.parseObject(gitLabCreateUserResult);
                    if (createUserRes.getString("error") != null || createUserRes.getString("message") != null) {//请求出现问题
                        errInfo = errInfo + groupAddUserEmil.getEmail() + "此用户创建失败;";
                    } else {
                        String id = createUserRes.getString("id");
                        if (groupAddUserEmil.getIsAddProject()) {
                            projectUserIds = projectUserIds + id + ",";
                        }
                        groupUserIds = groupUserIds + id + ",";
                    }
                } else {
                    errInfo = errInfo + groupAddUserEmil.getEmail() + "此用户不存在,如想添加请填写完整信息;";
                }
            }

        }

        //添加到组
        if ("".equals(groupUserIds)) {
            errInfo = errInfo + "没有用户加入到组;";
        } else {
            String gitLabUserAddGroupResult = requestComponent.gitLabUserAddGroup(groupUserIds.substring(0, groupUserIds.length() - 1),
                    "30", groupId);
            if (gitLabUserQuerryResult == null) {//请求出现问题
                errInfo = errInfo + "没有用户加入到组,无法请求GitLab服务器;";
            }
            JSONObject gitLabUserAddGroupRes = JSONObject.parseObject(gitLabUserAddGroupResult);
            if (gitLabUserAddGroupRes.getString("error") != null) {//请求出现问题
                errInfo = errInfo + "没有用户加入到组;";
            }
        }
        if ("".equals(projectUserIds)) {
            errInfo = errInfo + "没有用户加入到项目;";
        } else {
            if(null!=projectIdList&&projectIdList.size()>0){
                for(int i=0;i<projectIdList.size();i++){
                    projectId=projectIdList.get(i);
                    //添加到项目
                    String gitLabUserAddProjectResult = requestComponent.gitLabUserAddProject(projectUserIds.substring(0, projectUserIds.length() - 1), "30", projectId);
                    if (gitLabUserQuerryResult == null) {//请求出现问题
                        errInfo = errInfo + "没有用户加入到"+projectId+"项目,无法请求GitLab服务器;";
                    }
                    if (projectId != null) {
                        JSONObject gitLabUserAddProject = JSONObject.parseObject(gitLabUserAddProjectResult);
                        if (gitLabUserAddProject.getString("error") != null) {//请求出现问题
                            errInfo = errInfo + "没有用户加入到"+projectId+"项目;";
                        }
                    }
                }
            }
        }
        return errInfo;
    }

    private GitlabResultPojo createGroup(String groupName){
        String gitLabCreateGroupResult = requestComponent.gitLabCreateGroup(groupName, groupName);
        GitlabResultPojo gitlabResultPojo = new GitlabResultPojo();
        gitlabResultPojo.setSuccessFlag(Boolean.FALSE);
        if (gitLabCreateGroupResult == null) {//请求失败
            gitlabResultPojo.setResultMessage("组创建失败，无法请求GitLab服务器;");
            return gitlabResultPojo;
        }
        JSONObject createRes = JSONObject.parseObject(gitLabCreateGroupResult);
        if (createRes.getString("error") != null) {//请求出现问题
            logger.error(createRes.toJSONString());
            gitlabResultPojo.setResultMessage("组创建操作失败;");
            return gitlabResultPojo;
        } else if (createRes.getString("message") != null) {//组已经存在,查询组信息
            String gitLabGroupQueryResult = requestComponent.gitLabGroupQuery(groupName);//查询组信息
            if (gitLabGroupQueryResult == null) {//请求失败
                logger.error(gitLabGroupQueryResult);
                gitlabResultPojo.setResultMessage("组信息无法获取,无法请求GitLab服务器;");
                return gitlabResultPojo;
            }
            JSONObject queryRes = JSONObject.parseObject(gitLabGroupQueryResult);
            if (queryRes.getString("error") != null || queryRes.getString("message") != null) {//请求出现问题
                logger.error(queryRes.toJSONString());
                gitlabResultPojo.setResultMessage("组信息无法获取;");
                return gitlabResultPojo;
            }
            gitlabResultPojo.setSuccessFlag(Boolean.TRUE);
            gitlabResultPojo.setResultJson(queryRes);
            return gitlabResultPojo;
        } else { //组创建成功
            gitlabResultPojo.setSuccessFlag(Boolean.TRUE);
            gitlabResultPojo.setResultJson(createRes);
            return gitlabResultPojo;
        }
    }

    private GitlabResultPojo createSubGroup(String groupName,String groupId, String fullName){
        String gitLabCreateSubGroupResult = requestComponent.gitLabCreateSubGroup(groupName, groupName,groupId);
        GitlabResultPojo gitlabResultPojo = new GitlabResultPojo();
        gitlabResultPojo.setSuccessFlag(Boolean.FALSE);
        if (gitLabCreateSubGroupResult == null) {//请求失败
            gitlabResultPojo.setResultMessage("子组创建失败，无法请求GitLab服务器;");
            return gitlabResultPojo;
        }
        JSONObject createRes = JSONObject.parseObject(gitLabCreateSubGroupResult);
        if (createRes.getString("error") != null) {//请求出现问题
            logger.error(createRes.toJSONString());
            gitlabResultPojo.setResultMessage("子组创建操作失败;");
            return gitlabResultPojo;
        } else if (createRes.getString("message") != null) {//组已经存在,查询组信息
            String gitLabGroupQueryResult = requestComponent.gitLabGroupQuery(fullName);//查询组信息
            if (gitLabGroupQueryResult == null) {//请求失败
                logger.error(gitLabGroupQueryResult);
                gitlabResultPojo.setResultMessage("子组信息无法获取,无法请求GitLab服务器;");
                return gitlabResultPojo;
            }
            JSONObject queryRes = JSONObject.parseObject(gitLabGroupQueryResult);
            if (queryRes.getString("error") != null || queryRes.getString("message") != null) {//请求出现问题
                logger.error(queryRes.toJSONString());
                gitlabResultPojo.setResultMessage("子组信息无法获取;");
                return gitlabResultPojo;
            }
            gitlabResultPojo.setSuccessFlag(Boolean.TRUE);
            gitlabResultPojo.setResultJson(queryRes);
            return gitlabResultPojo;
        }else{ //组创建成功
            gitlabResultPojo.setSuccessFlag(Boolean.TRUE);
            gitlabResultPojo.setResultJson(createRes);
            return gitlabResultPojo;
        }
    }


}
