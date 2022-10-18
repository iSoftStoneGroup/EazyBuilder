package com.eazybuilder.ci.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eazybuilder.ci.controller.vo.ProjectBuildVo;
import com.eazybuilder.ci.controller.vo.UserVo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eazybuilder.ci.controller.vo.GitEvent;
import com.eazybuilder.ci.controller.vo.GitSystemPushEvent;
import com.eazybuilder.ci.entity.BuildJob;
import com.eazybuilder.ci.entity.QProject;
import com.eazybuilder.ci.entity.User;
import com.eazybuilder.ci.service.BuildJobService;
import com.eazybuilder.ci.service.PipelineServiceImpl;
import com.eazybuilder.ci.service.ProjectService;
import com.eazybuilder.ci.service.SystemPropertyService;
import com.eazybuilder.ci.service.UserService;
import com.eazybuilder.ci.util.AuthUtils;

/**
 * CI对外部提供的回调钩子
 *
 *
 */
@RestController
@RequestMapping("/wh")
public class WebHookController {
	private static Logger logger=LoggerFactory.getLogger(WebHookController.class);
	private static final String X_GITLAB_TOKEN = "X-Gitlab-Token";
	private static final String CI_TOKEN_DEFINE= "ci.gitlab.token";
	@Autowired
	BuildJobService jobService;
	@Autowired
    PipelineServiceImpl pipelineServiceImpl;
	@Autowired
	ProjectService projectService;
	@Autowired
	UserService userService;
	
	@Autowired
	SystemPropertyService sysPropService;
	
	@PostMapping("/job")
	public void jobTrigger(@RequestBody GitEvent event,@RequestParam("id")String jobId,HttpServletRequest request,HttpServletResponse response) {
		String token=request.getHeader(X_GITLAB_TOKEN);
		if(token==null) {
			logger.warn("TOKEN MISSING");
			response.setStatus(HttpStatus.FORBIDDEN.value());
			return;
		}
		BuildJob job=jobService.findOne(jobId);
		if(job==null||!token.equals(job.getWebHookToken())) {
			logger.warn("job not exist or token mismatch");
			response.setStatus(HttpStatus.FORBIDDEN.value());
			return;
		}
		logger.info("trigger job {} for event:{}",jobId,ToStringBuilder.reflectionToString(event));
		try {
			pipelineServiceImpl.triggerBatchPipeline(job,event);
		} catch (Exception e) {
			logger.error("error run job:",e);
		}
	}
	
	@PostMapping("/project")
	public void projectTrigger(@RequestBody GitEvent event,@RequestParam("id")String jobId,HttpServletRequest request,HttpServletResponse response) {
		String token=request.getHeader(X_GITLAB_TOKEN);
		if(token==null) {
			logger.warn("TOKEN MISSING");
			response.setStatus(HttpStatus.FORBIDDEN.value());
			return;
		}
		BuildJob job=jobService.findOne(jobId);
		if(job==null||!token.equals(job.getWebHookToken())) {
			logger.warn("job not exist or token mismatch");
			response.setStatus(HttpStatus.FORBIDDEN.value());
			return;
		}
		logger.info("trigger job {} for event:{}",jobId,ToStringBuilder.reflectionToString(event));
		try {
			pipelineServiceImpl.triggerBatchPipeline(job,event);
		} catch (Exception e) {
			logger.error("error run job:",e);
		}
	}
	
	@PostMapping("/system")
	public void systemTrigger(@RequestBody GitSystemPushEvent event,HttpServletRequest request,HttpServletResponse response) {
		logger.info("receive system hook event from {}",request.getRemoteAddr());
		//token validate
		String token=request.getHeader(X_GITLAB_TOKEN);
		if(token==null) {
			logger.warn("TOKEN MISSING");
			response.setStatus(HttpStatus.FORBIDDEN.value());
			return;
		}
		if(!token.equals(sysPropService.getValue(CI_TOKEN_DEFINE, "Eazybuilder-CI"))){
			logger.warn("WRONG TOKEN");
			response.setStatus(HttpStatus.FORBIDDEN.value());
			return;
		}
		if(event.getUser_email()==null||event.getProject()==null) {
			logger.warn("ignore event without user email && ref project");
			return;
		}
		//set push user
		User currentUser=userService.findByEmail(event.getUser_email());
		if(currentUser!=null) {
			AuthUtils.ACCESS_USER.set(UserVo.Instance(currentUser));
		}
		try {
			projectService.findAll(
					QProject.project.scm.url.endsWith(event.getProject().getPath_with_namespace())
					.or(QProject.project.scm.url.endsWith(event.getProject().getPath_with_namespace()+".git")))
			.forEach(project->{
				try {
					if(project.isAutoBuild()) {
						switch(event.getEvent_name()) {
						case "push":
						case "tag_push":
							project.getScm().setTagName(event.getRef());
							pipelineServiceImpl.triggerPipeline(project,event,"", new ProjectBuildVo());
							break;
						default:
							logger.warn("unsupported gitlab push event:{}",event.getEvent_name());
							return;
						}
					}else {
						logger.warn("skip auto build because project {} setting auto build to false",project.getName());
					}
				} catch (Exception e) {
					logger.error("error trigger pipeline for project:{},reason:{}",project.getName(),e.getMessage());
				}
			});
		}finally {
			AuthUtils.ACCESS_USER.remove();
		}
		
	}
}
