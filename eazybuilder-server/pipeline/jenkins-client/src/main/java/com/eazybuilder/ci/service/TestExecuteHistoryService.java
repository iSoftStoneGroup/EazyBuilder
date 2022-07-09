package com.eazybuilder.ci.service;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.entity.test.QTestExecuteHistory;
import com.eazybuilder.ci.entity.test.TestExecuteHistory;
import com.eazybuilder.ci.repository.TestExecuteHistoryDao;

@Service
public class TestExecuteHistoryService extends AbstractCommonServiceImpl<TestExecuteHistoryDao, TestExecuteHistory>
		implements CommonService<TestExecuteHistory>{
	
	@Autowired
	FileResourceService frs;

	public TestExecuteHistory findByUid(String uid){
		return dao.findOne(QTestExecuteHistory.testExecuteHistory.uid.eq(uid)).get();
	}

	@Override
	public Page<TestExecuteHistory> pageSearch(Pageable pageable, String searchText) {
		if(StringUtils.isBlank(searchText)){
			return super.pageSearch(pageable, searchText);
		}else{
			return dao.findAll(
					QTestExecuteHistory.testExecuteHistory.planName.like("%"+searchText+"%"), pageable);
		}
	}

	@Transactional
	@Override
	public void deleteBatch(List<String> ids) {
		for(String id:ids){
			delete(id);
		}
	}

	@Override
	public void delete(String id) {
		//delete old test report 
		TestExecuteHistory history=findOne(id);
		if(StringUtils.isNotBlank(history.getReportFileId())){
			frs.delete(history.getReportFileId());
		}
		super.delete(id);
	}
	
	
}
