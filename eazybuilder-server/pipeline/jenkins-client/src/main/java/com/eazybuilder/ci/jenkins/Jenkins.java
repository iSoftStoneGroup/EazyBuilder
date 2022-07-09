package com.eazybuilder.ci.jenkins;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.eazybuilder.ci.entity.Pipeline;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Job;

public class Jenkins {
	private static Logger logger=LoggerFactory.getLogger(Jenkins.class);
	SimpleJenkinsHttpClient httpClient;
	JenkinsServer jenkins;
	String adminUser;
	String adminPass;
	URI uri;
	
	public Jenkins(URI uri,String user,String pass) {
		httpClient=new SimpleJenkinsHttpClient(uri, user, pass);
		jenkins = new JenkinsServer(httpClient);
		this.adminUser=user;
		this.adminPass=pass;
		this.uri=uri;
	}
	
	public void addCredential(String id,String user,String pass,String desc,boolean crumbFlag) throws IOException{
		Map<String,Object> param=Maps.newHashMap();
		param.put("", "0");
		param.put("credentials", new Credential(id, user, pass, desc));
		
		httpClient.post_formData(
				uri.getScheme()+"://"+adminUser+":"+adminPass+"@"
						+uri.getHost()
						+(uri.getPort()>0?(":"+uri.getPort()):"")+(uri.getPath()==null?"":"/"+uri.getPath())+
						"/credentials/store/system/domain/_/createCredentials", 
						param, crumbFlag);
	}
	
	public Map<String, Job> getJobs() throws IOException{
		return jenkins.getJobs();
	}
	
	public Job getJob(String name) throws IOException{
		return jenkins.getJob(name);
	}
	
	public void createJob(String jobName, String jobXml,boolean crumbFlag) throws IOException{
		jenkins.createJob(jobName, jobXml,crumbFlag);
	}

	public void updateJob(String jobName, String jobXml, boolean crumbFlag) throws IOException{
		jenkins.updateJob(jobName, jobXml, crumbFlag);
	}
	
	public void deleteJob(String jobName,boolean crumbFlag) throws IOException{
		jenkins.deleteJob(jobName,crumbFlag);
	}
	

	public SimpleJenkinsHttpClient getHttpClient() {
		return httpClient;
	}

	public JenkinsServer getJenkins() {
		return jenkins;
	}
	
	public String getLastBuildNumber(String jobName){
		try {
			return httpClient.get("/job/"+jobName+"/lastBuild/buildNumber");
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("cannot get last build number");
		}
	}
	
	public String getLastBuildLog(String jobName) throws IOException {
		String buildNumber=getLastBuildNumber(jobName);
		try(InputStream is=httpClient.getStreamDirectly("/job/"+jobName+"/"+buildNumber+"/consoleText")){
			return IOUtils.toString(is, Charsets.UTF_8);
		}
	}
	
	public Pipeline getLastPipeline(String jobName) throws IOException {
		Map pipeRunInfo=httpClient.getDirectly("/job/"+jobName+"/wfapi/",Map.class);
		String lastRunId=pipeRunInfo.get("runCount").toString();
//		/job/:job-name/:run-id/wfapi/describe
		return httpClient.getDirectly("/job/"+jobName+"/"+lastRunId+"/wfapi/describe", Pipeline.class);
	}
	
	public boolean isPipeLineDone(String jobName){
		Pipeline ppl= null;
		try {
			ppl = getLastPipeline(jobName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(ppl==null){
			return true;
		}
//		logger.info("jenkins:{} 任务执行状态:{}",jobName,ToStringBuilder.reflectionToString(ppl, ToStringStyle.MULTI_LINE_STYLE));
		return ppl.getStatus().isFinished();
	}
	
	public void waitUnitRun(String queueUrl){
		Map queueMap=null;
		do{
		    queueMap=getQueueInfo(queueUrl+"/api/json");
		    try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}while(queueMap==null||(queueMap!=null&&queueMap.get("executable")==null));
		logger.info("JOB STARTED");
	}
	
	public Map getQueueInfo(String url){
		try {
			return httpClient.getDirectly(url, Map.class);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public String getCheckOutRevision(String jobName){
		try {
			Map lastRun=httpClient.getDirectly("/job/"+jobName+"/1/wfapi/", Map.class);
			List<Map> stages=(List<Map>) lastRun.get("stages");
			String stageId=null;
			for(Map stage:stages){
				if("checkout from scm".equals(stage.get("name"))){
					stageId=(String)stage.get("id");
					break;
				}
			}
			if(stageId!=null){
				Map stageDescribe=httpClient.getDirectly("/job/"+jobName+"/1/execution/node/"+stageId+"/wfapi/describe", Map.class);
				List<Map> stageNodes=(List<Map>) stageDescribe.get("stageFlowNodes");
				StringBuilder consoleLog=new StringBuilder();
				String nodeId=(String) stageNodes.get(0).get("id");
				if(nodeId!=null){
					Map log=httpClient.getDirectly("/job/"+jobName+"/1/execution/node/"+nodeId+"/wfapi/log", Map.class);
					consoleLog.append(log.get("text"));
				}
				String keywords="At revision";
				int idx=consoleLog.lastIndexOf(keywords);
				if(idx>0){
					return consoleLog.substring(idx+keywords.length()).trim();
				}
				keywords="Checking out Revision";
				idx=consoleLog.lastIndexOf(keywords);
				if(idx>0){
					return consoleLog.substring(idx+keywords.length()+1,consoleLog.indexOf(" ", idx+keywords.length()+1)).trim();
				}
			}
			return null;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		
	}
	
}
