package com.eazybuilder.ci.sql.service;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.eazybuilder.ci.sql.vo.SQLValidateResult;

@Component
public class MySQLValidateExecutor extends AbstractSQLDataSourceValidator{
	private static final String SYNTAX_ERR_MSG="You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near";
	
	@Resource(name="mysql5JdbcTemplate")
	JdbcTemplate template;

	@Override
	public JdbcTemplate getJdbcTemplate() {
		return template;
	}

	@Override
	public String getValidatorName() {
		return "mysql";
	}

	@Override
	public SQLValidateResult validateCompatible(String sql) {
		SQLValidateResult result= super.validateCompatible(sql);
		if(!result.isSuccess()&&result.getMsg().contains(SYNTAX_ERR_MSG)) {
			result.setMsg(StringUtils.replace(result.getMsg(),SYNTAX_ERR_MSG, "此处附近语法错误:"));
			System.err.println(result.getMsg());
		}
		return result;
	}
	
	
}
