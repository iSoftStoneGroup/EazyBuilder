package com.eazybuilder.ci.service;

import com.eazybuilder.ci.entity.Project;
import com.eazybuilder.ci.entity.devops.*;
import com.eazybuilder.ci.util.AuthUtils;
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
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class RedmineService {

    @Value("${redmine.getProjectsUrl}")
    private String getProjectsUrl;

    @Value("${redmine.getSprintsUrl}")
    private String getSprintsUrl;

    @Value("${redmine.getIssuesUrl}")
    private String getIssuesUrl;

    @Value("${redmine.getIssuesDetailUrl}")
    private String getIssuesDetailUrl;

    @Value("${redmine.getSprintsById}")
    private String getSprintsById;


    @Resource
    ProjectService projectService;
    @Resource
    ReleaseService releaseService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String REDMINEISSUE = "REDMINE:ISSUE:";
    private static final String REDMINESPRINTTEAM = "REDMINE:SPRINTTEAM:";
    private static final String REDMINESPRINTID = "REDMINE:SPRINTID:";


    private static Logger logger = LoggerFactory.getLogger(RedmineService.class);

    public List<RedmineProject> getRedmineTeams() throws Exception {
        String email = AuthUtils.getCurrentUser().getEmail();
        //调用redmine接口，传参为用户英文名，暂时取邮箱@符号之前的信息。
        String userName = "";
        if(email.contains("@")) {
            userName = email.substring(0, email.indexOf("@"));
        }else {
            userName = "demo";
        }
        logger.info("调用redmine查询项目组开始 Url:{} param:{}",getProjectsUrl,userName);
        String data = HttpUtil.getJson(getProjectsUrl, "userName="+userName);
        logger.info("调用redmine查询项目组结束{}",data);
        JSONObject jsonObject = JSONObject.fromObject(data);
        JSONArray listData = jsonObject.getJSONArray("data");
        return JSONArray.toList(listData, new RedmineProject(), new JsonConfig());
    }

    public List<RedmineSprint> getRedmineSprintByTeam(String teamId) throws Exception {
        logger.info("调用redmine查询spring开始{}",getSprintsUrl+"projectId="+teamId);
        String data = "";
        if (Boolean.TRUE.equals(redisTemplate.hasKey(REDMINESPRINTTEAM +teamId))) {
            logger.info("缓存中存在redmine sprint数据，直接返回{}",data);
            data = redisTemplate.opsForValue().get(REDMINESPRINTTEAM + teamId);
        }else {
            data = HttpUtil.getJson(getSprintsUrl, "projectId="+teamId);
            logger.info("调用redmine查询spring结束{}", data);
            redisTemplate.opsForValue().set(REDMINESPRINTTEAM +teamId, data);
            //一小时。
            redisTemplate.expire(REDMINESPRINTTEAM +teamId, 36000, TimeUnit.SECONDS);
        }
        JSONObject jsonObject = JSONObject.fromObject(data);
        JSONArray listData = jsonObject.getJSONArray("data");
        return JSONArray.toList(listData, new RedmineSprint(), new JsonConfig());
    }


    public List<RedmineIssues> getIssuesBySprintId(String sprintId) throws Exception {
        logger.info("调用redmine查询issues开始{}",getIssuesUrl+"sprintId="+sprintId);
        String data = "";
        if (Boolean.TRUE.equals(redisTemplate.hasKey(REDMINEISSUE+sprintId))) {
            logger.info("缓存中存在redmine issuesr数据，直接返回{}",data);
            data = redisTemplate.opsForValue().get(REDMINEISSUE + sprintId);
        }else {
            data = HttpUtil.getJson(getIssuesUrl, "sprintId=" + sprintId);
            logger.info("调用redmine查询issues结束{}", data);
            redisTemplate.opsForValue().set(REDMINEISSUE+sprintId, data);
            //一小时。
            redisTemplate.expire(REDMINEISSUE+sprintId, 36000, TimeUnit.SECONDS);
        }
        com.alibaba.fastjson.JSONArray listData = com.alibaba.fastjson.JSONObject.parseObject(data).getJSONArray("data");
        List<RedmineIssues> dataArr = com.alibaba.fastjson.JSONArray.parseArray(listData.toJSONString(), RedmineIssues.class);
        return dataArr;
    }


    public List<RedmineIssues> synIssuesBySprintId(String sprintId) throws Exception {
        String key  = REDMINEISSUE+sprintId;
        logger.info("调用redmine查询issues开始{}",getIssuesUrl+"sprintId="+sprintId);
        String data =   HttpUtil.getJson(getIssuesUrl, "sprintId=" + sprintId);;
        logger.info("调用redmine查询issues结束{}", data);
        redisTemplate.opsForValue().set(key, data);
        //一小时。
        redisTemplate.expire(key, 60, TimeUnit.SECONDS);
        com.alibaba.fastjson.JSONArray listData = com.alibaba.fastjson.JSONObject.parseObject(data).getJSONArray("data");
        List<RedmineIssues> dataArr = com.alibaba.fastjson.JSONArray.parseArray(listData.toJSONString(), RedmineIssues.class);
        return dataArr;
    }
    
    
    /*
     * 把redmine返回的数据，做一下整理，根据仓库地址，做一下汇总
     * 返回map,key:仓库地址，value:list,冲刺板中，仓库对应的任务（分支）
     */
    private  Map<String,List<RedmineIssues>> formatRedmineIssues(String data){
    	
        List<RedmineIssues> dataArr = null;
        Map<String,List<RedmineIssues>> map=new HashMap<String,List<RedmineIssues>>();
        
        List<RedmineIssues> list=com.alibaba.fastjson.JSONObject.parseArray(data,RedmineIssues.class);
        
        for(int i=0;i<list.size();i++) {
        	if(map.containsKey(list.get(i).getGitlabPath())) {
        		map.get(list.get(i).getGitlabPath()).add(list.get(i));
        	}else {
        		map.put(list.get(i).getGitlabPath(), new ArrayList<RedmineIssues>());
        	}
        }
        logger.info("格式化后的冲刺板数据:{}",com.alibaba.fastjson.JSONObject.toJSONString(map));
        return map;

    	
    }

    public com.alibaba.fastjson.JSONObject getIssueDetailById(String issueId) throws Exception {
        logger.info("调用redmine查询issues明细开始{}",getIssuesDetailUrl+"id="+issueId);
        String data = HttpUtil.getJson(getIssuesDetailUrl, "id="+issueId);
        logger.info("调用redmine查询issues明细结束{}",data);
        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(data).getJSONObject("data");
        //将中文字段改为英文
        if(jsonObject.containsKey("customField")){
            if(jsonObject.getJSONObject("customField").containsKey("是否更新sql")){
                jsonObject.put("sqlEdit",jsonObject.getJSONObject("customField").getString("是否更新sql"));
            }
            if(jsonObject.getJSONObject("customField").containsKey("是否更新配置文件")){
                jsonObject.put("configEdit",jsonObject.getJSONObject("customField").getString("是否更新sql"));
            }
        }
        return jsonObject;
    }

    public com.alibaba.fastjson.JSONObject getSprintsById(String springId) throws Exception {
        logger.info("调用redmine查询看板信息开始{}",getSprintsById+springId);
        String data = "";
        if (Boolean.TRUE.equals(redisTemplate.hasKey(REDMINESPRINTID +springId))) {
            logger.info("缓存中存在redmine 看板信息数据，直接返回{}",data);
            data = redisTemplate.opsForValue().get(REDMINESPRINTID + springId);
        }else {
            data =HttpUtil.getJson(getSprintsById+springId, null);
            logger.info("调用redmine查询看板信息结束{}",data);
            redisTemplate.opsForValue().set(REDMINESPRINTID +springId, data);
            //一周。
            redisTemplate.expire(REDMINESPRINTID +springId, 1530000, TimeUnit.SECONDS);
        }
        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(data).getJSONObject("data");
        return jsonObject;
    }


    public List<RedmineIssues> getBatchIssues(List<RedmineIssues> issuesBySprintId, String issuesId) {
        List<RedmineIssues> redmineIssuesList = new ArrayList<>();
        String[] split = issuesId.split(",");
        for(RedmineIssues issues:issuesBySprintId) {
            for (String s : split) {
                if(issues.getId().toString().equals(s)){
                    redmineIssuesList.add(issues);
                }
            }
        }
        return redmineIssuesList;
    }
    public List<RedmineIssues> getZtreeByGitUrl(List<RedmineIssues> issuesBySprintId) {
        //1.首先将所有的git路径拿出来，并且去重。
        Set<String> gitPaths = issuesBySprintId.stream().filter(data -> StringUtils.isNotBlank(data.getGitlabPath())).map(RedmineIssues::getGitlabPath).collect(
                Collectors.toSet());
        List<String> gitPathList = new ArrayList<>(gitPaths);
        //2.将不包含.git路径的数据过滤掉。
        List<RedmineIssues> redmineIssuesList = new ArrayList<>();
        //通过for循环将每个.git路径置为一级菜单。再将所属的所有需求罗列出来置为二级菜单。
        for (int i = 0; i < gitPathList.size(); i++) {
            String gitPath = gitPathList.get(i);
            RedmineIssues redmineIssues = new RedmineIssues();
            List<Project> projects = projectService.findByScmUrl(gitPath);
            redmineIssues.setSubject(projects.get(0).getDescription()+" ("+gitPath+")");
            redmineIssues.setReleaseId(i);
            redmineIssuesList.add(redmineIssues);
            for(RedmineIssues redmineIssue:issuesBySprintId){
                if(StringUtils.isNotBlank(redmineIssue.getGitlabPath())) {
                    if (redmineIssue.getGitlabPath().equals(gitPath)) {
                        redmineIssue.setReleaseParentId(i);
                        redmineIssue.setReleaseId(Integer.valueOf((int) (Math.random()*10000)));
                        redmineIssuesList.add(redmineIssue);
                    }
                }
            }
        }
        return redmineIssuesList;
    }
    public List<RedmineIssues> getZtreeByGitUrl(List<RedmineIssues> issuesBySprintId,String teamName) {
        //1将当前项目组下的所有项目查询出来
        List<Project> allProjects = new ArrayList<>();
        allProjects = projectService.findAllByTeamName(teamName);
        //1.首先将所有的git路径拿出来，并且去重。
        Set<String> gitPaths = issuesBySprintId.stream().filter(data -> StringUtils.isNotBlank(data.getGitlabPath())).map(RedmineIssues::getGitlabPath).collect(
                Collectors.toSet());
        List<String> gitPathList = new ArrayList<>(gitPaths);
        //2.将不包含.git路径的数据过滤掉。
        List<RedmineIssues> redmineIssuesList = new ArrayList<>();
        //通过for循环将每个.git路径置为一级菜单。再将所属的所有需求罗列出来置为二级菜单。
        int rootReleaseId = (int) (99999 + Math.random() * 10);
        RedmineIssues rootIssue = new RedmineIssues();
        rootIssue.setReleaseId(rootReleaseId);
        rootIssue.setSubject("此版本变更工程");
        redmineIssuesList.add(rootIssue);
        for (int i = 0; i < gitPathList.size(); i++) {
            String gitPath = gitPathList.get(i);
            RedmineIssues redmineIssues = new RedmineIssues();
            List<Project> projects = projectService.findByScmUrl(gitPath);
            if (!projects.isEmpty() && projects.size() > 0) {
                redmineIssues.setSubject(projects.get(0).getDescription() + " (" + gitPath + ")");
            } else {
                redmineIssues.setSubject(gitPath);
            }
            redmineIssues.setReleaseId(i);
            redmineIssues.setReleaseParentId(rootReleaseId);
            redmineIssuesList.add(redmineIssues);
            for (RedmineIssues redmineIssue : issuesBySprintId) {
                if (StringUtils.isNotBlank(redmineIssue.getGitlabPath())) {
                    if (redmineIssue.getGitlabPath().equals(gitPath)) {
                        redmineIssue.setReleaseParentId(i);
                        redmineIssue.setReleaseId(Integer.valueOf((int) (Math.random() * 10000)));
                        redmineIssuesList.add(redmineIssue);
                    }
                }
            }
                //将有活动的删掉,只留没有活动的项目
                for (int ii = 0; ii < allProjects.size(); ii++) {
                    if (allProjects.get(ii).getScm().getUrl().equals(gitPath)) {
                        allProjects.remove(allProjects.get(ii));
                        ii--;
                    }
                }
        }
        if(!allProjects.isEmpty()&&allProjects.size()>0){
            //没有活动的根节点
            int absenceIssueReleaseId = (int) (99999 + Math.random() * 10);
            RedmineIssues absenceIssue = new RedmineIssues();
            absenceIssue.setReleaseId(absenceIssueReleaseId);
            absenceIssue.setSubject("其它工程-(在该迭代版本中没有活动)");
            redmineIssuesList.add(absenceIssue);
            //将没有活动的项目拼到树节点里
            for(Project project:allProjects){
                RedmineIssues absenceIssues = new RedmineIssues();
                absenceIssues.setReleaseId(Integer.valueOf((int) (Math.random()*10000)));
                absenceIssues.setReleaseParentId(absenceIssueReleaseId);
                absenceIssues.setSubject(project.getDescription()+" ("+project.getScm().getUrl()+")");
                absenceIssues.setGitlabPath(project.getScm().getUrl());
                redmineIssuesList.add(absenceIssues);
            }
        }
        return redmineIssuesList;
    }
}
