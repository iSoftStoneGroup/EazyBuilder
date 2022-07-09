package com.eazybuilder.ci.dto;

import java.util.List;

import com.eazybuilder.ci.entity.test.AutomaticScript;
import com.eazybuilder.ci.entity.test.EnvSet;

/**
 * 自动化测试的DTO，Jenkins通过调用QTP/Selenium Agent进行测试时传输
 *
 */
public class AutomaticTestContext {
	//测试环境变量
	private EnvSet env;
	private List<AutomaticScript> scripts;
	private String uuid;
	private String planId;
	
	public EnvSet getEnv() {
		return env;
	}
	public void setEnv(EnvSet env) {
		this.env = env;
	}
	public List<AutomaticScript> getScripts() {
		return scripts;
	}
	public void setScripts(List<AutomaticScript> scripts) {
		this.scripts = scripts;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
}
