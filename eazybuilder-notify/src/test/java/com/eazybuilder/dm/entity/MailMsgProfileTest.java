package com.eazybuilder.dm.entity;

import org.junit.Assert;
import org.junit.Test;

/**
 * 发�?�消息配置（用于精细化设置都有哪些操作需要发送钉钉�?�邮件消息）
 */
public class MailMsgProfileTest {
	@Test
	public void doTest() {
		MailMsgProfile mailMsgProfile = new MailMsgProfile();
		

		mailMsgProfile.setId("");
		mailMsgProfile.setPipelineFail(false);
		mailMsgProfile.setFocusRedlightRepair(false);
		mailMsgProfile.setSqlScript(false);
		mailMsgProfile.setPush(false);
		mailMsgProfile.setMergeApply(false);
		mailMsgProfile.setMergeRefused(false);
		mailMsgProfile.setMergePass(false);
		mailMsgProfile.setReleaseApply(false);
		mailMsgProfile.setReleaseWait(false);
		mailMsgProfile.setReleasePass(false);
		mailMsgProfile.setReleaseRefused(false);
		mailMsgProfile.setOnlineApply(false);
		mailMsgProfile.setOnlineWait(false);
		mailMsgProfile.setOnlinePass(false);
		mailMsgProfile.setOnlineRefused(false);
		mailMsgProfile.setDeployWait(false);
		mailMsgProfile.setDeployRun(false);
		mailMsgProfile.setMonitoringDtpTestRun(false);
		mailMsgProfile.setMonitoringDtpTestStatus(false);
		mailMsgProfile.setMonitoringMeasureDataSync(false);
		mailMsgProfile.setMonitoringJobRun(false);
		mailMsgProfile.setConfigEdit(false);
		mailMsgProfile.setDtpPerformanceTest(false);
		mailMsgProfile.setDtpApiTest(false);
		mailMsgProfile.setDtpWebUiTest(false);
		mailMsgProfile.setDtpSecurityTest(false);
		mailMsgProfile.seteazybuilderuesStatusUpdate(false);
		mailMsgProfile.seteazybuilderuesAdd(false);
		mailMsgProfile.setMonitoringSql(false);
		mailMsgProfile.setNeedToDoPass3Day(false);


		Assert.assertFalse(mailMsgProfile.isPipelineFail());
		Assert.assertFalse(mailMsgProfile.isFocusRedlightRepair());
		Assert.assertFalse(mailMsgProfile.eazybuilderqlScript());
		Assert.assertFalse(mailMsgProfile.isPush());
		Assert.assertFalse(mailMsgProfile.isMergeApply());
		Assert.assertFalse(mailMsgProfile.isMergeRefused());
		Assert.assertFalse(mailMsgProfile.isMergePass());
		Assert.assertFalse(mailMsgProfile.isReleaseApply());
		Assert.assertFalse(mailMsgProfile.isReleaseWait());
		Assert.assertFalse(mailMsgProfile.isReleasePass());
		Assert.assertFalse(mailMsgProfile.isReleaseRefused());
		Assert.assertFalse(mailMsgProfile.isOnlineApply());
		Assert.assertFalse(mailMsgProfile.isOnlineWait());
		Assert.assertFalse(mailMsgProfile.isOnlinePass());
		Assert.assertFalse(mailMsgProfile.isOnlineRefused());
		Assert.assertFalse(mailMsgProfile.isDeployWait());
		Assert.assertFalse(mailMsgProfile.isDeployRun());
		Assert.assertFalse(mailMsgProfile.isMonitoringDtpTestRun());
		Assert.assertFalse(mailMsgProfile.isMonitoringDtpTestStatus());
		Assert.assertFalse(mailMsgProfile.isMonitoringMeasureDataSync());
		Assert.assertFalse(mailMsgProfile.isMonitoringJobRun());
		Assert.assertFalse(mailMsgProfile.isConfigEdit());
		Assert.assertFalse(mailMsgProfile.isDtpPerformanceTest());
		Assert.assertFalse(mailMsgProfile.isDtpApiTest());
		Assert.assertFalse(mailMsgProfile.isDtpWebUiTest());
		Assert.assertFalse(mailMsgProfile.isDtpSecurityTest());
		Assert.assertFalse(mailMsgProfile.iseazybuilderuesStatusUpdate());
		Assert.assertFalse(mailMsgProfile.iseazybuilderuesAdd());
		Assert.assertFalse(mailMsgProfile.isMonitoringSql());
		Assert.assertFalse(mailMsgProfile.isNeedToDoPass3Day());
		
	}
}
