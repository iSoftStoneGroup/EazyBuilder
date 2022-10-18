package com.eazybuilder.ci.job;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.transaction.Transactional;

import com.eazybuilder.ci.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.eazybuilder.ci.constant.MetricType;
import com.eazybuilder.ci.controller.vo.ProjectQAReport;
import com.eazybuilder.ci.controller.vo.WarnTeamReportVo;
import com.eazybuilder.ci.mail.MailSenderHelper;
import com.eazybuilder.ci.service.MetricService;
import com.eazybuilder.ci.service.PipelineServiceImpl;
import com.eazybuilder.ci.service.ProjectGroupService;
import com.eazybuilder.ci.service.ProjectService;
import com.eazybuilder.ci.service.WarnService;
import com.eazybuilder.ci.util.JsonMapper;

import freemarker.template.Configuration;
import freemarker.template.Template;
@Component
public class ReportJob {
	private static Logger logger=LoggerFactory.getLogger(ReportJob.class);
	
	@Autowired
	ProjectService projectService;
	
	@Autowired
	ProjectGroupService projectGroupService;
	@Autowired
    PipelineServiceImpl pipelineServiceImpl;
	@Autowired
	MetricService metricService;
	@Autowired
	WarnService warnService;
	@Autowired
	private Configuration configuration;
	@Autowired
	private MailSenderHelper mailService;
	
	@Value("${base.url}")
	private String baseUrl;
	
	@Transactional
	public void check(Warn warn){
		Set<ProjectGroup> groups=warn.getScanGroups();
		List<WarnTeamReportVo> voList = new ArrayList<WarnTeamReportVo>();
		List<User> tmpUsers = new ArrayList<User>(warn.getReceivingUsers());
		if(warn.getWarnType()== WarnType.gao_liang_all_code){
			voList = searchWarnTeamReportByWarnRule(groups,new ArrayList<WarnRule>(warn.getWarnRules()));
		}else if(warn.getWarnType()==WarnType.all_code){
			voList = searchWarnTeamReport(groups);
		}else if(warn.getWarnType()==WarnType.du_liang_report){
			logger.info("定期发送度量报告");
			logger.info(warn.getDuLiangUrl());
			Set<ProjectGroup> scanGroups = warn.getScanGroups();
			List<ProjectGroup> list = new ArrayList(scanGroups);
			try {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Calendar c = Calendar.getInstance();
				Map<String,Object> model=Maps.newHashMap();
				model.put("groupName", list.get(0).getName());
				model.put("reportDate", new Date());
				if(warn.getDuLiangType()==null) {
					model.put("buildUrl", warn.getDuLiangUrl());
				}else if(warn.getDuLiangType()==DuLiangType.weeks_report){
					//过去七天
					c.setTime(new Date());
					c.add(Calendar.DATE, - 7);
					Date d = c.getTime();
					String day = format.format(d);
					model.put("buildUrl", warn.getDuLiangUrl()+"&startDate="+day+"&endDate="+format.format(new Date()));
				}else if(warn.getDuLiangType()==DuLiangType.month_report){
					//过去一月
					c.setTime(new Date());
					c.add(Calendar.MONTH, -1);
					Date m = c.getTime();
					String mon = format.format(m);
					model.put("buildUrl", warn.getDuLiangUrl()+"&startDate="+mon+"&endDate="+format.format(new Date()));
				}
				logger.info(JsonMapper.nonDefaultMapper().toJson(model));
				Template template = configuration.getTemplate("mail-duliang.ftl");
				String contentHtml=FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
				warn.getScanTeams().forEach(team ->
						mailService.sendMail(mailService.getReceiverMailList(tmpUsers),
								null, contentHtml, list.get(0).getName()+"度量报告",MsgProfileType.monitoringMeasureReport,team.getCode()));

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(null!=voList&&voList.size()>0){
			try {
				Map<String,Object> model=Maps.newHashMap();
				model.put("projectReport", voList);
				model.put("reportDate", new Date());
				model.put("baseUrl",baseUrl);
				logger.info("-----WARN REPORT MODEL-----");
				logger.info(JsonMapper.nonDefaultMapper().toJson(model));
				Template template = configuration.getTemplate("warnTeamReport.ftl");
				String contentHtml=FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
				warn.getScanTeams().forEach(team ->
						mailService.sendMail(mailService.getReceiverMailList(tmpUsers),
								null, contentHtml, "质量检查报告",MsgProfileType.monitoringMeasureReport,team.getCode()));
			} catch (Exception e) {
				logger.error("render report mail failed",e);
			}
		}
	}

	private List<WarnTeamReportVo> searchWarnTeamReportByWarnRule(Set<ProjectGroup> groups,List<WarnRule> rules){
		if(null!=rules&&rules.size()>0){
			List<WarnTeamReportVo> voList = new ArrayList<WarnTeamReportVo>();
			Boolean flag = false;
			Boolean warnFlag = false;
			
			if(null!=groups&&groups.size()>0){
				for(ProjectGroup group:groups){
					Set<Project> projects=group.getProjects();
					if(projects!=null&&projects.size()>0){
						WarnTeamReportVo vo = new WarnTeamReportVo();
						vo.setProjectNum((long) projects.size());
						vo.setTeamName(group.getName());
						Long bugBlockerSum=0L;
						Long vulnerBlockerSum=0L;
						Long codeSmellBlocker=0L;
						Long dcHighSum=0L;
						Double unitTestCoverageRate=0d;
						List<ProjectQAReport> reports=Lists.newArrayList();
						List<ProjectQAReport> warnReports=Lists.newArrayList();
						for(Project project:projects){
							ProjectQAReport report=new ProjectQAReport();
							report.setProject(project);
							Page<Pipeline> pipPage= pipelineServiceImpl.pageQueryByProjectId(project.getId(),
									PageRequest.of(0, 1, Direction.DESC, "startTimeMillis"));
							if(pipPage!=null&&pipPage.getTotalElements()>0){
								Pipeline pl=pipPage.getContent().get(0);
								report.setLatestPipeline(pl);
								List<Metric> metricList=metricService.findLatestScanInfoByProjectId(project.getId());
								
								
								if(null!=metricList&&metricList.size()>0){
									for(int p=0;p<metricList.size();p++){
										for(int q=0;q<rules.size();q++){
											Metric tmpMetric = metricList.get(p);
											WarnRule tmpRule= rules.get(q);
											Double tmpValue = 0d;
											try{
												tmpValue = Double.valueOf(tmpMetric.getVal());
											}catch(Exception e){
											}
											
											if(tmpRule.getMetricType().equals(tmpMetric.getType())){
												if(null!=tmpRule.getThresholdMin()&&null!=tmpRule.getThresholdMax()){
													if(tmpValue>=tmpRule.getThresholdMin()&&tmpValue<=tmpRule.getThresholdMax()){
														flag=true;
														warnFlag=true;
														tmpMetric.setIsRed(true);
													}
												}else if(null!=tmpRule.getThresholdMin()&&null==tmpRule.getThresholdMax()){
													if(tmpValue>=tmpRule.getThresholdMin()){
														flag=true;
														warnFlag=true;
														tmpMetric.setIsRed(true);
													}
												}else if(null==tmpRule.getThresholdMin()&&null!=tmpRule.getThresholdMax()){
													if(tmpValue<=tmpRule.getThresholdMax()){
														flag=true;
														warnFlag=true;
														tmpMetric.setIsRed(true);
													}
												}else{
													flag=true;
													warnFlag=true;
													tmpMetric.setIsRed(true);
												}
											}
										}
									}
								}
								
								report.setMetrics(metricList);
								
								if(null!=metricList&&metricList.size()>0){
									for(int mt=0;mt<metricList.size();mt++){
										Metric mtemp = metricList.get(mt);
										try{
											if(MetricType.bug_blocker.equals(mtemp.getType())){
												bugBlockerSum+=Long.parseLong(mtemp.getVal());
											}else if(MetricType.vulner_blocker.equals(mtemp.getType())){
												vulnerBlockerSum+=Long.parseLong(mtemp.getVal());
											}else if(MetricType.code_smell_blocker.equals(mtemp.getType())){
												codeSmellBlocker+=Long.parseLong(mtemp.getVal());
											}else if(MetricType.dc_high.equals(mtemp.getType())){
												dcHighSum+=Long.parseLong(mtemp.getVal());
											}else if(MetricType.unit_test_coverage_rate.equals(mtemp.getType())){
												unitTestCoverageRate+=Double.parseDouble(mtemp.getVal().replaceAll("%", ""));
											}
										}catch(Exception e){
											e.printStackTrace();
										}
									}
								}
							}
							reports.add(report);
							
							if(warnFlag){
								warnReports.add(report);
							}
							
							warnFlag=false;
						}
						
						vo.setBugBlockerSum(bugBlockerSum);
						vo.setVulnerBlockerSum(vulnerBlockerSum);
						vo.setCodeSmellBlocker(codeSmellBlocker);
						vo.setDcHighSum(dcHighSum);
						vo.setUnitTestCoverageRate(unitTestCoverageRate/vo.getProjectNum());
						vo.setProjectQAReports(reports);
						vo.setWarnProjectReports(warnReports);
						voList.add(vo);
					}				
				}
				if(flag){
					return voList;
				}else{
					return null;
				}
				
			}
			return null;
		}else{
			return null;
		}
	}
	
	private List<WarnTeamReportVo> searchWarnTeamReport(Set<ProjectGroup> groups){
		List<WarnTeamReportVo> voList = new ArrayList<WarnTeamReportVo>();
		
		if(null!=groups&&groups.size()>0){
			for(ProjectGroup group:groups){
				Set<Project> projects=group.getProjects();
				if(projects!=null&&projects.size()>0){
					WarnTeamReportVo vo = new WarnTeamReportVo();
					vo.setProjectNum((long) projects.size());
					vo.setTeamName(group.getName());
					Long bugBlockerSum=0L;
					Long vulnerBlockerSum=0L;
					Long codeSmellBlocker=0L;
					Long dcHighSum=0L;
					Double unitTestCoverageRate=0d;
					
					List<ProjectQAReport> reports=Lists.newArrayList();
					for(Project project:projects){
						ProjectQAReport report=new ProjectQAReport();
						report.setProject(project);
						Page<Pipeline> pipPage= pipelineServiceImpl.pageQueryByProjectId(project.getId(),
								PageRequest.of(0, 1, Direction.DESC, "startTimeMillis"));
						if(pipPage!=null&&pipPage.getTotalElements()>0){
							Pipeline pl=pipPage.getContent().get(0);
							report.setLatestPipeline(pl);
							List<Metric> metricList = metricService.findLatestScanInfoByProjectId(project.getId());
							report.setMetrics(metricList);
							if(null!=metricList&&metricList.size()>0){
								for(int mt=0;mt<metricList.size();mt++){
									Metric mtemp = metricList.get(mt);
									try{
										if(MetricType.bug_blocker.equals(mtemp.getType())){
											bugBlockerSum+=Long.parseLong(mtemp.getVal());
										}else if(MetricType.vulner_blocker.equals(mtemp.getType())){
											vulnerBlockerSum+=Long.parseLong(mtemp.getVal());
										}else if(MetricType.code_smell_blocker.equals(mtemp.getType())){
											codeSmellBlocker+=Long.parseLong(mtemp.getVal());
										}else if(MetricType.dc_high.equals(mtemp.getType())){
											dcHighSum+=Long.parseLong(mtemp.getVal());
										}else if(MetricType.unit_test_coverage_rate.equals(mtemp.getType())){
											unitTestCoverageRate+=Double.parseDouble(mtemp.getVal().replaceAll("%", ""));
										}
									}catch(Exception e){
										e.printStackTrace();
									}
								}
							}
						}
						reports.add(report);
					}
					vo.setBugBlockerSum(bugBlockerSum);
					vo.setVulnerBlockerSum(vulnerBlockerSum);
					vo.setCodeSmellBlocker(codeSmellBlocker);
					vo.setDcHighSum(dcHighSum);
					vo.setUnitTestCoverageRate(unitTestCoverageRate/vo.getProjectNum());
					vo.setProjectQAReports(reports);
					vo.setWarnProjectReports(Lists.newArrayList());
					voList.add(vo);
				}				
			}	
			return voList;
		}
		return null;
	}

}
