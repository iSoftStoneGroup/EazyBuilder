package com.eazybuilder.ci.service;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.entity.appscan.AppScanScript;
import com.eazybuilder.ci.entity.appscan.QAppScanScript;
import com.eazybuilder.ci.repository.AppScanScriptDao;
import com.querydsl.core.types.dsl.BooleanExpression;

@Service
public class AppScanScriptService extends AbstractCommonServiceImpl<AppScanScriptDao, AppScanScript>
				implements CommonService<AppScanScript>{

	public Page<AppScanScript> pageSearchWithExcludes(Pageable pageable, String searchText,
			ArrayList<String> excludeIds) {
		BooleanExpression condition=null;
		
		if(StringUtils.isNotBlank(searchText)){
			condition=QAppScanScript.appScanScript.name.like("%"+searchText+"%");
		}
		if(excludeIds!=null&&excludeIds.size()>0){
			if(condition!=null){
				condition=condition.and(QAppScanScript.appScanScript.id.notIn(excludeIds));
			}else{
				condition=QAppScanScript.appScanScript.id.notIn(excludeIds);
			}
		}
		
		return condition==null?dao.findAll(pageable):dao.findAll(condition, pageable);
	}

}
