package com.eazybuilder.ci.conf;


import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.context.annotation.Configuration;

@Configuration

@ConfigurationProperties(prefix = "portal")
public class UpmsLoginConf {


    private Boolean used;


    private String loginUrl;


    private String credentialsUrl;


    private String getUserInfoUrl;


    private String getMenusForCurrentUser;


    public Boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getCredentialsUrl() {
        return credentialsUrl;
    }

    public void setCredentialsUrl(String credentialsUrl) {
        this.credentialsUrl = credentialsUrl;
    }

    public String getGetUserInfoUrl() {
        return getUserInfoUrl;
    }

    public void setGetUserInfoUrl(String getUserInfoUrl) {
        this.getUserInfoUrl = getUserInfoUrl;
    }

    public String getGetMenusForCurrentUser() {
        return getMenusForCurrentUser;
    }

    public void setGetMenusForCurrentUser(String getMenusForCurrentUser) {
        this.getMenusForCurrentUser = getMenusForCurrentUser;
    }
}
