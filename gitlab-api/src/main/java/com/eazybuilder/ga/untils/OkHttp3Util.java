package com.eazybuilder.ga.untils;

import okhttp3.*;
import okhttp3.Request.Builder;

import javax.net.ssl.X509TrustManager;
import java.util.Map;


public class OkHttp3Util {
    //MEDIA_TYPE <==> Content-Type
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    //MEDIA_TYPE_TEXT post请求不是application/x-www-form-urlencoded的，全部直接返回，不作处理，即不会解析表单数据来放到request parameter map中。所以通过request.getParameter(name)是获取不到的。只能使用最原始的方式，读取输入流来获取。
    private static final MediaType MEDIA_TYPE_TEXT = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    //没有将OkHttpClient单例化，造成的后果就是服务器OOM异常
    private static final OkHttpClient okHttpClient = new OkHttpClient();


    /**
     * get请求方式
     *
     * @param url 请求地址(可以传路径参数和?xx=xxx&xx=xxx)
     * @param headerMap 请求头参数
     * @return 响应体信息
     * @throws Exception
     */
    public static String get(String url, Map<String, String> headerMap) throws Exception {
        //请求参数处理
        Builder builder = new Builder(); //创建请求建造者
        if (headerMap != null && !headerMap.isEmpty()) { //判断请求头是否有参数
            headerMap.forEach((name, value) -> builder.addHeader(name, value)); //请求头
        }
        builder.url(url) //请求地址
                .get(); //请求方式
        Request request = builder.build(); //建造请求
        //请求发送
        Response response = okHttpClient.newCall(request).execute(); //请求数据,返回响应体
        String body = response.body().string(); //拿到响应体数据
        return body;
    }


    /**
     * post请求 请求体为application/x-www-form-urlencoded格式
     *
     * @param url 请求地址
     * @param bodyMap 请求体参数
     * @param headerMap 请求头参数
     * @return 响应体信息
     * @throws Exception
     */
    public static String post(String url, Map<String, String> bodyMap, Map<String, String> headerMap) throws Exception {
        //请求参数处理
        Builder builder = new Builder(); //创建请求建造者
        FormBody.Builder formBody =new FormBody.Builder();
        if (bodyMap != null && !bodyMap.isEmpty()) { //判断请求体是否有参数
            bodyMap.forEach((name, value) -> formBody.add(name, value)); //请求体
        }
        if (headerMap != null && !headerMap.isEmpty()) { //判断请求头是否有参数
            headerMap.forEach((name, value) -> builder.addHeader(name, value)); //请求头
        }
        builder
                .url(url) //请求地址
                .post(formBody.build()); //请求方式,请求体参数
        Request request = builder.build(); //建造请求
        //请求发送
        Response response = okHttpClient.newCall(request).execute(); //请求数据,返回响应体
        String body = response.body().string(); //拿到响应体数据
        return body;
    }

    /**
     * post请求 请求体为application/x-www-form-urlencoded格式
     * 绕过ssl验证
     * @param url 请求地址
     * @param bodyMap 请求体参数
     * @param headerMap 请求头参数
     * @return 响应体信息
     * @throws Exception
     */
    public static String postHttps(String url, Map<String, String> bodyMap, Map<String, String> headerMap) throws Exception {
        //请求参数处理
        Builder builder = new Builder(); //创建请求建造者
        FormBody.Builder formBody =new FormBody.Builder();
        if (bodyMap != null && !bodyMap.isEmpty()) { //判断请求体是否有参数
            bodyMap.forEach((name, value) -> formBody.add(name, value)); //请求体
        }
        if (headerMap != null && !headerMap.isEmpty()) { //判断请求头是否有参数
            headerMap.forEach((name, value) -> builder.addHeader(name, value)); //请求头
        }
        builder
                .url(url) //请求地址
                .post(formBody.build()); //请求方式,请求体参数
        Request request = builder.build(); //建造请求
        X509TrustManager manager = SSLSocketClientUtil.getX509TrustManager();

        //请求发送
        Response response = okHttpClient.newBuilder()
                .sslSocketFactory(SSLSocketClientUtil.getSocketFactory(manager),manager)
                .hostnameVerifier(SSLSocketClientUtil.getHostnameVerifier()).build()
                .newCall(request).execute(); //请求数据,返回响应体
        String body = response.body().string(); //拿到响应体数据
        return body;
    }

    /**
     * post请求 请求体为json格式
     *
     * @param url 请求地址
     * @param json 请求体参数
     * @param headerMap 请求头参数
     * @return 响应体信息
     * @throws Exception
     */
    public static String post(String url, String json, Map<String, String> headerMap) throws Exception {
        //请求参数处理
        Builder builder = new Builder();//创建请求建造者
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, json);
        if (headerMap != null && !headerMap.isEmpty()) {//判断请求头是否有参数
            headerMap.forEach((name, value) -> builder.addHeader(name, value));//请求头
        }
        builder
                .url(url) //请求地址
                .post(requestBody); //请求方式,请求体参数
        Request request = builder.build();////建造请求
        //请求发送
        Response response = okHttpClient.newCall(request).execute();//请求数据,返回响应体
        String body = response.body().string();//拿到响应体数据
        return body;
    }


    /**
     * put请求
     *
     * @param url
     * @param json
     * @param headerMap
     * @return
     * @throws Exception
     */
    public static String put(String url, String json, Map<String, String> headerMap)  throws Exception {
        //请求参数处理
        Builder builder = new Builder();//创建请求建造者
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, json);
        if (headerMap != null && !headerMap.isEmpty()) {//判断请求头是否有参数
            headerMap.forEach((name, value) -> builder.addHeader(name, value));//请求头
        }
        builder
                .url(url) //请求地址
                .put(requestBody); //请求方式,请求体参数
        Request request = builder.build();////建造请求
        //请求发送
        Response response = okHttpClient.newCall(request).execute();//请求数据,返回响应体
        String body = response.body().string();//拿到响应体数据
        return body;
    }

}
