package com.eazybuilder.dm.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;


@Component
@Lazy(false)
public class MailSenderHelper {
	
	@Value("${spring.mail.from}")
	private String from;
	
	@Value("${spring.mail.subject}")
	private String defaultSubject;

    @Resource
    private JavaMailSender mailSender;
	


    //通过List去重
    public String[] duplicateRemoval(String[] arrStr) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < arrStr.length; i++) {
            if (!list.contains(arrStr[i])) {
                list.add(arrStr[i]);
            }
        }
   //返回一个包含所有对象的指定类型的数组
        return list.toArray(new String[]{});
    }
    /**
     *
     * @param toAddress 接收人
     * @param ccAddress 抄送人
     * @param contentHtml
     * @param subject 标题
     */
	public void sendMail(String[] toAddress, String[] ccAddress,
                         String contentHtml, String subject){
        toAddress = duplicateRemoval(toAddress);
        ccAddress = duplicateRemoval(ccAddress);
        //创建邮件
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(from);
            helper.setTo(toAddress);
            if(ccAddress!=null&&ccAddress.length>0){
            	helper.setCc(ccAddress);
            }
            helper.setSubject(subject==null?defaultSubject:subject);
            helper.setText(contentHtml,true);
            mailSender.send(mimeMessage);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
	

}
