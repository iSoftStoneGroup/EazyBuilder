package com.eazybuilder.ci.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class NexusAnswer {

    private String resourceURI;
    private String groupId;
    private String artifactId;
    private String version;
    private String classifier;
    private String packaging;
    private String extension;
    private String repoId;
    private String contextId;
    private String pomLink;
    private String artifactLink;

    public final String getResourceURI() {
        return this.resourceURI;
    }

    public final void setResourceURI(final String resourceURI) {
        this.resourceURI = resourceURI;
    }

    public final String getGroupId() {
        return this.groupId;
    }

    public final void setGroupId(final String groupId) {
        this.groupId = groupId;
    }

    public final String getArtifactId() {
        return this.artifactId;
    }

    public final void setArtifactId(final String artifactId) {
        this.artifactId = artifactId;
    }

    public final String getVersion() {
        return this.version;
    }

    public final void setVersion(final String version) {
        this.version = version;
    }

    public final String getClassifier() {
        return this.classifier;
    }

    public final void setClassifier(final String classifier) {
        this.classifier = classifier;
    }

    public final String getPackaging() {
        return this.packaging;
    }

    public final void setPackaging(final String packaging) {
        this.packaging = packaging;
    }

    public final String getExtension() {
        return this.extension;
    }

    public final void setExtension(final String extension) {
        this.extension = extension;
    }

    public final String getRepoId() {
        return this.repoId;
    }

    public final void setRepoId(final String repoId) {
        this.repoId = repoId;
    }

    public final String getContextId() {
        return this.contextId;
    }

    public final void setContextId(final String contextId) {
        this.contextId = contextId;
    }

    public final String getPomLink() {
        return this.pomLink;
    }

    public final void setPomLink(final String pomLink) {
        this.pomLink = pomLink;
    }

    public final String getArtifactLink() {
        return this.artifactLink;
    }

    public final void setArtifactLink(final String artifactLink) {
        this.artifactLink = artifactLink;
    }

    @Override
    public final String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
    
    public Artifact toArtifact(){
    	return new Artifact(groupId,artifactId,version);
    }

}