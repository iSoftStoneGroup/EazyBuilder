package com.eazybuilder.ci.dto;

public class Doc {
	private String id;//: "commons-codec:commons-codec:1.11",
    private String g;//: "commons-codec",
    private String a;//": "commons-codec",
    private String v;//": "1.11",
    private String p;//": "jar",
    private long timestamp;//": 1508252070000,
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getG() {
		return g;
	}
	public void setG(String g) {
		this.g = g;
	}
	public String getA() {
		return a;
	}
	public void setA(String a) {
		this.a = a;
	}
	public String getV() {
		return v;
	}
	public void setV(String v) {
		this.v = v;
	}
	public String getP() {
		return p;
	}
	public void setP(String p) {
		this.p = p;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public Artifact toArtifact(){
		return new Artifact(g, a, v);
	}
}
