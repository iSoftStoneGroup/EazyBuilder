package com.eazybuilder.ga.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.eazybuilder.ga.component.NacosComponent;
import com.eazybuilder.ga.service.NacosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NacosServiceImpl implements NacosService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    NacosComponent nacosComponent;

    @Override
    public String releaseConfig(String namespace, String fileName, String group, String fileContent) {
        String postResult = nacosComponent.releaseConfig(namespace, fileName, group, fileContent);
        logger.info("############################"+postResult);
        if(null==postResult){
            return "请求nacos服务器失败";
        }
        if (Boolean.TRUE.toString().equals(postResult)) {
            return "";
        }else{
            return "发布失败"+postResult;
        }
    }
}
