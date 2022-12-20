package com.eazybuilder.ga.component.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Data
@RefreshScope
@Component
public class MailComponentConfig {
    @Value("${spring.mail.username}")
    private String From;

    @Value("${spring.mail.to}")
    private String to;
}
