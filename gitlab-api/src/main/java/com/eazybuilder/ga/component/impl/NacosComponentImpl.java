package com.eazybuilder.ga.component.impl;

import com.eazybuilder.ga.component.NacosComponent;
import com.eazybuilder.ga.component.config.NacosComponentConfig;
import com.eazybuilder.ga.untils.CurrencyUntil;
import com.eazybuilder.ga.untils.OkHttp3Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class NacosComponentImpl implements NacosComponent {

    @Autowired
    private NacosComponentConfig nacosComponentConfig;

    @Override
    public String releaseConfig(String tenant, String dataId, String group, String content) {

        String url = nacosComponentConfig.getUrl()+"cs/configs";

        Map<String, String> bodyMap = new HashMap<String, String>();
        bodyMap.put("tenant",tenant);
        bodyMap.put("dataId",dataId);
        bodyMap.put("group",group);
        bodyMap.put("content",content);
        bodyMap.put("username", nacosComponentConfig.getUsername());
        bodyMap.put("password", nacosComponentConfig.getPassword());
        try {
            String postResult = OkHttp3Util.postHttps(url, bodyMap,null);
            return postResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
