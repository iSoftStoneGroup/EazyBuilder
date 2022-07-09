package com.eazybuilder.ci.entity.test;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.*;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.eazybuilder.ci.entity.Scm;
@Entity
@Table(name="CI_ATM_SCRIPT",uniqueConstraints=@UniqueConstraint(columnNames="NAME"))
public class AutomaticScript {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",columnDefinition="int(10)")
	private String id;
	
	private String name;
	private String description;
	private ScriptType scriptType;
	
	@OneToOne(cascade=CascadeType.ALL)
	private Scm scm;
	
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public ScriptType getScriptType() {
		return scriptType;
	}
	public void setScriptType(ScriptType scriptType) {
		this.scriptType = scriptType;
	}
	public Scm getScm() {
		return scm;
	}
	public void setScm(Scm scm) {
		this.scm = scm;
	}
	
}
