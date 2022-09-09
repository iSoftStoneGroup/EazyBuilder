package test;

import com.eazybuilder.ci.config.LoadConfigYML;

import java.util.Properties;

public class testLoadConfig {
    private static Properties properties = new LoadConfigYML().getConfigProperties();

//    static{
//
//        System.out.println(properties.getProperty("sonarqube.serverUrl"));
//        System.out.println(properties.getProperty("sonarqube.user"));
//        System.out.println(properties.getProperty("sonarqube.password"));
//        System.out.println(properties.getProperty("sonarqube.url"));
//
//    }
    public static void main(String[] args) {
//        LoadConfigYML loadConfigYML = new LoadConfigYML();
//        Properties properties = loadConfigYML.getConfigProperties();
//        System.out.println(properties.getProperty("sonarqube.serverUrl"));
//        System.out.println(properties.getProperty("sonarqube.user"));
//        System.out.println(properties.getProperty("sonarqube.password"));
//        System.out.println(properties.getProperty("sonarqube.url"));
//
//        System.out.println(properties.getProperty("nexus3.url"));
//        System.out.println("http://" + properties.getProperty("nexus3.url") + "/repository/maven-public/");
//        System.out.println(properties.getProperty("jenkins.maven.url"));
//        System.out.println(properties.getProperty("registry.url"));
        System.out.println(properties.getProperty("email.suffix"));
    }
}
