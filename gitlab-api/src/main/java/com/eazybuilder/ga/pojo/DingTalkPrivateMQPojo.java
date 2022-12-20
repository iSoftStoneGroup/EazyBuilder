package com.eazybuilder.ga.pojo;

import com.eazybuilder.ga.constant.MsgProfileType;

import java.io.Serializable;
import java.util.List;

public class DingTalkPrivateMQPojo implements Serializable {

    private static final long serialVersionUID = -1L;

    private PrivateContentPojo content;

    private String msgType;

    private MsgProfileType msgProfile;

    private List<String> emailArray;

    private String chatMethods;

    public PrivateContentPojo getContent() {
        return content;
    }

    public void setContent(PrivateContentPojo content) {
        this.content = content;
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

    public List<String> getEmailArray() {
        return emailArray;
    }

    public void setEmailArray(List<String> emailArray) {
        this.emailArray = emailArray;
    }

    public String getChatMethods() {
        return chatMethods;
    }

    public void setChatMethods(String chatMethods) {
        this.chatMethods = chatMethods;
    }
}
