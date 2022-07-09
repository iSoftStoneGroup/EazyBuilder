package com.eazybuilder.ci.controller;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eazybuilder.ci.base.CRUDRestController;
import com.eazybuilder.ci.base.PageResult;
import com.eazybuilder.ci.entity.test.AutomaticScript;
import com.eazybuilder.ci.service.AutomaticScriptService;
import com.eazybuilder.ci.util.JsonMapper;
import com.wordnik.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/atmScript")
public class AutomaticScriptController extends CRUDRestController<AutomaticScriptService, AutomaticScript>{

	@RequestMapping(value="/pageWithExcludes",method=RequestMethod.GET)
	@ApiOperation("按页查询，支持指定排除项")
	public PageResult<AutomaticScript> page(
			@RequestParam(value="limit",defaultValue="10")int limit,
			@RequestParam(value="offset")int offset,
			@RequestParam(value="search",required=false)String searchText,@RequestParam(value="excludes",required=false)String excludes){
		if(StringUtils.isNotBlank(excludes)){
			JsonMapper mapper=JsonMapper.nonDefaultMapper();
			ArrayList<String> excludeIds=mapper.fromJson(excludes, mapper.contructCollectionType(ArrayList.class, String.class));
			Pageable pageable=PageRequest.of(Math.floorDiv(offset, limit), limit,Direction.DESC,"id");
			Page<AutomaticScript> page=service.pageSearchWithExcludes(pageable, searchText, excludeIds);
			return PageResult.create(page.getTotalElements(), page.getContent());
		}
		return super.page(limit, offset, searchText, null);
		
	}

}
