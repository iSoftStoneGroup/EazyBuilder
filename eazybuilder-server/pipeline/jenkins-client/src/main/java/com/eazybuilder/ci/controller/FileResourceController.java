package com.eazybuilder.ci.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.eazybuilder.ci.OperLog;
import com.eazybuilder.ci.base.CRUDRestController;
import com.eazybuilder.ci.entity.FileResource;
import com.eazybuilder.ci.service.FileResourceService;

@RestController
@RequestMapping("/api/fileResource")
public class FileResourceController extends CRUDRestController<FileResourceService, FileResource>{
	
	@Autowired
	FileResourceService fileResourceService;

	@RequestMapping(value="/upload",method=RequestMethod.POST)
	@OperLog(module = "file",opType = "upload",opDesc = "上传文件")
	public FileResource storeResource(@RequestParam("uploadfile")MultipartFile request) throws Exception{
		FileResource fr=new FileResource();
		fr.setData(request.getBytes());
		fr.setName(request.getOriginalFilename());
		fileResourceService.save(fr);
		return fr;
	}
}
