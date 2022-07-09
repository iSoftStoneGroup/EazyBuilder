package support;

import java.io.File;

import org.junit.Test;

import com.eazybuilder.ci.util.HttpUtil;

public class TestUploadFile {

	@Test
	public void doTest() {
		try {
			System.out.println(
					HttpUtil.postFileStream("http://localhost:8080/ci/file-upload", new File("pom.xml"))					);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void doTestGet() {
		
	}
}
