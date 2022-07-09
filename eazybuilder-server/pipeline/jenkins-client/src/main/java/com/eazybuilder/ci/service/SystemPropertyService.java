package com.eazybuilder.ci.service;

import org.springframework.stereotype.Service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.entity.QSystemProperty;
import com.eazybuilder.ci.entity.SystemProperty;
import com.eazybuilder.ci.repository.SystemPropertyDao;

import java.util.Optional;

@Service
public class SystemPropertyService extends AbstractCommonServiceImpl<SystemPropertyDao, SystemProperty> 
                                                implements CommonService<SystemProperty>{

	public String getValue(String key){
		SystemProperty prop=dao.findOne(QSystemProperty.systemProperty.cfgKey.eq(key)).get();
		if(prop==null){
			return null;
		}else{
			return prop.getCfgVal();
		}
	}
	
	public String getValue(String key,String defaultVal){
		Optional<SystemProperty> prop=dao.findOne(QSystemProperty.systemProperty.cfgKey.eq(key));
		if(prop.isPresent()){
			return prop.get().getCfgVal();
		}else{
			return defaultVal;
		}
	}
}
