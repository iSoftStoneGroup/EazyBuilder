package com.eazybuilder.ga.untils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.codec.Base64;
import okhttp3.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;

public class DingTalkUtil {
    private static OkHttpClient client = new OkHttpClient();//没有将OkHttpClient单例化，造成的后果就是服务器OOM异常


    /**
     * 对密钥进行加工
     *
     * @param timestamp 当前时间戳，单位是毫秒
     * @param secret    密钥
     * @return
     * @throws Exception
     */
    public static String getSign(Long timestamp, String secret) throws Exception {
        String stringToSign = timestamp + "\n" + secret;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
        String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
        return sign;
        //孙旭大佬代码
//        String stringToSign = time + "\n" + secret;
//        Mac mac = Mac.getInstance("HmacSHA256");
//        mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
//        byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
//        return URLEncoder.encode(new String(Base64.getEncoder().encode(signData)), "UTF-8");
    }
}
