package com.eazybuilder.ci.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eazybuilder.ci.entity.MsgProfileType;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.eazybuilder.ci.dto.AppScanContext;
import com.eazybuilder.ci.dto.AppScanResult;
import com.eazybuilder.ci.dto.ExecuteResult;
import com.eazybuilder.ci.dto.IssueSummary;
import com.eazybuilder.ci.entity.FileResource;
import com.eazybuilder.ci.entity.Team;
import com.eazybuilder.ci.entity.appscan.AppScanPlan;
import com.eazybuilder.ci.entity.appscan.ScanDetail;
import com.eazybuilder.ci.entity.appscan.ScanHistory;
import com.eazybuilder.ci.entity.report.Status;
import com.eazybuilder.ci.mail.MailSenderHelper;
import com.eazybuilder.ci.service.AppScanPlanService;
import com.eazybuilder.ci.service.FileResourceService;
import com.eazybuilder.ci.service.ScanDetailService;
import com.eazybuilder.ci.service.ScanHistoryService;
import com.eazybuilder.ci.service.SystemPropertyService;
import com.eazybuilder.ci.service.TeamServiceImpl;
import com.eazybuilder.ci.util.HttpUtil;
import com.eazybuilder.ci.util.JsonMapper;

import freemarker.template.Configuration;
import freemarker.template.Template;
@Controller
@RequestMapping("/appscan")
public class AppScanReportCallbackController {
	private static Logger logger=LoggerFactory.getLogger(AppScanReportCallbackController.class);
	
	@Autowired
	private FileResourceService resourceService;
	
	@Autowired
	private ScanHistoryService scanHistoryService;
	
	@Autowired
	private ScanDetailService scanDetailService;
	
	@Autowired
	private AppScanPlanService planService;
	
	@Autowired
	private TeamServiceImpl teamServiceImpl;
	
	@Autowired
	private Configuration configuration;
	
	@Autowired
	private MailSenderHelper mailService;
	
	@Autowired
	private SystemPropertyService propertyService;
	
	@Value("${appscan.agent.url}")
	private String agentUrl;
	
	@RequestMapping(value="/upload",method=RequestMethod.POST)
	public void upload(HttpServletRequest request,HttpServletResponse response) throws IOException{
		FileResource fr=new FileResource();
		fr.setName(URLDecoder.decode(request.getHeader("X-FILE-NAME"),"UTF-8"));
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		try(InputStream is=request.getInputStream()){
			IOUtils.copyLarge(is, baos);
		}
		fr.setData(baos.toByteArray());
		resourceService.save(fr);
		response.getOutputStream().write(fr.getId().getBytes());
		response.getOutputStream().flush();
	}
	
	
	@RequestMapping(value="/report",method=RequestMethod.POST)
	public void saveReport(@RequestBody AppScanResult result){
		logger.info("IT AGENT SENDED A TEST RESULT:");
		logger.info(ToStringBuilder.reflectionToString(result));
		ScanHistory history=scanHistoryService.findByUid(result.getExecuteUid());
		if(history==null){
			throw new IllegalArgumentException("uid not exist");
		}
		history.setExecuteStatus(result.getStatus());
		int totalHigh=0;
		int totalMedium=0;
		int totalLow=0;
		int totalInformational=0;
		
		if(result.getSummarys()!=null&&result.getSummarys().size()>0){
			List<ScanDetail> details=Lists.newArrayList();
			int i=0;
			for(Entry<String,List<IssueSummary>> entry:result.getSummarys().entrySet()){
				String scriptName=entry.getKey();
				ScanDetail detail=new ScanDetail();
				detail.setScriptName(scriptName);
				detail.setHistoryId(history.getId());
				detail.setExecuteOrder(i++);
				List<IssueSummary> summarys=entry.getValue();
				for(IssueSummary summary:summarys){
					if(summary.getStatus()==Status.SUCCESS){
						switch(summary.getSeverity()){
						case High:
							totalHigh+=summary.getCount();
							detail.setTotalHigh(detail.getTotalHigh()+summary.getCount());
							break;
						case Medium:
							totalMedium+=summary.getCount();
							detail.setTotalMedium(detail.getTotalMedium()+summary.getCount());
							break;
						case Low:
							totalLow+=summary.getCount();
							detail.setTotalLow(detail.getTotalLow()+summary.getCount());
							break;
						case Informational:
							totalInformational+=summary.getCount();
							detail.setTotalInformational(detail.getTotalInformational()+summary.getCount());
							break;
						}
					}
				}
				details.add(detail);
			}
			scanDetailService.save(details);
		}
		
		history.setEndTime(result.getEndExecuteTime());
		history.setReportFileId(result.getReportFileId());
		history.setRemindMsg("");
		history.setTotalHigh(totalHigh);
		history.setTotalMedium(totalMedium);
		history.setTotalLow(totalLow);
		history.setTotalInformational(totalInformational);
		scanHistoryService.save(history);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					sendScanReport(result, history);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private void sendScanReport(AppScanResult result, ScanHistory history) throws Exception {
		logger.info("-------------");
		logger.info(JsonMapper.nonDefaultMapper().toJson(result));
		logger.info(JsonMapper.nonDefaultMapper().toJson(history));
		//渲染邮件正文
		Team team= teamServiceImpl.findOne(history.getTeamId());
		
		if(result.getStatus()==Status.SUCCESS){
			FileResource fr=resourceService.findOne(result.getReportFileId());
			
			Map<String,Object> model=Maps.newHashMap();
			model.put("result", result);
			model.put("summary",history);
			model.put("name",history.getPlanName());
			
			Template mailTemplate=configuration.getTemplate("scanReport.ftl");
			String contentHtml=FreeMarkerTemplateUtils.processTemplateIntoString(mailTemplate, model);
			
			mailService.sendMail(mailService.getReceiverMailList(team), 
					mailService.getCCMailList(team), contentHtml, "安全测试报告——"+history.getPlanName(), fr.getName(),
					new ByteArrayResource(fr.getData()));
		}else{
			Map<String,Object> model=Maps.newHashMap();
			model.put("result", result);
			model.put("summary",history);
			model.put("name",history.getPlanName());
			
			Template mailTemplate=configuration.getTemplate("scanFailedReport.ftl");
			String contentHtml=FreeMarkerTemplateUtils.processTemplateIntoString(mailTemplate, model);
			
			mailService.sendMail(mailService.getReceiverMailList(team), 
					mailService.getCCMailList(team), contentHtml, "安全测试报告——"+history.getPlanName(), MsgProfileType.dtpSecurityTest,team.getCode());
		}
		
	}
	
	@RequestMapping(value="/run",method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void trigger(@RequestParam("planId")String planId,@RequestParam("uid")String uid){
		AppScanPlan plan=planService.findOne(planId);
		
		ScanHistory history=new ScanHistory();
		history.setStartTime(new Date());
		history.setExecuteStatus(Status.NOT_EXECUTED);
		history.setPlanId(planId);
		history.setPlanName(plan.getName());
		history.setTargetUrl(plan.getTargetStartingUrl());
		history.setTeamId(plan.getTeamId());
		history.setTeamName(teamServiceImpl.findOne(plan.getTeamId()).getName());
		history.setUid(uid);
		
		scanHistoryService.save(history);
		
		
		//异步执行调远程agent，并更新记录
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					AppScanContext context=new AppScanContext();
					context.setPlanId(planId);
					context.setScripts(plan.getScripts());
					context.setTargetUrl(plan.getTargetStartingUrl());
					context.setUuid(uid);
					if(plan.getScripts()==null||plan.getScripts().isEmpty()){
						throw new IllegalArgumentException("没有任何待执行的测试脚本");
					}
					String resultJson=HttpUtil.postJson(propertyService.getValue("appscan.agent.url", agentUrl), JsonMapper.nonDefaultMapper().toJson(context));
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
				scanHistoryService.save(history);
			}
		}).start();
		
		
	}
}
