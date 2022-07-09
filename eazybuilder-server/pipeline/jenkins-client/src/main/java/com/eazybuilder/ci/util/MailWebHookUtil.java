package com.eazybuilder.ci.util;

import com.alibaba.fastjson.JSONObject;
import com.eazybuilder.ci.entity.MsgProfileType;
import com.eazybuilder.ci.entity.MsgType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MailWebHookUtil {
	private static Logger logger=LoggerFactory.getLogger(MailWebHookUtil.class);

    /**
     *
	 * @param title 标题
	 * @param toAddress 接收人
	 * @param ccAddress 抄送人
     * @param contentHtml 正文
     * @throws Exception
	 */
	public static JSONObject getSendMailMsg(String title, String[] toAddress, String[] ccAddress, String contentHtml, MsgProfileType profile,String teamCode) {

		JSONObject msgJson = new JSONObject();

		msgJson.put("title", title);
		msgJson.put("toAddress", toAddress);
		msgJson.put("ccAddress", ccAddress);
		msgJson.put("content", contentHtml);
		msgJson.put("msgType", MsgType.mail);
		msgJson.put("msgProfile", profile);
		msgJson.put("teamCode",teamCode);
		logger.info("发送邮件消息：{}", msgJson.toJSONString());
		return msgJson;
	}

}
