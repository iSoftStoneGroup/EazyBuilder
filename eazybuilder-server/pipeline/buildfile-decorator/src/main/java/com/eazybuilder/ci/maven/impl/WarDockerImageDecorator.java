package com.eazybuilder.ci.maven.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.model.Model;

import com.eazybuilder.ci.maven.POMDecorator;

public class WarDockerImageDecorator implements POMDecorator{

	static String DOCKER_FILE_TEMPLATE;
	static{
		File templateFile=new File("/opt/ci-tool/Dockerfile.tpl");
		if(templateFile.exists()){
			try {
				DOCKER_FILE_TEMPLATE=FileUtils.readFileToString(templateFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			try (InputStream is=WarDockerImageDecorator.class.getClassLoader().getResourceAsStream("Dockerfile.tpl")){
				IOUtils.copy(is, baos);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			DOCKER_FILE_TEMPLATE=baos.toString();
		}
		
	}
	
	@Override
	public void decorate(Model original,String nexusUrl) {
		
		//check dockerfile existence
		File dockerfile=new File(original.getPomFile().getParentFile(),
				"src/main/docker/Dockerfile");
		if(!dockerfile.exists()&&"war".equalsIgnoreCase(original.getPackaging())){
			dockerfile.getParentFile().mkdirs();
			try {
				String finalName=original.getArtifactId();
				if(original.getBuild()!=null&&original.getBuild().getFinalName()!=null){
					finalName=original.getBuild().getFinalName();
				}
				String dockerfileString=String.format(DOCKER_FILE_TEMPLATE, finalName,finalName);
				FileUtils.write(dockerfile, dockerfileString);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
