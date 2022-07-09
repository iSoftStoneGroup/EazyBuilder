package com.eazybuilder.ci.maven.impl;

import org.apache.maven.model.Model;
import org.apache.maven.model.Repository;

import com.eazybuilder.ci.maven.POMDecorator;

public class AliyunMavenRepoDecorator implements POMDecorator{
	private static final Repository ALIYUN_REPO=new Repository();
	static{
		ALIYUN_REPO.setId("nexus3-snapshots");
		ALIYUN_REPO.setUrl("https://maven.aliyun.com/repository/public/");
	}
	@Override
	public void decorate(Model original) {
		original.getPluginRepositories().add(ALIYUN_REPO);
	}

}
