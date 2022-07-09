package com.eazybuilder.ci.jenkins;

import com.eazybuilder.ci.service.SystemPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JenkinsPipelineEnv {
	
	@Autowired
	SystemPropertyService propService;
	
	String baseUrl="";

	@Value("${jenkins.url}")
	String jenkinsUrl;
	@Value("${jenkins.user}")
	String user;
	@Value("${jenkins.password}")
	String password;
	@Value("${tool.git}")
	String toolGitUrl;
	
	@Value("${sonar.url}")
	String sonarUrl;
	@Value("${sonar.user}")
	String sonarUser;
	@Value("${sonar.password}")
	String sonarPassword;
	
	@Value("${build.docker-host}")
	String dockerBuildHost;
	
	
	public boolean clearLegacyWorkspace() {
		return Boolean.parseBoolean(propService.getValue("pipeline.legacy.clear", "true"));
	}
	
	public boolean crumb() {
		return Boolean.parseBoolean(propService.getValue("jenkins.csrf.crumb", "false"));
	}

	public String getJenkinsUrl() {
		return propService.getValue("jenkins.url", jenkinsUrl);
	}

	public void setJenkinsUrl(String jenkinsUrl) {
		this.jenkinsUrl = jenkinsUrl;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToolGitUrl() {
		return propService.getValue("tool.git", toolGitUrl);
	}

	public void setToolGitUrl(String toolGitUrl) {
		this.toolGitUrl = toolGitUrl;
	}

	public String getSonarUrl() {
		return propService.getValue("sonar.url", sonarUrl);
	}

	public void setSonarUrl(String sonarUrl) {
		this.sonarUrl = sonarUrl;
	}

	public String getSonarUser() {
		return propService.getValue("sonar.user", sonarUser);
	}

	public void setSonarUser(String sonarUser) {
		this.sonarUser = sonarUser;
	}

	public String getSonarPassword() {
		return propService.getValue("sonar.password", sonarPassword);
	}
	
	public boolean isK8sSupport() {
		return Boolean.parseBoolean(propService.getValue("jenkins.k8s.support","false"));
	}

	public void setSonarPassword(String sonarPassword) {
		this.sonarPassword = sonarPassword;
	}

	public String getArm64DockerBuildHost() {
		return propService.getValue("build.docker-host.arm64", dockerBuildHost);
	}
	
	public String getDockerBuildHost() {
		return propService.getValue("build.docker-host", dockerBuildHost);
	}

	public void setDockerBuildHost(String dockerBuildHost) {
		this.dockerBuildHost = dockerBuildHost;
	}

	public String getBaseUrl() {
		return propService.getValue("base.url","");
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	
	public String getJenkinsLimitMeory() {
		return propService.getValue("jenkins.limit.memory", "8000");
	}
	public String getJenkinsNetworkHost() {
		return propService.getValue("jenkins.network.host", "localhost");
	}
	public String getJenkinsDataPath() {
		return propService.getValue("jenkins.data.path", "/data/jenkins");
	}
	public String getJenkinsRequestMeory() {
		return propService.getValue("jenkins.request.memory", "4000");
	}
	public String getJenkinsPathType() {
		return propService.getValue("jenkins.path.type", "");
	}
	public String getJenkinsMavenUrl() {
		return propService.getValue("jenkins.maven.url", "192.168.1.1");
	}
	public String getJenkinsBuildNode() {
		return propService.getValue("jenkins.build.node", "jenkins");
	}
	public String getJenkinsTeamGitlabUrl() {
		return propService.getValue("jenkins.team.gitlabUrl", "");
	}
	public String getJenkinsTeamGitlabHost() {
		return propService.getValue("jenkins.team.gitlabHost", "");
	}
	
	public Map<String,Object> initEnvParams(Map<String,Object> params) {
		params.put("jenkinsDataPath",this.getJenkinsDataPath());
		params.put("jenkinsLimitMeory",this.getJenkinsLimitMeory());
		params.put("jenkinsNetworkHost",this.getJenkinsNetworkHost());
		params.put("jenkinsRequestMeory",this.getJenkinsRequestMeory());
		params.put("jenkinsPathType",this.getJenkinsPathType());
		params.put("jenkinsMavenUrl",this.getJenkinsMavenUrl());
		params.put("jenkinsBuildNode",this.getJenkinsBuildNode());
		params.put("jenkinsTeamGitlabUrl",this.getJenkinsTeamGitlabUrl());
		params.put("jenkinsTeamGitlabHost",this.getJenkinsTeamGitlabHost());
		
		return params;
	}
	
	
}
