package test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.building.ModelBuilder;
import org.apache.maven.model.building.ModelBuildingException;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.junit.Test;

import com.eazybuilder.ci.maven.POMParser;
import com.eazybuilder.ci.maven.POMProcessor;

public class TestParsePOM {

	@Test
	public void testGetBaseName(){
		System.out.println(FilenameUtils.getName("test-ui.vue"));
	}	
	@Test
	public void testParse() throws ModelBuildingException{
		Model model=POMParser.getEffectiveModel(new File("D:/debug/maven/pom.xml"));
		System.out.println(model.getPackaging());
		model.getBuild().getPlugins().forEach(plugin->{
			System.out.println(plugin.getArtifactId());
			System.out.println(plugin.getVersion());
		});
		System.out.println(model.getArtifactId());
	}
	
	@Test
	public void testRewite()throws ModelBuildingException{
		Model model=POMParser.getEffectiveModel(new File("pom.xml"));
		Plugin plugin=new Plugin();
		plugin.setArtifactId("docker-maven-plugin");
		plugin.setGroupId("com.spotify");
		plugin.setVersion("1.0.0");
		model.getBuild().addPlugin(plugin);
		
		try {
			new MavenXpp3Writer().write(System.out, model);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
//	@Test
//	public void testProcess(){
//		try {
//			POMProcessor.getInstance().processPOM("../../../ci-demo");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
}
