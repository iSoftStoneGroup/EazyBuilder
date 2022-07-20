package com.eazybuilder.dm.domain.function;


import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Result implements Serializable {
	private static final long serialVersionUID = 1L;
	private boolean success;
	private Map<String, Object> result = new HashMap();
	public static final String DEFAULT_MODEL_KEY = "value";
	private String modelKey = "value";
	private String resultCode;
	private String[] resultCodeParams;
	private String errorMessage;
	private String successMessage;

	public Result(boolean success) {
		this.success = success;
	}

	public Result() {
	}

	public Object addDefaultModel(Object obj) {
		return this.result.put("value", obj);
	}

	public Object addDefaultModel(String key, Object obj) {
		this.modelKey = key;
		return this.result.put(key, obj);
	}

	public Set<String> keySet() {
		return this.result.keySet();
	}

	public Map<String, Object> getMap() {
		return this.result;
	}

	public Object get() {
		return this.result.get(this.modelKey);
	}

	public Object get(String key) {
		return this.result.get(key);
	}

	public Collection values() {
		return this.result.values();
	}

	public boolean isSuccess() {
		return this.success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getResultCode() {
		return this.resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public void setResultCode(String resultCode, String... args) {
		this.resultCode = resultCode;
		this.resultCodeParams = args;
	}

	public String[] getResultCodeParams() {
		return this.resultCodeParams;
	}

	public void setResultCodeParams(String[] resultCodeParams) {
		this.resultCodeParams = resultCodeParams;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getSuccessMessage() {
		return this.successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}
}
