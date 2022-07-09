package com.eazybuilder.ci.jenkins;


import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import com.eazybuilder.ci.entity.devops.Release;
import com.eazybuilder.ci.storage.ResourceStorageService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.google.common.collect.Maps;
import com.eazybuilder.ci.entity.Pipeline;
import com.eazybuilder.ci.entity.Project;
import com.eazybuilder.ci.entity.ScmType;
import com.eazybuilder.ci.util.JsonMapper;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.QueueReference;

import cn.hutool.core.codec.Base64;
import freemarker.template.Configuration;
import freemarker.template.Template;


public class JenkinsPipelineService {
	private static Logger logger=LoggerFactory.getLogger(JenkinsPipelineService.class);
	Configuration configuration;
	JenkinsPipelineEnv env;
	String serverUrl;
	boolean k8sCloudSupport;
	String sonarUrl;
	Jenkins jenkins;
	String dockerImageTag;
	Release release;
	private ResourceStorageService storageService;

	public JenkinsPipelineService(String jenkinsUrl,
								  String sonarUrl,
								  Configuration configuration,
								  boolean k8sSupport,
								  JenkinsPipelineEnv env,
								  String dockerImageTag
	) throws Exception {
		this.configuration=configuration;
		this.env=env;
		if(jenkinsUrl==null||sonarUrl==null) {
			throw new IllegalArgumentException("jenkins url or sonar url must not be null");
		}
		this.serverUrl=jenkinsUrl;
		this.sonarUrl=sonarUrl;
		this.k8sCloudSupport=k8sSupport;
		this.dockerImageTag = dockerImageTag;
//		logger.info("Jenkins URL:{} K8s Support:{},SonarQube URL:{}",this.serverUrl,this.k8sCloudSupport,this.sonarUrl);
		initJenkins();
	}

	public void initJenkins() throws Exception{
		jenkins=new Jenkins(new URI(serverUrl), env.getUser(), env.getPassword());
	}


	public String generatePipeLineJob(Project project,String pipeLineUid) throws Exception{
		boolean crumbflag=env.crumb();
		String credentialId=UUID.randomUUID().toString();
		jenkins.addCredential(credentialId, project.getScm().getUser(), project.getScm().getPassword(), "",crumbflag);

		Map<String,Object> params=Maps.newHashMap();
		params.put("credentialsId", credentialId);
		params.put("project",project);
		if(!project.getDeployConfigList().isEmpty()&&project.getDeployConfigList().size()>0) {
			params.put("deployConfig", project.getDeployConfigList().get(0));
			params.put("deployConfigList", project.getDeployConfigList());
		}
		params.put("projectJSON", Base64.encode(JsonMapper.nonDefaultMapper().toJson(project)));
		params.put("pipelineUID", pipeLineUid);
		params.put("toolGitUrl",env.getToolGitUrl());
		params.put("baseUrl", env.getBaseUrl());
		params.put("k8sSupport", this.k8sCloudSupport);
		params.put("sonarUrl",this.sonarUrl);
		params.put("sonarUser",env.getSonarUser());
		params.put("sonarPassword",env.getSonarPassword());
		params.put("release",release);
		
//		params.put("jenkinsDataPath",env.getJenkinsDataPath());
//		params.put("jenkinsLimitMeory",env.getJenkinsLimitMeory());
//		params.put("jenkinsNetworkHost",env.getJenkinsNetworkHost());
//		params.put("jenkinsRequestMeory",env.getJenkinsRequestMeory());
//		params.put("jenkinsPathType",env.getJenkinsPathType());
//		params.put("jenkinsMavenUrl",env.getJenkinsMavenUrl());
//		params.put("jenkinsBuildNode",env.getJenkinsBuildNode());
//		params.put("jenkinsTeamGitlabUrl",env.getJenkinsTeamGitlabUrl());
//		params.put("jenkinsTeamGitlabHost",env.getJenkinsTeamGitlabHost());
		params=env.initEnvParams(params);
		

		if(!project.getDeployConfigList().isEmpty()) {
			String yamlId=project.getDeployConfigList().get(0).getYamlId();
			String nameSpace=project.getDeployConfigList().get(0).getNameSpace();
			String rolloutName=project.getDeployConfigList().get(0).getName();
			logger.info("yaml文件在华为obs中的key值:{}",yamlId);
			params.put("yamlId",yamlId);
			params.put("namespace",nameSpace);
			params.put("rolloutName",rolloutName);
		}

//)
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
		if(StringUtils.isBlank(dockerImageTag)){
			dockerImageTag=df.format(new Date());
		}
		params.put("dockerImageTag",dockerImageTag);

		if(project.getProfile()!=null&&project.getProfile().isBuildArm64Image()) {
			//构建ARM64镜像时 使用单独指定的docker host(ARM64平台)
			params.put("dockerBuildHost",env.getArm64DockerBuildHost());
		}else {
			params.put("dockerBuildHost",env.getDockerBuildHost());
		}
		if(StringUtils.isNotBlank(project.getBuildParam())){
			params.put("buildParam",project.getBuildParam());
		}else if(StringUtils.isNotBlank(project.getProfile().getBuildParam())){
			params.put("buildParam",project.getProfile().getBuildParam());
		}else{
			params.put("buildParam",project.getProfile().getBuildParam());
		}


		if(StringUtils.isNotBlank(project.getScm().getArriveTagName())){
			params.put("tagName",project.getScm().getArriveTagName());
		}else if(StringUtils.isNotBlank(project.getScm().getTagName())){
			params.put("tagName",project.getScm().getTagName());
		}else{
			params.put("tagName","master");
		}
		logger.info("流水线运行分支：{}",params.get("tagName"));
		Template template=null;
		if(project.getScm().getType()==ScmType.svn){
			template=configuration.getTemplate("pipe-svn-maven.ftl");
		}else{
			template=configuration.getTemplate("pipe-git-maven.ftl");
		}
		if(logger.isDebugEnabled()) {
			logger.debug("-------prepared to generate pipeline------");
			logger.debug(JsonMapper.nonDefaultMapper().toJson(params));
		}
		return FreeMarkerTemplateUtils.processTemplateIntoString(template, params);
	}

	public String createPipeLine(Project project,String pipeLineUid,boolean override) throws Exception{
		boolean crumbflag=env.crumb();
		long tag=System.currentTimeMillis();
		//给job名字加一个时间戳，允许任务并行
//		String jobName=project.getName()+"-"+pipeLineUid;
		logger.info("jenkins job name:{}",project.getJobName());

		try {
			Job job=jenkins.getJob(project.getJobName());
			if(job!=null){
				if(override){
					jenkins.deleteJob(project.getJobName(),crumbflag);
				}else{
					throw new RuntimeException("Pipe Line :"+project.getJobName()+" Already Exsited!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除job出错，捕捉异常，不作处理",e);

		}
		String jenkinsPipe=generatePipeLineJob(project,pipeLineUid);
		logger.debug("jenkins job xml:");
		logger.debug("jenkins日志:{}",jenkinsPipe);
		System.out.println("jenkins日志:{}: "+jenkinsPipe);
		jenkins.createJob(project.getJobName(), jenkinsPipe,crumbflag);
		return project.getJobName();
	}

	public Pipeline getLastPipeline(String projectName) throws IOException {
		return jenkins.getLastPipeline(projectName);
	}

	public void runPipeLine(String name) throws IOException{
		boolean crumbflag=env.crumb();
		logger.info("run job:{} crumb:{}",name,crumbflag);
		
		 
		
		QueueReference qr=jenkins.getJob(name).build(crumbflag);
		
		String jenkinsQueueId = qr.getQueueItemUrlPart().substring(qr.getQueueItemUrlPart().indexOf("/queue/item"));
		logger.info("jenkins job任务号:{}",jenkinsQueueId);
		this.jenkins.waitUnitRun(jenkinsQueueId);
		
//		String[] jenkins = qr.getQueueItemUrlPart().split("jenkins");
//		String[] jenkins = qr.getQueueItemUrlPart().split("/");
//
//		System.err.println("jenkins job创建"+jenkins.toString());
////		logger.info("jenkins job:",jenkins);
//		this.jenkins.waitUnitRun(jenkins[2]);
	}

	/**
	 * 获取jenkins console日志
	 * @param projectName
	 * @return  jenkins 日志信息
	 * @throws IOException
	 */
	public String getLastPipelineConsoleText(String projectName) throws IOException {
		String consoleText =  jenkins.getLastBuildLog(projectName);
		logger.debug("jenkins执行日志:{}",consoleText);
		if(logger.isDebugEnabled()) {
			logger.debug("jenkins执行日志:{}",consoleText);
		}
		return consoleText;
	}

	public Pipeline attachPipeLine(String name){
		long start=System.currentTimeMillis();
		boolean done=false;
		do{
			try {
				done=jenkins.isPipeLineDone(name);
				if(!done){
					try {
						Thread.sleep(10*1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				if((System.currentTimeMillis()-start)>5*60*1000){
					throw new RuntimeException("attach pipeline timeout");
				}else {
					try {
						//try again later
						Thread.sleep(10*1000);
					} catch (InterruptedException e1) {
					}
				}
			}
		}while(!done);

		logger.info("流水线执行完毕:{}",name);
		try {
			Pipeline pipeline=jenkins.getLastPipeline(name);
			pipeline.setScmVersion(jenkins.getCheckOutRevision(name));
			return pipeline;
		} catch (Exception e) {
			logger.error("failed to fetch scm revision in this build",e);
		}
		return null;
	}
	/**
	 * 是否在老项目流水线运行完毕后清理workspace以节约disk空间
	 *
	 * (ant类项目工程内通常均包含大量不可复用jar，这类工程硕大无比，默认开启清理以腾出磁盘空间)
	 * @return
	 */
	public boolean clearLegacyWorkspaceAfterPipeline(){
		try{
			return env.clearLegacyWorkspace();
		}catch(Exception e){
			return true;
		}
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public String getSonarUrl() {
		return sonarUrl;
	}

	public void setSonarUrl(String sonarUrl) {
		this.sonarUrl = sonarUrl;
	}


}
