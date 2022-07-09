package com.eazybuilder.sql.impl.ibatis;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.eazybuilder.sql.SQLExtractor;
import com.eazybuilder.sql.impl.ibatis.helper.IbatisExtractUtil;
import com.eazybuilder.sql.impl.ibatis.helper.MybatisExtractUtil;

public class IBatisSQLExtractor implements SQLExtractor{
	@Override
	public Map<String,String> extractSQL(InputStream resource,String charset) throws Exception{
		DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
		dbf.setIgnoringComments(true);
		dbf.setCoalescing(true);
		dbf.setValidating(false);
		Map<String,String> sqls=new HashMap<>();
		try(InputStream is=resource;){
			dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			Document dom=dbf.newDocumentBuilder().parse(is);
			if(IbatisExtractUtil.getChildNode("sqlMap",dom)!=null) {
				parseIbatisSQLMapper(sqls, dom);
			}else if(IbatisExtractUtil.getChildNode("mapper", dom)!=null) {
				parseMybatisSQLMapper(sqls, dom);
			}
			return sqls;
		}
	}

	private void parseIbatisSQLMapper(Map<String, String> sqls, Document dom) {
		Map<String,Node> mappers=IbatisExtractUtil.toMapper(dom);
		Map<String,String> args=new HashMap<>();
		for(Entry<String,Node> entry:mappers.entrySet()) {
			try {
				String sqlId=entry.getKey();
				Node node=entry.getValue();
				if(!"sql".equals(node.getNodeName())) {
					String sql=IbatisExtractUtil.getChildStatement(sqlId, mappers, args);
					if(StringUtils.isNotBlank(sql)) {
						sqls.put(sqlId,sql.replaceAll("\n", "").replaceAll("\r", ""));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void parseMybatisSQLMapper(Map<String, String> sqls, Document dom) {
		Map<String,Node> mappers=MybatisExtractUtil.toMapper(dom);
		Map<String,String> args=new HashMap<>();
		for(Entry<String,Node> entry:mappers.entrySet()) {
			try {
				Node node=entry.getValue();
				String sqlId=entry.getKey();
				if(!"sql".equals(node.getNodeName())&&
						!"resultMap".equals(node.getNodeName())&&!"cache".equals(node.getNodeName())) {
					String sql=MybatisExtractUtil.getChildStatement(sqlId, mappers, args);
					if(StringUtils.isNotBlank(sql)) {
						sqls.put(sqlId,sql.replaceAll("\n", "").replaceAll("\r", ""));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean match(File sourceFile) {
		String fileNameAndPath=sourceFile.getAbsolutePath();
		if(!fileNameAndPath.endsWith(".xml")||fileNameAndPath.contains(File.separator+"target"+File.separator)
				||fileNameAndPath.contains(File.separator+"classes"+File.separator)||fileNameAndPath.contains(File.separator+"mysql"+File.separator)) {
			return false;
		}
		return true;
	}

}
