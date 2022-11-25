package com.eazybuilder.dm.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eazybuilder.dm.constant.MsgType;
import com.eazybuilder.dm.controller.ReceiveMsg;
import com.eazybuilder.dm.entity.Team;
import com.eazybuilder.dm.entity.User;
import com.eazybuilder.dm.util.DingtalkWebHookUtil;
import com.eazybuilder.dm.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MsgServiceImpl {

    private static Logger logger = LoggerFactory.getLogger(MsgServiceImpl.class);

    @Value("${dingtalk.appKey}")
    public String appKey;
    @Value("${dingtalk.appSecret}")
    public String appSecret;
    @Value("${dingtalk.getTokenUrl}")
    public String getTokenUrl;
    @Value("${dingtalk.getUserIdUrl}")
    public String getUserIdUrl;
    @Value("${dingtalk.batchSendUrl}")
    public String batchSendUrl;
    @Value("${dingtalk.robotCode}")
    public String robotCode;
    @Value("${dingtalk.url}")
    public String dingtalkUrl;
    @Resource
    MailSenderHelper mailSenderHelper;
    @Resource
    UserServiceImpl userService;

    @Autowired
    private RestTemplate restTemplate;
    @Resource
    TeamServiceImpl teamService;

    public boolean matchMsg(JSONObject jsonObject) {
        try {
            if (jsonObject.containsKey("teamCode")) {
                String teamCode = jsonObject.getString("teamCode");
                Team team = teamService.findByTeamCode(teamCode);
                String msgType = jsonObject.getString("msgType");
                String msgProfile = jsonObject.getString("msgProfile");
                if (msgType.equals(MsgType.ding.toString())) {
                    net.sf.json.JSONObject jsonDingProfile = net.sf.json.JSONObject.fromObject(team.getDingMsgProfile());
                    if(jsonDingProfile.containsKey(msgProfile)){
                        return jsonDingProfile.getBoolean(msgProfile);
                    }else {
                        return true;
                    }
                } else if (msgType.equals(MsgType.mail.toString())) {
                    net.sf.json.JSONObject jsonMailProfile = net.sf.json.JSONObject.fromObject(team.getMailMsgProfile());
                    if(jsonMailProfile.containsKey(msgProfile)) {
                        return jsonMailProfile.getBoolean(msgProfile);
                    }else{
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            logger.info("匹配消息精准推送出现异常：{}", e.getMessage(), e);
            return true;
        }
        return true;
    }

    public void dingMsg(JSONObject messageJson) throws Exception {
        if (messageJson.containsKey("chatMethods") && messageJson.getString("chatMethods").equals("private")) {
            logger.info("钉钉消息按照私聊方式通知");
            sendDingPrivateMsg(messageJson);
        } else {
            logger.info("钉钉消息按照群聊方式通知");
            sendDingGroupMsg(messageJson);
        }
    }
    public void sendDingPrivateMsg(JSONObject messageJson) throws Exception {
        String content = "";
        if (messageJson.containsKey("markdownContent")) {
            content = messageJson.getString("markdownContent");
        } else if (messageJson.containsKey("content")) {
            content = messageJson.getString("content");
        }
        //1.获取企业内部应用的 access_token 。调用服务端API获取应用资源时，需要通过access_token来鉴权调用者身份进行授权。
        String getTokenData = HttpUtil.get(getTokenUrl);
        String access_token = JSONObject.parseObject(getTokenData).getString("access_token");
        //2.根据access_token和手机号查询用户userid
        List<String> userIdList = new ArrayList<>();
        JSONArray emailArray = messageJson.getJSONArray("emailArray");
        emailArray.forEach(jsonObj->{
            String email=(String)jsonObj;
            Integer employeeIdByEmail = userService.getEmployeeIdByEmail(email);
            userIdList.add(employeeIdByEmail.toString());
        });

        //3.根据userId发送消息
        JSONObject requestJson = new JSONObject();
        requestJson.put("robotCode", robotCode);
        requestJson.put("userIds", userIdList.toArray());
        requestJson.put("msgKey", "sampleMarkdown");
        requestJson.put("msgParam", content);
        HttpHeaders headers = new HttpHeaders();

        headers.set("x-acs-dingtalk-access-token", access_token);
        try {
            restTemplate.postForEntity(batchSendUrl, new HttpEntity<>(requestJson, headers), JSONObject.class);
        } catch (RestClientException e) {
            logger.error("调用钉钉接口出现异常：{}{}",e.getMessage(),e);
            Thread.sleep(5 * 1000);
            logger.info("等待5秒后重新调用钉钉接口");
            restTemplate.postForEntity(batchSendUrl, new HttpEntity<>(requestJson, headers), JSONObject.class);
        }
    }
    public void sendDingGroupMsg(JSONObject messageJson) throws Exception {

        String content = "";
        if (messageJson.containsKey("markdownContent")) {
            content = messageJson.getString("markdownContent");
        } else if (messageJson.containsKey("content")) {
            content = messageJson.getString("content");
        }
        String url = "";
        Team team = null;
        try {
            if (messageJson.containsKey("teamCode")) {
                team = teamService.findByTeamCode(messageJson.getString("teamCode"));
            }
            if (!messageJson.containsKey("access_token")) {
                url = team.getDingWebHookUrl();
            } else {
                url = dingtalkUrl + "?access_token=" + messageJson.getString("access_token");
            }
            if (!messageJson.containsKey("dingtalkSecret")) {
                messageJson.put("dingtalkSecret", team.getDingSecret());
            }
        } catch (Exception e) {
            logger.info("根据项目组编号查询数据出现异常" + e.getMessage(), e);
        }
        logger.info("钉钉api url：{}", url);

        if (messageJson.containsKey("mobilelist")) {
            JSONArray mobilelist = messageJson.getJSONArray("mobilelist");
            mobilelist.removeAll(Collections.singleton(null));
            if (null != mobilelist && !mobilelist.isEmpty()) {
                logger.info("发送消息精准提醒到人:{}", mobilelist);
                List<String> mobiles = new ArrayList<>();
                mobilelist.forEach(mobile -> {
                    mobiles.add(mobile.toString());
                });
                try {
                    DingtalkWebHookUtil.sendTextMessageDingTalk(messageJson.getString("title"), content, messageJson.getString("dingtalkSecret"), url, mobiles);
                } catch (Exception e) {
                    logger.error("调用钉钉接口出现异常：{}{}",e.getMessage(),e);
                    Thread.sleep(5 * 1000);
                    logger.info("等待5秒后重新调用钉钉接口");
                    DingtalkWebHookUtil.sendTextMessageDingTalk(messageJson.getString("title"), content, messageJson.getString("dingtalkSecret"), url, mobiles);
                }
            } else {
                DingtalkWebHookUtil.sendDingtalkMsg(messageJson.getString("title"), content, messageJson.getString("dingtalkSecret"), url);
            }
        } else {
            DingtalkWebHookUtil.sendDingtalkMsg(messageJson.getString("title"), content, messageJson.getString("dingtalkSecret"), url);
        }

    }
    public void mailMsg(JSONObject messageJson) {
        String[] toAddress = new String[]{};
        String[] ccAddress = new String[]{};
        if (messageJson.containsKey("toAddress")) {
            JSONArray toAddressJsonArray = messageJson.getJSONArray("toAddress");
            if (!toAddressJsonArray.isEmpty()) {
                toAddress = toAddressJsonArray.toArray(new String[]{});
            }
        }
        if (messageJson.containsKey("ccAddress")) {
            JSONArray ccAddressJsonArray = messageJson.getJSONArray("ccAddress");
            if (!ccAddressJsonArray.isEmpty()) {
                ccAddress = ccAddressJsonArray.toArray(new String[]{});
            }
        }
        String contentHtml = messageJson.getString("content");
        String subject = messageJson.getString("title");
        mailSenderHelper.sendMail(toAddress, ccAddress, contentHtml, subject);
    }

}
