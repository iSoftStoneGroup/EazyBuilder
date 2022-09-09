package com.eazybuilder.ci.util;

import com.eazybuilder.ci.controller.vo.UserVo;
import com.eazybuilder.ci.dto.DevopsInitDto;
import com.eazybuilder.ci.entity.DingMsgProfile;
import com.eazybuilder.ci.entity.User;
import com.eazybuilder.ci.entity.devops.DevopsInit;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
import java.util.List;

/**
 * @author: mlxuef
 * @createTime: 2022/8/29
 * @description:
 **/
public class DtoEntityUtilTest {

    @Test
    public void trans() {
        boolean onlineApply = true;
        DingMsgProfile dingMsgProfile = new DingMsgProfile();
        dingMsgProfile.setOnlineApply(true);
        DevopsInitDto devopsInitDto = new DevopsInitDto();
        devopsInitDto.setDingMsgProfile(dingMsgProfile);
        DevopsInit devopsInit =  DtoEntityUtil.trans(devopsInitDto, DevopsInit.class);
        Assert.assertEquals(devopsInit.getDingMsgProfile().isOnlineApply(), onlineApply);
    }


    @Test
    public void testTrans1() {
        String email = "user@163.com";
        UserVo userVo = new UserVo();
        userVo.setEmail(email);
        UserVo[] vos = {userVo};
        List<User> users =  DtoEntityUtil.trans(vos, User.class);
        Assert.assertEquals(users.get(0).getEmail(), email);
    }

    @Test
    public void testTrans2() {
        String email = "user@163.com";
        UserVo userVo = new UserVo();
        userVo.setEmail(email);
        List<UserVo> vos = Lists.newArrayList();
        vos.add(userVo);
        List<User> users =  DtoEntityUtil.trans(vos, User.class);
        Assert.assertEquals(users.get(0).getEmail(), email);
    }
}