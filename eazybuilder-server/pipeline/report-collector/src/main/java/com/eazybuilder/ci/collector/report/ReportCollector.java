package com.eazybuilder.ci.collector.report;

import java.io.File;

import com.eazybuilder.ci.entity.Project;
import com.eazybuilder.ci.entity.report.Report;

public interface ReportCollector {

	public Report collectReport(File workspace, Project project,String baseUrl);
}
