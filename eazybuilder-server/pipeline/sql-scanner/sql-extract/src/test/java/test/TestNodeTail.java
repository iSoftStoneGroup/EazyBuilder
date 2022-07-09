package test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.eazybuilder.sql.impl.ibatis.helper.MybatisExtractUtil;

public class TestNodeTail {

	@Test
	public void doTest() throws Exception {
		String xml="<root><a>content of a</a> text after a<b>content of b</b> text after b</root>";
		
		DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
		dbf.setIgnoringComments(true);
		dbf.setCoalescing(true);
		dbf.setValidating(false);
		try(InputStream is=new ByteArrayInputStream(xml.getBytes());){
			dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			Document dom=dbf.newDocumentBuilder().parse(is);
			
			Node root=dom.getChildNodes().item(0);
			Node next=root.getChildNodes().item(0).getNextSibling();
			System.out.println(next);
			System.out.println(next.getTextContent());
			for(int i=0;i<root.getChildNodes().getLength();i++) {
				Node childOfRoot=root.getChildNodes().item(i);
				System.out.println(childOfRoot.getNodeName());
			}
			
		}
	}
	
	@Test
	public void doTestRegx() throws Exception {
		String xml="<root><select> content of a<test>test</test>\nbbb </select> text after a<b>content of b</b> text after b</root>";
		DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
		dbf.setIgnoringComments(true);
		dbf.setCoalescing(true);
		dbf.setValidating(false);
		try(InputStream is=new ByteArrayInputStream(xml.getBytes());){
			dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			Document dom=dbf.newDocumentBuilder().parse(is);
			Node select=dom.getChildNodes().item(0).getChildNodes().item(0);
			System.out.println(MybatisExtractUtil.convertChildren(MybatisExtractUtil.toMapper(dom), select, null));
		}
	
	}
	@Test
	public void doTestGetPropertiesOfInclude()throws Exception{
		String xml="<root>aaa<property  name=\"name\" value=\"value\" /> <property  name=\"name2\" value=\"value2\" /> </root>";
		DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
		dbf.setIgnoringComments(true);
		dbf.setCoalescing(true);
		dbf.setValidating(false);
		try(InputStream is=new ByteArrayInputStream(xml.getBytes());){
			dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			Document dom=dbf.newDocumentBuilder().parse(is);
			Node root=dom.getChildNodes().item(0);
			System.out.println(MybatisExtractUtil.getPropertiesOfInclude(root));
		}
	}
	
	@Test
	public void doTestInclude() throws Exception {
		String xml=FileUtils.readFileToString(new File("src/test/resources/TestMybatis3Mapper.xml"), "utf-8");
		
		DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
		dbf.setIgnoringComments(true);
		dbf.setCoalescing(true);
		dbf.setValidating(false);
		try(InputStream is=new ByteArrayInputStream(xml.getBytes());){
			dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			Document dom=dbf.newDocumentBuilder().parse(is);
			Map<String,Node> mapper=MybatisExtractUtil.toMapper(dom);
			System.out.println(MybatisExtractUtil.getChildStatement("testInclude", mapper, new HashMap<>()));
		}
	}
	@Test
	public void doTestIf() throws Exception{
		String xml=FileUtils.readFileToString(new File("src/test/resources/TestMybatis3Mapper.xml"), "utf-8");
		
		DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
		dbf.setIgnoringComments(true);
		dbf.setCoalescing(true);
		dbf.setValidating(false);
		try(InputStream is=new ByteArrayInputStream(xml.getBytes());){
			dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			Document dom=dbf.newDocumentBuilder().parse(is);
			Map<String,Node> mapper=MybatisExtractUtil.toMapper(dom);
			System.out.println(MybatisExtractUtil.getChildStatement("testIf", mapper, new HashMap<>()));
		}
	}
	
	@Test
	public void doTestChoose()throws Exception{
		String xml=FileUtils.readFileToString(new File("src/test/resources/TestMybatis3Mapper.xml"), "utf-8");
		DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
		dbf.setIgnoringComments(true);
		dbf.setCoalescing(true);
		dbf.setValidating(false);
		try(InputStream is=new ByteArrayInputStream(xml.getBytes());){
			dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			Document dom=dbf.newDocumentBuilder().parse(is);
			Map<String,Node> mapper=MybatisExtractUtil.toMapper(dom);
			System.out.println(MybatisExtractUtil.getChildStatement("testChoose", mapper, new HashMap<>()));
		}
	}
	@Test
	public void doTestPrefixOverride() {
		System.out.println(" and a=? and b=?".replaceFirst("^[\\s]*?(and|or)", ""));
		System.out.println(" AND a=? and b=?".replaceFirst("^[\\s]*?(AND|OR)", ""));
		System.out.println(" or a=? and b=?".replaceFirst("^[\\s]*?(and|or)", ""));
		System.out.println(" OR a=? and b=?".replaceFirst("^[\\s]*?(and|OR)", ""));
	}
	
	@Test
	public void doTestSuffixOverride() {
		System.out.println(replaceLast("aa,,, \n ", "(,|;)(\\s+)?$", ""));
		System.out.println(replaceLast("aa,,; \n ", "(,|;)(\\s+)?$", ""));
	}
	
	public static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
	}
	
	@Test
	public void doTestForeach() throws Exception {
		String xml=FileUtils.readFileToString(new File("src/test/resources/TestMybatis3Mapper.xml"), "utf-8");
		DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
		dbf.setIgnoringComments(true);
		dbf.setCoalescing(true);
		dbf.setValidating(false);
		try(InputStream is=new ByteArrayInputStream(xml.getBytes());){
			dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			Document dom=dbf.newDocumentBuilder().parse(is);
			Map<String,Node> mapper=MybatisExtractUtil.toMapper(dom);
			System.out.println(MybatisExtractUtil.getChildStatement("testForeach", mapper, new HashMap<>()));
		}
	}
	
	@Test
	public void doTestBind() throws Exception{
		String xml=FileUtils.readFileToString(new File("src/test/resources/TestMybatis3Mapper.xml"), "utf-8");
		DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
		dbf.setIgnoringComments(true);
		dbf.setCoalescing(true);
		dbf.setValidating(false);
		try(InputStream is=new ByteArrayInputStream(xml.getBytes());){
			dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			Document dom=dbf.newDocumentBuilder().parse(is);
			Map<String,Node> mapper=MybatisExtractUtil.toMapper(dom);
			System.out.println(MybatisExtractUtil.getChildStatement("testBind", mapper, new HashMap<>()));
		}
	}
	
}
