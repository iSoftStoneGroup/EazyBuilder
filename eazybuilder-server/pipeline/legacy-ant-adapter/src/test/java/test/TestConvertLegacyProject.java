package test;

import java.io.File;

import org.apache.tools.ant.taskdefs.Ant;
import org.junit.Test;

import com.eazybuilder.ci.ant.AntProjectToMavenAdapter;

public class TestConvertLegacyProject {

	@Test
	public void doConvert(){
		File project=new File("E:/eazybuilder/eazybuilder-platform-service/trunk");
		AntProjectToMavenAdapter.getInstance().convertAntProject(project);
	}
}
