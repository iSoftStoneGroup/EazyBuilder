package com.eazybuilder.ci.maven.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import com.eazybuilder.ci.maven.POMDecorator;
import com.eazybuilder.ci.maven.POMParser;

public class JacocoPluginDecorator implements POMDecorator{
	/*<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.7.8</version>
    <executions>
        <execution>
            <id>pre-unit-test</id>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
            <configuration><propertyName>surefireArgLine</propertyName></configuration>
        </execution>
        <execution>
            <id>post-unit-test</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
            <configuration><outputDirectory>${project.reporting.outputDirectory}/jacoco-ut</outputDirectory></configuration>
        </execution>
    </executions>
</plugin>*/
	
	private static final Plugin JACOCO_PLUGIN=new Plugin();
	static{
		JACOCO_PLUGIN.setArtifactId("jacoco-maven-plugin");
		JACOCO_PLUGIN.setGroupId("org.jacoco");
		JACOCO_PLUGIN.setVersion("0.8.5");
		
		Xpp3Dom config = null;
		try (InputStream is=DockerPluginDecorator.class.getClassLoader().getResourceAsStream("jacoco-plugin-config.xml")){
			config = Xpp3DomBuilder.build(is,"utf-8");
		} catch (XmlPullParserException | IOException ex) {
			ex.printStackTrace();
		}
		JACOCO_PLUGIN.setConfiguration(config);
		
		PluginExecution execution=new PluginExecution();
		execution.addGoal("prepare-agent");
		execution.addGoal("prepare-agent-integration");
		execution.addGoal("report");
		execution.addGoal("report-integration");
		/*try {
			preJunit.setConfiguration(Xpp3DomBuilder.build(new StringReader("<configuration><propertyName>surefireArgLine</propertyName></configuration>")));
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		JACOCO_PLUGIN.addExecution(execution);
		
		/*PluginExecution postJunit=new PluginExecution();
		postJunit.setId("post-unit-test");
		postJunit.addGoal("report");
		postJunit.setPhase("test");
		try {
			preJunit.setConfiguration(Xpp3DomBuilder.build(new StringReader("<configuration><outputDirectory>${project.reporting.outputDirectory}/jacoco-ut</outputDirectory></configuration>")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		JACOCO_PLUGIN.addExecution(postJunit);*/
		
		
	}
	
	/**
	 * 仅针对parent
	 */
	@Override
	public void decorate(Model original) {
		//skip if already exsited
		for(Plugin plugin:original.getBuild().getPlugins()){
			if(plugin.getArtifactId().equals("jacoco-maven-plugin")){
				return;
			}
		}
		original.getBuild().addPlugin(JACOCO_PLUGIN);
	}
	
	public void createReportModel(List<Model> models) throws Exception{
		
		try(InputStream is=getClass().getClassLoader().getResourceAsStream("report-module-plugin.xml");
				OutputStream os=new FileOutputStream("report-module-plugin.xml")){
			IOUtils.copy(is, os);
		}
		File templateFile=new File("report-module-plugin.xml");
		Model model=POMParser.getEffectiveModel(templateFile);
		Model parentModel=models.get(0);
		if("pom".equals(parentModel.getPackaging())){
			//多级工程，添加jacoco module用于做aggregate
			String artifactId="jacoco-"+parentModel.getArtifactId();
			parentModel.addModule(artifactId);
			Parent parent=new Parent();
			parent.setArtifactId(parentModel.getArtifactId());
			parent.setGroupId(parentModel.getGroupId());
			parent.setVersion(parentModel.getVersion());
			model.setParent(parent);
			model.setGroupId(parentModel.getGroupId());
			model.setVersion(parentModel.getVersion());
			model.setArtifactId(artifactId);
			//skip docker build in this module
			model.addProperty("skip.docker.build", "true");
			//skip sonar scan in this module
			model.addProperty("sonar.skip", "true");
			for(int i=1;i<models.size();i++){
				Model subModel=models.get(i);
				if(!subModel.getPackaging().equals("pom")){
					Dependency depend=new Dependency();
					depend.setArtifactId(subModel.getArtifactId());
					depend.setGroupId(subModel.getGroupId());
					depend.setVersion(subModel.getVersion());
					model.addDependency(depend);
				}
			}
			//create jacoco-report dir and write pom.xml
			File dir=new File(parentModel.getPomFile().getAbsoluteFile().getParentFile(),artifactId);
			if(!dir.exists()){
				dir.mkdirs();
			}
			try(FileWriter writer=new FileWriter(new File(dir,"pom.xml"))){
				new MavenXpp3Writer().write(writer, model);
			}catch(Exception e){
				e.printStackTrace();
				throw new RuntimeException("write pom error");
			}
		}
		//save parent pom.xml
		try(FileWriter writer=new FileWriter(parentModel.getPomFile())){
			new MavenXpp3Writer().write(writer, parentModel);
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException("write pom error");
		}
		
		
	}

}
