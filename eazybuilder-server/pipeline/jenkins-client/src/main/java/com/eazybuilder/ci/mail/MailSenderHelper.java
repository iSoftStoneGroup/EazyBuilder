package com.eazybuilder.ci.mail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import com.alibaba.fastjson.JSONObject;
import com.eazybuilder.ci.constant.RoleEnum;
import com.eazybuilder.ci.entity.MsgProfileType;
import com.eazybuilder.ci.rabbitMq.SendRabbitMq;
import com.eazybuilder.ci.util.MailWebHookUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.eazybuilder.ci.entity.Role;
import com.eazybuilder.ci.entity.Team;
import com.eazybuilder.ci.entity.User;


@Component
@Lazy(false)
public class MailSenderHelper {
	
	@Value("${spring.mail.from}")
	private String from;
	
	@Value("${spring.mail.subject}")
	private String defaultSubject;

	@Resource
    SendRabbitMq sendRabbitMq;

	@Autowired
    private JavaMailSender mailSender;
	
	
	public String[] getReceiverMailList(Team team){
		List<String> emails=Lists.newArrayList();
		team.getMembers().forEach(member->{
            List<Role> roles = member.getRoles();
            if(Role.existRole(roles,RoleEnum.admin)){
				emails.add(member.getEmail());
			}
		});
		
		return emails.toArray(new String[0]);
	}
	
	public String[] getReceiverMailList(List<User> users){
		List<String> emails=Lists.newArrayList();
		for(int i=0;i<users.size();i++){
			emails.add(users.get(i).getEmail());
		}
		return emails.toArray(new String[0]);
	}
	
	public String[] getCCMailList(Team team){
		List<String> emails=Lists.newArrayList();
		team.getMembers().forEach(member->{
            List<Role> roles = member.getRoles();
            if(!Role.existRole(roles,RoleEnum.admin) || !Role.existRole(roles,RoleEnum.teamleader)){
				emails.add(member.getEmail());
			}
		});
		
		return emails.toArray(new String[0]);
	}

    //通过List去重
    public String[] duplicateRemoval(String[] arrStr) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < arrStr.length; i++) {
            if (!list.contains(arrStr[i])) {
                list.add(arrStr[i]);
            }
        }
        //返回一个包含所有对象的指定类型的数组
        return list.toArray(new String[1]);
    }
    /**
     *
     * @param toAddress 接收人
     * @param ccAddress 抄送人
     * @param contentHtml
     * @param subject 标题
     */
	public void sendMail(String[] toAddress, String[] ccAddress,
                         String contentHtml, String subject, MsgProfileType profile,String teamCode) {
        toAddress = duplicateRemoval(toAddress);
        ccAddress = duplicateRemoval(ccAddress);
        //创建邮件
        JSONObject sendMailMsg = MailWebHookUtil.getSendMailMsg(subject, toAddress, ccAddress, contentHtml,profile,teamCode);
        sendRabbitMq.publish(sendMailMsg.toJSONString());
    }
	
	public void sendMail(String[] toAddress,String[] ccAddress,
			String contentHtml, String subject,String attachmentName,File attachment){
		
        //创建邮件
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(from);
            helper.setTo(toAddress);
            if(ccAddress!=null&&ccAddress.length>0){
            	helper.setCc(ccAddress);
            }
            helper.setSubject(subject);
            helper.setText(contentHtml,true);
            helper.addAttachment(attachmentName, attachment);
            mailSender.send(mimeMessage);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

	public void sendMail(String[] toAddress, String[] ccAddress,
			String contentHtml, String subject,String attachmentName,ByteArrayResource attachment){
		
        //创建邮件
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(from);
            helper.setTo(toAddress);
            if(ccAddress!=null&&ccAddress.length>0){
            	helper.setCc(ccAddress);
            }
            helper.setSubject(subject);
            helper.setText(contentHtml,true);
            if(attachment!=null){
            	helper.addAttachment(attachmentName,attachment);
            }
            mailSender.send(mimeMessage);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
