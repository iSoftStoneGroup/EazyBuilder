package com.eazybuilder.ci.controller;

import java.io.IOException;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.eazybuilder.ci.constant.MetricType;
import com.eazybuilder.ci.entity.Metric;
import com.eazybuilder.ci.entity.report.ResourceItem;
import com.eazybuilder.ci.service.MetricService;
import com.eazybuilder.ci.storage.ResourceStorageService;

import cn.hutool.http.HttpStatus;

/**
 * Resource Download Controller
 *
 */
@Controller
public class ResourceController {

	@Autowired
	ResourceStorageService storageService;
	
	@Autowired
	MetricService metricService;
	
	
	@GetMapping("/link/sonarqube/{type}/{severities}")
	public void sonarUrl(@RequestParam("projectKey")String projectKey,
			@PathVariable("severities")String serverities,
			@PathVariable("type")String type,HttpServletResponse response) {
		StringBuilder url=new StringBuilder();
		url.append("/sonarqube/project/issues?id=").append(projectKey).append("&resolved=false")
				.append("&severities=").append(serverities)
				.append("&types=")
				.append(type);
		response.setStatus(HttpStatus.HTTP_MOVED_PERM);
		response.setHeader("Location", url.toString());
	}
	
	@GetMapping("/link/dependency-check")
	public void dependencyCheckUrl(@RequestParam("projectId")String projectId,HttpServletResponse response) {
		List<Metric> metrics=metricService.findLatestScanInfoByProjectId(projectId);
		if(metrics!=null) {
			String resourceUrl=null;
			for(Metric m:metrics) {
				if(m.getType()==MetricType.dc_high) {
					resourceUrl=m.getLink();
					break;
				}
			}
			if(resourceUrl!=null) {				
				response.setStatus(HttpStatus.HTTP_MOVED_TEMP);
				response.setHeader("Location", resourceUrl);
			}
		}
	}
	
	
	@GetMapping("/resources/{resourceId}")
	public void download(@PathVariable("resourceId")String resourceId,HttpServletResponse response) throws IOException {
		ResourceItem resource=storageService.get(resourceId);
		if(resource==null) {
			response.setStatus(404);
			return;
		}
		String resourceName=resource.getName()==null?resourceId:resource.getName();
		String mimeType=URLConnection.guessContentTypeFromName(resourceName);
		if(mimeType!=null&&!mimeType.startsWith("application")) {
			response.setContentType(mimeType);
			if(mimeType.startsWith("text")) {
				response.setCharacterEncoding("utf-8");
			}
		}else {
			String encodedfileName = URLEncoder.encode(resourceName, StandardCharsets.UTF_8.name());
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedfileName + "\"");
		}
		
		response.getOutputStream().write(resource.getData());
	}
}
