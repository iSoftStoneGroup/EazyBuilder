package com.eazybuilder.ci.collector.report.impl;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.eazybuilder.ci.entity.ProjectType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.eazybuilder.ci.collector.report.CiReportUtil;
import com.eazybuilder.ci.collector.report.ReportCollector;
import com.eazybuilder.ci.entity.Project;
import com.eazybuilder.ci.entity.report.Report;
import com.eazybuilder.ci.entity.report.Summary;
import com.eazybuilder.ci.entity.report.Type;

public class JacocoReportCollector implements ReportCollector{
	private static Logger logger=LoggerFactory.getLogger(JacocoReportCollector.class);
	
	@Override
	public Report collectReport(File workspace,Project project,String baseUrl) {
		if(project==null||project.getProjectType().equals(ProjectType.net)||
				(project.getProfile()!=null&&project.getProfile().isSkipUnitTest())
				||!workspace.exists()){
			logger.info("SKIP COLLECT JACOCO REPORT (unit test is skipped)");
			return null;
		}
		File reportFile=new File(workspace,"site/jacoco-aggregate/index.html");
		Report report=Report.create("测试覆盖率情况");
		report.setType(Type.jacoco);
		
		try {
			Document doc = Jsoup.parse(reportFile, "utf-8");
			Elements tables=doc.select("#coveragetable");
			Elements headers=tables.first().select("thead td");
			
			Map<String,Integer> headerDuplicate=Maps.newHashMap();
			Summary summary=new Summary();
			headers.forEach(head->{
				if(head.hasText()){
					String headTxt=head.text();
					if(headerDuplicate.containsKey(headTxt)){
						int no=(headerDuplicate.get(headTxt)+1);
						summary.addHeader(headTxt+no);
						headerDuplicate.put(headTxt, no);
					}else{
						summary.addHeader(head.text());
						headerDuplicate.put(headTxt, 1);
					}
				}
			});
			
			List<String> row=Lists.newArrayList();
			Elements tds=tables.first().select("tfoot td");
			tds.forEach(td->{
				if(td.hasText()){
					row.add(td.text());
				}
			});
			summary.getRows().add(row.toArray(new String[0]));
			summary.convertDataMap();
			report.setSummary(summary);
		
			String resourceId=CiReportUtil.saveFile(reportFile.getAbsoluteFile().getParentFile());
			report.setAttachmentId(resourceId);
			report.setLink(baseUrl+"/resources/"+resourceId);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return report;
	}

}
