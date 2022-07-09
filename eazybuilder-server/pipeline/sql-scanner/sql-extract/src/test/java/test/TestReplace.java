package test;

import org.junit.Test;

import com.eazybuilder.sql.impl.ibatis.helper.MybatisExtractUtil;

public class TestReplace {

	@Test
	public void doTest() {
		String sql="select * from icip_adapter where				adapterCode = #{_parameter} ";
		System.out.println(MybatisExtractUtil.replaceAllParam(sql));
	}
}
