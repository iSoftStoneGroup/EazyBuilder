package com.eazybuilder.ci.controller;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.eazybuilder.ci.entity.FileResource;
import com.eazybuilder.ci.service.FileResourceService;

@Deprecated
@Controller
@RequestMapping("/download")
public class ResourceDownloadController {
	
	@Autowired
	FileResourceService fileResourceService;

	@RequestMapping("/testEnvTemplate")
	public void downloadTemplate(HttpServletResponse response) throws Exception{
		File file=new File("config/template/测试环境变量模板.xlsx");
		String encodedfileName = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8.name());
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedfileName + "\"");
		response.getOutputStream().write(FileUtils.readFileToByteArray(file));
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public void downloadFile(@RequestParam("fileId")String fileId,HttpServletResponse response)throws Exception{
		FileResource resource=fileResourceService.findOne(fileId);
		String encodedfileName = URLEncoder.encode(resource.getName(), StandardCharsets.UTF_8.name());
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedfileName + "\"");
		response.getOutputStream().write(resource.getData());
	}
}
