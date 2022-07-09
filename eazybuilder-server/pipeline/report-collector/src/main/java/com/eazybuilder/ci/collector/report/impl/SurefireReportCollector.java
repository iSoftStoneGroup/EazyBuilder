package com.eazybuilder.ci.collector.report.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.eazybuilder.ci.entity.ProjectType;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.eazybuilder.ci.collector.report.CiReportUtil;
import com.eazybuilder.ci.collector.report.ReportCollector;
import com.eazybuilder.ci.entity.Project;
import com.eazybuilder.ci.entity.report.Report;
import com.eazybuilder.ci.entity.report.Summary;
import com.eazybuilder.ci.entity.report.Type;
public class SurefireReportCollector implements ReportCollector{
	private static Logger logger=LoggerFactory.getLogger(SurefireReportCollector.class);
	
	
	@Override
	public Report collectReport(File workspace,Project project,String baseUrl) {
		if(project==null||project.getProjectType().equals(ProjectType.net)||
				(project.getProfile()!=null&&project.getProfile().isSkipUnitTest())||!workspace.exists()){
			logger.info("SKIP COLLECT SUREFIRE REPORT (unit test is skipped)");
			return null;
		}
		List<File> siteSurefireReports=(List<File>) FileUtils.listFiles(workspace, 
				FileFilterUtils.nameFileFilter("surefire-report.html"), 
				TrueFileFilter.INSTANCE);
		File reportFile=siteSurefireReports.get(0);
		Report report=Report.create("单元测试情况");
		report.setType(Type.junit);
		
		try {
			Document doc = Jsoup.parse(reportFile, "utf-8");
			Elements tables=doc.select("table.bodyTable");
			Elements headers=tables.first().select("th");
			
			Summary summary=new Summary();
			headers.forEach(head->{
				if(head.hasText()){
					summary.addHeader(head.text());
				}
			});
			
			List<String> row=Lists.newArrayList();
			Elements tds=tables.first().select("td");
			tds.forEach(td->{
				if(td.hasText()){
					row.add(td.text());
				}
			});
			summary.getRows().add(row.toArray(new String[0]));
			summary.convertDataMap();
			report.setSummary(summary);
		
			String resourceId=CiReportUtil.saveFile(reportFile);
			report.setAttachmentId(resourceId);
			
			report.setLink(baseUrl+"/resources/"+resourceId);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return report;
	}

}
