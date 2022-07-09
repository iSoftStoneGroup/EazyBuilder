package com.eazybuilder.sql;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

public interface SQLExtractor {
	/**
	 * 从给定的输入流中提取SQL
	 * @param resource
	 * @return <SQL-ID,SQL>
	 * @throws Exception 
	 */
	public Map<String,String> extractSQL(InputStream resource,String charset) throws Exception;
	
	/**
	 * 检查当前提取器是否适用于该文件
	 * @param sourceFile
	 * @return
	 */
	public boolean match(File sourceFile);
}
