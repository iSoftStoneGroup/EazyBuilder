package com.eazybuilder.ci.sql;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class JdbcTemplateConfig {

	@Primary
	@Bean(name = "mysql5JdbcTemplate")
	public JdbcTemplate ds1JdbcTemplate(@Qualifier("mysql5DataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
	
	@Bean(name = "postgreJdbcTemplate")
	public JdbcTemplate ds2JdbcTemplate(@Qualifier("postgreDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

}
