package com.eazybuilder.ci.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.eazybuilder.ci.OperLog;
import com.eazybuilder.ci.base.PageResult;
import com.eazybuilder.ci.controller.vo.ProjectBuildVo;
import com.eazybuilder.ci.controller.vo.ScmVersionInfo;
import com.eazybuilder.ci.dto.ProjectLastBuildInfo;
import com.eazybuilder.ci.entity.*;
import com.eazybuilder.ci.entity.devops.Release;
import com.eazybuilder.ci.entity.devops.ReleaseProject;
import com.eazybuilder.ci.entity.report.ResourceItem;
import com.eazybuilder.ci.entity.report.Stage;
import com.eazybuilder.ci.entity.report.Status;
import com.eazybuilder.ci.exception.CIException;
import com.eazybuilder.ci.service.*;
import com.eazybuilder.ci.storage.ResourceStorageService;
import com.eazybuilder.ci.util.AuthUtils;
import com.wordnik.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pipeline")
public class PipeLineController{
	private static Logger logger = LoggerFactory.getLogger(PipeLineController.class);
	@Autowired
    PipelineServiceImpl service;
	
	@Autowired
	ProjectService projectService;

	@Autowired
	MetricService metricService;

	@Resource
	PipelineLogService pipelineLogService;


	@Autowired
	ResourceStorageService storageService;


	@Resource
	PipelineProfileService pipelineProfileService;

	@Resource
	TeamNamespaceService teamNamespaceService;

	@Resource
	PipelineBuildService pipelineBuildService;

	@Resource
	ReleaseService releaseService;

	@Resource
	ReleaseProjectService releaseProjectService;

	/**
	 * 重新执行流水线，仅限失败的才可以重新执行
	 * @param pipelineId
	 */
	@ApiOperation("重新执行流水线")
	@RequestMapping(method=RequestMethod.GET,value="/retryPipeline")
	public void retryPipeline(@RequestParam("pipelineId")String pipelineId) throws Exception {
		//1.根据pipelineId找到历史记录
		Pipeline pipeline = service.findOne(pipelineId);
		PipelineBuild pipelineBuild = pipelineBuildService.findByHistoryId(pipelineId);
		//2.将历史数据转为可以直接调用流水线的数据结构
		ProjectBuildVo projectBuildVo = new ProjectBuildVo();
		BeanUtils.copyProperties(pipelineBuild,projectBuildVo);
		PipelineProfile profile = pipelineProfileService.findOne(pipeline.getProfileId());
		String newTag = "";
		if (profile.isUpdateTag()) {
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
			String createTagVersion = projectBuildVo.getCreateTagVersion();
			newTag = createTagVersion.substring(0, createTagVersion.lastIndexOf("_")) + "_" + df.format(new Date());
			projectBuildVo.setCreateTagVersion(newTag);
			logger.info("构建过程中需要更新标签:{},{}", profile, newTag);
		}
		Pipeline newPipeline = service.triggerPipeline(pipeline.getProject().getId(), projectBuildVo);
		try {
			if(pipeline.getPipelineType()==PipelineType.release){
				logger.info("重试的流水线类型为提测类型，更新之前提测绑定的历史记录");
				Release release = releaseService.findByPipeline(pipeline);
				release.getPipelineList().remove(pipeline);
				release.getPipelineList().add(newPipeline);
				releaseService.save(release);
				ReleaseProject releaseProject = releaseProjectService.findByPipelineId(pipeline.getId());
				if(StringUtils.isNotBlank(newTag)) {
					releaseProject.setCreateTagVersion(newTag);
					releaseProjectService.save(releaseProject);
				}
			}
		} catch (Exception e) {
			logger.error("重试流水线时 更新之前提测绑定的历史记录出现异常： {}-{}",e.getMessage(),e);
		}
	}

	@RequestMapping(method={RequestMethod.POST,RequestMethod.PUT})
	@ApiOperation("构建项目(运行流水线)")
	@ResponseStatus(code=HttpStatus.OK)
	@OperLog(module = "pipeline",opType = "triggerByManual",opDesc = "执行流水线")
	public void startPipeline(@RequestBody ProjectBuildVo  buildParam){
		PipelineLog pipelineLog = new PipelineLog();
		for(String projectId:buildParam.getProjects()){
			try {
				if(!AuthUtils.getCurrentUser().isAdmin()){
					if(StringUtils.isNotBlank(buildParam.getProfile())) {
						PipelineProfile pipelineProfile = pipelineProfileService.findOne(buildParam.getProfile());
						TeamNamespace teamNamespace = teamNamespaceService.findByNameSpaceName(pipelineProfile);
						if (teamNamespace.getNamespaceType() == NamespaceType.prod) {
							throw new CIException(HttpStatus.FORBIDDEN.value(), "用户角色权限不足，无法执行运行生产环境流水线");
						}
					}
				}
				buildParam.setPipelineType(PipelineType.manual);
				Pipeline pipeline = service.triggerPipeline(projectId,buildParam);
				if(null!=pipeline) {
					pipelineLog.setPipelineId(pipeline.getId());
				}
			} catch (Exception e) {
				pipelineLog.setExceptionLog(e.getMessage());
				pipelineLog.setStatus(Status.FAILED);
				throw new CIException(e.getMessage(),e);
			}finally {
				//保存流水线执行日志
				pipelineLog.setExecuteType(ExecuteType.manualTrigger);
				pipelineLogService.save(pipelineLog);
			}
		}
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public Pipeline getPipelineInfo(@RequestParam("projectName")String projectName){
		return service.getLastPipeline(projectName);
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/buildStatus")
	public ProjectLastBuildInfo getLastBuildStatus(@RequestParam("projectId")String projectId) {
		return service.getLastBuild(projectId);
	}

	@RequestMapping(method=RequestMethod.GET,value="/dtpReport")
	public List<DtpReport> getDtpReportInfo(@RequestParam("pipelineId")String pipelineId){
		return service.findDtpReportByPipelineId(pipelineId);
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/stage")
	public List<Stage> getStageInfo(@RequestParam("pipelineId")String pipelineId){
		return service.findStageByPipelineId(pipelineId);
	}

	@RequestMapping(method=RequestMethod.GET,value="/metric")
	public Iterable<Metric> getAnalysisMetrics(@RequestParam("pipelineId")String pipelineId){
		return metricService.findByPipelineId(pipelineId);
	}

	@RequestMapping(method=RequestMethod.GET,value="/projectLatestMetric")
	public Iterable<Metric> getLastestBuildMetric(@RequestParam("projectId")String projectId){
		Page<Pipeline> history=service.pageQueryByProjectId(projectId, PageRequest.of(0, 1, Direction.DESC, "startTimeMillis"));
		if(history!=null&&history.getTotalElements()==1){
			Pipeline pl=history.getContent().get(0);
			return getAnalysisMetrics(pl.getId());
		}else{
			return Collections.emptyList();
		}
	}
	
	@RequestMapping(value="/projectPage",method=RequestMethod.GET)
	@ApiOperation("按页查询项目流水线历史")
	public PageResult<Pipeline> page(
			@RequestParam(value="limit",defaultValue="10")int limit,
			@RequestParam(value="offset")int offset,
			@RequestParam(value="project",required=false)String projectId,
			@RequestParam(value="projectName",required=false)String projectName,
			@RequestParam(value="sourceBranch",required=false)String sourceBranch,
			@RequestParam(value="targetBranch",required=false)String targetBranch,
			@RequestParam(value="profileName",required=false)String profileName,
			@RequestParam(value="date",required=false)@DateTimeFormat(pattern="yyyy-MM-dd")Date date,
			HttpServletRequest request){
		Pageable pageable=PageRequest.of(Math.floorDiv(offset, limit), limit,Direction.DESC,"endTimeMillis");
		List<String> projectIds=Lists.newArrayList();
		if(projectId!=null) {
			projectIds.add(projectId);
		}else {
			projectIds.addAll(projectService.getMyProjectIds());
		}
		
		Page<Pipeline> page=service.pageSearch(pageable, projectIds,projectName,sourceBranch,targetBranch,profileName,date);
		PageResult<Pipeline> result=PageResult.create(page.getTotalElements(), page.getContent());
		return result;
	}


	@RequestMapping(value="/getStageLog",method=RequestMethod.POST)
	@ApiOperation("查询项目变更记录")
	public Map<String,String> getStageLog(
			@RequestParam(value="logId",required=true)String pipelineId,
			@RequestBody Stage stage) throws Exception{
		ResourceItem resource=storageService.get(pipelineId);
		Map<String,String> result = Maps.newHashMap();
		result.put(stage.getName(),service.subStageLogByName(new String(resource.getData(), StandardCharsets.UTF_8),stage));
		return result;
	}
	
}
