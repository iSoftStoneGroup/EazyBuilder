package com.eazybuilder.ci.collector.sonar.client.authentication;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

/**
 * Created by chengkeqian on 2017/6/20.
 */
public class PreemptiveAuth implements HttpRequestInterceptor {
    public PreemptiveAuth() {
    }

    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
        AuthState authState = (AuthState)context.getAttribute("http.auth.target-scope");
        if(authState.getAuthScheme() == null) {
            AuthScheme authScheme = (AuthScheme)context.getAttribute("preemptive-auth");
            CredentialsProvider credsProvider = (CredentialsProvider)context.getAttribute("http.auth.credentials-provider");
            HttpHost targetHost = (HttpHost)context.getAttribute("http.target_host");
            if(authScheme != null) {
                Credentials creds = credsProvider.getCredentials(new AuthScope(targetHost.getHostName(), targetHost.getPort()));
                if(creds == null) {
                    throw new HttpException("No credentials for preemptive authentication");
                }

                authState.update(authScheme, creds);
            }
        }
    }
}
