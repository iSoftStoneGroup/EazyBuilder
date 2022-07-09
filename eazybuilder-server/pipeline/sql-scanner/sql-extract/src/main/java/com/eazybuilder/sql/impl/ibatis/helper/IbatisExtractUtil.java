package com.eazybuilder.sql.impl.ibatis.helper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class IbatisExtractUtil {

	static final String REGEX_PARAM_1="\\#.+?\\#";
	static final String REGEX_PARAM_2="\\$.+?\\$";
	
	static final String REGEX_PREFIX_OVERRIDE = "^[\\s]*?({0})";
	static final String REGEX_SUFFIX_OVERRIDE = "({0})(\\s+)?$";
	
	static final List<String> QUERY_TYPES= Arrays.asList("statement","select","insert","update","delete","procedure");
	static final List<String> COMPARE_TYPES=Arrays.asList(
			"isGreaterThan","isGreaterEqual","isLessThan","isLessEqual","isNotEmpty","isNotNull","isEqual","isNotEqual",
			"isPropertyAvailable","isNotPropertyAvailable","isNull","isEmpty","isParameterPresent","isNotParameterPresent"
			);
	
	public static String replaceAllParam(String xmlContent) {
		String xml=xmlContent.replaceAll(REGEX_PARAM_1, "1");
		return xml.replaceAll(REGEX_PARAM_2, "1");
	}
	
	
	/**
	 * 获取mapper文件中指定sqlId的statement
	 * @param sqlId
	 * @param mapper
	 * @param args
	 * @return
	 */
	public static String getChildStatement(String sqlId,Map<String,Node> mapper,Map<String,String> globalArgs) {
		String sql="";
		Node node=mapper.get(sqlId);
		if(globalArgs==null) {
			globalArgs=new HashMap<>();
		}
		Map<String,Object> sqlParseContext=new HashMap<>();
		sql += convertChildren(mapper, node, globalArgs,sqlParseContext);
	    //The child element has children
		for(int i=0;i<node.getChildNodes().getLength();i++) {
			Node nextChild=node.getChildNodes().item(i);
			sql += convertChildren(mapper, nextChild, globalArgs,sqlParseContext);
		}
	    return sql;
	}
	
	
	public static Node getChildNode(String name,Document dom) {
		for(int i=0;i<dom.getChildNodes().getLength();i++) {
			Node node=dom.getChildNodes().item(i);
			if(node.getNodeName().equals(name)&&node.getNodeType()==Node.ELEMENT_NODE) {
				return node;
			}
		}
		return null;
	}
	
	
	public static Map<String,Node> toMapper(Document dom){
		Node mapperNode=getChildNode("sqlMap",dom);
		NodeList nodes=mapperNode.getChildNodes();
		Map<String,Node> mapper=new HashMap<>();
		for(int i=0;i<nodes.getLength();i++) {
			Node node=nodes.item(i);
			if(node.getNodeType()==Node.ELEMENT_NODE) {
				String id=getAttribute(node, "id", null);
				if(id!=null) {
					mapper.put(id, node);
				}
			}
		}
		return mapper;
	}
	
	
	public static String convertChildren(Map<String,Node> mapperIdDom,Node child,Map<String,String> args, Map<String, Object> sqlParseContext) {
		String node=child.getNodeName();
		if(QUERY_TYPES.contains(node)) {
			return convertParameters(child, true, true);
		}else if(COMPARE_TYPES.contains(node)) {
			return convertCompares(mapperIdDom,child,args,sqlParseContext);
		}else if("dynamic".equals(node)) {
			return convertDynamic(mapperIdDom, child, args, sqlParseContext);
		}else if("iterate".equals(node)) {
			return convertIterate(mapperIdDom,child,args,sqlParseContext);
		}
		return " ";
	}
	
	private static String convertIterate(Map<String, Node> mapperIdDom, Node child, Map<String, String> args,
			Map<String, Object> sqlParseContext) {
		String prepend="",open="",close="",conjunction="";
		
		prepend = getAttribute(child, "prepend", "");
		open=getAttribute(child, "open", "");
		close=getAttribute(child, "close", "");
		conjunction=getAttribute(child, "conjunction", "");
		
		String convert_string="";
		convert_string += convertParameters(child, true, false);
		for(int i=0;i<child.getChildNodes().getLength();i++) {
			Node nextChild=child.getChildNodes().item(i);
			convert_string +=convertChildren(mapperIdDom, nextChild, args,sqlParseContext);
		}
		convert_string = prepend+" "+open+convert_string+conjunction+convert_string+close;
		convert_string +=convertParameters(child, false, true);
	    return convert_string;
	}


	private static String convertDynamic(Map<String, Node> mapperIdDom, Node child, Map<String, String> args, Map<String, Object> sqlParseContext) {
		String convert_string="";
		String prepend=getAttribute(child, "prepend", "");
		//if text
		convert_string += convertParameters(child, true,false);
		for(int i=0;i<child.getChildNodes().getLength();i++) {
			Node nextChild=child.getChildNodes().item(i);
			convert_string += convertChildren(mapperIdDom, nextChild, args,sqlParseContext);
		}
		//if tail
		convert_string += convertParameters(child, false, true);
		if(sqlParseContext.containsKey("HAS_CONDITION")) {
			return prepend+" "+convert_string;
		}
		return convert_string;
	}
	
	private static String convertCompares(Map<String, Node> mapperIdDom, Node child, Map<String, String> args, Map<String, Object> sqlParseContext) {
		String convert_string="";
		String prepend=getAttribute(child, "prepend", "");
		Integer count=0;
		//if text
		convert_string += convertParameters(child, true,false);
		if(StringUtils.isNoneBlank(convert_string)) {
			sqlParseContext.put("HAS_CONDITION","");
			if(sqlParseContext.containsKey("TOTAL_CONDITION")) {
				count=(Integer) sqlParseContext.get("TOTAL_CONDITION");
			}
			count++;
			sqlParseContext.put("TOTAL_CONDITION", count);
		}
		for(int i=0;i<child.getChildNodes().getLength();i++) {
			Node nextChild=child.getChildNodes().item(i);
			convert_string += convertChildren(mapperIdDom, nextChild, args,sqlParseContext);
		}
		//if tail
		convert_string += convertParameters(child, false, true);
		
//		if(count>1) {
			return prepend+" "+convert_string;
//		}else {
//			return convert_string;
//		}
	}


	private static String getAttribute(Node node,String name,String defaultValue) {
		Node attNode=node.getAttributes().getNamedItem(name);
		if(attNode!=null&&attNode.getNodeValue()!=null) {
			return attNode.getNodeValue();
		}
		return defaultValue;
	}
	

	//
	
	 public static String replaceLast(String text, String regex, String replacement) {
	        return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
	  }


	/**
	 * 获取include节点的nv pair(如果有)
	 * @param child
	 * @return 
	 */
	public static Map<String, String> getPropertiesOfInclude(Node child) {
		NodeList includeChild=child.getChildNodes();
		Map<String,String> props=new HashMap<>();
		for(int i=0;i<includeChild.getLength();i++) {
			Node node=includeChild.item(i);
			if("property".equals(node.getNodeName())) {
				String name=node.getAttributes().getNamedItem("name").getNodeValue();
				String value=node.getAttributes().getNamedItem("value").getNodeValue();
				props.put(name, value);
			}
		}
		return props;
	}


	/**
	 * 获取指定子节点中的内容
	 * @param child
	 * @param text 是否获取文本内容
	 * @param tail 是否获取tail(即nextSibling)文本节点内容
	 * @return
	 */
	public static String convertParameters(Node child,boolean text,boolean tail) {
		String child_text=getFirstTextContent(child);
		String child_tail="";
		child_text=StringUtils.isNotBlank(child_text)?child_text.trim():"";
		if(child.getNextSibling()!=null&&child.getNextSibling().getNodeType()==Node.TEXT_NODE) {
			String tailText=child.getNextSibling().getTextContent();
			if(StringUtils.isNotBlank(tailText)) {
				child_tail=tailText.trim();
			}
		}
		String convert_string="";
		if(text&&tail) {
			convert_string= child_text+child_tail;
		}else if(text) {
			convert_string= child_text;
		}else if(tail) {
			convert_string= child_tail;
		}
		//CDATA通过解析dom时DocumentBuilderFactory.setCoalescing(true)转换为文本节点了，所以这里不再处理
		return replaceAllParam(convert_string);
		
	}
	
	
	private static String getFirstTextContent(Node node) {
	    NodeList list = node.getChildNodes();
	    for (int i = 0; i < list.getLength(); ++i) {
	        Node child = list.item(i);
	        if (child.getNodeType() == Node.TEXT_NODE&&StringUtils.isNotBlank(child.getTextContent().trim()))
	            return child.getTextContent();
	    }
	    return "";
	}
}
