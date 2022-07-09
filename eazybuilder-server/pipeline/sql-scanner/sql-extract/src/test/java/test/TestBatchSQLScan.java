package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eazybuilder.sql.impl.ibatis.IBatisSQLExtractor;
import com.eazybuilder.sql.util.HttpUtil;

public class TestBatchSQLScan {

	@Test
	public void doScan() {
		Collection<File> xmls=FileUtils.listFiles(new File("E:\\iCIP\\source\\trunk\\icip-parent"), new String[]{"xml"}, true);
		xmls.forEach(xml->{
			try {
				String content=FileUtils.readFileToString(xml, "utf-8");
				if(content.contains("</sqlMap>")) {
					System.out.println(xml.getAbsolutePath());
					try(InputStream is=new FileInputStream(xml)){
						Map<String,String> sqls=new IBatisSQLExtractor().extractSQL(is, "utf-8");
						String result=HttpUtil.postJson("http://localhost:8081/sql/validate/mysql5", JSON.toJSONString(sqls));
						
						Map<String,JSONObject> map=JSON.parseObject(result, HashMap.class);
						map.entrySet().forEach(entry->{
							if(!entry.getValue().getBoolean("success")) {
								System.out.println("SQL-ID:"+entry.getKey());
								System.out.println("-----imcompatible SQL-------");
								System.out.println(sqls.get(entry.getKey()));
								System.out.println(entry.getValue().getString("code"));
								System.out.println(entry.getValue().getString("msg"));
								System.out.println("----------------------------");
							}
						});
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	@Test
	public void scanICIP() {
		Collection<File> xmls=FileUtils.listFiles(new File("src/test/resources/oracle/route"), new String[]{"xml"}, true);
		xmls.forEach(xml->{
			try {
					try(InputStream is=new FileInputStream(xml)){
						Map<String,String> sqls=new IBatisSQLExtractor().extractSQL(is, "utf-8");
						sqls.entrySet().forEach(sql->{
							System.out.println("-------------------------");
							System.out.println(sql.getKey());
							System.out.println(sql.getValue());
						});
					}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
