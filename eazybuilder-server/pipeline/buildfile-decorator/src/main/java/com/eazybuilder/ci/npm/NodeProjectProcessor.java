package com.eazybuilder.ci.npm;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.slf4j.helpers.MessageFormatter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.eazybuilder.ci.maven.impl.DockerPluginDecorator;

public class NodeProjectProcessor {
	static String POM_TEMPLATE=null;
	static {
		try (InputStream is=DockerPluginDecorator.class.getClassLoader().getResourceAsStream("node-pom.xml")){
			POM_TEMPLATE=IOUtils.toString(is, Charsets.UTF_8);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void process(String workspace) {
		File packageJsonFile=workspace.endsWith("package.json")?new File(workspace):new File(workspace,"package.json");
		if(!packageJsonFile.exists()) {
			throw new RuntimeException("project entry package.json not exist:"+packageJsonFile.getAbsolutePath());
		}
		try {
			JSONObject project=JSON.parseObject(FileUtils.readFileToString(packageJsonFile,"utf-8"));
			String projectName=project.getString("name");
			String version=project.getString("version");
			FileUtils.write(new File("pom.xml"), 
					MessageFormatter.format(POM_TEMPLATE, FilenameUtils.getName(projectName), 
							version==null?"1.0":version ).getMessage(),Charsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("read package.json error:"+e.getMessage());
		}
		
	}
}
