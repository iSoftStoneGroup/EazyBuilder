package com.eazybuilder.ci.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.entity.test.AutomaticScript;
import com.eazybuilder.ci.entity.test.QAutomaticScript;
import com.eazybuilder.ci.repository.AutomaticScriptDao;
import com.querydsl.core.types.dsl.BooleanExpression;

@Service
public class AutomaticScriptService extends AbstractCommonServiceImpl<AutomaticScriptDao, AutomaticScript>
			implements CommonService<AutomaticScript>{

	public Page<AutomaticScript> pageSearchWithExcludes(Pageable pageable, String searchText,List<String> excludeIds) {
		BooleanExpression condition=null;
		
		if(StringUtils.isNotBlank(searchText)){
			condition=QAutomaticScript.automaticScript.name.like("%"+searchText+"%");
		}
		if(excludeIds!=null&&excludeIds.size()>0){
			if(condition!=null){
				condition=condition.and(QAutomaticScript.automaticScript.id.notIn(excludeIds));
			}else{
				condition=QAutomaticScript.automaticScript.id.notIn(excludeIds);
			}
		}
		
		return condition==null?dao.findAll(pageable):dao.findAll(condition, pageable);
	}
}
