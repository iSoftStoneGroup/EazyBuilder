package com.eazybuilder.ci.sql.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.Charsets;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.mvel.MVELRuleFactory;
import org.jeasy.rules.support.YamlRuleDefinitionReader;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import com.eazybuilder.ci.sql.vo.SQLValidateResult;
/**
 * 根据已知的固定模式，追加检测项
 *
 */
@Component
public class KnownCompatiblePatternValidator {

	static Rules compatibleCheckRules =null;
	static{
			MVELRuleFactory ruleFactory = new MVELRuleFactory(new YamlRuleDefinitionReader());
			try{
				compatibleCheckRules = ruleFactory.createRules(
						new FileReader("config/sql-compatible-rules.yml"));
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	public SQLValidateResult validate(String sql) {
		 RulesEngine rulesEngine = new DefaultRulesEngine();
		 SQLValidateResult result=new SQLValidateResult();
		 Facts facts = new Facts();
		 facts.put("sql", sql);
		 facts.put("regex", new StringValidateHelper());
		 facts.put("result", result);
	     rulesEngine.fire(compatibleCheckRules, facts);
	     return result;
	}
	
	
	/*
	 * public SQLValidateResult validateCompatible(String sql) {
	 * 
	 * 
	 * 
	 * // return true; }
	 */
}
