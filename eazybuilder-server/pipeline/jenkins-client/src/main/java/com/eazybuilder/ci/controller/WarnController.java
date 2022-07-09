package com.eazybuilder.ci.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eazybuilder.ci.base.CRUDRestController;
import com.eazybuilder.ci.controller.vo.ExecuteResult;
import com.eazybuilder.ci.entity.Warn;
import com.eazybuilder.ci.job.ReportJob;
import com.eazybuilder.ci.service.WarnService;

@RestController
@RequestMapping("/api/warn")
public class WarnController extends CRUDRestController<WarnService,Warn>{
	@Autowired
	ReportJob job;
	
	@RequestMapping(method=RequestMethod.POST,value="/{id}")
	public ExecuteResult executeRule(@PathVariable("id")String warnRuleId){
		Warn warn=service.findOne(warnRuleId);
		if(warn==null){
			return ExecuteResult.failed("规则不存在");
		}else{
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							job.check(warn);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();
		}
		return ExecuteResult.success();
	}
}
