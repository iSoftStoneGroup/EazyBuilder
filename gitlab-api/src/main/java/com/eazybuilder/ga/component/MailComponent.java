package com.eazybuilder.ga.component;

public interface MailComponent {
    //发送简单邮件
    boolean sendSimpleMail(String subject, String content);
    //发送html格式的邮件
    boolean sendHtmlMail(String subject, String content);
    //发送带附件的邮件
    boolean sendAttachmentsMail(String subject, String content, String filePath);
    //发送嵌入静态资源（一般是图片）的邮件
    boolean sendInlineResourceMail(String subject, String content, String rscPath, String rscId);
}
