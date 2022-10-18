package com.eazybuilder.ci.maven;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.util.Properties;

public class LoadConfigYML {
    public static Properties getConfigProperties(){
        YamlPropertiesFactoryBean yamlProFb = new YamlPropertiesFactoryBean();
        yamlProFb.setResources(new ClassPathResource("config\\config.yml"));
        Properties properties = yamlProFb.getObject();
        return properties;
    }
}
