package com.eazybuilder.ga.component.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Data
@RefreshScope
@Component
public class GitLabComponentConfig {
    @Value("${gitLab.url}")
    private String url;

    @Value("${gitLab.token}")
    private String token;
}
