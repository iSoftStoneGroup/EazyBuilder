package com.eazybuilder.ga.component;

import com.eazybuilder.ga.constant.MsgProfileType;

import java.util.List;

public interface DingTalkComponent {
    boolean sendTextMessageDingTalk(String text);

    boolean sendTextMessageDingTalk(String title, String content, String secret, String webhook_url, List<String> mobileList);

    public void sendDingMessageMQ(String title, String content, String secret, String webhook_url, List<String> mobileList,
                                  String groupName, MsgProfileType msgProfileType, String chatMethod);

    public void sendDingPrivateMessageMQ(String title, String content, List<String> emailList,
                                         MsgProfileType msgProfileType, String chatMethod);
}
