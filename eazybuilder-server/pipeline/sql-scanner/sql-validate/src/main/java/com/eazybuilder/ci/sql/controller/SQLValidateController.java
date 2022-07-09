package com.eazybuilder.ci.sql.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eazybuilder.ci.sql.service.AbstractSQLDataSourceValidator;
import com.eazybuilder.ci.sql.service.KnownCompatiblePatternValidator;
import com.eazybuilder.ci.sql.service.MySQLValidateExecutor;
import com.eazybuilder.ci.sql.service.PostgreValidateExecutor;
import com.eazybuilder.ci.sql.vo.SQLValidateResult;

@RestController
@RequestMapping("/validate")
public class SQLValidateController {

	@Autowired
	MySQLValidateExecutor mysqlExecutor;
	
	@Autowired
	PostgreValidateExecutor pgExecutor; 
	
	@Autowired
	KnownCompatiblePatternValidator patternValidator;
	
	@PostMapping("/mysql5")
	public Map<String,SQLValidateResult> validateMySQL5(@RequestBody Map<String,String> sqls) {
		return validate(mysqlExecutor,sqls);
	}

	private Map<String, SQLValidateResult> validate(AbstractSQLDataSourceValidator validator,Map<String, String> sqls) {
		Map<String,SQLValidateResult> result=new ConcurrentHashMap<>();
		sqls.entrySet().forEach(sql->{
			try {
				SQLValidateResult checkResult=validator.validateCompatible(sql.getValue());
				if(checkResult.isSuccess()) {
					checkResult=patternValidator.validate(sql.getValue());
				}
				result.put(sql.getKey(), checkResult);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return result;
	}
	
	@PostMapping("/pg")
	public Map<String,SQLValidateResult> validatePostgre(@RequestBody Map<String,String> sqls) {
		return validate(pgExecutor,sqls);
	}
}
