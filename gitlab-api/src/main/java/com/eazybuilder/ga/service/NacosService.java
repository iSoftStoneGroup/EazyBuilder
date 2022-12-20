package com.eazybuilder.ga.service;

public interface NacosService {

    String releaseConfig(String namespace, String fileName, String group, String fileContent);

}
