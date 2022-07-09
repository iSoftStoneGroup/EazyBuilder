package test;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.eazybuilder.ci.entity.Scm;

public class ScmUtil {

	@Test
	public void testSerialize() throws Exception {
		Scm scm=new Scm();
		scm.setName("hello");
		scm.setPassword("test123");
		
		ObjectMapper mapper=new ObjectMapper();
		System.out.println(mapper.writeValueAsString(scm));
	}
	
	@Test
	public void testDeserialize()throws Exception{
		String json="{\"password\":\"123456\",\"id\":null,\"name\":\"hello\",\"type\":null,\"url\":null,\"tagName\":null,\"user\":null,\"changePwd\":false}";
		ObjectMapper mapper=new ObjectMapper();
		Scm scm=mapper.readValue(json, Scm.class);
		System.out.println(scm.getPassword());
	}
}
