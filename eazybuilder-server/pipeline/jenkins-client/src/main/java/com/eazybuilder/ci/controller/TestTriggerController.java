package com.eazybuilder.ci.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.eazybuilder.ci.dto.AutomaticTestResult;
import com.eazybuilder.ci.dto.AutomaticTestResult.Summary;
import com.eazybuilder.ci.entity.FileResource;
import com.eazybuilder.ci.entity.Team;
import com.eazybuilder.ci.entity.test.TestDetailResult;
import com.eazybuilder.ci.entity.test.TestExecuteHistory;
import com.eazybuilder.ci.mail.MailSenderHelper;
import com.eazybuilder.ci.service.FileResourceService;
import com.eazybuilder.ci.service.IntegrateTestPlanService;
import com.eazybuilder.ci.service.TeamServiceImpl;
import com.eazybuilder.ci.service.TestDetailResultService;
import com.eazybuilder.ci.service.TestExecuteHistoryService;

import freemarker.template.Configuration;
import freemarker.template.Template;
@Controller
@RequestMapping("/it")
public class TestTriggerController {
	private static Logger logger=LoggerFactory.getLogger(TestTriggerController.class);
	
	@Autowired
	private TeamServiceImpl teamServiceImpl;
	
	@Autowired
	private IntegrateTestPlanService itPlanService;
	
	@Autowired
	private TestExecuteHistoryService historyService;
	
	@Autowired
	private FileResourceService resourceService;
	
	@Autowired
	TestDetailResultService detailService;
	
	@Autowired
	FileResourceService fileService;
	
	@Autowired
	MailSenderHelper mailService;
	
	@Autowired
	Configuration configuration;
	
	
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
	public void saveReport(@RequestBody AutomaticTestResult result){
		logger.info("IT AGENT SENDED A TEST RESULT:");
		logger.info(ToStringBuilder.reflectionToString(result));
		TestExecuteHistory history=historyService.findByUid(result.getExecuteUid());
		if(history==null){
			throw new IllegalArgumentException("uid not exist");
		}
		history.setExecuteStatus(result.getStatus());
		history.setTotalFailed(result.getTotalFailed());
		history.setTotalPass(result.getTotalPass());
		history.setTotalWarning(result.getTotalWarning());
		history.setEndTime(result.getEndExecuteTime());
		history.setReportFileId(result.getReportFileId());
		history.setRemindMsg("");
		historyService.save(history);
		
		List<TestDetailResult> details=Lists.newArrayList();
		if(result.getScriptsSummary()!=null&&result.getScriptsSummary().size()>0){
			int idx=1;
			for(Entry<String,Summary> entry:result.getScriptsSummary().entrySet()){
				TestDetailResult dr=new TestDetailResult();
				dr.setExecuteOrder(idx);
				dr.setHistoryId(history.getId());
				dr.setScriptName(entry.getKey());
				dr.setTotalFailed(entry.getValue().getTotalFailed());
				dr.setTotalPass(entry.getValue().getTotalPass());
				dr.setTotalWarning(entry.getValue().getTotalWarning());
				details.add(dr);
				idx++;
			}
		}
		detailService.save(details);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					sendTestReport(result,history);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();;
	}
	
	
	private void sendTestReport(AutomaticTestResult result, TestExecuteHistory history) throws Exception {
		//渲染邮件正文
		Team team= teamServiceImpl.findOne(history.getTeamId());
		FileResource fr=fileService.findOne(result.getReportFileId());
		
		Map<String,Object> model=Maps.newHashMap();
		model.put("result", result);
		model.put("name",history.getPlanName());
		
		Template mailTemplate=configuration.getTemplate("testReport.ftl");
		String contentHtml=FreeMarkerTemplateUtils.processTemplateIntoString(mailTemplate, model);
		
		mailService.sendMail(mailService.getReceiverMailList(team), 
				mailService.getCCMailList(team), contentHtml, "自动化测试报告——"+history.getPlanName(), fr.getName(),
				new ByteArrayResource(fr.getData()));
		
	}
	
	@RequestMapping(value="/run",method=RequestMethod.GET)
	@ResponseStatus(code=HttpStatus.OK)
	public void trigger(@RequestParam("id")String planId,@RequestParam("uid")String uid){
		itPlanService.trigger(planId, uid);
	}
}
