package com.eazybuilder.ci.rabbitMq;

import com.eazybuilder.ci.controller.vo.ProjectBuildVo;
import com.eazybuilder.ci.entity.*;
import com.eazybuilder.ci.entity.pipeline.CIPackage;
import com.eazybuilder.ci.service.*;
import com.eazybuilder.ci.util.DingtalkWebHookUtil;
import com.eazybuilder.ci.util.JsonMapper;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import freemarker.template.Configuration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
public class ReceiveMsg {
    private static Logger logger = LoggerFactory.getLogger(ReceiveMsg.class);

    @Value("${message.queue}")
    public String ITEM_QUEUE;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    PipelineServiceImpl pipelineServiceImpl;
    @Autowired
    Configuration configuration;
    @Autowired
    CIPackageService ciPackageService;
    @Autowired
    DtpReportService dtpReportService;

    @Autowired
    DevopsInitServiceImpl devopsInitService;

    @Autowired
    PipelineServiceImpl pipelineService;

    @Resource
    PipelineLogService pipelineLogService;
    @Autowired
    private SendRabbitMq sendRabbitMq;

    @Resource
    private EventService eventService;


    @Value("${ci.dingtalk.url}")
    String dingtalkUrl;

    @Value("${ci.dingtalk.secret}")
    String dingtalkSecret;

    @Value("${ci.dingtalk.accessToken}")
    String accessToken;

    @Value("${message.dtpexchange}}")
    String broadcastExchange;

    @Value("${message.ciDataSyncExchange}}")
    String ciDataSyncExchange;

    @Bean
    public Queue queue() {
        return new Queue(ITEM_QUEUE, true);
    }

    @Bean
    public Broadcast broadcast() {
        return new Broadcast(broadcastExchange);
    }

    @Bean
    public Broadcast broadcastCiDataSync() {
        return new Broadcast(ciDataSyncExchange);
    }

    @RabbitListener(queues = "#{queue.name}")
    public void msg(Message message, Channel channel) throws Exception{
        Long tag = null;
        String payload = "";
        PipelineLog pipelineLog = new PipelineLog();
        try {
            MessageHeaders headers = message.getHeaders();

            logger.info("监听到消息:{}", JSON.toJSON(headers));
            // 消息唯一标记
            tag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
            // 这里的消息ID即为生产端所生成的消息UUID
            String msgId = headers.get("id").toString();

            JSONObject jsonObject = null;
            if (message.getPayload() instanceof byte[]) {
                payload = new String((byte[]) message.getPayload());
                JsonMapper mapper = new JsonMapper();
                Map map = mapper.fromJson(payload, Map.class);
                jsonObject = (JSONObject) JSON.toJSON(map);
            } else {
                logger.info("消息payload：{}", message.getPayload());
                jsonObject = JSONObject.parseObject(message.getPayload().toString());
            }


            logger.info("消费者收到消息了：{},msgId:{},tag:{}", jsonObject, msgId, tag);
            // 当redis中存在此消息ID时,说明此消息已让消费过(用于幂等性的处理)
            if (redisTemplate.opsForHash().entries("rancherMessageCache").containsKey(msgId)) {
                logger.info("[{}]消息已消费过", msgId);
                channel.basicAck(tag, false);
            }
            logger.info("[{}]消息没有被消费过，继续后续流程", msgId);
            redisTemplate.opsForHash().put("rancherMessageCache", msgId, msgId);

            CIPackage ciPackage = JSON.toJavaObject(jsonObject, CIPackage.class);
            ciPackage.setCreateDate(new Date());
            logger.info("保存CIPackage表：{}", JSON.toJSON(ciPackage));
            ciPackageService.save(ciPackage);
            // 手动确认消息已消费
            channel.basicAck(tag, false);
            jsonObject.put("ciPackageId", ciPackage.getId());

            Map<Project, List<ProjectBuildVo>> eventProject = eventService.getEventProject(jsonObject);
            pipelineServiceImpl.triggerPipeline(eventProject);
            pipelineLog.setExecuteType(ExecuteType.eventTrigger);
        } catch (Exception e) {
            logger.error("处理消息出现异常:{}", e);
            pipelineLog.setExceptionLog("流水线执行异常:" + e.getMessage());
            throw new IllegalArgumentException("事件设定匹配出现异常");
        } finally {
            try {
                channel.basicAck(tag, false);
            } catch (IOException e) {
                logger.error("手动确认消息已消费出现异常:{}", e);
            }
            //保存流水线日志，发送钉钉消息
            pipelineLogService.save(pipelineLog);
            String msg = "gitlab触发的流水线已执行完毕,pipelineLogId:" + pipelineLog.getId() + ",触发的事件类型:" + pipelineLog.getEventType();
            if (!StringUtils.isEmpty(pipelineLog.getExceptionLog())) {
                msg = msg + ",流水线出现异常:" + pipelineLog.getExceptionLog();
            }
            DingtalkWebHookUtil.sendDingtalkGroupMsgBymq("流水线通知", msg, dingtalkSecret, accessToken, sendRabbitMq, MsgProfileType.monitoringJobRun);
        }
    }

    @RabbitListener(bindings = {
            @QueueBinding(value = @org.springframework.amqp.rabbit.annotation.Queue(value = "ci.dtp.report"), exchange = @Exchange(type = ExchangeTypes.FANOUT, name = "devops.dtp.testTopic"))})
    public void dtpReport(Message message, Channel channel) {
        Long tag = null;

        String payload = "";
        JSONObject jsonObject = null;
        String result = null;
        try {
            MessageHeaders headers = message.getHeaders();
            logger.info("监听到dtp测试报告消息:{}", JSON.toJSONString(message));
            // 消息唯一标记
            tag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
            // 这里的消息ID即为生产端所生成的消息UUID
            String msgId = headers.get("id").toString();


            if (message.getPayload() instanceof byte[]) {
                payload = new String((byte[]) message.getPayload());
                JsonMapper mapper = new JsonMapper();
                Map map = mapper.fromJson(payload, Map.class);
                jsonObject = (JSONObject) JSON.toJSON(map);
            } else {
                logger.info("消息payload：{}", message.getPayload());
                jsonObject = JSON.parseObject(message.getPayload().toString());
            }


            logger.info("消费者收到消息了：{},msgId:{},tag:{}", jsonObject, msgId, tag);
            // 当redis中存在此消息ID时,说明此消息已让消费过(用于幂等性的处理)
            if (redisTemplate.opsForHash().entries("rancherMessageCache").containsKey(msgId)) {
                logger.info("[{}]消息已消费过", msgId);
                channel.basicAck(tag, false);
                return;
            }
            logger.info("[{}]消息没有被消费过，继续后续流程", msgId);
            redisTemplate.opsForHash().put("rancherMessageCache", msgId, msgId);
            //将dtp返回的测试报告单独保存起来
            result = dtpReportService.updateDtpRerport(jsonObject);
            // 手动确认消息已消费
            // channel.basicAck(tag, false);
        } catch (Exception e) {
            logger.error("处理消息出现异常:{}", e);
        } finally {
            try {
                channel.basicAck(tag, false);
            } catch (IOException e) {
                logger.error("手动确认消息已消费出现异常:{}", e);
            }
            //保存流水线日志，发送钉钉消息
            DingtalkWebHookUtil.sendDingtalkGroupMsgBymq("流水线通知", "CI收到dtp自动化测试结果:" + jsonObject.toString() + ",处理详情:" + result, dingtalkSecret, accessToken, sendRabbitMq, MsgProfileType.monitoringDtpTestStatus);
        }
    }

    @RabbitListener(bindings = {
            @QueueBinding(value = @org.springframework.amqp.rabbit.annotation.Queue(value = "ci.datasync.result.queues"), exchange = @Exchange(type = ExchangeTypes.FANOUT, name = "#{broadcastCiDataSync.exchange}"))})
    public void ciDatasyncResult(Message message, Channel channel) {
        Long tag = null;

        String payload = "";
        JSONObject jsonObject = null;
        String result = null;
        try {
            MessageHeaders headers = message.getHeaders();
            logger.info("监听到dtp测试报告消息:{}", JSON.toJSONString(message));
            // 消息唯一标记
            tag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
            // 这里的消息ID即为生产端所生成的消息UUID
            String msgId = headers.get("id").toString();


            if (message.getPayload() instanceof byte[]) {
                payload = new String((byte[]) message.getPayload());
                JsonMapper mapper = new JsonMapper();
                Map map = mapper.fromJson(payload, Map.class);
                jsonObject = (JSONObject) JSON.toJSON(map);
            } else {
                logger.info("消息payload：{}", message.getPayload());
                jsonObject = JSON.parseObject(message.getPayload().toString());
            }


            logger.info("消费者收到消息了：{},msgId:{},tag:{}", jsonObject, msgId, tag);
            // 当redis中存在此消息ID时,说明此消息已让消费过(用于幂等性的处理)
            if (redisTemplate.opsForHash().entries("rancherMessageCache").containsKey(msgId)) {
                logger.info("[{}]消息已消费过", msgId);
                channel.basicAck(tag, false);
                return;
            }
            logger.info("[{}]消息没有被消费过，继续后续流程", msgId);
            redisTemplate.opsForHash().put("rancherMessageCache", msgId, msgId);
            //将dtp返回的测试报告单独保存起来
            devopsInitService.ciDatasyncResult(jsonObject);
            // 手动确认消息已消费
            // channel.basicAck(tag, false);
        } catch (Exception e) {
            logger.error("处理消息出现异常:{}", e);
        } finally {
            try {
                channel.basicAck(tag, false);
            } catch (IOException e) {
                logger.error("手动确认消息已消费出现异常:{}", e);
            }
            //保存流水线日志，发送钉钉消息
            DingtalkWebHookUtil.sendDingtalkGroupMsgBymq("流水线通知", "CI收到dtp自动化测试结果:" + jsonObject.toString() + ",处理详情:" + result, dingtalkSecret, accessToken, sendRabbitMq, MsgProfileType.monitoringDtpTestStatus);
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
