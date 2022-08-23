package com.eazybuilder.ci.controller;

import com.eazybuilder.ci.OperLog;
import com.eazybuilder.ci.entity.*;
import com.eazybuilder.ci.service.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eazybuilder.ci.base.CRUDRestController;
import com.eazybuilder.ci.base.PageResult;
import com.eazybuilder.ci.upms.QueryUpmsData;
import com.wordnik.swagger.annotations.ApiOperation;

import java.util.List;

@RestController
@RequestMapping("/api/pipelineProfile")
public class PipelineProfileController extends CRUDRestController<PipelineProfileService, PipelineProfile>{

	private static org.slf4j.Logger logger=LoggerFactory.getLogger(PipelineProfileController.class);
    @Autowired
    ProfileHistoryService profileHistoryService;
    
    @Autowired
	QueryUpmsData queryUpmsData;

    @Autowired
    ProjectService projectService;

    @Autowired
    TeamNamespaceService teamNamespaceService;

    @Autowired
    TeamServiceImpl teamServiceImpl;

    @RequestMapping(method={RequestMethod.POST,RequestMethod.PUT})
    @ApiOperation("保存")
    @OperLog(module = "persist",opType = "save",opDesc = "保存")
    public PipelineProfile save(@RequestBody PipelineProfile entity){
        TeamNamespace teamNamespace = teamNamespaceService.findByNameSpaceName(entity);
        entity.setGitlabApiDomain(teamNamespace.getGitlabApiDomain());
        service.save(entity);
        return entity;
    }


    @RequestMapping(value="/history",method= RequestMethod.GET)
    @ApiOperation("按页查询，查询指定项目的历史数据")
    public PageResult<ProfileHistory> history(
            @RequestParam(value="limit",defaultValue="10")int limit,
            @RequestParam(value="offset")int offset,
            @RequestParam(value="profileId",required=false)String profileId){
        Pageable pageable= PageRequest.of(Math.floorDiv(offset, limit), limit, Sort.Direction.DESC,"id");
        Page<ProfileHistory> page=profileHistoryService.pageHistory(pageable, profileId);
        return PageResult.create(page.getTotalElements(), page.getContent());
    }

    @RequestMapping(value="/recover",method=RequestMethod.POST)
    @ApiOperation("切换项目历史数据版本")
    public void recover(
            @RequestBody ProfileHistory projectHistory){
        service.recover(projectHistory);
    }
    
    @RequestMapping(value="/byauth",method=RequestMethod.POST)
    @ApiOperation("根据权限查询构建过程")
    public void byauth(
            @RequestBody ProjectHistory projectHistory){
    	//访问upms获取数据权限
    	//ci在upms目前配置了三种角色类型
    	//eazybuilder:ci:all:teamleader,eazybuilder:ci:dev:developer,eazybuilder:ci:all:teamleader
    	//第三位代表数据权限，可以查询哪个环境的数据，第四位代表菜单权限
    	//例如：eazybuilder:ci:dev:developer的意思是，ci普通开发人员，只能看开发对应的菜单，并且只能看开发环境（命名空间）的数据
    	try {
			String roleStr=queryUpmsData.getCurrentUserRole();
			JSONObject role=JSON.parseObject(roleStr);
			String roleKey=role.getString("roleKey");
			String namespace=roleKey.split(":")[2];
			String auth=roleKey.split(":")[3];
			
			logger.info("用户权限详情:{},菜单权限:{},数据权限:{}",roleKey,auth,namespace);
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
    	
    	
    	
    	
       //系统管理员，可以看到所有构建过程
    	
      //项目管理人员，可以看到本项目下面所有的构建过程
    	
     //开发人员，可以看到本项目开发环境的所有的构建过程
    	
    	
    	
    	
    	
    }
    @GetMapping(value = "/getFromBelongsTeamsAndNameSpace")
    @ApiOperation("根据用户所在项目组查询profile")
    public Iterable<PipelineProfile> getFromBelongsTeamsAndNameSpace(@RequestParam String teamId,
                                                         @RequestParam String nameSpace){
        List<Team> teams = (List)teamServiceImpl.findAll(QTeam.team.devopsTeamId.eq(teamId));
        BooleanExpression or = QPipelineProfile.pipelineProfile.nameSpace.isEmpty().or(
                QPipelineProfile.pipelineProfile.teamId.eq(teams.get(0).getId()).and(QPipelineProfile.pipelineProfile.nameSpace.eq(nameSpace))
        );

        Iterable<PipelineProfile> all = service.findAll( or   );
        return all;

    }


}
