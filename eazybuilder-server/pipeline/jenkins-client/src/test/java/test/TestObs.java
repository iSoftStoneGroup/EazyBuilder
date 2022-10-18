package test;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.eazybuilder.ci.Application;
import com.eazybuilder.ci.storage.impl.HWCloudOBSStorageService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestObs {
	
	@Autowired
	HWCloudOBSStorageService hWCloudOBSStorageService;
	@Test
	public void saveFile() throws Exception {
		 
		File file=new File("D:\\1");
		hWCloudOBSStorageService.save(file);
		System.err.println("保存成功");
		
		 
	}
}
