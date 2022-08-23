package com.eazybuilder.ci.jenkins;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Credential {
    private String scope="GLOBAL";
    private String id;//": "svn",
    private String username;//": "zhangs",
    private String password;//": "xxxx",
    private String description;//": "test",
    @JsonProperty("$class")
    private String cls="com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl";
    
    
    public Credential() {
		// TODO Auto-generated constructor stub
	}
	public Credential(String id, String username, String password, String description) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.description = description;
	}



	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCls() {
		return cls;
	}
	public void setCls(String cls) {
		this.cls = cls;
	}
    
}
