package com.eazybuilder.ci.sql.vo;

public class ScanStatus {
	public static ScanStatus UNKOWN=new ScanStatus();

	private JobStatus status;
	
	public static ScanStatus build(JobStatus status) {
		ScanStatus ss=new ScanStatus();
		ss.setStatus(status);
		return ss;
	}

	public JobStatus getStatus() {
		return status;
	}

	public void setStatus(JobStatus status) {
		this.status = status;
	}
	
}
