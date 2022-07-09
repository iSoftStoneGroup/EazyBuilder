package com.eazybuilder.ci.maven.vo;

public class ArtifactDescription {

	private String groupId;
	private String artifactId;
	private String version;
	private Packaging packaging;
	private String classifier;
	
	public ArtifactDescription() {
	}
	
	
	public ArtifactDescription(String groupId, String artifactId, String version, Packaging packaging,
			String classifier) {
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.version = version;
		this.packaging = packaging;
		this.classifier = classifier;
	}


	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getArtifactId() {
		return artifactId;
	}
	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Packaging getPackaging() {
		return packaging;
	}
	public void setPackaging(Packaging packaging) {
		this.packaging = packaging;
	}
	public String getClassifier() {
		return classifier;
	}
	public void setClassifier(String classifier) {
		this.classifier = classifier;
	}
}
