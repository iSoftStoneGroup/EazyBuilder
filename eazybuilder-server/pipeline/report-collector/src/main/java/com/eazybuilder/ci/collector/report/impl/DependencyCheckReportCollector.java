package com.eazybuilder.ci.collector.report.impl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

import com.eazybuilder.ci.entity.ProjectType;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.eazybuilder.ci.collector.report.CiReportUtil;
import com.eazybuilder.ci.collector.report.ReportCollector;
import com.eazybuilder.ci.collector.util.HttpUtil;
import com.eazybuilder.ci.entity.Project;
import com.eazybuilder.ci.entity.report.Report;
import com.eazybuilder.ci.entity.report.Summary;
import com.eazybuilder.ci.entity.report.Type;

import de.siegmar.fastcsv.reader.CsvContainer;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;

public class DependencyCheckReportCollector implements ReportCollector{

	private static Logger logger=LoggerFactory.getLogger(DependencyCheckReportCollector.class);
	
	@Override
	public Report collectReport(File workspace, Project project,String baseUrl) {
		if(project==null||project.getProjectType().equals(ProjectType.net)||
				(project.getProfile()!=null&&project.getProfile().isSkipDependencyCheck())||!workspace.exists()||!workspace.isDirectory()){
			logger.info("SKIP OWASP DC REPORT");
			return null;
		}
		try {
			List<File> dcCvsReports=(List<File>) FileUtils.listFiles(workspace, 
					FileFilterUtils.nameFileFilter("dependency-check-report.csv"), 
					null);
			if(dcCvsReports==null||dcCvsReports.isEmpty()){
				logger.warn("dependency check report not existed!");
				return null;
			}
			
			File reportFile=((List<File>)FileUtils.listFiles(workspace, 
					FileFilterUtils.nameFileFilter("dependency-check-report.html"), 
					null)).get(0);
			
			
			Report report=Report.create("依赖包安全检查");
			report.setType(Type.dependency_check);
			
			String resourceId=CiReportUtil.saveFile(reportFile);
			report.setAttachmentId(resourceId);
			report.setLink(baseUrl+"/resources/"+resourceId);
			report.setSummary(readCsv(dcCvsReports.get(0)));
			return report;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Summary readCsv(File file) throws IOException{
		CsvReader csvReader = new CsvReader();
		csvReader.setContainsHeader(true);

		CsvContainer csv = csvReader.read(file, StandardCharsets.UTF_8);
		
//		{"漏洞包总计":}
		// "含高危漏洞："
		// "含中等漏洞："
		Set<String> uniqueJars=Sets.newHashSet();
		Set<String> hightVulners=Sets.newHashSet();
		Set<String> mediumVulners=Sets.newHashSet();
		if(csv!=null&&csv.getRows()!=null){
			for (CsvRow row : csv.getRows()) {
				String jarId=row.getField("Identifiers");
				uniqueJars.add(jarId);
				
				if("HIGH".equalsIgnoreCase(row.getField("CVSSv2_Severity"))||"HIGH".equalsIgnoreCase(row.getField("CVSSv3_BaseSeverity"))
						||"CRITICAL".equalsIgnoreCase(row.getField("CVSSv2_Severity"))||"CRITICAL".equalsIgnoreCase(row.getField("CVSSv3_BaseSeverity"))){
					hightVulners.add(jarId);
				}
				
				if("MEDIUM".equalsIgnoreCase(row.getField("CVSSv2_Severity"))||"MEDIUM".equalsIgnoreCase(row.getField("CVSSv3_BaseSeverity"))){
					mediumVulners.add(jarId);
				}
			}
		}
		Summary summary=Summary.create();
		summary.setHeaders(Lists.newArrayList("Total","High","Medium"));
		summary.addRow(""+uniqueJars.size(),""+hightVulners.size(),""+mediumVulners.size());
		summary.convertDataMap();
		return summary;
		
	}

}
