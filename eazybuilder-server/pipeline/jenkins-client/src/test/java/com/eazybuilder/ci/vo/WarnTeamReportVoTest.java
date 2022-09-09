package com.eazybuilder.ci.vo;
import com.google.common.collect.Lists;

import com.eazybuilder.ci.controller.vo.WarnTeamReportVo;
import org.junit.Assert;
import org.junit.Test;

public class WarnTeamReportVoTest {

    @Test
    public  void upmsUserVoTest() {
        WarnTeamReportVo warnTeamReportVo = new WarnTeamReportVo();


        warnTeamReportVo.setTeamName("");
        warnTeamReportVo.setProjectQAReports(Lists.newArrayList());
        warnTeamReportVo.setBugBlockerSum(0L);
        warnTeamReportVo.setVulnerBlockerSum(0L);
        warnTeamReportVo.setCodeSmellBlocker(0L);
        warnTeamReportVo.setDcHighSum(0L);
        warnTeamReportVo.setUnitTestCoverageRate(0.0D);
        warnTeamReportVo.setProjectNum(0L);
        warnTeamReportVo.setWarnProjectReports(Lists.newArrayList());

        Assert.assertNotNull(warnTeamReportVo.getTeamName());
        Assert.assertNotNull(warnTeamReportVo.getProjectQAReports());
        Assert.assertNotNull(warnTeamReportVo.getBugBlockerSum());
        Assert.assertNotNull(warnTeamReportVo.getVulnerBlockerSum());
        Assert.assertNotNull(warnTeamReportVo.getCodeSmellBlocker());
        Assert.assertNotNull(warnTeamReportVo.getDcHighSum());
        Assert.assertNotNull(warnTeamReportVo.getUnitTestCoverageRate());
        Assert.assertNotNull(warnTeamReportVo.getProjectNum());
        Assert.assertNotNull(warnTeamReportVo.getWarnProjectReports());
    }
}
