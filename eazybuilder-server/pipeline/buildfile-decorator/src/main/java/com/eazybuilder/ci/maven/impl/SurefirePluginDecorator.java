package com.eazybuilder.ci.maven.impl;

import java.io.IOException;
import java.io.InputStream;

import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import com.eazybuilder.ci.maven.POMDecorator;

public class SurefirePluginDecorator implements POMDecorator{
	private static final Plugin SUREFIRE_PLUGIN=new Plugin();
	static{
		SUREFIRE_PLUGIN.setArtifactId("maven-surefire-plugin");
		SUREFIRE_PLUGIN.setGroupId("org.apache.maven.plugins");
		SUREFIRE_PLUGIN.setVersion("3.0.0-M4");
		Xpp3Dom config = null;
		try (InputStream is=DockerPluginDecorator.class.getClassLoader().getResourceAsStream("surefire-plugin-config.xml")){
			config = Xpp3DomBuilder.build(is,"utf-8");
		} catch (XmlPullParserException | IOException ex) {
			ex.printStackTrace();
		}
		SUREFIRE_PLUGIN.setConfiguration(config);
	}
	@Override
	public void decorate(Model original,String nexusUrl) {
		//skip if already exsited
		for(Plugin plugin:original.getBuild().getPlugins()){
			if(plugin.getArtifactId().equals("maven-surefire-plugin")){
				return;
			}
		}
		original.getBuild().addPlugin(SUREFIRE_PLUGIN);
	}


}
