package com.eazybuilder.ci.collector.sonar;

public class Serverities {
	private QualityType type;
	private int minor;
	private int info;
	private int major;
	private int critical;
	private int blocker;
	
	public int getInfo() {
		return info;
	}
	public void setInfo(int info) {
		this.info = info;
	}
	public int getMinor() {
		return minor;
	}
	public void setMinor(int minor) {
		this.minor = minor;
	}
	public int getMajor() {
		return major;
	}
	public void setMajor(int major) {
		this.major = major;
	}
	public int getCritical() {
		return critical;
	}
	public void setCritical(int critical) {
		this.critical = critical;
	}
	public int getBlocker() {
		return blocker;
	}
	public void setBlocker(int blocker) {
		this.blocker = blocker;
	}
	public QualityType getType() {
		return type;
	}
	public void setType(QualityType type) {
		this.type = type;
	}
	
}
