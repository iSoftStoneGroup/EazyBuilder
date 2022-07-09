package test;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.eazybuilder.ci.config.ConfigUtils;
import com.eazybuilder.ci.entity.Project;

public class TestReadConfigfile {

	@Test
	public void read() throws IOException{
		Project project=ConfigUtils.readConfig(new File("config.yaml"));
		System.out.println(project.getName());
	}
	
	@Test
	public void testPrintProperty(){
		System.out.println(System.getProperty("user.dir"));
	}
}
