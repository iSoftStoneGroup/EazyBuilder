package com.eazybuilder.ci.controller;

import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.eazybuilder.ci.rabbitMq.SendRabbitMq;

@RestController
@RequestMapping("/api/demandBatchImport")
public class DemandBatchImportController {
	private static Logger logger = LoggerFactory.getLogger(DemandBatchImportController.class);
	@Autowired
	private SendRabbitMq sendRabbitMq;

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public List<DemandTemplateVo> storeResource(@RequestParam("uploadfile") MultipartFile request) throws Exception {
		return parseBatchProject(request.getInputStream());
	}

	@RequestMapping(method = RequestMethod.POST)
	public List<DemandImportResult> importResult(@RequestBody List<DemandTemplateVo> demands) {
		List<DemandImportResult> results = Lists.newArrayList();
		DemandImportResult projectImportResult = new DemandImportResult();
		projectImportResult.setProjectSimpleName("项目组需求批量导入");
		projectImportResult.setRemindMsg("需求批量导入申请已经提交，请稍候在需求管理平台查看");
		logger.info("需求批量上传:{}", demands);
		JSONObject jsonData = new JSONObject();
		jsonData.put("messageType", "demandBatchImport");
		jsonData.put("demands", demands);
		logger.info("准备发送消息给redmine:{}", jsonData.toString());
		sendRabbitMq.sendMsg(jsonData.toString());

		projectImportResult.setSuccess(true);
		return results;
	}

	public static class DemandImportResult {
		String projectSimpleName;
		boolean success;
		String remindMsg;

		public String getProjectSimpleName() {
			return projectSimpleName;
		}

		public void setProjectSimpleName(String projectSimpleName) {
			this.projectSimpleName = projectSimpleName;
		}

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public String getRemindMsg() {
			return remindMsg;
		}

		public void setRemindMsg(String remindMsg) {
			this.remindMsg = remindMsg;
		}
	}

	public static class DemandTemplateVo {

		// 需求（无需合并单元格）
		@ExcelProperty("需求（无需合并单元格）")
		String subject;
		// 功能
		@ExcelProperty("功能")
		String feature;
		// 指派人（邮箱/中文名/登录名）
		@ExcelProperty("指派人（邮箱/中文名/登录名）")
		String assign;
		// 说明
		@ExcelProperty("说明")
		String describe;
		// 项目名称
		@ExcelProperty("项目名称")
		String projectName;
		// 开发小组
		@ExcelProperty("开发小组")
		String team;
		// 开始时间
		@ExcelProperty("开始时间")
		String startDate;
		// 预计完成时间
		@ExcelProperty("预计完成时间")
		String endDate;
		// 预期工时（小时）
		@ExcelProperty("预期工时（小时）")
		String workingHours;
		// 预期工时（小时）
		@ExcelProperty("迭代版本")
		String sprint;

		public String getSprint() {
			return sprint;
		}

		public void setSprint(String sprint) {
			this.sprint = sprint;
		}

		public String getSubject() {
			return subject;
		}

		public String getFeature() {
			return feature;
		}

		public String getAssign() {
			return assign;
		}

		public String getDescribe() {
			return describe;
		}

		public String getProjectName() {
			return projectName;
		}

		public String getTeam() {
			return team;
		}



		public String getEndDate() {
			return endDate;
		}

		public String getWorkingHours() {
			return workingHours;
		}

		public void setSubject(String subject) {
			this.subject = subject;
		}

		public void setFeature(String feature) {
			this.feature = feature;
		}

		public void setAssign(String assign) {
			this.assign = assign;
		}

		public void setDescribe(String describe) {
			this.describe = describe;
		}

		public void setProjectName(String projectName) {
			this.projectName = projectName;
		}

		public void setTeam(String team) {
			this.team = team;
		}



		public String getStartDate() {
			return startDate;
		}

		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}

		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}

		public void setWorkingHours(String workingHours) {
			this.workingHours = workingHours;
		}

	}

	private List<DemandTemplateVo> parseBatchProject(InputStream excelInputStream) throws Exception {
		List<DemandTemplateVo> excelData = Lists.newArrayList();
		EasyExcel.read(excelInputStream, DemandTemplateVo.class, new AnalysisEventListener<DemandTemplateVo>() {
			@Override
			public void onException(Exception exception, AnalysisContext context) throws Exception {
				throw new Exception(
						"导入第" + context.readRowHolder().getRowIndex() + "行数据出错，原因：" + exception.getMessage());
			}

			@Override
			public void invoke(DemandTemplateVo data, AnalysisContext context) {

				excelData.add(data);
			}

			@Override
			public void doAfterAllAnalysed(AnalysisContext context) {

			}
		}).sheet().doRead();

		return excelData;
	}
}
