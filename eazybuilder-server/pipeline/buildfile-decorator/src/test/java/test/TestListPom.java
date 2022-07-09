package test;

import java.io.File;

import org.apache.maven.model.building.ModelBuildingException;
import org.junit.Test;

import com.eazybuilder.ci.maven.POMParser;

public class TestListPom {

	@Test
	public void list() throws ModelBuildingException{
		POMParser.listPom(new File("../")).forEach(model->{
			try {
				System.out.println(model.getPomFile().getCanonicalPath());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
