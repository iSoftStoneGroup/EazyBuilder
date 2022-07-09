package com.eazybuilder.ci.sql.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eazybuilder.ci.sql.vo.ReportModel;

import freemarker.template.Configuration;
import freemarker.template.Template;
@Controller
@RequestMapping("/report")
public class SQLValidateReportController {
	
	@Autowired
	Configuration configuration;

	@RequestMapping("/html")
	public void htmlReport(@RequestParam("projectName")String projectName,@RequestBody ReportModel reportModel,
			HttpServletResponse response) {
		reportModel.setProjectName(projectName);
		Map<String,Object> renderModel=new HashMap<>();
		renderModel.put("report", reportModel);
		try {
			Template template=configuration.getTemplate("compatible-html-report.ftl");
			String html=FreeMarkerTemplateUtils.processTemplateIntoString(template, renderModel);
			
			String encodedfileName = URLEncoder.encode("sql-scan-result.html", StandardCharsets.UTF_8.name());
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedfileName + "\"");
			response.getOutputStream().write(html.getBytes("utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

}
