package test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.junit.Test;

import com.eazybuilder.sql.impl.ibatis.IBatisSQLExtractor;

public class TestIbatisSqlExtractor {
	@Test
	public void doTest() {
		try(InputStream is=new FileInputStream("src/test/resources/Example-debug.xml")){
			Map<String,String> sqls=new IBatisSQLExtractor().extractSQL(is, "utf-8");
			sqls.entrySet().forEach(System.out::println);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
