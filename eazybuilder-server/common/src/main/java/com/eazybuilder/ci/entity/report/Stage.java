package com.eazybuilder.ci.entity.report;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="CI_PIPELINE_STAGE")
public class Stage {
	 @Id
	 private String id;//: "2014-10-16_13-07-52",
	 private String name;//": "#16",
	 private String status;//": "PAUSED_PENDING_INPUT",
	 private long startTimeMillis;//": 1413461275770,
	 private long durationMillis;//": 10229,
	 
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public long getStartTimeMillis() {
		return startTimeMillis;
	}
	public void setStartTimeMillis(long startTimeMillis) {
		this.startTimeMillis = startTimeMillis;
	}
	public long getDurationMillis() {
		return durationMillis;
	}
	public void setDurationMillis(long durationMillis) {
		this.durationMillis = durationMillis;
	}
	@Override
	public String toString() {
		return ""+name+":"+status + "("+durationMillis/1000+"s)";
	}

	public boolean isSuccess(){
		return this.status!=null && "SUCCESS".equals(status);
	}

}
