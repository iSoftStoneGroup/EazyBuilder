package com.eazybuilder.ci.controller;

import com.eazybuilder.ci.entity.*;
import com.eazybuilder.ci.entity.devops.DevopsInit;
import com.eazybuilder.ci.entity.devops.QDevopsInit;
import com.eazybuilder.ci.repository.TeamNamespaceDao;
import com.google.common.collect.Lists;
import com.eazybuilder.ci.base.CRUDRestController;
import com.eazybuilder.ci.base.PageResult;
import com.eazybuilder.ci.controller.vo.ProjectQAReport;
import com.eazybuilder.ci.dto.ProjectLastBuildInfo;
import com.eazybuilder.ci.entity.devops.Release;
import com.eazybuilder.ci.service.*;
import com.eazybuilder.ci.util.JsonMapper;
import com.wordnik.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.internal.guava.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api/project")
public class ProjectController extends CRUDRestController<ProjectService, Project>{
	private  static Logger logger = LoggerFactory.getLogger(ProjectController.class);

	@Autowired
	ProjectHistoryService projectHistoryService;

	@Autowired
	ProjectGroupService projectGroupService;
	
	@Autowired
    PipelineServiceImpl pipelineServiceImpl;
	
	@Autowired
	MetricService metricService;

	@Autowired
	RedmineService redmineService;

	@Resource
	ReleaseService releaseService;

	@Autowired
	ProjectService projectService;

	@Autowired
	DevopsInitServiceImpl devopsInitService;

	@Autowired
	TeamServiceImpl teamServiceImpl;


	@RequestMapping(value = "/projectsByTeamId/{id}", method = RequestMethod.GET)
	@ApiOperation("根据ID查找")
	public List<Project> getTeamNameSpaces(@PathVariable("id") String id) {
		return service.findAllByTeamId(id);
	}


	@RequestMapping(value="/history",method=RequestMethod.GET)
	@ApiOperation("按页查询，查询指定项目的历史数据")
	public PageResult<ProjectHistory> history(
			@RequestParam(value="limit",defaultValue="10")int limit,
			@RequestParam(value="offset")int offset,
			@RequestParam(value="projectId",required=false)String projectId){
		Pageable pageable=PageRequest.of(Math.floorDiv(offset, limit), limit,Direction.DESC,"id");
		Page<ProjectHistory> page=projectHistoryService.pageHistory(pageable, projectId);
		return PageResult.create(page.getTotalElements(), page.getContent());
	}

	@RequestMapping(value="/recover",method=RequestMethod.POST)
	@ApiOperation("切换项目历史数据版本")
	public void recover(
			@RequestBody ProjectHistory projectHistory){
	    service.recover(projectHistory);
	}



	@RequestMapping(value="/pageWithExcludes",method=RequestMethod.GET)
	@ApiOperation("按页查询，支持指定排除项")
	public PageResult<Project> page(
			@RequestParam(value="limit",defaultValue="10")int limit,
			@RequestParam(value="offset")int offset,
			@RequestParam(value="search",required=false)String searchText,@RequestParam(value="excludes",required=false)String excludes){
		if(StringUtils.isNotBlank(excludes)){
			JsonMapper mapper=JsonMapper.nonDefaultMapper();
			ArrayList<String> excludeIds=mapper.fromJson(excludes, mapper.contructCollectionType(ArrayList.class, String.class));
			Pageable pageable=PageRequest.of(Math.floorDiv(offset, limit), limit,Direction.DESC,"id");
			Page<Project> page=service.pageSearchWithExcludes(pageable, searchText, excludeIds);
			return PageResult.create(page.getTotalElements(), page.getContent());
		}
		return super.page(limit, offset, searchText, null);

	}

	@RequestMapping(value="/pipelineByGitpaths",method=RequestMethod.GET)
	@ApiOperation("含流水线信息")
	public PageResult<ProjectLastBuildInfo> pipelineByGitpaths(
			@RequestParam(value="limit",defaultValue="10")int limit,
			@RequestParam(value="offset")int offset,
			@RequestParam(value="releaseId")String releaseId) throws Exception {
		Pageable pageable=PageRequest.of(Math.floorDiv(offset, limit), limit,Direction.DESC,"id");
		Release release = releaseService.findOne(releaseId);

		List<String> gitPaths = Arrays.asList(release.getGitPath().split(","));
		Page<Project> page=service.pageSearchByGitpaths(pageable, gitPaths);

		List<ProjectLastBuildInfo> prjWithBuilds=Lists.newArrayList();
		page.getContent().forEach(prj->{
			prjWithBuilds.add(pipelineServiceImpl.getLastBuildRecord(prj));
		});

		return PageResult.create(page.getTotalElements(), prjWithBuilds);

	}
	
	@RequestMapping(value="/pipeline",method=RequestMethod.GET)
	@ApiOperation("含流水线信息")
	public PageResult<ProjectLastBuildInfo> pageWithPipeline(
			@RequestParam(value="limit",defaultValue="10")int limit,
			@RequestParam(value="offset")int offset,
			@RequestParam(value="search",required=false)String searchText){
		Pageable pageable=PageRequest.of(Math.floorDiv(offset, limit), limit,Direction.DESC,"id");
		Page<Project> page=service.pageSearch(pageable, searchText);
		
		List<ProjectLastBuildInfo> prjWithBuilds=Lists.newArrayList();
		page.getContent().forEach(prj->{
			prjWithBuilds.add(pipelineServiceImpl.getLastBuildRecord(prj));
		});
		
		return PageResult.create(page.getTotalElements(), prjWithBuilds);
		
	}
	
	@RequestMapping(value="/qaReport",method=RequestMethod.GET)
	@ApiOperation("查询项目质量信息")
	public PageResult<ProjectQAReport> queryQAReport(@RequestParam(value="limit",defaultValue="10")int limit,
			@RequestParam(value="offset")int offset,
			@RequestParam(value="search",required=false)String searchText,
			@RequestParam(value="groupIds[]",required = false)List<String> groupIds,HttpServletRequest request){
		
		if(groupIds!=null) {
			List<ProjectQAReport> reports=Lists.newArrayList();
			groupIds.forEach(groupId->{
				ProjectGroup group=projectGroupService.findOne(groupId);
				if(group!=null&&group.getProjects()!=null) {
					for(Project project:group.getProjects()){
						ProjectQAReport report=new ProjectQAReport();
						report.setGroupName(group.getName());
						report.setGroupLeader(group.getLeader());
						report.setProject(project);
						Page<Pipeline> pipPage= pipelineServiceImpl.pageQueryByProjectId(project.getId(),
								PageRequest.of(0, 1, Direction.DESC, "startTimeMillis"));
						if(pipPage!=null&&pipPage.getTotalElements()>0){
							Pipeline pl=pipPage.getContent().get(0);
							report.setLatestPipeline(pl);
							report.setMetrics(metricService.findLatestScanInfoByProjectId(project.getId()));
						}
						reports.add(report);
					}
				}
			});
			return PageResult.create(reports.size(),reports);
		}else {
			PageResult<Project> projects=page(limit, offset, searchText, request);
			if(projects!=null&&projects.getRows().size()>0){
				List<ProjectQAReport> reports=Lists.newArrayList();
				for(Project project:projects.getRows()){
					ProjectQAReport report=new ProjectQAReport();
					report.setProject(project);
					
					Page<Pipeline> pipPage= pipelineServiceImpl.pageQueryByProjectId(project.getId(),
							PageRequest.of(0, 1, Direction.DESC, "startTimeMillis"));
					if(pipPage!=null&&pipPage.getTotalElements()>0){
						Pipeline pl=pipPage.getContent().get(0);
						report.setLatestPipeline(pl);
						report.setMetrics(metricService.findLatestScanInfoByProjectId(project.getId()));
					}
					reports.add(report);
				}
				
				return PageResult.create(projects.getTotal(),reports);
			}else{
				return PageResult.create(0, Collections.emptyList());
			}
		}
	}

	@GetMapping("/getNameSpacesByProjectId")
	public  Set<String>   getNameSpacesByProjectId(@RequestParam("projectId") String projectId){
		Set<String> teamNamespaces = Sets.newHashSet();
		Project project = projectService.findOne(projectId);
		Set<TeamNamespace> teamNameSpace = teamServiceImpl.getTeamNameSpace(project.getTeam());
		teamNameSpace.forEach(teamNamespace ->teamNamespaces.add(teamNamespace.getCode()) );
		return teamNamespaces;
	}

}
