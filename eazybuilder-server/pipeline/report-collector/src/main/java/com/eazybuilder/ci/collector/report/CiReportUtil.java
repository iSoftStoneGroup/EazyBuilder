package com.eazybuilder.ci.collector.report;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import com.alibaba.fastjson.JSON;
import com.eazybuilder.ci.collector.util.HttpUtil;
import com.eazybuilder.ci.collector.util.ZipUtil;
import com.eazybuilder.ci.entity.report.Report;

public class CiReportUtil {
	
	private static final String CI_ADDRESS=System.getProperty("ci.address", "http://ci-jenkins-client:8080/ci");

	public static String saveFile(File file) throws Exception {
		if(file.isDirectory()) {
			File zipFile=new File(FileUtils.getTempDirectory(),UUID.randomUUID().toString()+".zip");
			ZipUtil.pack(file.getAbsolutePath(), zipFile.getAbsolutePath());
			return HttpUtil.postFileStream(CI_ADDRESS+"/pipeline-callback/file-upload", zipFile);
		}
		return HttpUtil.postFileStream(CI_ADDRESS+"/pipeline-callback/file-upload", file);
	}
	
	public static void sendReport(String pipelineUid,List<Report> reports)throws Exception{
		System.out.println("发送sonarqube扫描报告给CI,url:"+CI_ADDRESS);
		System.out.println("发送sonarqube扫描报告给CI,pipelineUid:"+pipelineUid);
		System.out.println("发送sonarqube扫描报告给CI,data:"+JSON.toJSONString(reports));
		HttpUtil.postJson(CI_ADDRESS+"/pipeline-callback/report?pipelineUid="+pipelineUid, JSON.toJSONString(reports));
	}
	
}
