package test;

public class SQLValidateResult {
	private boolean success=true;
	private String code;
	private String msg;
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
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
}
