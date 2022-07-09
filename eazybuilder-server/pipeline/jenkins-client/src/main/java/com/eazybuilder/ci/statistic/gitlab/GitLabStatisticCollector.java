package com.eazybuilder.ci.statistic.gitlab;

import java.io.Closeable;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.gitlab4j.api.Constants.MergeRequestScope;
import org.gitlab4j.api.Constants.MergeRequestState;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.CommitStats;
import org.gitlab4j.api.models.Event;
import org.gitlab4j.api.models.MergeRequest;
import org.gitlab4j.api.models.MergeRequestFilter;
import org.gitlab4j.api.models.Project;
import org.gitlab4j.api.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.eazybuilder.ci.entity.UserActivityStatistic;
import com.eazybuilder.ci.statistic.UserStatisticCollector;

import cn.hutool.core.date.DateUtil;

public class GitLabStatisticCollector implements UserStatisticCollector,Closeable{
	private static Logger logger=LoggerFactory.getLogger(GitLabStatisticCollector.class);
	
	static Cache<String,Map<Integer,Project>> REPO_PROJECT_CACHE=CacheBuilder.newBuilder()
			.maximumSize(10000)
			.expireAfterWrite(10, TimeUnit.MINUTES).build();
	
	static Cache<String,List<User>> REPO_USER_CACHE=CacheBuilder.newBuilder()
			.maximumSize(10000)
			.expireAfterWrite(10, TimeUnit.MINUTES).build();
	
	GitLabApi gitApi;
	
	String repoUrl;
	/**
	 * 初始化Gitlab活动信息采集器
	 * @param repoUrl  gitlab地址
	 * @param accessToken  token需要有管理员权限才能查看所有
	 */
	public GitLabStatisticCollector(String repoUrl,String accessToken) {
		gitApi=new GitLabApi(repoUrl, accessToken);
		this.repoUrl=repoUrl;
	}
	
	/**
	 * 收集所有普通用户的活动
	 * @return
	 * @throws GitLabApiException
	 */
	public List<UserActivityStatistic> collectUserActivities(Date date) throws Exception {
		List<UserActivityStatistic> activities=Lists.newLinkedList();
		Map<Integer,Project> projectMap=REPO_PROJECT_CACHE.getIfPresent(this.repoUrl);
		if(projectMap==null) {
			projectMap=getProjects();
			REPO_PROJECT_CACHE.put(this.repoUrl, projectMap);
		}
		List<User> users=REPO_USER_CACHE.getIfPresent(this.repoUrl);
		if(users==null) {
			users=gitApi.getUserApi().getActiveUsers();
//			users=gitApi.getUserApi().getActiveUsersStream()
//					.filter(user->!user.getIsAdmin()).collect(Collectors.toList());
			REPO_USER_CACHE.put(this.repoUrl, users);
		}
		for(User user:users) {
			try {
				List<UserActivityStatistic> userActivities=collectUserActivity(user,date,projectMap);
				activities.addAll(userActivities);
			} catch (GitLabApiException e) {
				e.printStackTrace();
			}
		}
		return activities;
	}
	/**
	 * 收集指定用户的活动
	 * @param user
	 * @param projectMap
	 * @return
	 * @throws GitLabApiException 
	 */
	private List<UserActivityStatistic> collectUserActivity(User user, Date date, Map<Integer, Project> projectMap) throws GitLabApiException {
		Date startTime=DateUtil.beginOfDay(date);
		Date endTime=DateUtil.endOfDay(new Date(date.getTime()+24*60*60*1000));
		
		//用户的Commits，按目标工程项目分组
		Map<Integer,HashSet<String>> projectCommits=gitApi.getEventsApi().getUserEventsStream(user.getId(), null, null,endTime, startTime, null)
			.filter(event->event.getPushData()!=null)//只查有PushData的
			.collect(Collectors.toMap(Event::getProjectId, event->{
					return Sets.newHashSet(event.getPushData().getCommitTo());
				},(HashSet<String> original,HashSet<String> last)->{
					original.addAll(last);
					return original;
				}
			));
		
		//由用户创建的MR
		MergeRequestFilter filter=new MergeRequestFilter();
		filter.setCreatedBefore(endTime);
		filter.setCreatedAfter(startTime);
		filter.setState(MergeRequestState.OPENED);
		filter.setScope(MergeRequestScope.ALL);
		filter.setAuthorId(user.getId());
		//用户的open MergeRequest，按目标工程项目分组
		Map<Integer,HashSet<String>> openedMRs=gitApi.getMergeRequestApi()
				.getMergeRequests(filter).stream().collect(
						Collectors.toMap(MergeRequest::getProjectId, 
						mr->{
							return Sets.newHashSet(mr.getSha());
						},(HashSet<String> original,HashSet<String> last)->{
							original.addAll(last);
							return original;
						}
					));
		
		//由用户合并的MR
		filter=new MergeRequestFilter();
		filter.setUpdatedBefore(endTime);
		filter.setUpdatedAfter(startTime);
		filter.setState(MergeRequestState.MERGED);
		filter.setScope(MergeRequestScope.ALL);
		filter.setAssigneeId(user.getId());
		
		//指派给用户，并且已合并的 MergeRequest，按目标工程项目分组
		Map<Integer,HashSet<String>> mergedMRs=gitApi.getMergeRequestApi()
				.getMergeRequests(filter).stream().filter(mr->{
					return mr.getUpdatedAt()!=null
							&&DateUtil.formatDate(mr.getUpdatedAt()).equals(DateUtil.formatDate(date));
					
				}).collect(
						Collectors.toMap(MergeRequest::getProjectId, 
								mr->{
									return Sets.newHashSet(mr.getMergeCommitSha());
								},(HashSet<String> original,HashSet<String> last)->{
									original.addAll(last);
									return original;
								}
								));
		//统计信息
		return getStatistic(user,projectCommits,openedMRs,mergedMRs,projectMap,startTime,endTime);
		
	}

	private List<UserActivityStatistic> getStatistic(User user, Map<Integer, HashSet<String>> projectCommits,
			Map<Integer, HashSet<String>> openedMRs, Map<Integer, HashSet<String>> mergedMRs,
			Map<Integer, Project> projectMap, Date startTime, Date endTime) {
		String day=DateUtil.format(startTime, "yyyy-MM-dd");
		List<UserActivityStatistic> activities=Lists.newLinkedList();
		projectMap.values().stream()
			//滤掉无关的工程
			.filter(project->projectCommits.containsKey(project.getId())
								||openedMRs.containsKey(project.getId())
									||mergedMRs.containsKey(project.getId())
				)
			.forEach(project->{
				UserActivityStatistic activity=new UserActivityStatistic();
				activity.setProjectName(project.getName());
				activity.setGroupName(project.getNamespace().getName());

				if(project.getNamespace().getFullPath().contains("/")) {
					activity.setNameSpace(project.getNamespace().getFullPath().split("/")[0]);
				}else{
					activity.setNameSpace(project.getNamespace().getFullPath());
				}

				activity.setUserName(user.getName());
				activity.setEmail(user.getEmail()==null?user.getName():user.getEmail());
				activity.setDay(day);
				
				if(projectCommits.containsKey(project.getId())) {
					HashSet<String> commitIds=projectCommits.get(project.getId());
					//总共推送次数
					activity.setPushed(commitIds.size());
					//统计新增行数
					int additions=0;
					//统计删除行数
					int deletions=0;
					for(String commitSha:commitIds) {
						try {
//							if(StringUtils.isNotEmpty(commitSha)){
								CommitStats commitStats=getCommitStats(project.getId(),commitSha);
								additions+=commitStats.getAdditions();
								deletions+=commitStats.getDeletions();
//							}
						} catch (GitLabApiException e) {
                            logger.error("统计删除和新增行数出现异常，不影响主流程，不做处理");
						}
					}
					activity.setAdditions(additions);
					activity.setDeletions(deletions);
				}
				if(openedMRs.containsKey(project.getId())) {
					activity.setOpenedMRs(openedMRs.get(project.getId()).size());
				}
				if(mergedMRs.containsKey(project.getId())) {
					activity.setMergedMRs(mergedMRs.get(project.getId()).size());
				}
				activities.add(activity);
		});
		return activities;
	}
	
	private CommitStats getCommitStats(Integer projectId,String commitSha) throws GitLabApiException {
		return gitApi.getCommitsApi().getCommit(projectId, commitSha).getStats();
	}

	private Map<Integer,Project> getProjects() throws GitLabApiException{
		return gitApi.getProjectApi().getProjectsStream().collect(Collectors.toMap(Project::getId, p->p));
	}
	

	@Override
	public void close() throws IOException {
		if(gitApi!=null) {
			gitApi.close();
		}
	}
}
