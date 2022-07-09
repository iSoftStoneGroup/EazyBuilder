package com.eazybuilder.ci.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.entity.QWarn;
import com.eazybuilder.ci.entity.Warn;
import com.eazybuilder.ci.repository.WarnDao;

@Service
public class WarnService extends AbstractCommonServiceImpl<WarnDao, Warn>
		implements CommonService<Warn> {
	
	@Override
	@Transactional
	public void save(Warn entity) {
		if (!CronSequenceGenerator.isValidExpression(entity.getCron())) {
			throw new IllegalArgumentException("错误的CRON表达式");
		}
		Date nextTime = new CronTrigger(entity.getCron())
				.nextExecutionTime(new SimpleTriggerContext());
		entity.setNextTime(nextTime.getTime());
		entity=dao.save(entity);
		
		
	}
	/**
	 * 查找所有启用中的规则
	 * @return
	 */
	public Iterable<Warn> findAllEnableWarn(){
		return dao.findAll(QWarn.warn.isEnable.eq(Boolean.TRUE));
	}
	
	@Override
	public void delete(String id) {
		dao.deleteById(id);
	}
	
	@Transactional
	@Override
	public void deleteBatch(List<String> ids) {
		for(String id:ids){
			dao.deleteById(id);
		}
	}
}
