package com.eazybuilder.ga.pojo;

import com.eazybuilder.ga.constant.MsgProfileType;

import java.io.Serializable;
import java.util.List;

public class DingTalkMQPojo implements Serializable {

    private static final long serialVersionUID = -1L;

    private String title;

    private String content;

    private String teamCode;

    private String msgType;

    private MsgProfileType msgProfile;

    private String access_token;

    private String dingtalkSecret;

    private List<String> mobilelist;

    private String chatMethods;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(String teamCode) {
        this.teamCode = teamCode;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public MsgProfileType getMsgProfile() {
        return msgProfile;
    }

    public void setMsgProfile(MsgProfileType msgProfile) {
        this.msgProfile = msgProfile;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getDingtalkSecret() {
        return dingtalkSecret;
    }

    public void setDingtalkSecret(String dingtalkSecret) {
        this.dingtalkSecret = dingtalkSecret;
    }

    public List<String> getMobilelist() {
        return mobilelist;
    }

    public void setMobilelist(List<String> mobilelist) {
        this.mobilelist = mobilelist;
    }

    public String getChatMethods() {
        return chatMethods;
    }

    public void setChatMethods(String chatMethods) {
        this.chatMethods = chatMethods;
    }
}
