package com.eazybuilder.ci.maven;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.maven.model.Model;
import org.apache.maven.model.building.ModelBuildingException;

import com.google.common.collect.Lists;

import io.takari.maven.resolver.EffectiveModelResolver;

public class POMParser {
	static File localRepo=new File(System.getProperty("maven.local.repo", "/usr/share/maven-repo"));
	public static Model getEffectiveModel(File pom) throws ModelBuildingException{
	  EffectiveModelResolver resolver=new EffectiveModelResolver(localRepo);
	  return resolver.resolveModel(pom);
	}
	
	public static List<Model> listPom(File workspace) throws ModelBuildingException{
		List<Model> poms=Lists.newArrayList();
		File rootPom=workspace.isFile()?workspace:new File(workspace,"pom.xml");
		if(!rootPom.exists()){
			return Collections.emptyList();
		}
		Model model=getEffectiveModel(rootPom);
		poms.add(model);
		traverse(rootPom,model, poms);
		return poms;
	}
	
	private static void traverse(File root,Model model,Collection<Model> poms){
		model.getModules().forEach(module->{
			File moduleDir=new File(root.getAbsoluteFile().getParentFile(),module);
			File pom=new File(moduleDir,"pom.xml");
			if(pom.exists()){
				try {
					Model current=getEffectiveModel(pom);
					poms.add(current);
					traverse(pom,current, poms);
				} catch (Exception e) {
					e.printStackTrace();
				}
				//make sure every module has a target folder (for docker build plugin)
				File targetPath=new File(moduleDir,"target");
				if(!targetPath.exists()){
					try {
						targetPath.mkdir();
					} catch (Exception e2) {
					}
				}
			}
			
		});
	}
}

