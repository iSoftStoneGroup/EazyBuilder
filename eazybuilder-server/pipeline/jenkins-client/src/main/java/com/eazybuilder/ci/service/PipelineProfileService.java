package com.eazybuilder.ci.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.eazybuilder.ci.controller.vo.UserVo;
import com.eazybuilder.ci.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.repository.PipelineProfileDao;
import com.eazybuilder.ci.util.AuthUtils;
import com.querydsl.core.types.dsl.BooleanExpression;

import javax.annotation.Resource;

@Service
public class PipelineProfileService 
         extends AbstractCommonServiceImpl<PipelineProfileDao, PipelineProfile>
         implements CommonService<PipelineProfile>{

	@Resource
	ProfileHistoryService profileHistoryService;
	@Autowired
    TeamServiceImpl teamServiceImpl;


	public void recover(ProfileHistory profileHistory) {
		//先将之前的数据备份到历史数据中。
		saveHistory(dao.findById(profileHistory.getProfileId()).get());
		//切换当前数据为历史版本
		String jsonData = profileHistory.getJsonData();
		net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(jsonData);
		PipelineProfile profile = (PipelineProfile) net.sf.json.JSONObject.toBean(jsonObject, PipelineProfile.class);
//		List<DeployConfig> deployConfigs = (List<DeployConfig>) JSONArray.parseArray(jsonObject.getJSONArray("deployConfigList").toString(), DeployConfig.class);
//		project.setDeployConfigList(deployConfigs);
		dao.save(profile);
	}

	public PipelineProfile findByName(String name){
		return dao.findOne(QPipelineProfile.pipelineProfile.name.eq(name)).get();
	}
	@Override
	public Iterable<PipelineProfile> findAll() {
		BooleanExpression condition=null;
		condition = checkCurrentRole(condition);
		Iterable<PipelineProfile> pipelineProfiles = condition != null ? dao.findAll(condition) : dao.findAll();
		for (PipelineProfile profile : pipelineProfiles) {
			if(StringUtils.isBlank(profile.getNameSpace())){
				profile.setNameSpace("dev");
			}
			if(StringUtils.isBlank(profile.getTeamName())){
				profile.setTeamName(teamServiceImpl.findOne(profile.getTeamId()).getName());
				dao.save(profile);
			}
			profile.setAllName(profile.getName()+"-"+profile.getTeamName()+"-"+profile.getNameSpace());
		}
		return pipelineProfiles;
	}

	private BooleanExpression checkCurrentRole(BooleanExpression condition) {
		UserVo currentUser=AuthUtils.getCurrentUser();
		if (currentUser != null && !currentUser.isAdmin()) {
			condition = QPipelineProfile.pipelineProfile.publicProfile.eq(true);
			Collection<Team> teams = teamServiceImpl.findByUser(currentUser);
			List<String> teamIds = Lists.newArrayList();
			if (teams != null && teams.size() > 0) {
				teams.forEach(team -> teamIds.add(team.getId()));
				condition = condition.or(QPipelineProfile.pipelineProfile.teamId.in(teamIds));
			}
		}
		return condition;
	}

	@Override
	public Page<PipelineProfile> pageSearch(Pageable pageable, String searchText) {
		BooleanExpression condition=null;
		condition = checkCurrentRole(condition);
		if(StringUtils.isNotBlank(searchText)){
			if(condition!=null){
				condition=condition.and(QPipelineProfile.pipelineProfile.name.like("%"+searchText+"%")
						.or(QPipelineProfile.pipelineProfile.teamName.like("%"+searchText+"%")));
			}else{
				condition=QPipelineProfile.pipelineProfile.name.like("%"+searchText+"%")
						.or(QPipelineProfile.pipelineProfile.teamName.like("%"+searchText+"%"));
			}
		}
		Page<PipelineProfile> pipelineProfiles = condition == null ? dao.findAll(pageable) : dao.findAll(condition, pageable);
		List<PipelineProfile> profiles = pipelineProfiles.getContent();
		for(PipelineProfile profile:profiles){
			String nameSpace = profile.getNameSpace();
			if(StringUtils.isBlank(nameSpace)){
				profile.setNameSpace("dev");
			}
			if(StringUtils.isBlank(profile.getTeamName())){
				profile.setTeamName(teamServiceImpl.findOne(profile.getTeamId()).getName());
				dao.save(profile);
			}
		}
		return pipelineProfiles;
	}

	@Override
	public void save(PipelineProfile entity) {
		//重写之前的更新方法，每次更新会新增一条数据
		if (StringUtils.isNotBlank(entity.getId())) {
			//总共有两种方法，第一种是改造原来的表结构，在原有的表中重新插入一条数据。但由于涉及过多，所有新增了一个历史表
			saveHistory(dao.findById(entity.getId()).get());
			super.save(entity);
		}else{
			super.save(entity);
			saveHistory(entity);
		}
	}
	/**
	 *
	 * @param
	 */
	public void saveHistory(PipelineProfile pipelineProfile) {
		ProfileHistory projectHistory = new ProfileHistory();
		projectHistory.setProfileName(pipelineProfile.getName());
		projectHistory.setNameSpace(pipelineProfile.getNameSpace());
		projectHistory.setTeamName(pipelineProfile.getTeamName());
		projectHistory.setProfileId(pipelineProfile.getId());
		projectHistory.setJsonData(JSONObject.toJSONString(pipelineProfile));
		if(null!= AuthUtils.getCurrentUser()) {
			projectHistory.setUpdateUser(AuthUtils.getCurrentUser().getName());
			projectHistory.setUserId(AuthUtils.getCurrentUser().getId());
		}
		projectHistory.setUpdateTime(new Date());
		profileHistoryService.save(projectHistory);
	}

}
