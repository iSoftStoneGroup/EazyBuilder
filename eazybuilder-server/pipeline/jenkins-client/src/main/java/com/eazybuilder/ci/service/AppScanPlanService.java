package com.eazybuilder.ci.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.constant.ExecuteType;
import com.eazybuilder.ci.entity.appscan.AppScanPlan;
import com.eazybuilder.ci.entity.appscan.AppScanScript;
import com.eazybuilder.ci.entity.appscan.QAppScanPlan;
import com.eazybuilder.ci.repository.AppScanPlanDao;
import com.eazybuilder.ci.repository.AppScanScriptDao;

@Service
public class AppScanPlanService extends AbstractCommonServiceImpl<AppScanPlanDao, AppScanPlan>
				implements CommonService<AppScanPlan>{

	@Autowired
	AppScanScriptDao scriptDao;
	
	@Override
	@Transactional
	public void save(AppScanPlan entity) {
		List<AppScanScript> scripts=Lists.newArrayList();
		if(entity.getScripts()!=null&&entity.getScripts().size()>0){
			for(AppScanScript script:entity.getScripts()){
				scripts.add(scriptDao.findById(script.getId()).get());
			}
		}
		entity.setScripts(scripts);
		if(entity.getExecuteType()==ExecuteType.timePlan){
			if(!CronSequenceGenerator.isValidExpression(entity.getCron())){
				throw new IllegalArgumentException("错误的CRON表达式");
			}
			Date nextTime=new CronTrigger(entity.getCron()).nextExecutionTime(new SimpleTriggerContext());
			entity.setNextTime(nextTime.getTime());
		}
		super.save(entity);
	}
	
	@Override
	public Page<AppScanPlan> pageSearch(Pageable pageable, String searchText) {
		if(StringUtils.isBlank(searchText)){
			return super.pageSearch(pageable, searchText);
		}else{
			return dao.findAll(QAppScanPlan.appScanPlan.name.like("%"+searchText+"%"), pageable);
		}
	}
}
