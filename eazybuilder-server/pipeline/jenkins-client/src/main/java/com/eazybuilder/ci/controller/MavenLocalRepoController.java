package com.eazybuilder.ci.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.eazybuilder.ci.maven.LocalRepoInstallManager;
import com.eazybuilder.ci.maven.vo.ArtifactDescription;
import com.eazybuilder.ci.maven.vo.Packaging;

@Controller
@RequestMapping("/repo/install")
public class MavenLocalRepoController {

	@Autowired
	LocalRepoInstallManager manager;
	
	@RequestMapping(value="/local",method=RequestMethod.POST)
	@ResponseStatus(code=HttpStatus.OK)
	public void uploadArtifact(
			@RequestParam("teamId")String teamId,
			@RequestParam("groupId")String groupId,
			@RequestParam("artifactId")String artifactId,
			@RequestParam("version")String version,
			@RequestParam(value="packaging",defaultValue="jar")Packaging packaging,
			@RequestParam(value="classifier",required=false)String classifier,
			@RequestParam("uploadfile")MultipartFile file) throws IOException, Exception{
		
		ArtifactDescription ad=new ArtifactDescription(groupId, artifactId, version, packaging, classifier);
		manager.installArtifact(teamId,ad, file.getBytes());
	}
}
