package com.eazybuilder.ci.controller;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.eazybuilder.ci.base.CRUDRestController;
import com.eazybuilder.ci.base.PageResult;
import com.eazybuilder.ci.entity.FileResource;
import com.eazybuilder.ci.entity.appscan.AppScanScript;
import com.eazybuilder.ci.service.AppScanScriptService;
import com.eazybuilder.ci.service.FileResourceService;
import com.eazybuilder.ci.util.JsonMapper;
import com.wordnik.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/appscan/script")
public class AppScanScriptController extends CRUDRestController<AppScanScriptService, AppScanScript>{

	@Autowired
	FileResourceService fileResourceService;

	@RequestMapping(value="/upload",method=RequestMethod.POST)
	public FileResource storeResource(@RequestParam("uploadfile")MultipartFile request) throws Exception{
		FileResource fr=new FileResource();
		fr.setData(request.getBytes());
		fr.setName(request.getOriginalFilename());
		fileResourceService.save(fr);
		return fr;
	}
	
	@RequestMapping(value="/pageWithExcludes",method=RequestMethod.GET)
	@ApiOperation("按页查询，支持指定排除项")
	public PageResult<AppScanScript> page(
			@RequestParam(value="limit",defaultValue="10")int limit,
			@RequestParam(value="offset")int offset,
			@RequestParam(value="search",required=false)String searchText,@RequestParam(value="excludes",required=false)String excludes){
		if(StringUtils.isNotBlank(excludes)){
			JsonMapper mapper=JsonMapper.nonDefaultMapper();
			ArrayList<String> excludeIds=mapper.fromJson(excludes, mapper.contructCollectionType(ArrayList.class, String.class));
			Pageable pageable=PageRequest.of(Math.floorDiv(offset, limit), limit,Direction.DESC,"id");
			Page<AppScanScript> page=service.pageSearchWithExcludes(pageable, searchText, excludeIds);
			return PageResult.create(page.getTotalElements(), page.getContent());
		}
		return super.page(limit, offset, searchText, null);
		
	}

}
