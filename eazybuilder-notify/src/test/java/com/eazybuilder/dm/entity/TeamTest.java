package com.eazybuilder.dm.entity;
import com.google.common.collect.Lists;
import com.eazybuilder.dm.entity.DingMsgProfile;
import com.eazybuilder.dm.entity.MailMsgProfile;

import org.junit.Assert;
import org.junit.Test;

/**
 * 发�?�消息配置（用于精细化设置都有哪些操作需要发送钉钉�?�邮件消息）
 */
public class TeamTest {
	@Test
	public void doTest(){
		Team team=new Team();
		team.setTeamBeginDate("");
		team.setTeamEndDate("");
		team.setTeamName("");
		team.setTeamCode("");
		team.setDingSecret("");
		team.setDingWebHookUrl("");
		team.setId("");
		team.setGroupId(0L);
		team.setTenantId(0L);
		team.setUserId(0L);
		team.setDingMsgProfile(new DingMsgProfile());
		team.setMailMsgProfile(new MailMsgProfile());
		team.setDevopsUsers(Lists.newArrayList());

	}

}
