package com.eazybuilder.ci.maven.impl;

import com.eazybuilder.ci.maven.LoadConfigYML;
import org.apache.maven.model.Model;
import org.apache.maven.model.Repository;

import com.eazybuilder.ci.maven.POMDecorator;


import java.util.Properties;

public class AliyunMavenRepoDecorator implements POMDecorator{
	private static final Repository ALIYUN_REPO=new Repository();

	private static Properties properties = new LoadConfigYML().getConfigProperties();


	static{
		ALIYUN_REPO.setId("nexus3-snapshots");
		ALIYUN_REPO.setUrl("http://" + properties.getProperty("nexus3.url") + "/repository/maven-public/");
	}
	@Override
	public void decorate(Model original,String nexusUrl) {
		for(Repository repo:original.getPluginRepositories()){
			if(repo.getUrl().contains(properties.getProperty("nexus3.url"))){
				return;
			}
		}
		original.getPluginRepositories().add(ALIYUN_REPO);
	}

}
