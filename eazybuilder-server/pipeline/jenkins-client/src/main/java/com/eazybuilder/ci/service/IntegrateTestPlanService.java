package com.eazybuilder.ci.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.constant.ExecuteType;
import com.eazybuilder.ci.controller.vo.ExecuteResult;
import com.eazybuilder.ci.dto.AutomaticTestContext;
import com.eazybuilder.ci.entity.Team;
import com.eazybuilder.ci.entity.report.Status;
import com.eazybuilder.ci.entity.test.AutomaticScript;
import com.eazybuilder.ci.entity.test.IntegrateTestPlan;
import com.eazybuilder.ci.entity.test.QIntegrateTestPlan;
import com.eazybuilder.ci.entity.test.ScriptType;
import com.eazybuilder.ci.entity.test.TestExecuteHistory;
import com.eazybuilder.ci.repository.AutomaticScriptDao;
import com.eazybuilder.ci.repository.IntegrateTestPlanDao;
import com.eazybuilder.ci.util.HttpUtil;
import com.eazybuilder.ci.util.JsonMapper;

@Service
public class IntegrateTestPlanService extends AbstractCommonServiceImpl<IntegrateTestPlanDao, IntegrateTestPlan> 
		implements CommonService<IntegrateTestPlan>{
	private static Logger logger=LoggerFactory.getLogger(IntegrateTestPlanService.class);

	@Autowired
	private JdbcTemplate template;
	
	@Autowired
	AutomaticScriptDao scriptDao;
	
	@Autowired
	private TestEnvSetService envService;
	
	@Autowired
	private TeamServiceImpl teamServiceImpl;
	
	@Autowired
	private TestExecuteHistoryService historyService;
	
	@Value("${qtp.agent.url}")
	private String qtpAgentUrl;
	
	@Value("${selenium.agent.url}")
	private String seleniumAgentUrl;
	
	@Autowired
	private SystemPropertyService propertyService;
	
	@Override
	@Transactional
	public void save(IntegrateTestPlan entity) {
		List<AutomaticScript> scripts=Lists.newArrayList();
		if(entity.getScripts()!=null&&entity.getScripts().size()>0){
			for(AutomaticScript script:entity.getScripts()){
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
	public Page<IntegrateTestPlan> pageSearch(Pageable pageable, String searchText) {
		if(StringUtils.isBlank(searchText)){
			return super.pageSearch(pageable, searchText);
		}else{
			return dao.findAll(QIntegrateTestPlan.integrateTestPlan.name.like("%"+searchText+"%"), pageable);
		}
	}

	public void trigger(String planId,String uid){
		IntegrateTestPlan plan= findOne(planId);
		if(plan==null){
			throw new IllegalArgumentException("plan not found");
		}
		Team team= teamServiceImpl.findOne(plan.getTeamId());
		
		AutomaticTestContext ctx=new AutomaticTestContext();
		ctx.setPlanId(plan.getId());
		ctx.setScripts(plan.getScripts());
		ctx.setEnv(envService.findOne(plan.getEnvId()));
		ctx.setUuid(uid);
		
		TestExecuteHistory history=new TestExecuteHistory();
		history.setUid(uid);
		history.setStartTime(new Date());
		history.setExecuteStatus(Status.NOT_EXECUTED);
		history.setPlanId(plan.getId());
		history.setPlanName(plan.getName());
		history.setTeamId(plan.getTeamId());
		history.setTeamName(team.getName());
		
		historyService.save(history);
		
		//异步执行调远程agent，并更新记录
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String agentUrl=null;
					if(plan.getScripts()==null||plan.getScripts().isEmpty()){
						throw new IllegalArgumentException("没有任何待执行的测试脚本");
					}
					if(plan.getScripts().get(0).getScriptType()==ScriptType.qtp){
						agentUrl=propertyService.getValue("qtp.agent.url",qtpAgentUrl);
					}else{
						agentUrl=propertyService.getValue("selenium.agent.url",seleniumAgentUrl);
					}
					String resultJson=HttpUtil.postJson(agentUrl, JsonMapper.nonDefaultMapper().toJson(ctx));
					ExecuteResult result=JsonMapper.nonEmptyMapper().fromJson(resultJson, ExecuteResult.class);
					if(result.isSuccess()){
						history.setExecuteStatus(Status.IN_PROGRESS);
						history.setRemindMsg("正在执行");
					}else{
						history.setExecuteStatus(Status.ABORTED);
						history.setRemindMsg("[AGENT ERROR]"+result.getRemindMsg());
					}
				} catch (Exception e) {
					logger.error("invoke test agent failed",e);
					history.setExecuteStatus(Status.ABORTED);
					history.setRemindMsg(e.getMessage());
				}
				historyService.save(history);
			}
		}).start();
	}
	
	public Iterable<IntegrateTestPlan> findAllJob(){
		return dao.findAll(QIntegrateTestPlan.integrateTestPlan.executeType.eq(ExecuteType.timePlan));
	}
	
	public void updateNextTime(IntegrateTestPlan job,Long nextTime){
		template.update("update ci_test_plan set next_time = ? , last_trigger = ? where id=? and next_time=?", new Object[]{nextTime,System.currentTimeMillis(),job.getId(),job.getNextTime()});
		job.setNextTime(nextTime);
	}
	
	public Iterable<IntegrateTestPlan> findAllTriggerByProjectId(String projectId){
		return dao.findAll(QIntegrateTestPlan.integrateTestPlan.executeType.eq(ExecuteType.afterDeploy)
				.and(QIntegrateTestPlan.integrateTestPlan.projectId.eq(projectId)));
	}
}
