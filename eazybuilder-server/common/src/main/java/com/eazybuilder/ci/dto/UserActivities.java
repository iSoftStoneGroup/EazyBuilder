package com.eazybuilder.ci.dto;

public class UserActivities {
	private String userName;
	private Integer push;
	private Integer lineAdd;
	private Integer lineRemove;
	private Integer opened;
	private Integer merged;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Integer getPush() {
		return push;
	}
	public void setPush(Integer push) {
		this.push = push;
	}
	public Integer getLineAdd() {
		return lineAdd;
	}
	public void setLineAdd(Integer lineAdd) {
		this.lineAdd = lineAdd;
	}
	public Integer getLineRemove() {
		return lineRemove;
	}
	public void setLineRemove(Integer lineRemove) {
		this.lineRemove = lineRemove;
	}
	public Integer getOpened() {
		return opened;
	}
	public void setOpened(Integer opened) {
		this.opened = opened;
	}
	public Integer getMerged() {
		return merged;
	}
	public void setMerged(Integer merged) {
		this.merged = merged;
	}
	
}
