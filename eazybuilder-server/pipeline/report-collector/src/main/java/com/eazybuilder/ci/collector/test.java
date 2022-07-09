package com.eazybuilder.ci.collector;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import com.eazybuilder.ci.collector.sonar.SonarServer;

public class test {

	public static void main(String[] args) throws Exception {
		String serverUrl = "http://sonarqubexxxxx";
		String user = "admin";
		String password = "admin123";
		SonarServer server = new SonarServer(new URI(serverUrl), user, password);
		String url="http://sonarqubexxxxx/api/ce/task?id=AXvxxRD_0S3DvZAIEzr4";
		server.checkTaskStatus(url);
		 

	}

}
