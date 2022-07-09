package com.eazybuilder.ci.maven;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.google.common.collect.Maps;
import com.eazybuilder.ci.maven.vo.ArtifactDescription;
import com.eazybuilder.ci.maven.vo.Packaging;

import cn.hutool.crypto.digest.DigestUtil;
import freemarker.template.Configuration;
@Component
public class LocalRepoInstallManager {

	private static final String UTF_8 = "utf-8";

	@Value("${maven.local_repo}")
	private String localRepo;
	
	@Autowired
	private Configuration configuration;
	
	public void installArtifact(String teamId,ArtifactDescription ad,byte[] artifactData) throws Exception{
		File dir=checkDir(teamId,ad);
		if(ad.getPackaging()==null){
			ad.setPackaging(Packaging.jar);
		}
		writeFileAndSHA1(new File(dir,ad.getArtifactId()+"-"+ad.getVersion()+(StringUtils.isNotBlank(ad.getClassifier())?"-"+ad.getClassifier():"")+"."+ad.getPackaging()),artifactData);
		if(Packaging.pom==ad.getPackaging()){
			return;
		}
		String pom=generatePOM(ad);
		writeFileAndSHA1(new File(dir,ad.getArtifactId()+"-"+ad.getVersion()+(StringUtils.isNotBlank(ad.getClassifier())?"-"+ad.getClassifier():"")+".pom"),pom.getBytes(UTF_8));
	}
	
	private void writeFileAndSHA1(File file,byte[] data) throws IOException{
		String sha1=generateSHA1(data);
		FileUtils.writeByteArrayToFile(file, data);
		FileUtils.write(new File(file.getAbsolutePath()+".sha1"), sha1, UTF_8);
	}
	
	private File checkDir(String teamId,ArtifactDescription ad){
		File dir=new File(localRepo+"/teams/"+teamId+"/"+ad.getGroupId().replaceAll("\\.", "/")
									+"/"+ad.getArtifactId()+"/"+ad.getVersion());
		if(!dir.exists()){
			dir.mkdirs();
		}
		return dir;
	}
	
	private String generateSHA1(byte[] data){
		return DigestUtil.sha1Hex(data);
	}
	
	private String generatePOM(ArtifactDescription ad) throws Exception{
		Map<String,Object> model=Maps.newHashMap();
		model.put("artifact", ad);
		return FreeMarkerTemplateUtils.processTemplateIntoString(
				configuration.getTemplate("local-artifact-pom.ftl"), model);
	}
}
