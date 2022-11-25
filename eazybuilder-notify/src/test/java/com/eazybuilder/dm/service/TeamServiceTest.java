package com.eazybuilder.dm.service;

import com.eazybuilder.dm.NotifyApplication;
import com.eazybuilder.dm.util.HttpUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NotifyApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TeamServiceTest {

    @Resource
    UserServiceImpl userService;

    @Test
    public void findByEmail()  {
        userService.findByEmail(new String[]{"user@eazybuilder.com"});
    }

}
