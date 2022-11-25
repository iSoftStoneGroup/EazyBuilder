package com.eazybuilder.dm.entity;

import org.junit.Assert;
import org.junit.Test;

/**
 * 发�?�消息配置（用于精细化设置都有哪些操作需要发送钉钉�?�邮件消息）
 */
public class UserTest {
	@Test
	public void doTest(){
		User user=new User();
		

		user.setPhoneNumber("");
		user.setUserName("");
		user.setNickName("");
		user.setEmail("");
		user.setDeptName("");

		Assert.assertEquals(user.getPhoneNumber(),"");
		Assert.assertEquals(user.getUserId(),"");
		Assert.assertEquals(user.getUserName(),"");
		Assert.assertEquals(user.getNickName(),"");
		Assert.assertEquals(user.getEmail(),"");
		Assert.assertEquals(user.getDeptName(),"");
	}

}
