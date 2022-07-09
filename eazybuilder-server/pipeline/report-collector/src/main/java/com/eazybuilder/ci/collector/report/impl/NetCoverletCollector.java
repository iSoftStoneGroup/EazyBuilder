package com.eazybuilder.ci.collector.report.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.eazybuilder.ci.entity.ProjectType;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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

public class NetCoverletCollector implements ReportCollector{
    private static Logger logger=LoggerFactory.getLogger(NetCoverletCollector.class);

    @Override
    public Report collectReport(File workspace,Project project,String baseUrl) {
        if(project==null||!project.getProjectType().equals(ProjectType.net)||
                (project.getProfile()!=null&&project.getProfile().isSkipUnitTest())
                ||!workspace.exists()){
            logger.info("SKIP COLLECT COVERLET REPORT (unit test is skipped)");
            return null;
        }

        List<File> coverageFiles=(List<File>) FileUtils.listFiles(workspace,
                FileFilterUtils.nameFileFilter("coverage.xml"),
                TrueFileFilter.INSTANCE);
        File reportFile=coverageFiles.get(0);
        Report report=Report.create("测试覆盖率情况");
        report.setType(Type.jacoco);

        try {
            Document doc = Jsoup.parse(reportFile, "utf-8");

            Elements coverageSessions = doc.getElementsByTag("CoverageSession");
            Element coverageSession = coverageSessions.get(0);
            Elements coverletSummaries = coverageSession.getElementsByTag("Summary");

            List<String> headers = new ArrayList<String>();
            headers.add("numSequencePoints");
            headers.add("visitedSequencePoints");
            headers.add("numBranchPoints");
            headers.add("visitedBranchPoints");
            headers.add("sequenceCoverage");
            headers.add("branchCoverage");
            headers.add("maxCyclomaticComplexity");
            headers.add("minCyclomaticComplexity");
            headers.add("visitedClasses");
            headers.add("numClasses");
            headers.add("visitedMethods");
            headers.add("numMethods");
            Summary summary=new Summary();
            summary.setHeaders(headers);

            for(int i=0;i<coverletSummaries.size();i++){
                Element coverletSummary = coverletSummaries.get(i);
                String[] row = new String[12];
                row[0]=coverletSummary.attr("numSequencePoints");
                row[1]=coverletSummary.attr("visitedSequencePoints");
                row[2]=coverletSummary.attr("numBranchPoints");
                row[3]=coverletSummary.attr("visitedBranchPoints");
                row[4]=coverletSummary.attr("sequenceCoverage");
                row[5]=coverletSummary.attr("branchCoverage");
                row[6]=coverletSummary.attr("maxCyclomaticComplexity");
                row[7]=coverletSummary.attr("minCyclomaticComplexity");
                row[8]=coverletSummary.attr("visitedClasses");
                row[9]=coverletSummary.attr("numClasses");
                row[10]=coverletSummary.attr("visitedMethods");
                row[11]=coverletSummary.attr("numMethods");
                summary.addRow(row);
            }
            summary.convertDataMap();
            report.setSummary(summary);

            String resourceId=CiReportUtil.saveFile(reportFile.getAbsoluteFile().getParentFile());
            report.setAttachmentId(resourceId);
            report.setLink("");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return report;
    }

}
