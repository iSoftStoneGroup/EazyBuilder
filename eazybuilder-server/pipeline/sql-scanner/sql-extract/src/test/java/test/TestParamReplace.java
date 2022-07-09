package test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.eazybuilder.sql.impl.ibatis.helper.MybatisExtractUtil;

public class TestParamReplace {

	@Test
	public void testReplace() throws IOException {
		String xmlContent=FileUtils.readFileToString(
				new File("src/test/resources/TestMybatis3Mapper.xml"), "utf-8");
		System.out.println(MybatisExtractUtil.replaceAllParam(xmlContent));
	}
	
}
