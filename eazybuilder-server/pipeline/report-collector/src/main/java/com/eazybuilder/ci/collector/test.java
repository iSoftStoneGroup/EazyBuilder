package com.eazybuilder.ci.collector;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import com.eazybuilder.ci.config.LoadConfigYML;
import java.util.Properties;
import com.eazybuilder.ci.collector.sonar.SonarServer;

public class test {
	private static Properties properties = new LoadConfigYML().getConfigProperties();

	public static void main(String[] args) throws Exception {
		String serverUrl = properties.getProperty("sonarqube.serverUrl");
		String user = properties.getProperty("sonarqube.user");
		String password = properties.getProperty("sonarqube.password");
		SonarServer server = new SonarServer(new URI(serverUrl), user, password);
		String url= properties.getProperty("sonarqube.url");
		server.checkTaskStatus(url);
	}

}
