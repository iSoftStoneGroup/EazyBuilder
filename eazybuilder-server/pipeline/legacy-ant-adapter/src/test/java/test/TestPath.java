package test;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class TestPath {

	@Test
	public void print() throws IOException{
		File file=new File("d:/test/./");
		System.out.println(file.getName());
		System.out.println(file.getCanonicalFile().getName());
	}
}
