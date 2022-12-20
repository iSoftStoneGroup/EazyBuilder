package com.eazybuilder.ga.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eazybuilder.ga.pojo.DingTalkHookPojo;
import com.eazybuilder.ga.pojo.GitLabPojo;
import com.eazybuilder.ga.pojo.GitLabUserPojo;
import com.eazybuilder.ga.pojo.InitResultPojo;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class InitReceiveMessage {

    private static Logger logger = LoggerFactory.getLogger(InitReceiveMessage.class);

    @Autowired
    private SettingService settingService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    // 广播交换机名称
    @Value("${message.broadcastExchange}")
    public String broadcastExchange;

    @Autowired
    SendInitMessage sendInitMessage;

    @Bean
    public Broadcast broadcast() {
        return new Broadcast(broadcastExchange);
    }

    @RabbitListener(bindings = {
            @QueueBinding(value = @org.springframework.amqp.rabbit.annotation.Queue(value = "gitlab.datasync.queue"), exchange = @Exchange(type = ExchangeTypes.FANOUT, name = "#{broadcast.exchange}")) })
    public void broadcastMsg(Message message, Channel channel) throws IOException {

        MessageHeaders headers = message.getHeaders();
        Long tag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG); // 消息唯一标记
        String msgId = "";
        if (null != headers.get("spring_returned_message_correlation")) {
            msgId = headers.get("spring_returned_message_correlation").toString(); // 这里的消息ID即为生产端所生成的消息UUID
        }else{
            msgId = headers.get("id").toString();
        }

        logger.info("消费者收到广播消息了：{},msgId:{},tag:{}", message.getPayload(), msgId, tag);

        // 当redis中存在此消息ID时,说明此消息已让消费过(用于幂等性的处理)
        if (redisTemplate.opsForHash().entries("gitlabMessageCache").containsKey(msgId)) {
            logger.info("[{}]消息已消费过", msgId);
            channel.basicAck(tag, false); // 手动确认消息已消费
            return;
        }

        logger.info("[{}]广播消息没有被消费过，继续后续流程", msgId);

        InitResultPojo initResultPojo = new InitResultPojo();
        try{
            // 当redis中存在此消息ID时,说明此消息已让消费过(用于幂等性的处理)
            if (redisTemplate.opsForHash().entries("gitlabMessageCache").containsKey(msgId)) {
                logger.info("[{}]消息已消费过", msgId);
                channel.basicAck(tag, false); // 手动确认消息已消费
            }

            logger.info("[{}]消息没有被消费过，继续后续流程,Payload type:{}", msgId,message.getPayload().getClass());

            JSONObject json;
            if (message.getPayload() instanceof String) {
                json = JSONObject.parseObject(String.valueOf(message.getPayload()));
            }else {
                json = (JSONObject) JSONObject.toJSON(message.getPayload());
            }

            GitLabPojo gitLabPojo = new GitLabPojo();
            String teamCode = json.getString("teamCode");
            String id = json.getString("id");


            initResultPojo.setId(id);
            initResultPojo.setProjectCode("gitlab");
            initResultPojo.setTeamCode(teamCode);

            gitLabPojo.setGroupPath(teamCode);

            DingTalkHookPojo dingTalkHookPojo = new DingTalkHookPojo();
            dingTalkHookPojo.setSign(json.getString("dingSecret"));
            dingTalkHookPojo.setWebhook(json.getString("dingWebHookUrl"));
            redisTemplate.opsForHash().put(teamCode.toLowerCase(), teamCode.toLowerCase(), JSONObject.toJSONString(dingTalkHookPojo));


            List<String> projectList = new ArrayList<String>();
            List<GitLabUserPojo> groupAddUserEmails = new ArrayList<GitLabUserPojo>();

            JSONArray userJsonArray = json.getJSONArray("devopsUsers");
            JSONArray projectJsonArray = json.getJSONArray("devopsProjects");

            for(int i=0;i<userJsonArray.size();i++){
                JSONObject userJsonObject = userJsonArray.getJSONObject(i);
                GitLabUserPojo gitLabUserPojo = new GitLabUserPojo();

                String mail = userJsonObject.getString("email");
                String phoneNumber = userJsonObject.getString("phoneNumber");
                String login = mail.split("@")[0];
                redisTemplate.opsForHash().put(login, login, phoneNumber);


                gitLabUserPojo.setAdmin(false);
                gitLabUserPojo.setEmail(mail);
                gitLabUserPojo.setIsAddProject(true);
                gitLabUserPojo.setUsername(login);
                gitLabUserPojo.setPassword("isoftstone");

                groupAddUserEmails.add(gitLabUserPojo);
            }

            for(int i=0;i<projectJsonArray.size();i++){
                JSONObject projectJsonObject = userJsonArray.getJSONObject(i);
                projectList.add(projectJsonObject.getString("description"));
            }

            gitLabPojo.setProjectList(projectList);
            gitLabPojo.setGroupAddUserEmails(groupAddUserEmails);

            String result = settingService.gitLabSetting(gitLabPojo);
            if(StringUtils.isNotBlank(result)){
                initResultPojo.setStatus("FAILED");
            }else{
                initResultPojo.setStatus("SUCCESS");
            }
            logger.info(result);

            redisTemplate.opsForHash().put("gitlabMessageCache", msgId, msgId);
            channel.basicAck(tag, false); // 手动确认消息已消费
        }catch (Exception e){
            e.printStackTrace();
            channel.basicAck(tag, false); // 手动确认消息已消费
            initResultPojo.setStatus("FAILED");
        }finally {
            sendInitMessage.sendInitResultMessage(initResultPojo);
        }
    }

    class Broadcast {
        private final String exchange;

        Broadcast(String exchange) {
            this.exchange = exchange;
        }

        public String getExchange() {
            return this.exchange;
        }
    }

}
