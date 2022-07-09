package com.eazybuilder.ci.maven;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;

import com.google.common.collect.Lists;
import com.eazybuilder.ci.maven.impl.AliyunMavenRepoDecorator;
import com.eazybuilder.ci.maven.impl.DockerPluginDecorator;
import com.eazybuilder.ci.maven.impl.JacocoPluginDecorator;
import com.eazybuilder.ci.maven.impl.SurefirePluginDecorator;
import com.eazybuilder.ci.maven.impl.SurefireReportPluginDecorator;
import com.eazybuilder.ci.maven.impl.WarDockerImageDecorator;

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
	}

	public synchronized static POMProcessor getInstance(){
		return INSTANCE;
	}
	
	public void processPOM(String workspace) throws Exception{
		List<Model> models=POMParser.listPom(new File(workspace));
		if(models!=null&&models.size()>0){
			models.forEach(model->{
				decorators.forEach(decorator->{
					decorator.decorate(model);
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
			surefire.decorate(parentModel);
			surefireReport.decorate(parentModel);
			aliyunRepo.decorate(parentModel);
			jacocoPluginDecorator.decorate(parentModel);
			//jacocoPluginDecorator.createReportModel(models);
		}
		
	}
	
}
