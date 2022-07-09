package com.eazybuilder.ci.sql.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class StringValidateHelper {
	
	static ConcurrentHashMap<String, Pattern> cache=new ConcurrentHashMap<>();
	
	public boolean matches(String pattern,String target) {
		Pattern p=cache.get(pattern);
		if(p==null) {
			p=Pattern.compile(pattern);
			cache.put(pattern, p);
		}
		Matcher m = p.matcher(target);
        return m.matches();
	}
	
	public boolean containsFunction(String function,String target) {
		return matches(".*("+function.toLowerCase()+"\\s*\\(|"+function.toUpperCase()+"\\s*\\().*", target);
	}
	
	public int count(String keywords,String target) {
		return StringUtils.countMatches(target, keywords);
	}
	
	public boolean containsSQLKeywords(String keyword,String target) {
		if(target.startsWith(keyword.toLowerCase())||target.startsWith(keyword.toUpperCase())) {
			return true;
		}
		return matches(".*(\\s+"+keyword.toLowerCase()+"\\s+|\\s+"+keyword.toUpperCase()+"\\s+).*", target);
	}
	
	public int countSQLKeywords(String keyword,String target) {
		return countMatches("(\\s+"+keyword.toLowerCase()+"\\s+|\\s+"+keyword.toUpperCase()+"\\s+)", target);
	}
	
	public int countMatches(String pattern,String target) {
		Pattern p=cache.get(pattern);
		if(p==null) {
			p=Pattern.compile(pattern);
			cache.put(pattern, p);
		}
		Matcher m = p.matcher(target);
		int i=0;
		while(m.find()) {
			i++;
		}
		return i;
	}
}
