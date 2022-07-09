package com.eazybuilder.ci.ant.util;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;
import com.eazybuilder.ci.dto.Artifact;

public class Lib2PomConverter {

	public static void convert(String libPath){
		File path=new File(libPath);
		List<Artifact> knowedCentralArtifacts=Lists.newArrayList();
		List<Artifact> knowedNexusArtifacts=Lists.newArrayList();
		List<File> unkownArtifacts=Lists.newArrayList();
		FileUtils.listFiles(path, new String[]{"jar"}, true).forEach(jar->{
			boolean found=false;
			try {
				Artifact centralArtifact=CentralMavenSearchService.search(jar);
				if(centralArtifact!=null){
					knowedCentralArtifacts.add(centralArtifact);
					found=true;
				}
			} catch (Exception e) {
			}
			if(!found){
				try {
					Artifact artifact=NexusSearchService.search(jar);
					if(artifact!=null){
						knowedNexusArtifacts.add(artifact);
						found=true;
					}
				} catch (Exception e) {
				}
			}
			if(!found){
				unkownArtifacts.add(jar);
			}
		});
		
		System.out.println("CENTRAL ARTIFACT:");
		knowedCentralArtifacts.forEach(System.out::println);
		System.out.println("NEXUS ARTIFACT:");
		knowedNexusArtifacts.forEach(System.out::println);
		System.out.println("UNKOWN JAR:");
		unkownArtifacts.forEach(System.out::println);
	}
}
