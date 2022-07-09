package com.eazybuilder.ci.maven.impl;

import org.apache.maven.model.Model;
import org.apache.maven.model.ReportPlugin;

import com.eazybuilder.ci.maven.POMDecorator;

public class SurefireReportPluginDecorator  implements POMDecorator{
	private static final ReportPlugin SUREFIRE_REPORT_PLUGIN=new ReportPlugin();
	static{
		SUREFIRE_REPORT_PLUGIN.setArtifactId("maven-surefire-report-plugin");
		SUREFIRE_REPORT_PLUGIN.setGroupId("org.apache.maven.plugins");
		SUREFIRE_REPORT_PLUGIN.setVersion("2.21.0");
	}
	@Override
	public void decorate(Model original) {
		original.getReporting().addPlugin(SUREFIRE_REPORT_PLUGIN);
		
	}
}
