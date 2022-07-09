package com.eazybuilder.ci.sql.service;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.eazybuilder.ci.sql.vo.SQLValidateResult;
/**
 * SQL验证器，连接指定数据源，执行SQL，根据错误代码判定SQL是否有效
 * 
 * 
 * 目前的局限性：
 * 
 * 1.MySQL 简单函数无法判定,比如to_date,to_char
 * 

 *
 */
public abstract class AbstractSQLDataSourceValidator {
	/* SQL92中定义的标准错误码
	 * 
	42703 未定义的字段（UNDEFINED COLUMN） 
	42883 未定义的函数（UNDEFINED FUNCTION） 
	42P01 未定义的表（UNDEFINED TABLE） 
	42P02 未定义的参数（UNDEFINED PARAMETER） 
	42704 未定义对象（UNDEFINED OBJECT） 
	*/
	static final List<String> SQL_SYNTAX_ERROR_UNDEFINED= Arrays.asList(
			"42703","42883","42P01","42P02","42704","42S02");	

	public abstract JdbcTemplate getJdbcTemplate();
	
	public abstract String getValidatorName();
	
	public SQLValidateResult validateCompatible(String sql) {
		SQLValidateResult result=new SQLValidateResult();
		try {
			getJdbcTemplate().execute(sql);
			return result;
		} catch (Exception e) {
			//PSQLException SQLState 42601 ->语法错误
			if(e.getCause()!=null&&(e.getCause() instanceof SQLException)) {
				SQLException pe=(SQLException)e.getCause();
				String errorCode=pe.getSQLState();
				if(errorCode!=null&&errorCode.startsWith("42")
						&&!SQL_SYNTAX_ERROR_UNDEFINED.contains(errorCode)) {
					System.out.println("SQLSate:"+errorCode+",Msg:"+pe.getMessage());
					//42 类 语法错误或者违反访问规则 
					result.setSuccess(false);
					result.setCode(errorCode);
					result.setMsg(pe.getMessage());
					result.setValidatorName(getValidatorName());
				}
				
			}
		} 
		return result;
	}
}
