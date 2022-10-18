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

public class CustomStringSerializerTest {


    @Test
    public void isEmpty() {
        CustomStringSerializer customStringSerializer = new CustomStringSerializer();
        boolean empty = customStringSerializer.isEmpty("");
        Assert.assertEquals(true, empty);
    }


}
