package com.eazybuilder.ci.collector.report.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.eazybuilder.ci.collector.report.ReportCollector;
import com.eazybuilder.ci.collector.sonar.QualityType;
import com.eazybuilder.ci.collector.sonar.Serverities;
import com.eazybuilder.ci.collector.sonar.SonarServer;
import com.eazybuilder.ci.collector.util.HttpUtil;
import com.eazybuilder.ci.entity.Project;
import com.eazybuilder.ci.entity.report.Report;
import com.eazybuilder.ci.entity.report.Summary;
import com.eazybuilder.ci.entity.report.Type;

import com.eazybuilder.ci.config.LoadConfigYML;
import java.util.Properties;

public class SonarReportCollector implements ReportCollector{
	private static Logger logger=LoggerFactory.getLogger(SonarReportCollector.class);
	private static Properties properties = new LoadConfigYML().getConfigProperties();
	
	String serverUrl=System.getProperty("sonar.url", properties.getProperty("sonarqube.serverUrl"));
	
	String user=System.getProperty("sonar.user", properties.getProperty("sonarqube.user"));
	
	String password=System.getProperty("sonar.password", properties.getProperty("sonarqube.password"));
	
	String projectTagName=System.getProperty("project.tagName", "master");
	String projectUrl=System.getProperty("project.url", properties.getProperty("project.url"));
	
	
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
		
		logger.info("收集sonarqube代码质量报告--project:{},isSkipScan:{},workspace.exists():{}",project,project.getProfile().isSkipScan(),workspace.exists());
		logger.info("workspace-------{}",workspace.getAbsolutePath());
		
		if(project==null||
				(project.getProfile()!=null&&project.getProfile().isSkipScan())||!workspace.exists()){
			logger.info("SKIP COLLECT SONAR REPORT (sonar scan is skipped)");
			return null;
		}
		List<File> taskFiles=(List<File>) FileUtils.listFiles(workspace, 
				FileFilterUtils.nameFileFilter("report-task.txt"), 
				TrueFileFilter.INSTANCE);
		File sonarTaskFile=taskFiles.get(0);
		Report report=Report.create("Sonar扫描报告");
		report.setType(Type.sonar);
		
		try (InputStream is=new FileInputStream(sonarTaskFile)){
			Properties props=new Properties();
			props.load(is);
			String projectKey=props.getProperty("projectKey");
			if(projectKey!=null){
				report.setSonarKey(projectKey);
			}
			report.setLink(props.getProperty("dashboardUrl"));
			
			String taskUrl=props.getProperty("ceTaskUrl");
			
			long start=System.currentTimeMillis();
			System.out.println("sonar连接信息："+JSONObject.toJSONString(server));
			while(!server.checkTaskStatus(taskUrl)){
				Thread.sleep(5000);
				if(System.currentTimeMillis()-start>3600*1000){
					//timeout
					break;
				}
			}
			Summary summary=new Summary();
			summary.headers("类型","阻断","严重","主要","普通","轻微");
			for(QualityType type:QualityType.values()){
				Serverities serverity=server.getServerity(projectKey, type, projectTagName);
				summary.addRow(type.toString(),
						""+serverity.getBlocker(),
						""+serverity.getCritical(),
						""+serverity.getMajor(),
						""+serverity.getInfo(),
						""+serverity.getMinor());
			}
			
			summary.convertDataMap();
			report.setSummary(summary);
//			report.setLink(HttpUtil.getRootUrl(baseUrl)+"/sonarqube/project/issues?id="+URLEncoder.encode(projectKey,"utf-8")+"&resolved=false");
			report.setLink(HttpUtil.getRootUrl(serverUrl)+"/project/issues?id="+URLEncoder.encode(projectKey,"utf-8")+"&resolved=false&branch="+projectTagName);

			//设置gitlab对应的url，tag
			System.out.println("git地址："+projectUrl+",分支:"+projectTagName);
			report.setProjectTagName(projectTagName);
			report.setProjectUrl(projectUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return report;
	
	}

}
