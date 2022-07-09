package com.eazybuilder.ci.service.async;

import java.util.Map;

import com.eazybuilder.ci.entity.Pipeline;
import com.eazybuilder.ci.entity.Project;
import com.eazybuilder.ci.entity.report.Report;
import org.apache.commons.lang3.StringUtils;

public class PipelineExecuteResult {

	private String success;
	private Pipeline pipeline;
	private Project project;
	private Map<String, Report> typedReport;
	
	public static PipelineExecuteResult newSuccess() {
		PipelineExecuteResult result=new PipelineExecuteResult();
		result.success();
		return result;
	}
	
	public static PipelineExecuteResult newFailed(){
		PipelineExecuteResult result=new PipelineExecuteResult();
		result.failed();
		return result;
	}
	public static PipelineExecuteResult newUnkown() {
		PipelineExecuteResult result=new PipelineExecuteResult();
		result.unknow();
		return result;
	}

	public  void success(){
		this.setSuccess(Boolean.toString(true));
	}

	public  void failed(){
		this.setSuccess(Boolean.toString(false));
	}

	public  void unknow(){
		this.setSuccess("unkown");
	}

	public boolean isSuccess(){
		return StringUtils.equals(this.getSuccess(),Boolean.toString(true));
	}

	
	public Pipeline getPipeline() {
		return pipeline;
	}
	public void setPipeline(Pipeline pipeline) {
		this.pipeline = pipeline;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public Map<String, Report> getTypedReport() {
		return typedReport;
	}
	public void setTypedReport(Map<String, Report> typedReport) {
		this.typedReport = typedReport;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

}
