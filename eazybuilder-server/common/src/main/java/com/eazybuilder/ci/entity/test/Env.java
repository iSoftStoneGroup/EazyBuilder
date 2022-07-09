package com.eazybuilder.ci.entity.test;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.*;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="CI_TEST_ENV")
public class Env {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",columnDefinition="int(10)")
	private String id;
	
	@Column(name="env_key")
	private String key;
	
	@Column(name="env_val")
	private String val;
	
	public Env() {
	}
	
	public Env(String key, String val) {
		this.key = key;
		this.val = val;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
}
