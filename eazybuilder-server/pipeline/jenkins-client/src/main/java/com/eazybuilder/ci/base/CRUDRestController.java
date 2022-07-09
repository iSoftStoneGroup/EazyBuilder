package com.eazybuilder.ci.base;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.eazybuilder.ci.OperLog;
import com.wordnik.swagger.annotations.ApiOperation;

public abstract class CRUDRestController<S extends CommonService<T>,T> {

	@Autowired
	protected S service;

	@RequestMapping(method={RequestMethod.POST,RequestMethod.PUT})
	@ApiOperation("保存")
	@OperLog(module = "persist",opType = "save",opDesc = "保存")
	public T save(@RequestBody T entity){
		service.save(entity);
		return entity;
	}

	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	@ApiOperation("根据ID查找")
	public T findOne(@PathVariable("id")String id){
		return service.findOne(id);
	}

	@RequestMapping(method=RequestMethod.DELETE)
	@ApiOperation("删除")
	@OperLog(module = "persist",opType = "delete",opDesc = "删除")
	public void delete(@RequestBody List<String>  ids){
		service.deleteBatch(ids);
	}

	@RequestMapping(method=RequestMethod.GET)
	@ApiOperation("查询全部")
	public Iterable<T> list(){
		return service.findAll();
	}

	@RequestMapping(value="/page",method=RequestMethod.GET)
	@ApiOperation("按页查询")
	public PageResult<T> page(
			@RequestParam(value="limit",defaultValue="10")int limit,
			@RequestParam(value="offset")int offset,
			@RequestParam(value="search",required=false)String searchText,HttpServletRequest request){

		Pageable pageable=PageRequest.of(Math.floorDiv(offset, limit), limit,Direction.DESC,"id");
//		PageRequest.of(Math.floorDiv(offset, limit), limit, Direction.DESC, "");

		Page<T> page=service.pageSearch(pageable, searchText);

		PageResult<T> result=PageResult.create(page.getTotalElements(), page.getContent());
		return result;
	}

}

