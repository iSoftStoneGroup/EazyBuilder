package com.eazybuilder.ci.collector.report.impl;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.eazybuilder.ci.collector.report.CiReportUtil;
import com.eazybuilder.ci.collector.report.ReportCollector;
import com.eazybuilder.ci.entity.Project;
import com.eazybuilder.ci.entity.report.Report;
import com.eazybuilder.ci.entity.report.Summary;
import com.eazybuilder.ci.entity.report.Type;
public class SQLScanReportCollector implements ReportCollector{

	private static Logger logger=LoggerFactory.getLogger(SQLScanReportCollector.class);
	
	
	@Override
	public Report collectReport(File workspace, Project project,String baseUrl) {
		if(project==null||
				(project.getProfile()!=null&&project.getProfile().isSkipSqlScan())||!workspace.exists()||!workspace.isDirectory()){
			logger.info("SKIP SQL SCAN REPORT");
			return null;
		}
		try {
			List<File> sqlScanReports=(List<File>) FileUtils.listFiles(workspace, 
					FileFilterUtils.nameFileFilter("sql-scan-result.json"), 
					TrueFileFilter.INSTANCE);
			if(sqlScanReports==null||sqlScanReports.isEmpty()){
				logger.warn("sql scan report not existed!");
				return null;
			}
			
			File reportFile=((List<File>)FileUtils.listFiles(workspace, 
					FileFilterUtils.nameFileFilter("sql-scan-result.html"), 
					TrueFileFilter.INSTANCE)).get(0);
			
			
			Report report=Report.create("SQL检查");
			report.setType(Type.sql_scan);
			
			String resourceId=CiReportUtil.saveFile(reportFile);
			report.setAttachmentId(resourceId);
			report.setLink(baseUrl+"/resources/"+resourceId);
			report.setSummary(readJson(sqlScanReports.get(0)));
			return report;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Summary readJson(File file) throws Exception {
		String json=FileUtils.readFileToString(file, "utf-8");
		JSONObject result=JSON.parseObject(json);
		int totalSql=result.getIntValue("totalSql");
		int totalFound=result.getIntValue("totalFound");
		
		Summary summary=Summary.create();
		summary.setHeaders(Lists.newArrayList("total","imcompatible"));
		summary.addRow(""+totalSql,""+totalFound);
		summary.convertDataMap();
		return summary;
	}

}
