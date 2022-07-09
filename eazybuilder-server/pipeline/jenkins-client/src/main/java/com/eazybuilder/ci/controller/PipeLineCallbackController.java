package com.eazybuilder.ci.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eazybuilder.ci.dto.ExecuteResult;
import com.eazybuilder.ci.entity.QUserActivityStatistic;
import com.eazybuilder.ci.entity.UserActivityStatistic;
import com.eazybuilder.ci.entity.report.Report;
import com.eazybuilder.ci.entity.report.ResourceItem;
import com.eazybuilder.ci.rabbitMq.SendRabbitMq;
import com.eazybuilder.ci.service.PipelineServiceImpl;
import com.eazybuilder.ci.service.UserActivityStatisticService;
import com.eazybuilder.ci.storage.ResourceStorageService;
/**
 * 处理流水线执行后的回调/信息上报

 *
 */
@RestController
@RequestMapping("/pipeline-callback")
public class PipeLineCallbackController {
	private static Logger logger=LoggerFactory.getLogger(PipeLineCallbackController.class);
	@Autowired
	ResourceStorageService storageService;
	
	@Autowired
	PipelineServiceImpl pipelineServiceImpl;
	
	@Autowired
	UserActivityStatisticService userActivityService;
	
	@Autowired
	SendRabbitMq sendRabbitMq;
	
	@PostMapping("/file-upload")
	public void upload(HttpServletRequest request,HttpServletResponse response) {
		String fileName=request.getHeader("X-FILE-NAME");
		ResourceItem item=new ResourceItem();
		item.setName(fileName);
		try {
			item.setData(IOUtils.toByteArray(request.getInputStream()));
			String fid=storageService.save(item);
			response.setStatus(200);
			response.getWriter().write(fid);
		} catch (IOException e) {
		  logger.error("上传文件出错:{}",e.getMessage());
		}
		
	}
	
	@PostMapping("/report")
	public ExecuteResult pipelineReport(@RequestBody List<Report> reports,@RequestParam("pipelineUid")String pipelineUid) {
		logger.info("RECEIVE REPORT FOR PIPELINE:{}",pipelineUid);
		
		JSONObject codeReport=new JSONObject();
		codeReport.put("devopsEventType", "codeReport");
		codeReport.put("pipelineUid", pipelineUid);
		codeReport.put("reports", new JSONArray());
		//codeReport.put("stages", new JSONArray());
		codeReport.getJSONArray("reports").addAll(reports);
		pipelineServiceImpl.getReportCache().put(pipelineUid, reports);
		
		return ExecuteResult.success();
	}

	@PostMapping("/activity-statistic")
	public ExecuteResult scmReport(@RequestBody List<UserActivityStatistic> uas) {
		if(uas!=null) {
			uas.forEach(ua->{
				saveIfNotPresent(ua);
			});
		}
		return ExecuteResult.success();
	}
	
	private void saveIfNotPresent(UserActivityStatistic activity) {
		Iterable<UserActivityStatistic> records = userActivityService
				.findAll(QUserActivityStatistic.userActivityStatistic.email.eq(activity.getEmail())
						.and(QUserActivityStatistic.userActivityStatistic.projectId.eq(activity.getProjectId()))
						.and(QUserActivityStatistic.userActivityStatistic.day.eq(activity.getDay())));
		if (records != null && records.iterator().hasNext()) {
			// already exist skip
			logger.info("user {} activity for day {}, project:{} already exist", activity.getEmail(), activity.getDay(),
					activity.getGroupName() + "/" + activity.getProjectName());
		} else {
			userActivityService.save(activity);
		}

	}

}
