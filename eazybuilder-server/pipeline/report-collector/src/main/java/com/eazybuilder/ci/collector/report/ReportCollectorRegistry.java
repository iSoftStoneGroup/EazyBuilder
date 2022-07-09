package com.eazybuilder.ci.collector.report;

import com.google.common.collect.Lists;
import com.eazybuilder.ci.collector.report.impl.DependencyCheckReportCollector;
import com.eazybuilder.ci.collector.report.impl.JacocoReportCollector;
import com.eazybuilder.ci.collector.report.impl.SQLScanReportCollector;
import com.eazybuilder.ci.collector.report.impl.SonarAnalysisCollector;
import com.eazybuilder.ci.collector.report.impl.SonarReportCollector;
import com.eazybuilder.ci.collector.report.impl.SurefireReportCollector;

import java.util.List;

public class ReportCollectorRegistry {

	static private List<ReportCollector> registry=Lists.newArrayList();
	
	static {
		registry.add(new DependencyCheckReportCollector());
		registry.add(new JacocoReportCollector());
		registry.add(new SonarAnalysisCollector());
		registry.add(new SonarReportCollector());
		registry.add(new SQLScanReportCollector());
		registry.add(new SurefireReportCollector());
	}
	
	public static List<ReportCollector> getCollectors(){
		return registry;
	}
	
}
