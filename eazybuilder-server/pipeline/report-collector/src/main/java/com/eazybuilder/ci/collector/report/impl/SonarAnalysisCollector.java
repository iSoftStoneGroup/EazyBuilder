package com.eazybuilder.ci.collector.report.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eazybuilder.ci.collector.report.ReportCollector;
import com.eazybuilder.ci.collector.sonar.SonarServer;
import com.eazybuilder.ci.collector.util.HttpUtil;
import com.eazybuilder.ci.entity.Project;
import com.eazybuilder.ci.entity.report.Report;
import com.eazybuilder.ci.entity.report.Summary;
import com.eazybuilder.ci.entity.report.Type;

import com.eazybuilder.ci.config.LoadConfigYML;
import java.util.Properties;

public class SonarAnalysisCollector implements ReportCollector{
	private static Logger logger=LoggerFactory.getLogger(SonarAnalysisCollector.class);

	private static Properties properties = new LoadConfigYML().getConfigProperties();

	String serverUrl=System.getProperty("sonar.url", properties.getProperty("sonarqube.serverUrl"));
	
	String user=System.getProperty("sonar.user", properties.getProperty("sonarqube.user"));
	
	String password=System.getProperty("sonar.password",properties.getProperty("sonarqube.password"));

	String projectTagName=System.getProperty("project.tagName", "master");
	
	SonarServer server;
	
	public void init(){
		try {
			this.server=new SonarServer(new URI(serverUrl), user, password);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Report collectReport(File workspace,Project project,String baseUrl) {
		if(server==null) {
			init();
		}
		logger.info("sonarqube server:{}",server);
		if(project==null||
				(project.getProfile()!=null&&project.getProfile().isSkipScan())||!workspace.exists()){
			logger.info("SKIP COLLECT SONAR ANALYSIS (sonar scan is skipped)");
			return null;
		}
		List<File> taskFiles=(List<File>) FileUtils.listFiles(workspace, 
				FileFilterUtils.nameFileFilter("report-task.txt"), 
				TrueFileFilter.INSTANCE);
		File sonarTaskFile=taskFiles.get(0);
		Report report=Report.create("project分析");
		report.setType(Type.project_analysis);
		
		try (InputStream is=new FileInputStream(sonarTaskFile)){
			Properties props=new Properties();
			props.load(is);
			String projectKey=props.getProperty("projectKey");
			report.setLink(props.getProperty("dashboardUrl"));
			
			String taskUrl=props.getProperty("ceTaskUrl");
			
			long start=System.currentTimeMillis();
			while(!server.checkTaskStatus(taskUrl)){
				Thread.sleep(5000);
				if(System.currentTimeMillis()-start>3600*1000){
					//timeout
					break;
				}
			}
			Summary summary=new Summary();
			summary.headers("代码行数","单元测试覆盖率","单元测试成功率","新增代码行数","技术债","新增bug数","新增安全漏洞数","新增坏味道数","新增技术债","新增单元测试覆盖率","新增未覆盖代码行数");
			summary.addRow(server.getCodeLineNumbers(projectKey,projectTagName),
					server.getCoverage(projectKey,projectTagName),
					server.getTestSuccess(projectKey,projectTagName),
					server.getNewLineNumbers(projectKey,projectTagName),
					server.getTechnologyDept(projectKey,projectTagName),
					server.getNewBugNumbers(projectKey,projectTagName),
					server.getNewVulnerabilitieNumbers(projectKey,projectTagName),
					server.getNewCodeSmellNumbers(projectKey,projectTagName),
					server.getNewCodeTechnologyDept(projectKey,projectTagName),
					server.getNewCoverage(projectKey,projectTagName),
					server.getNewUncoveredLines(projectKey,projectTagName));
			summary.convertDataMap();
			report.setSummary(summary);
//			report.setLink(HttpUtil.getRootUrl(baseUrl)+"/sonarqube/dashboard?id="+URLEncoder.encode(projectKey,"utf-8"));
			report.setLink(HttpUtil.getRootUrl(serverUrl)+"/dashboard?branch="+projectTagName+"&id="+URLEncoder.encode(projectKey,"utf-8"));
	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return report;
	
	}
}
