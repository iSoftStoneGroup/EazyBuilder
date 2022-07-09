package com.eazybuilder.ci.sql;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DataSourceConfig {
	@Primary
	@Bean(name="mysql5Config")
	@ConfigurationProperties(prefix = "spring.datasource.mysql5")
	public DataSourceProperties mysql5Config() {
		return new DataSourceProperties();
	}
	
	@Primary
	@Bean(name="mysql5DataSource")
	public DataSource mysql5(@Qualifier("mysql5Config")DataSourceProperties dsp) {
		return dsp.initializeDataSourceBuilder().build();
	}
	
	
	@Bean(name="postgreConfig")
	@ConfigurationProperties(prefix = "spring.datasource.postgre")
	public DataSourceProperties postgreConfig() {
		return new DataSourceProperties();
	}
	
	@Bean(name="postgreDataSource")
	public DataSource postgre(@Qualifier("postgreConfig")DataSourceProperties dsp) {
		return dsp.initializeDataSourceBuilder().build();
	}
	
	
}
