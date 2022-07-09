package com.eazybuilder.ci.collector;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.eazybuilder.ci.entity.ProjectType;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.eazybuilder.ci.collector.report.CiReportUtil;
import com.eazybuilder.ci.collector.report.ReportCollectorRegistry;
import com.eazybuilder.ci.entity.Project;
import com.eazybuilder.ci.entity.report.Report;

public class Main {

	public static void main(String[] args) throws IOException {
		
		 
		
		if(args==null||args.length!=2) {
			System.err.println("usage: java -jar report-collector.jar ${WORKSPACE} ${RESOURCE_BASE_URL}");
			System.exit(0);
		}
		String workspace=args[0];
		File workspaceFile=new File(workspace);
		if(workspaceFile.isFile()) {
			workspace=workspaceFile.getParentFile().getAbsolutePath();
		}
		String baseUrl=args[1];
		
		File projectInfoFile=new File("./","ci-project.json");
		if(!projectInfoFile.exists()) {
			System.err.println("missing file ci-project.json in workspace: "+workspace);
			System.exit(0);
		}
		
		File pipelineUidFile=new File("./","pipeline-uuid");
		if(!pipelineUidFile.exists()) {
			System.err.println("missing file pipeline-uuid in workspace: "+workspace);
			System.exit(0);
		}
		String jsonText=new String(Base64.decodeBase64(
				FileUtils.readFileToString(projectInfoFile, Charsets.UTF_8)),"UTF-8");
		System.out.println("jsonText内容："+jsonText);
		String uuid=FileUtils.readFileToString(pipelineUidFile);
		 
		Project project=JSON.parseObject(jsonText, Project.class);

		
		List<Report> reports=Lists.newArrayList();
		String reportCollectTarget=workspace;
		System.out.println("collect report from :"+reportCollectTarget);
		ReportCollectorRegistry.getCollectors().forEach(collector->{
			Report report=null;
			if(project.getProjectType().equals(ProjectType.net)){
				report=collector.collectReport(new File(reportCollectTarget),project,baseUrl);
			}else{
				report=collector.collectReport(new File(reportCollectTarget+"/target"),project,baseUrl);
			}

			if(report!=null){
				reports.add(report);
			}
		});
		try {
			System.out.println("collect report from :"+reports.toString());
			CiReportUtil.sendReport(uuid, reports);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}
