package com.eazybuilder.ci.controller;

import com.eazybuilder.ci.constant.RoleEnum;
import com.eazybuilder.ci.entity.TeamNamespace;
import com.eazybuilder.ci.entity.User;
import org.springframework.web.bind.annotation.*;

import com.eazybuilder.ci.base.CRUDRestController;
import com.eazybuilder.ci.entity.Role;
import com.eazybuilder.ci.entity.Team;
import com.eazybuilder.ci.service.TeamServiceImpl;
import com.eazybuilder.ci.util.AuthUtils;
import com.wordnik.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/team")
public class TeamController extends CRUDRestController<TeamServiceImpl, Team>{

	@RequestMapping(method=RequestMethod.GET,path = "/my")
	@ApiOperation("查询用户的全部项目组")
	public Iterable<Team> listMyTeam(){
		User currentUser=AuthUtils.getCurrentUser();
		List<Role> roles = currentUser.getRoles();
		//管理员返回所有项目组
		if(Role.existRole(roles,RoleEnum.admin)) {
			return service.findAll();
		}
		//否则返回当前用户对应的项目组
		return service.findByUser(AuthUtils.getCurrentUser());
	}


	@RequestMapping(value="/teamNameSpaces/{id}",method=RequestMethod.GET)
	@ApiOperation("根据ID查找")
	public Set<TeamNamespace> getTeamNameSpaces(@PathVariable("id")String id){
		Team team = service.findOne(id);
		return service.getTeamNameSpace(team);
	}

	@RequestMapping(value = "/getUserByTeamName",method = {RequestMethod.GET})
	@ApiOperation("根据releaseId 查对应的需求")
	public Team getUserByTeamName(@RequestParam(value = "teamName") String teamName) throws Exception {
		return service.findByName(teamName);
	}


	@PostMapping(value = "/updateTeamThresholds")
	@ApiOperation("根据releaseId 查对应的需求")
	public Team updateTeamThresholds(@RequestBody Team entity) throws Exception {
	 	Team team = service.findOne(entity.getId());
		team.setTeamThresholds(entity.getTeamThresholds());
		team.setSprintMultiTest(entity.getSprintMultiTest());
		team.setCheckReleasePipeline(entity.getCheckReleasePipeline());
		service.save(team);
		return team;
	}
}
