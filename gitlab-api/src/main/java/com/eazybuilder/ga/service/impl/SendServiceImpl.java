package com.eazybuilder.ga.service.impl;

import com.eazybuilder.ga.component.DingTalkComponent;
import com.eazybuilder.ga.service.SendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SendServiceImpl implements SendService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DingTalkComponent dingTalkComponent;

    @Override
    public String messageSend(String message) {
        boolean sendTextMessageDingTalkResult = dingTalkComponent.sendTextMessageDingTalk(message);
        if (!sendTextMessageDingTalkResult){
            return "钉钉发送失败";
        }
        return "";
    }
}
