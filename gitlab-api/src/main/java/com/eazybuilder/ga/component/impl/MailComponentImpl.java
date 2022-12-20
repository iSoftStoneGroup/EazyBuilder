package com.eazybuilder.ga.component.impl;

import com.eazybuilder.ga.component.MailComponent;
import com.eazybuilder.ga.component.config.MailComponentConfig;
import com.eazybuilder.ga.untils.MailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;


@Component
public class MailComponentImpl implements MailComponent {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JavaMailSender sender;

    @Autowired
    private MailComponentConfig mailConfig; //导入邮件配置

    /**
     * 发送纯文本的简单邮件
     * @param subject 主题
     * @param content 内容
     */
    public boolean sendSimpleMail(String subject, String content){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailConfig.getFrom());
        message.setTo(mailConfig.getTo());
        message.setSubject(subject);
        message.setText(content);

        try {
            sender.send(message);
            logger.info("简单邮件已经发送。");
            return true;
        } catch (Exception e) {
            logger.error("发送简单邮件时发生异常！", e);
            return false;
        }
    }

    /**
     * 发送html格式的邮件
     * @param subject 主题
     * @param content 内容
     */
    public boolean sendHtmlMail(String subject, String content){
        MimeMessage message = sender.createMimeMessage();

        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helperResultResult = MailUtil.newMimeMessage(message,mailConfig.getFrom(),mailConfig.getTo(),subject,content);
            sender.send(message);
            logger.info("html邮件已经发送。");
            return true;
        } catch (MessagingException e) {
            logger.error("发送html邮件时发生异常！", e);
            return false;
        }
    }

    /**
     * 发送带附件的邮件
     * @param subject 主题
     * @param content 内容
     * @param filePath 静态资源路径和文件名
     */
    public boolean sendAttachmentsMail(String subject, String content, String filePath){
        MimeMessage message = sender.createMimeMessage();

        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helperResult = MailUtil.newMimeMessage(message,mailConfig.getFrom(),mailConfig.getTo(),subject,content);

            FileSystemResource file = new FileSystemResource(new File(filePath));
            String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
            helperResult.addAttachment(fileName, file);

            sender.send(message);
            logger.info("带附件的邮件已经发送。");
            return true;
        } catch (MessagingException e) {
            logger.error("发送带附件的邮件时发生异常！", e);
            return false;
        }
    }

    /**
     * 发送嵌入静态资源（一般是图片）的邮件
     * @param subject 主题
     * @param content 邮件内容，需要包括一个静态资源的id，比如：<img src=\"cid:rscId01\" >
     * @param rscPath 静态资源路径和文件名
     * @param rscId 静态资源id
     */
    public boolean sendInlineResourceMail(String subject, String content, String rscPath, String rscId){
        MimeMessage message = sender.createMimeMessage();

        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helperResult = MailUtil.newMimeMessage(message,mailConfig.getFrom(),mailConfig.getTo(),subject,content);

            FileSystemResource res = new FileSystemResource(new File(rscPath));
            helperResult.addInline(rscId, res);

            sender.send(message);
            logger.info("嵌入静态资源的邮件已经发送。");
            return true;
        } catch (MessagingException e) {
            logger.error("发送嵌入静态资源的邮件时发生异常！", e);
            return false;
        }
    }
}
