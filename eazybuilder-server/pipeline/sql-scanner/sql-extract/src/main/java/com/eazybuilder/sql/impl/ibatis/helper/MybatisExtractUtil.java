package com.eazybuilder.sql.impl.ibatis.helper;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MybatisExtractUtil {

	static final String REGEX_PARAM_1="\\#\\{.+?\\}";
	static final String REGEX_PARAM_2="\\$\\{.+?\\}";
	
	static final String REGEX_PREFIX_OVERRIDE = "^[\\s]*?({0})";
	static final String REGEX_SUFFIX_OVERRIDE = "({0})(\\s+)?$";
	
	static final List<String> QUERY_TYPES= Arrays.asList("sql","select","insert","update","delete");
	static final List<String> CHOISE_TYPES=Arrays.asList("choose","when","otherwise");
	static final List<String> KW_TYPES=Arrays.asList("trim","where","set");
	
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
	public static String getChildStatement(String sqlId,Map<String,Node> mapper,Map<String,String> args) {
		String sql="";
		Node node=mapper.get(sqlId);
		if(args==null) {
			args=new HashMap<>();
		}
		sql += convertChildren(mapper, node, args);
	    //The child element has children
		for(int i=0;i<node.getChildNodes().getLength();i++) {
			Node nextChild=node.getChildNodes().item(i);
			sql += convertChildren(mapper, nextChild, args);
		}
	    return sql;
	}
	
	
	private static Node getChildNode(String name,Document dom) {
		for(int i=0;i<dom.getChildNodes().getLength();i++) {
			Node node=dom.getChildNodes().item(i);
			if(node.getNodeName().equals(name)&&node.getNodeType()==Node.ELEMENT_NODE) {
				return node;
			}
		}
		return null;
	}
	
	
	public static Map<String,Node> toMapper(Document dom){
		Node mapperNode=getChildNode("mapper",dom);
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
	
	
	public static String convertChildren(Map<String,Node> mapperIdDom,Node child,Map<String,String> args) {
		String node=child.getNodeName();
		if(QUERY_TYPES.contains(node)) {
			return convertParameters(child, true, true);
		}else if("include".equals(node)) {
	        return convertInclude(mapperIdDom, child, args);
		}else if("if".equals(node)) {
			return convertIf(mapperIdDom,child,args);
		}else if(CHOISE_TYPES.contains(node)) {
			return convertChooseWhenOtherwise(mapperIdDom,child,args);
		}else if(KW_TYPES.contains(node)) {
			//where set  trim
			return convertTrimSetWhere(mapperIdDom,child,args);
		}else if("foreach".equals(node)) {
			return convertForeach(mapperIdDom,child,args);
		}
		return " ";
	}
	
	private static String getAttribute(Node node,String name,String defaultValue) {
		Node attNode=node.getAttributes().getNamedItem(name);
		if(attNode!=null&&attNode.getNodeValue()!=null) {
			return attNode.getNodeValue();
		}
		return defaultValue;
	}
	
	private static String convertForeach(Map<String, Node> mapperIdDom, Node child, Map<String, String> args) {
		String collection=null,item=null,index=null,open="",close="",separator="";
		collection = getAttribute(child, "collection", null);
		item=getAttribute(child, "item", null);
		index=getAttribute(child, "index", null);
		open=getAttribute(child, "open", "");
		close=getAttribute(child, "close", "");
		separator=getAttribute(child, "separator", "");
		
		String convert_string="";
		convert_string += convertParameters(child, true, false);
		for(int i=0;i<child.getChildNodes().getLength();i++) {
			Node nextChild=child.getChildNodes().item(i);
			convert_string +=convertChildren(mapperIdDom, nextChild, args);
		}
		convert_string = open+convert_string+separator+convert_string+close;
		convert_string +=convertParameters(child, false, true);
	    return convert_string;
	}

	//
	private static String convertTrimSetWhere(Map<String, Node> mapperIdDom, Node child, Map<String, String> args) {
		String prefix=null,suffix=null,prefix_overrides=null,suffix_overrides=null;
		switch(child.getNodeName()) {
		case "trim":
			prefix=getAttribute(child, "prefix", null);
			suffix=getAttribute(child, "suffix", null);
			prefix_overrides=getAttribute(child, "prefixOverrides", null);
			suffix_overrides=getAttribute(child, "suffixOverrides", null);
			break;
		case "set":
			prefix = "SET";
		    suffix_overrides = ",";
			break;
		case "where":
			prefix = "WHERE";
	        prefix_overrides = "and|AND|or|OR";
			break;
		default:
			return "";
		}
		String convert_string="";
		// Add trim/where/set text
		convert_string += convertParameters(child, true,false);
		// # Convert children first
		for(int i=0;i<child.getChildNodes().getLength();i++) {
			Node nextChild=child.getChildNodes().item(i);
			convert_string += convertChildren(mapperIdDom, nextChild, args);
		}
		//# Remove prefixOverrides
		if(prefix_overrides!=null) {
			convert_string=convert_string.replaceFirst(MessageFormat.format(REGEX_PREFIX_OVERRIDE,prefix_overrides),"");
		}
		//# Remove suffixOverrides
		if(suffix_overrides!=null) {
			convert_string=convert_string.replaceFirst(MessageFormat.format(REGEX_SUFFIX_OVERRIDE,suffix_overrides),"");
		}
		if(StringUtils.isNoneBlank(convert_string)) {
			if(prefix!=null) {
				convert_string=prefix+" "+convert_string;
			}
			if(suffix!=null) {
				convert_string=convert_string+" "+suffix;
			}
		}
		//tail
		convert_string += convertParameters(child, false,true);
	    return convert_string;
	}
	
	 public static String replaceLast(String text, String regex, String replacement) {
	        return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
	  }

	private static String convertChooseWhenOtherwise(Map<String, Node> mapperIdDom, Node child,
			Map<String, String> args) {
		String convert_string="";
		for(int i=0;i<child.getChildNodes().getLength();i++) {
			Node nextChild=child.getChildNodes().item(i);
			if("when".equals(nextChild.getNodeName())) {
				convert_string += convertParameters(nextChild, true, true);
			}else if("otherwise".equals(nextChild.getNodeName())) {
				 convert_string += convertParameters(nextChild, true, true);
			}
			convert_string += convertChildren(mapperIdDom, nextChild, args);
		}
		return convert_string;
	}

	private static String convertIf(Map<String, Node> mapperIdDom, Node child, Map<String, String> args) {
		String convert_string="";
		//if text
		convert_string += convertParameters(child, true,false);
		for(int i=0;i<child.getChildNodes().getLength();i++) {
			Node nextChild=child.getChildNodes().item(i);
			convert_string += convertChildren(mapperIdDom, nextChild, args);
		}
		//if tail
		convert_string += convertParameters(child, false, true);
	    return convert_string;
	}

	private static String convertInclude(Map<String,Node> mapper, Node child, Map<String, String> args) {
		/*
		 for next_child in child:
		        if next_child.tag == 'property':
		            properties[next_child.attrib.get('name')] = next_child.attrib.get('value')
		 */
		//获取include内部的property map
		args.putAll(getPropertiesOfInclude(child));
		String include_child_id = child.getAttributes().getNamedItem("refid").getNodeValue();
		//如果refid有变量形式，做替换
		if(include_child_id.matches(REGEX_PARAM_1)) {
			include_child_id=include_child_id.replaceAll("#{", "").replaceAll("}", "");
			include_child_id=args.get(include_child_id);
		}else if(include_child_id.matches(REGEX_PARAM_2)) {
			include_child_id=include_child_id.replaceAll("\\$\\{", "").replaceAll("\\}", "");
			include_child_id=args.get(include_child_id);
		}
		//根据refid找到对应的sql mapper 节点
		Node include_child = mapper.get(include_child_id);
		String convert_string="";
		//递归
		convert_string += convertChildren(mapper, include_child,args);
		//添加include的text节点
		convert_string += convertParameters(child, true,false);
		
		//处理ref的SQL id里还有的其他子节点
		for(int i=0;i<include_child.getChildNodes().getLength();i++) {
			Node refNextChild=include_child.getChildNodes().item(i);
			convert_string += convertChildren(mapper, refNextChild, args);
		}
		//添加include的tail
		convert_string += convertParameters(child, false,true);
		return convert_string;
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
	
	
	private static String getFirstLevelTextContent(Node node) {
	    NodeList list = node.getChildNodes();
	    StringBuilder textContent = new StringBuilder();
	    for (int i = 0; i < list.getLength(); ++i) {
	        Node child = list.item(i);
	        if (child.getNodeType() == Node.TEXT_NODE)
	            textContent.append(child.getTextContent());
	    }
	    return textContent.toString();
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
