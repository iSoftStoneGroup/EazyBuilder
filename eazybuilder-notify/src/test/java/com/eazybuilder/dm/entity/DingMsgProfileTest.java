package com.eazybuilder.dm.entity;

import org.junit.Assert;
import org.junit.Test;

import javax.persistence.*;

/**
 * 发�?�消息配置（用于精细化设置都有哪些操作需要发送钉钉�?�邮件消息）
 */
public class DingMsgProfileTest {
	@Test
	public void doTest(){
		DingMsgProfile dingMsgProfile=new DingMsgProfile();
		dingMsgProfile.setId("");
		dingMsgProfile.setPipelineFail(false);
		dingMsgProfile.setFocusRedlightRepair(false);
		dingMsgProfile.setSqlScript(false);
		dingMsgProfile.setPush(false);
		dingMsgProfile.setMergeApply(false);
		dingMsgProfile.setMergeRefused(false);
		dingMsgProfile.setMergePass(false);
		dingMsgProfile.setReleaseApply(false);
		dingMsgProfile.setReleaseWait(false);
		dingMsgProfile.setReleasePass(false);
		dingMsgProfile.setReleaseRefused(false);
		dingMsgProfile.setOnlineApply(false);
		dingMsgProfile.setOnlineWait(false);
		dingMsgProfile.setOnlinePass(false);
		dingMsgProfile.setOnlineRefused(false);
		dingMsgProfile.setDeployWait(false);
		dingMsgProfile.setDeployRun(false);
		dingMsgProfile.setMonitoringDtpTestRun(false);
		dingMsgProfile.setMonitoringDtpTestStatus(false);
		dingMsgProfile.setMonitoringMeasureDataSync(false);
		dingMsgProfile.setMonitoringJobRun(false);
		dingMsgProfile.setConfigEdit(false);
		dingMsgProfile.setDtpPerformanceTest(false);
		dingMsgProfile.setDtpApiTest(false);
		dingMsgProfile.setDtpWebUiTest(false);
		dingMsgProfile.setDtpSecurityTest(false);
		dingMsgProfile.seteazybuilderuesStatusUpdate(false);
		dingMsgProfile.seteazybuilderuesAdd(false);
		dingMsgProfile.setMonitoringSql(false);
		dingMsgProfile.setNeedToDoPass3Day(false);

		Assert.assertFalse(dingMsgProfile.isPipelineFail());
		Assert.assertFalse(dingMsgProfile.isPipelineFail());
		Assert.assertFalse(dingMsgProfile.isFocusRedlightRepair());
		Assert.assertFalse(dingMsgProfile.eazybuilderqlScript());
		Assert.assertFalse(dingMsgProfile.isPush());
		Assert.assertFalse(dingMsgProfile.isMergeApply());
		Assert.assertFalse(dingMsgProfile.isMergeRefused());
		Assert.assertFalse(dingMsgProfile.isMergePass());
		Assert.assertFalse(dingMsgProfile.isReleaseApply());
		Assert.assertFalse(dingMsgProfile.isReleaseWait());
		Assert.assertFalse(dingMsgProfile.isReleasePass());
		Assert.assertFalse(dingMsgProfile.isReleaseRefused());
		Assert.assertFalse(dingMsgProfile.isOnlineApply());
		Assert.assertFalse(dingMsgProfile.isOnlineWait());
		Assert.assertFalse(dingMsgProfile.isOnlinePass());
		Assert.assertFalse(dingMsgProfile.isOnlineRefused());
		Assert.assertFalse(dingMsgProfile.isDeployWait());
		Assert.assertFalse(dingMsgProfile.isDeployRun());
		Assert.assertFalse(dingMsgProfile.isMonitoringDtpTestRun());
		Assert.assertFalse(dingMsgProfile.isMonitoringDtpTestStatus());
		Assert.assertFalse(dingMsgProfile.isMonitoringMeasureDataSync());
		Assert.assertFalse(dingMsgProfile.isMonitoringJobRun());
		Assert.assertFalse(dingMsgProfile.isConfigEdit());
		Assert.assertFalse(dingMsgProfile.isDtpPerformanceTest());
		Assert.assertFalse(dingMsgProfile.isDtpApiTest());
		Assert.assertFalse(dingMsgProfile.isDtpWebUiTest());
		Assert.assertFalse(dingMsgProfile.isDtpSecurityTest());
		Assert.assertFalse(dingMsgProfile.iseazybuilderuesStatusUpdate());
		Assert.assertFalse(dingMsgProfile.iseazybuilderuesAdd());
		Assert.assertFalse(dingMsgProfile.isMonitoringSql());
		Assert.assertFalse(dingMsgProfile.isNeedToDoPass3Day());
	}

}
