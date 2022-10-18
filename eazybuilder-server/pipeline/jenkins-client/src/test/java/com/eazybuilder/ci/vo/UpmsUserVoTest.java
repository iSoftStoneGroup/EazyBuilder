package com.eazybuilder.ci.vo;

import com.eazybuilder.ci.entity.Upms.UpmsUserVo;
import org.junit.Assert;
import org.junit.Test;

public class UpmsUserVoTest {

    @Test
    public  void upmsUserVoTest(){
        UpmsUserVo upmsUserVo = new UpmsUserVo();




        upmsUserVo.setInTeam(false);
        upmsUserVo.setUserId("");
        upmsUserVo.setUserName("");
        upmsUserVo.setNickName("");
        upmsUserVo.setEmail("");
        upmsUserVo.setDeptName("");
        upmsUserVo.setPhoneNumber("");
        upmsUserVo.setEmployeeId(0);
        upmsUserVo.equals(upmsUserVo);
        Assert.assertEquals(upmsUserVo,upmsUserVo);
        Assert.assertNotNull(upmsUserVo.isInTeam());
        Assert.assertNotNull(upmsUserVo.getUserId());
        Assert.assertNotNull(upmsUserVo.getUserName());
        Assert.assertNotNull(upmsUserVo.getNickName());
        Assert.assertNotNull(upmsUserVo.getEmail());
        Assert.assertNotNull(upmsUserVo.getDeptName());
        Assert.assertNotNull(upmsUserVo.getPhoneNumber());
        Assert.assertNotNull(upmsUserVo.getEmployeeId());

    }
}
