package com.eazybuilder.ci.collector.sonar.util;

import com.google.gson.Gson;

/**
 * Created by chengkeqian on 2017/6/20.
 */
public class HttpResponseWrapper {

    public static <T> T wrap(String json, Class<T> cls) {
    	return new Gson().fromJson(json,cls);
    }
}
