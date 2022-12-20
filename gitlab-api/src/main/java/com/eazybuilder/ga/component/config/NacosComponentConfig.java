package com.eazybuilder.ga.component.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Data
@RefreshScope
@Component
public class NacosComponentConfig {

    @Value("${nacos.url}")
    private String url;

    @Value("${nacos.username:devops}")
    private String username;

    @Value("${nacos.password:devops}")
    private String password;

}
