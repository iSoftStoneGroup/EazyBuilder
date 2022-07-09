package test;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class TestDirectoryMove {

	@Test
	public void moveSrc() throws IOException{
		File home=new File("ant-project");
		File src=new File(home,"src");
		File webcontent=new File(home,"WebContent");
		moveDirectory(home, src,new File(home,"src/main/java"));
	}

	private void moveDirectory(File home, File src,File target) throws IOException {
		String tmp=UUID.randomUUID().toString();
		File tmpFolder=new File(home,tmp);
		FileUtils.moveDirectory(src, tmpFolder);
		FileUtils.moveDirectory(tmpFolder, target);
	}
}
