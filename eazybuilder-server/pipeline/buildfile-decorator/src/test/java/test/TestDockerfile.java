package test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.eazybuilder.ci.maven.impl.WarDockerImageDecorator;

public class TestDockerfile {

	@Test
	public void render(){
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		try (InputStream is=WarDockerImageDecorator.class.getClassLoader().getResourceAsStream("Dockerfile.tpl")){
			IOUtils.copy(is, baos);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		String DOCKER_FILE_TEMPLATE=baos.toString();
		String dockerfileString=String.format(DOCKER_FILE_TEMPLATE, "hello12312-asdas_asda.aa","hello12312-asdas_asda.aa");
		System.out.println(dockerfileString);
	}
}
