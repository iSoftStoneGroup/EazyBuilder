package com.eazybuilder.ci.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.*;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="CI_CONFIG",uniqueConstraints=@UniqueConstraint(columnNames="cfgKey"))
public class SystemProperty {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",columnDefinition="int(8)")
	private String id;
	private String cfgKey;
	private String cfgVal;
	private String description;
	
	public String getCfgKey() {
		return cfgKey;
	}
	public void setCfgKey(String cfgKey) {
		this.cfgKey = cfgKey;
	}
	public String getCfgVal() {
		return cfgVal;
	}
	public void setCfgVal(String cfgVal) {
		this.cfgVal = cfgVal;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	

}
