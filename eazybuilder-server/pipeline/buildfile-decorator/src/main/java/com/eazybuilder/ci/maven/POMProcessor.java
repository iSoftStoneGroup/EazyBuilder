package com.eazybuilder.ci.maven;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import com.eazybuilder.ci.maven.impl.*;
import org.apache.maven.model.DistributionManagement;
import org.apache.maven.model.Model;
import org.apache.maven.model.ModelBase;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;

import com.google.common.collect.Lists;

public class POMProcessor {

	private static POMProcessor INSTANCE=new POMProcessor();
	private List<POMDecorator> decorators=Lists.newArrayList();
	
	private JacocoPluginDecorator jacocoPluginDecorator=new JacocoPluginDecorator();
	private SurefirePluginDecorator surefire=new SurefirePluginDecorator();
	private AliyunMavenRepoDecorator aliyunRepo=new AliyunMavenRepoDecorator();
	private SurefireReportPluginDecorator surefireReport=new SurefireReportPluginDecorator();
	private POMProcessor() {
		decorators.add(new WarDockerImageDecorator());
		decorators.add(new DockerPluginDecorator());
		decorators.add(new DistributionManagementDecorator());
	}

	public synchronized static POMProcessor getInstance(){
		return INSTANCE;
	}
	
	public void processPOM(String workspace,String nexusUrl) throws Exception{
		List<Model> models=POMParser.listPom(new File(workspace));
		if(models!=null&&models.size()>0){
			models.forEach(model->{
				decorators.forEach(decorator->{
					decorator.decorate(model,nexusUrl);
				});
				//write 
				try(FileWriter writer=new FileWriter(model.getPomFile())){
					new MavenXpp3Writer().write(writer, model);
				}catch(Exception e){
					e.printStackTrace();
					throw new RuntimeException("write pom error");
				}
			});
			//parent
			Model parentModel=models.get(0);
			surefire.decorate(parentModel,"");
			surefireReport.decorate(parentModel,"");
			aliyunRepo.decorate(parentModel,"");
			jacocoPluginDecorator.decorate(parentModel,"");
			//jacocoPluginDecorator.createReportModel(models);
		}
		
	}
	
}
