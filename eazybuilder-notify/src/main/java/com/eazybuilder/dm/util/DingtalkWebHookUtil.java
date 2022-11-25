package com.eazybuilder.dm.util;

import java.net.URLEncoder;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;



public class DingtalkWebHookUtil {
	private static Logger logger=LoggerFactory.getLogger(DingtalkWebHookUtil.class);

	/**
	 * 推送钉钉消息
	 * @param title 消息标题
	 * @param markdownContent markdown正文
	 * @param secret 密钥
	 * @param webhook_url webhook地址
	 * @throws Exception
	 */
	public static void sendDingtalkMsg(String title,String markdownContent,String secret,String webhook_url) throws Exception {
		
		long time=System.currentTimeMillis();
		String sign=getSignature(time, secret);
		
		DingtalkApiRequest request=new DingtalkApiRequest(title, markdownContent);
		JsonMapper mapper=JsonMapper.nonDefaultMapper();
		String response= null;
		try {
			response = HttpUtil.postJson(webhook_url+"&timestamp="+time+"&sign="+sign, mapper.toJson(request));
		}catch (Exception e) {
			logger.error("调用钉钉接口出现异常：{}{}",e.getMessage(),e);
			Thread.sleep(5 * 1000);
			logger.info("等待5秒后重新调用钉钉接口");
			response = HttpUtil.postJson(webhook_url+"&timestamp="+time+"&sign="+sign, mapper.toJson(request));
		}
		logger.info(response);
	}
	
	
    public static boolean sendTextMessageDingTalk(String title, String content, String secret, String webhook_url, List<String> mobileList) {

        long time = System.currentTimeMillis();
        String sign = null;
        try {
            sign = getSignature(time, secret);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        //消息内容
        Map<String, String> contentMap = Maps.newHashMap();
        contentMap.put("content", content);
        //通知人
        Map<String, Object> atMap = Maps.newHashMap();
        //1.是否通知所有人
        atMap.put("isAtAll", false);
        //2.通知具体人的手机号码列表
        atMap.put("atMobiles", mobileList);

        Map<String, Object> reqMap = Maps.newHashMap();
        reqMap.put("msgtype", "text");
        reqMap.put("text", contentMap);
        reqMap.put("at", atMap);
        JsonMapper mapper=JsonMapper.nonDefaultMapper();
        try {
            String postResult = HttpUtil.postJson(webhook_url + "&timestamp=" + time + "&sign=" + sign, mapper.toJson(reqMap));
            logger.info("发送钉钉消息结果:{}",postResult);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
	
	
	
	private static String getSignature(long time,String secret) throws Exception{
		String stringToSign = time + "\n" + secret;
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
		byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
		return URLEncoder.encode(new String(Base64.getEncoder().encode(signData)),"UTF-8");
	}

	private static class DingtalkApiRequest{
		public DingtalkApiRequest(String title,String text) {
			this.msgtype="markdown";
			this.markdown=new MarkdownContent();
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
	
	private static class MarkdownContent{
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
