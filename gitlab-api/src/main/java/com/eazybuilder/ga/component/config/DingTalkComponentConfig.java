package com.eazybuilder.ga.component.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@RefreshScope
@Component
public class DingTalkComponentConfig {
    @Value("${dingTalk.url}")
    private String url;

    @Value("${dingTalk.sign}")
    private String sign;

    @Value("${dingTalk.isAtAll}")
    private boolean isAtAll;

    @Value("#{'${dingTalk.atMobiles}'.split(',')}")
    private List<String> atMobiles;

}
