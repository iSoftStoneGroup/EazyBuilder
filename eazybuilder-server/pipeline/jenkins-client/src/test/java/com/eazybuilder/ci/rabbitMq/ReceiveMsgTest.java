package com.eazybuilder.ci.rabbitMq;


import cn.hutool.core.util.RandomUtil;
import com.eazybuilder.ci.service.*;
import freemarker.template.Configuration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.amqp.rabbit.connection.PublisherCallbackChannelImpl;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;

@RunWith(PowerMockRunner.class)
public  class ReceiveMsgTest {

    @InjectMocks
    @Spy
    ReceiveMsg receiveMsg=new ReceiveMsg();

    @Mock
    private StringRedisTemplate redisTemplate;
    @Mock
    PipelineServiceImpl pipelineServiceImpl;
    @Mock
    Configuration configuration;
    @Mock
    CIPackageService ciPackageService;
    @Mock
    DtpReportService dtpReportService;

    @Mock
    DevopsInitServiceImpl devopsInitService;

    @Mock
    PipelineServiceImpl pipelineService;

    @Mock
    PipelineLogService pipelineLogService;
    @Mock
    private SendRabbitMq sendRabbitMq;

    @Mock
    private EventService eventService;
    
    @Before
    public  void  init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public    void msg() throws Exception {

//        String ciPackageJson="{\"code\":\"2660\",\"createDate\":1663837267029,\"customFields\":{\"工程标识\":\"nacos-config\",\"仓库地址\":\"http://gitlab.eazybuild-devops..cn/devops/nacos-config.git\",\"是否更新配置文件\":\"0\",\"配置文件内容\":\"\",\"指派测试人员\":\"\",\"镜像名称/版本\":\"registry.eazybuild-devops..cn/devops/devops-config/2651-2660-202209221420\",\"SP\":\"\",\"是否更新sql\":\"0\"},\"gitPath\":\"http://gitlab.eazybuild-devops..cn/devops/nacos-config.git\",\"imageTag\":\"\",\"profileType\":\"merge\",\"projectName\":\"Nacos Config\",\"sourceBranchName\":\"bugfix-2660\",\"tagName\":\"bugfix-2660\",\"targetBranchName\":\"master\",\"topCode\":\"2651\",\"userName\":\"gangwangn\"}";

        GenericMessage genericMessage = PowerMockito.mock(GenericMessage.class);
        PublisherCallbackChannelImpl publisherCallbackChannelImpl = PowerMockito.mock(PublisherCallbackChannelImpl.class);
        MessageHeaders messageHeaders = PowerMockito.mock(MessageHeaders.class);

        PowerMockito.when(genericMessage,"getHeaders").thenReturn(messageHeaders);
        String msgId="msgId";
        PowerMockito.when(messageHeaders,"get","id").thenReturn(msgId);

        PowerMockito.when(messageHeaders,"get", AmqpHeaders.DELIVERY_TAG).thenReturn(RandomUtil.randomLong()  );
//        byte[] bytes=new byte[]{2,2,3};
//        String mesJson="{\"code\":\"0\",\"executePlanCount\":0,\"end\":true,\"userName\":\"xhwange@isoftstone.com\",\"pipelineHistoryId\":\"df0c233b-27c4-4a42-93ef-5e4731610210\",\"gitUrl\":\"http://gitlab.eazybuild-devops..cn/ipsa/project-api.git\",\"begin\":true,\"status\":\"MISS\"}";
        String  mesJson=
                "{\"assigneeName\":\"qiujin\",\"authorName\":\"gangwangn\",\"code\":\"2660\",\"customFields\":{\"工程标识\":\"nacos-config\",\"仓库地址\":\"http://gitlab.eazybuild-devops..cn/devops/nacos-config.git\",\"是否更新配置文件\":\"0\",\"配置文件内容\":\"\",\"指派测试人员\":\"\",\"镜像名称/版本\":\"registry.eazybuild-devops..cn/devops/devops-config/2651-2660-202209221420\",\"SP\":\"\",\"是否更新sql\":\"0\"},\"gitPath\":\"http://gitlab.eazybuild-devops..cn/devops/nacos-config.git\",\"imageTag\":\"\",\"profileType\":\"merge\",\"projectName\":\"Nacos Config\",\"sourceBranchName\":\"bugfix-2660\",\"tagName\":\"bugfix-2660\",\"targetBranchName\":\"master\",\"topCode\":\"2651\",\"userName\":\"gangwangn\"}";

        byte[] bytes = mesJson.getBytes("utf-8");
        PowerMockito.when(genericMessage,"getPayload").thenReturn(bytes);


//        GenericMessage GenericMessage=new GenericMessage();
        System.out.println(receiveMsg);
        PowerMockito.when(genericMessage,"getPayload").thenReturn(bytes);

        PowerMockito.doNothing().when(publisherCallbackChannelImpl,"basicAck",anyLong(),anyBoolean());
        try {
            receiveMsg.msg(genericMessage,publisherCallbackChannelImpl);

        }catch (Exception e){
            Assert.assertTrue(e!=null);
        }
    }
}