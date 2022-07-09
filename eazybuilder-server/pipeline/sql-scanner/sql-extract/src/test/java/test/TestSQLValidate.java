package test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.eazybuilder.sql.util.HttpUtil;

public class TestSQLValidate {

	@Test
	public void doPost() throws Exception {
		String sql="select to_char1('','') from1 test";
		Map<String,String> sqls=new HashMap<>();
		sqls.put("test", sql);
		String result=HttpUtil.postJson("http://localhost:8081/sql/validate/mysql5", JSON.toJSONString(sqls));
		System.out.println(result);
	}
	
	@Test
	public void testMod() {
		System.out.println(10%10);
	}
}
