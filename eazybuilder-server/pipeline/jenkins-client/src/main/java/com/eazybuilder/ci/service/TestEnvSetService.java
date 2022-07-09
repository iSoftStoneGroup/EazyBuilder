package com.eazybuilder.ci.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.entity.test.EnvSet;
import com.eazybuilder.ci.entity.test.QEnvSet;
import com.eazybuilder.ci.repository.EnvSetDao;
@Service
public class TestEnvSetService extends AbstractCommonServiceImpl<EnvSetDao, EnvSet> 
			implements CommonService<EnvSet>{

	@Override
	public Page<EnvSet> pageSearch(Pageable pageable, String searchText) {
		if(StringUtils.isBlank(searchText)){
			return super.pageSearch(pageable, searchText);
		}else{
			return dao.findAll(QEnvSet.envSet.name.like("%"+searchText+"%"), pageable);
		}
	}

}
