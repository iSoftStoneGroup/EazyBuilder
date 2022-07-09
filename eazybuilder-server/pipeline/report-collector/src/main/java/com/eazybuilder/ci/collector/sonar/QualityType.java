package com.eazybuilder.ci.collector.sonar;

public enum QualityType {
	BUG,
	VULNERABILITY,
	CODE_SMELL;
	
	public String toString(){
		switch (this) {
		case CODE_SMELL:
			return "编码规范/坏味道";
		case VULNERABILITY:
			return "安全漏洞";
		default:
			return name();
		}
	}
}
