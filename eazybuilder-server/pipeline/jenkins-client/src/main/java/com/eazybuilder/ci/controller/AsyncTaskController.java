package com.eazybuilder.ci.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eazybuilder.ci.util.AsyncTaskTracker;

@RestController
@RequestMapping("/api/asyncTask")
public class AsyncTaskController {

	@Autowired
	AsyncTaskTracker tracker;
	
	@RequestMapping(value="/{uid}",method=RequestMethod.GET)
	public Map<String,Object> getTaskProgress(@PathVariable("uid")String taskUid){
		return tracker.getExecuteProgress(taskUid);
	}
}
