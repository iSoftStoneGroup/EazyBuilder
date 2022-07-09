package com.eazybuilder.ci.collector.sonar.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;

/**
 * Created by chengkeqian on 2017/6/20.
 */
public class HttpResponseValidator {

    public static void validateResponse(HttpResponse response) throws HttpResponseException {
        int status = response.getStatusLine().getStatusCode();
        if(status < 200 || status >= 400) {
            throw new HttpResponseException(status, response.getStatusLine().getReasonPhrase());
        }
    }
}
