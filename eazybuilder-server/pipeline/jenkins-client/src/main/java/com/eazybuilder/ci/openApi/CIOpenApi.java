package com.eazybuilder.ci.openApi;

import javax.transaction.Transactional;

import com.eazybuilder.ci.controller.vo.ProjectBuildVo;
import com.eazybuilder.ci.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eazybuilder.ci.dto.ExecuteResult;
import com.eazybuilder.ci.openApi.vo.PipelineProfileVO;
import com.eazybuilder.ci.openApi.vo.ProjectVO;
import com.eazybuilder.ci.service.DockerRegistryService;
import com.eazybuilder.ci.service.PipelineServiceImpl;
import com.eazybuilder.ci.service.ProjectService;
import com.eazybuilder.ci.util.AuthUtils;

import java.util.List;

/**
 * CI平台对外部开放的API接口
 *
 */
@RestController
@RequestMapping("/open-api")
public class CIOpenApi {
	
	private static Logger logger=LoggerFactory.getLogger(CIOpenApi.class);
	
	@Autowired
	ProjectService projectService;
	
	@Autowired
    PipelineServiceImpl pipelineServiceImpl;
	
	@Autowired
	DockerRegistryService registryService;
	/**
	 * 创建或者更新工程项目(工程name（即artifactId）唯一)
	 * @param project
	 * @return
	 */
	@PostMapping("/project")
	@Transactional
	public ExecuteResult createOrUpdate(@RequestBody ProjectVO project) {
//		Project entity=new Project();
		try {
			Project entity=projectService.findByNameAndRepo(project.getName(), project.getScmUrl());
			List<DockerRegistry> dockerRegistries=registryService.findByUrl(project.getRegistry().getUrl());
			DockerRegistry registry = new DockerRegistry();
			if(!dockerRegistries.isEmpty()&&dockerRegistries.size()>0) {
				registry = dockerRegistries.get(0);
				if (registry == null) {
					registry = new DockerRegistry();
					registry.setEmail(project.getRegistry().getEmail());
					registry.setPassword(project.getRegistry().getPassword());
					registry.setSchema(project.getRegistry().getSchema());
					registry.setUrl(project.getRegistry().getUrl());
					registry.setUser(project.getRegistry().getUser());
					registryService.save(registry);
				}
			}
			
			if(entity!=null) {
				//only update scm info(especially tag)
				entity.getScm().setTagName(project.getScmTagName());
				entity.getScm().setUser(project.getScmUser());
				entity.getScm().setPassword(project.getScmPassword());
				entity.setImageSchema(project.getRegistryNamespace());
			}else {
				//create
				entity=new Project();
				entity.setDescription(project.getDescription());
				entity.setName(project.getName());
				entity.setProjectType(project.getProjectType());
				entity.setImageSchema(project.getRegistryNamespace());
				Scm scm=new Scm();
				scm.setName(project.getName());
				scm.setUrl(project.getScmUrl());
				scm.setUser(project.getScmUser());
				scm.setPassword(project.getScmPassword());
				scm.setTagName(project.getScmTagName());
				entity.setTeam(AuthUtils.getDefaultTeam());
				entity.setScm(scm);
			}
			//update registry information
			entity.setRegistry(registry);
			projectService.save(entity);
			return ExecuteResult.success();
		} catch (Exception e) {
			logger.error("create or update project failed:",e);
			return ExecuteResult.failed(e.getMessage());
		}
		
	}
	
	/**
	 * 获取流水线信息（进行中的或者最后一次），可用来追踪正在进行中的或者最后一次流水线任务的结果
	 * @param projectName
	 * @return
	 */
	@GetMapping("/pipeline")
	public Pipeline getPipelineInfo(@RequestParam("artifactId")String projectName) {
		return pipelineServiceImpl.getLastPipeline(projectName);
	}
	/**
	 * 触发构建流水线
	 * @param customBuild
	 * @return
	 */
	@PostMapping("/build")
	public ExecuteResult triggerBuild(@RequestParam("artifactId")String projectName,
			@RequestBody PipelineProfileVO customBuild) {
		try {
			Project project=projectService.findByName(projectName);
			PipelineProfile profile=new PipelineProfile();
			profile.setBuildArm64Image(customBuild.isBuildArm64Image());
			profile.setBuildParam(customBuild.getBuildParam());
			profile.setSkipDependencyCheck(customBuild.isSkipDependencyCheck());
			profile.setSkipDeploy(customBuild.isSkipDeploy());
			profile.setSkipDockerBuild(customBuild.isSkipDockerBuild());
			profile.setSkipJsScan(customBuild.isSkipJsScan());
			profile.setSkipScan(customBuild.isSkipScan());
			profile.setSkipSqlScan(customBuild.isSkipSqlScan());
			profile.setSkipUnitTest(customBuild.isSkipUnitTest());
			
			project.setProfile(profile);
			pipelineServiceImpl.triggerPipeline(project,"", new ProjectBuildVo());
		} catch (Exception e) {
			logger.error("",e);
			return ExecuteResult.failed(e.getMessage());
		}
		return ExecuteResult.success();
	}
}
