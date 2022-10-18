package com.eazybuilder.ci.service.async;

import com.eazybuilder.ci.entity.Pipeline;
import com.eazybuilder.ci.entity.Project;
import com.eazybuilder.ci.entity.report.Report;

import java.util.Map;

public class PipelineExecuteResult {
	private String jobId;
	private String success;
	private Pipeline pipeline;
	private Project project;
	private Map<String, Report> typedReport;
	
	public static PipelineExecuteResult newSuccess() {
		PipelineExecuteResult result=new PipelineExecuteResult();
		result.setSuccess("SUCCESS");
		return result;
	}
	
	public static PipelineExecuteResult newFailed(){
		PipelineExecuteResult result=new PipelineExecuteResult();
		result.setSuccess("FAILED");
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

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
}
