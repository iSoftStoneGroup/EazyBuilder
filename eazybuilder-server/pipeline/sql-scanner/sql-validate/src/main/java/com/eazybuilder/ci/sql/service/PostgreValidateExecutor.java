package com.eazybuilder.ci.sql.service;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class PostgreValidateExecutor extends AbstractSQLDataSourceValidator{
	
	@Resource(name="postgreJdbcTemplate")
	JdbcTemplate template;

	@Override
	public JdbcTemplate getJdbcTemplate() {
		return template;
	}

	@Override
	public String getValidatorName() {
		return "PostgreSQL";
	}
	
	
}
