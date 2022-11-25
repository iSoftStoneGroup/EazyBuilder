package com.eazybuilder.dm.service;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;

public class UserServiceTest {
    //	@Test
    public void user(){
        HttpRequest get = HttpUtil.createGet("http://0.0.0.0:8686/system/openapi/v1/user/info?email=xxx");
        get.auth("Bearer xxxxx");
        get.header("Grant-Type","api_token");
        HttpResponse execute = get.execute();
        String body = execute.body();
        System.out.println(body);
    }
    //	@Test
    public void doTest(){
        HttpRequest get = HttpUtil.createGet("http://0.0.0.0:8686/system/openapi/v1/group");
        get.auth("Bearer xxxx");
        get.header("Grant-Type","api_token");
        HttpResponse execute = get.execute();
        String body = execute.body();
        System.out.println(body);
    }

    //	@Test
    public void alluser(){
        String api = "user";

        HttpRequest get = HttpUtil.createGet("http://0.0.0.0:8686/system/openapi/v1/" + api);
        get.auth("Bearer xxxx");
        get.header("Grant-Type","api_token");
        HttpResponse execute = get.execute();
        String body = execute.body();
        System.out.println(body);
    }
}
