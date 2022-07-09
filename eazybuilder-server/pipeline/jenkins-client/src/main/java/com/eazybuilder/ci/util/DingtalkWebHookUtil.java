package com.eazybuilder.ci.util;

import java.net.URLEncoder;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.eazybuilder.ci.entity.MsgProfileType;
import com.eazybuilder.ci.entity.MsgType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.eazybuilder.ci.rabbitMq.SendRabbitMq;


public class DingtalkWebHookUtil {
    private static Logger logger = LoggerFactory.getLogger(DingtalkWebHookUtil.class);


    /**
     * 以消息队列的形式，发送钉钉消息
     *
     * @param title
     * @param markdownContent
     * @throws Exception
     */
    public static void sendDingtalkMsgBymq(String title, String markdownContent, String dingtalkSecret, String accessToken, SendRabbitMq sendRabbitMq, MsgProfileType msgProfileType) {

		JSONObject msgJson = new JSONObject();
		msgJson.put("title", title);
		msgJson.put("content", markdownContent);
		msgJson.put("dingtalkSecret", dingtalkSecret);
		msgJson.put("access_token", accessToken);
		msgJson.put("msgType", MsgType.ding);
		msgJson.put("msgProfile", msgProfileType);
		logger.info("发送钉钉消息：{}", msgJson.toJSONString());

		sendRabbitMq.publish(msgJson.toJSONString());

	}


    /**
     * 推送钉钉消息
     *
     * @param title           消息标题
     * @param markdownContent markdown正文
     * @param secret          密钥
     * @param webhook_url     webhook地址
     * @throws Exception
     */
    public static void sendDingtalkMsg(String title, String markdownContent, String secret, String webhook_url) throws Exception {

        long time = System.currentTimeMillis();
        String sign = getSignature(time, secret);

        DingtalkApiRequest request = new DingtalkApiRequest(title, markdownContent);
        JsonMapper mapper = JsonMapper.nonDefaultMapper();
        String response = HttpUtil.postJson(webhook_url + "&timestamp=" + time + "&sign=" + sign, mapper.toJson(request));
        logger.info(response);
    }

    private static String getSignature(long time, String secret) throws Exception {
        String stringToSign = time + "\n" + secret;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
        return URLEncoder.encode(new String(Base64.getEncoder().encode(signData)), "UTF-8");
    }

    private static class DingtalkApiRequest {
        public DingtalkApiRequest(String title, String text) {
            this.msgtype = "markdown";
            this.markdown = new MarkdownContent();
            this.markdown.setTitle(title);
            this.markdown.setText(text);
        }

        String msgtype;
        MarkdownContent markdown;

        public String getMsgtype() {
            return msgtype;
        }

        public void setMsgtype(String msgtype) {
            this.msgtype = msgtype;
        }

        public MarkdownContent getMarkdown() {
            return markdown;
        }

        public void setMarkdown(MarkdownContent markdown) {
            this.markdown = markdown;
        }
    }

    private static class MarkdownContent {
        String title;
        String text;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

    }
}
