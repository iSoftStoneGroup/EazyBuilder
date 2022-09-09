package com.eazybuilder.ci.maven.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import com.eazybuilder.ci.maven.POMDecorator;
/**
 * 将符合条件的pom注入docker plugin
 * 
 * <plugin>
                <groupId>com.spotify</groupId>
                <artifactId>docker-maven-plugin</artifactId>
                <version>1.0.0</version>
                <configuration>
                    <imageName>${docker.registry}/${docker.build.groupId}/${project.artifactId}:${docker.build.version}</imageName>
                    <dockerDirectory>${project.basedir}/src/main/docker${docker.platform}</dockerDirectory>
                    <dockerHost>http://${docker.build.host}</dockerHost>
                    <registryUrl>http://${docker.registry}</registryUrl>
                    <skipDockerBuild>${skip.docker.build}</skipDockerBuild>
                    <skipDockerPush>${skip.docker.build}</skipDockerPush>
                    <skipDockerTag>${skip.docker.build}</skipDockerTag>
                    <forceTags>true</forceTags>
                    <resources>
                        <resource>
                            <directory>${project.build.directory}</directory>
                            <include>${project.build.finalName}.jar</include>
                        </resource>
                        <resource>
                            <targetPath>/</targetPath>
                            <directory>${project.basedir}</directory>
                            <include>config/</include>
                        </resource>
                    </resources>
                </configuration>
           </plugin>
        </plugins>
    </build>
 */
public class DockerPluginDecorator implements POMDecorator{

	private static final Plugin DOCKER_PLUGIN=new Plugin();
	static{
		DOCKER_PLUGIN.setArtifactId("docker-maven-plugin");
		DOCKER_PLUGIN.setGroupId("com.spotify");
		DOCKER_PLUGIN.setVersion("1.0.0");
		
		Xpp3Dom config = null;
		try (InputStream is=DockerPluginDecorator.class.getClassLoader().getResourceAsStream("docker-plugin-config.xml")){
			config = Xpp3DomBuilder.build(is,"utf-8");
		} catch (XmlPullParserException | IOException ex) {
			ex.printStackTrace();
		}
		DOCKER_PLUGIN.setConfiguration(config);
	}

	@Override
	public void decorate(Model original,String nexusUrl) {
		if(pluginNotExist(original)){
			if(original.getOrganization()!=null&&original.getOrganization().getName()!=null){
				original.addProperty("docker.build.groupId", original.getOrganization().getName());
			}else{
				original.addProperty("docker.build.groupId", original.getGroupId().replaceAll("\\.", "-"));
			}
			original.addProperty("docker.platform", "");
			original.getBuild().addPlugin(DOCKER_PLUGIN);
			
			if(dockerfileExist(original)){
				File dockerIgnore=new File(original.getPomFile().getAbsoluteFile().getParentFile(),"src/main/docker/.dockerignore");
				if(!dockerIgnore.exists()){
					try {
						//add dockerignore file
						dockerIgnore.createNewFile();
						FileUtils.writeStringToFile(dockerIgnore, ".svn");
					} catch (IOException e) {
					}
				}
				original.addProperty("skip.docker.build", "false");
			}else{
				original.addProperty("skip.docker.build", "true");
			}
		}
	}


	private boolean pluginNotExist(Model original) {
		for(Plugin plugin:original.getBuild().getPlugins()){
			if(plugin.getArtifactId().equals("docker-maven-plugin")){
				return false;
			}
		}
		return true;
	}

	private boolean dockerfileExist(Model original) {
		if(new File(original.getPomFile().getAbsoluteFile().getParentFile(),"src/main/docker/Dockerfile").exists()){
			return true;
		}
		return false;
	}

}
