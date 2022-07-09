package test;

import org.junit.Test;

import com.eazybuilder.ci.sql.service.KnownCompatiblePatternValidator;
import com.eazybuilder.ci.sql.vo.SQLValidateResult;

public class TestCompatibleRule {

	@Test
	public void doTest() {
		KnownCompatiblePatternValidator validator=new KnownCompatiblePatternValidator();
		SQLValidateResult result=validator.validate("select to_char  () from dual");
		System.out.println(result);
		
		result=validator.validate("select to_date  () from dual");
		System.out.println(result);
		
		result=validator.validate("select to_date() to_char () from dual");
		System.out.println(result);
	}
	
	@Test
	public void doTestToManyJoin() {
		KnownCompatiblePatternValidator validator=new KnownCompatiblePatternValidator();
		SQLValidateResult result=validator.validate("aa join  aa  join aa join aaa join aaa");
		System.out.println(result);
		
		result=validator.validate("aa on  aa  on aa on aaa on aaa");
		System.out.println(result);
	}
	
	@Test
	public void doTestCallProcedure() {
		KnownCompatiblePatternValidator validator=new KnownCompatiblePatternValidator();
		SQLValidateResult result=validator.validate("call aaa");
		System.out.println(result);
	}
	
	@Test
	public void doTestInsertAll() {
		KnownCompatiblePatternValidator validator=new KnownCompatiblePatternValidator();
		SQLValidateResult result=validator.validate(" select * from ITS_LOGINACCOUNTLOCK where loginAccount = 1 ");
		System.out.println(result);
	}
}
