package com.eazybuilder.ci.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.entity.test.QTestDetailResult;
import com.eazybuilder.ci.entity.test.TestDetailResult;
import com.eazybuilder.ci.repository.TestDetailResultDao;

@Service
public class TestDetailResultService extends AbstractCommonServiceImpl<TestDetailResultDao, TestDetailResult>
				implements CommonService<TestDetailResult>{

	public Iterable<TestDetailResult> findByHistoryId(String historyId){
		return dao.findAll(QTestDetailResult.testDetailResult.historyId.eq(historyId),Sort.by(Sort.Direction.DESC,"executeOrder"));
	}
}
