package com.eazybuilder.ga.component.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.eazybuilder.ga.component.DingTalkComponent;
import com.eazybuilder.ga.component.config.DingTalkComponentConfig;
import com.eazybuilder.ga.constant.MsgProfileType;
import com.eazybuilder.ga.pojo.DingTalkMQPojo;
import com.eazybuilder.ga.pojo.DingTalkPrivateMQPojo;
import com.eazybuilder.ga.pojo.DingTalkText;
import com.eazybuilder.ga.pojo.PrivateContentPojo;
import com.eazybuilder.ga.service.SendDingMessage;
import com.eazybuilder.ga.untils.DingTalkUtil;
import com.eazybuilder.ga.untils.OkHttp3Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.List;
import java.util.Map;


@Component
public class DingTalkComponentImpl implements DingTalkComponent {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DingTalkComponentConfig dingTalkConfig; //导入钉钉配置

    @Autowired
    private SendDingMessage sendDingMessage;

    /**
     * 发送简单文本钉钉消息
     * @param content 文本信息
     * @return
     */
    @Override
    public boolean sendTextMessageDingTalk(String content){
        DingTalkText dingdingText = new DingTalkText();
        dingdingText.setIsAtAll(dingTalkConfig.isAtAll());//是否全员呼叫
        dingdingText.setAtMobiles(dingTalkConfig.getAtMobiles());//设置呼叫人
        dingdingText.setContent(content);//添加文本
        long time = System.currentTimeMillis();
        try {
            String getSignResult = DingTalkUtil.getSign(time, dingTalkConfig.getSign());//sign加工
            String url = dingTalkConfig.getUrl() + "&timestamp=" + time + "&sign=" + getSignResult;
            String json = dingdingText.getJSONObjectString();
            String postResult = OkHttp3Util.post(url, json, null);
            if (postResult==null){
                logger.info("钉钉消息发送失败,请检查服务器");
                return false;
            }
            JSONObject res = JSONObject.parseObject(postResult);
            boolean getIntValue=  res.getIntValue("errcode") == 0;
            if (getIntValue){
                logger.info("钉钉消息发送成功");
            }else{
                logger.info("钉钉消息发送失败");
            }
            return getIntValue;
        } catch (Exception e) {
            logger.info("钉钉消息发送异常");
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendTextMessageDingTalk(String title, String content, String secret, String webhook_url, List<String> mobileList) {

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
        try {
            String postResult = OkHttp3Util.post(webhook_url + "&timestamp=" + time + "&sign=" + sign, JSONObject.toJSONString(reqMap), null);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void sendDingMessageMQ(String title, String content, String secret,
                                  String webhook_url, List<String> mobileList,
                                  String groupName, MsgProfileType msgProfileType,
                                  String chatMethod) {

        DingTalkMQPojo dingTalkMQPojo = new DingTalkMQPojo();
        String[] temp = webhook_url.split("access_token=");
        dingTalkMQPojo.setTitle(title);
        dingTalkMQPojo.setContent(content);
        dingTalkMQPojo.setMsgType("ding");
        dingTalkMQPojo.setMsgProfile(msgProfileType);
        dingTalkMQPojo.setAccess_token(temp[1]);
        dingTalkMQPojo.setDingtalkSecret(secret);
        dingTalkMQPojo.setTeamCode(groupName);
        dingTalkMQPojo.setMobilelist(mobileList);
        dingTalkMQPojo.setChatMethods(chatMethod);
        sendDingMessage.sendDingMessage(dingTalkMQPojo);
    }

    public void sendDingPrivateMessageMQ(String title, String content, List<String> emailList,
                                         MsgProfileType msgProfileType, String chatMethod) {

    	logger.info("发送私聊钉钉消息,title:{},content:{},emailList:{}",title,content,emailList);
        DingTalkPrivateMQPojo dingTalkPrivateMQPojo = new DingTalkPrivateMQPojo();
        PrivateContentPojo privateContentPojo = new PrivateContentPojo();
        privateContentPojo.setText(content);
        privateContentPojo.setTitle(title);
        dingTalkPrivateMQPojo.setContent(privateContentPojo);
        dingTalkPrivateMQPojo.setMsgType("ding");
        dingTalkPrivateMQPojo.setMsgProfile(msgProfileType);
        dingTalkPrivateMQPojo.setEmailArray(emailList);

        dingTalkPrivateMQPojo.setChatMethods(chatMethod);
        sendDingMessage.sendDingPrivateMessage(dingTalkPrivateMQPojo);
    }

    private String getSignature(long time, String secret) throws Exception {
        String stringToSign = time + "\n" + secret;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
        return URLEncoder.encode(new String(Base64.getEncoder().encode(signData)), "UTF-8");
    }
}
