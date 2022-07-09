package com.eazybuilder.ci.sql.vo;

public class SQLValidateResult {
	private boolean success=true;
	private String code;
	private String msg;
	private String validatorName;
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg=msg;
	}
	
	public void addMsg(String msg) {
		if(msg!=null&&this.msg!=null) {
			this.msg=this.msg+"|"+msg;
		}else {
			this.msg = msg;
		}
	}
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		if(this.code!=null&&code!=null) {
			this.code=this.code+"|"+code;
		}else {
			this.code = code;
		}
	}
	@Override
	public String toString() {
		return "SQLValidateResult [success=" + success + ", code=" + code + ", msg=" + msg + "]";
	}
	public String getValidatorName() {
		return validatorName;
	}
	public void setValidatorName(String validatorName) {
		this.validatorName = validatorName;
	}
}
