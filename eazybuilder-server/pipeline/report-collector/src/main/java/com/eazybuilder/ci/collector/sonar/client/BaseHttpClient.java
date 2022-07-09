package com.eazybuilder.ci.collector.sonar.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.google.gson.Gson;
import com.eazybuilder.ci.collector.sonar.client.authentication.PreemptiveAuth;
import com.eazybuilder.ci.collector.sonar.util.HttpResponseValidator;
import com.eazybuilder.ci.collector.sonar.util.HttpResponseWrapper;

/**
 * Created by chengkeqian on 2017/6/20.
 */
public class BaseHttpClient {
    private final Logger logger;

    private URI uri;
    private CloseableHttpClient client;

    private String context;
    private HttpContext localContext;

    protected Gson gson;

    public BaseHttpClient(URI uri, CloseableHttpClient client) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.context = uri.getPath();
        if (this.context.endsWith("/")) {
            this.context = this.context.substring(0, this.context.length()-1);
        }

        this.uri = uri;
        this.client = client;
        this.logger.debug("uri={}", uri.toString());
        this.gson = new Gson();
    }

    public BaseHttpClient(URI uri, HttpClientBuilder builder) {
        this(uri, builder.build());
    }

    public BaseHttpClient(URI uri) {
        this(uri, HttpClientBuilder.create());
    }

    public BaseHttpClient(URI uri, String username, String password) {
        this(uri, addAuthentication(HttpClientBuilder.create(), uri, username, password));
        if (StringUtils.isNotBlank(username)) {
            this.localContext = new BasicHttpContext();
            this.localContext.setAttribute("preemptive-auth", new BasicScheme());
        }
    }

    private static HttpClientBuilder addAuthentication(HttpClientBuilder builder, URI uri, String username, String password) {
        if (StringUtils.isNotBlank(username)) {
            BasicCredentialsProvider provider = new BasicCredentialsProvider();
            AuthScope scope = new AuthScope(uri.getHost(), uri.getPort(), "realm");
            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
            provider.setCredentials(scope, credentials);
            builder.setDefaultCredentialsProvider(provider);
            builder.addInterceptorFirst(new PreemptiveAuth());
        }
        return builder;
    }

    private void releaseConnection(HttpRequestBase httpRequestBase) {
        httpRequestBase.releaseConnection();
    }

    public String get(String path) throws IOException, URISyntaxException {
    	logger.info("path----:{}",path);
        HttpGet getMethod = new HttpGet(this.api(path));
        logger.info("getMethod----:{}",getMethod);
      
        CloseableHttpResponse response = this.client.execute(getMethod, this.localContext);
        this.logger.debug("get({}),responseCode={}", new Object[]{path, Integer.valueOf(response.getStatusLine().getStatusCode())});

        String var;
        try {
            HttpResponseValidator.validateResponse(response);
            var = EntityUtils.toString(response.getEntity());
        } finally {
            EntityUtils.consume(response.getEntity());
            this.releaseConnection(getMethod);
        }
        return var;
    }

    public <T> T get(String path, Class<T> cls) throws IOException, URISyntaxException {
        String content = get(path);
        return HttpResponseWrapper.wrap(content, cls);
    }

    public String post(String path, String json) throws IOException, URISyntaxException {
        HttpPost httpPost = new HttpPost(this.api(path));
        StringEntity se = new StringEntity(json, ContentType.create("application/json", "UTF-8"));
        httpPost.setEntity(se);
        CloseableHttpResponse response = null;
        String var = null;
        try {
            response = this.client.execute(httpPost);
            this.logger.debug("post(url={} and data={}),responseCode={}", new Object[]{path, json, Integer.valueOf(response.getStatusLine().getStatusCode())});
            HttpResponseValidator.validateResponse(response);
            var = EntityUtils.toString(response.getEntity());
        } finally {
            EntityUtils.consume(response.getEntity());
            this.releaseConnection(httpPost);
        }
        return var;
    }

    private URI api(String path) throws URISyntaxException {
    	if(path.startsWith("http")){
    		System.out.println("URL:"+path);
    		return new URI(path);
    	}
    	if(context!=null){
    		path=context+path;
    	}
    	URI uri= this.uri.resolve("/").resolve(path.replace(" ", "%20"));
        System.out.println("URL:"+uri);
        return uri;
    }
}
