package com.eazybuilder.ci.aspect;

import com.google.common.collect.Maps;
import com.eazybuilder.ci.entity.MsgProfileType;
import com.eazybuilder.ci.rabbitMq.SendRabbitMq;
import com.eazybuilder.ci.util.DingtalkWebHookUtil;
import com.eazybuilder.ci.util.FreemakerUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

@Aspect
@Component
public class AopAdvice {

    @Value("${errorDingTalk.emails}")
    private  String emails;

    @Resource
    SendRabbitMq sendRabbitMq;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static String dingTalkError = "### DevOps系统异常警告\n" +
            "\n" +
            "\n" +
            "- 项目名称： Ci(持续集成)；\n" +
            "\n" +
            "- 异常信息:   ${errorDetail}；\n";



    @Pointcut("execution (* com.eazybuilder.ci.aspect.ExceptionHander.*(..))")
    public void aopAdvice() {
        logger.error("进入了异常切面代码块");
    }

//    @Before("aodAdvice()")
//    public void beforeAdvice() {
//        System.out.println("beforeAdvice...");
//    }

    @After("aopAdvice()")
    public void afterAdvice(JoinPoint point) {
        logger.error("异常拦截器捕获到了异常，给DevOps小组人员发送钉钉提醒");
        Map<String, Object> models = Maps.newHashMap();
//        models.put("errorCode","1001");
        Exception arg = (Exception) (point.getArgs())[0];
//        if(arg instanceof CIException){
//            Throwable message = ((CIException) arg).getCause();
//        StackTraceElement[] stackTrace = arg.getCause().getStackTrace();
        StackTraceElement[] stackTrace = arg.getStackTrace();
        StringBuffer errorDetail = new StringBuffer();
        int errorLength = 30;
        if(stackTrace.length<30){
            errorLength=stackTrace.length;
        }
        for(int i = 0;i<errorLength;i++) {
            errorDetail.append(stackTrace[i].toString()).append("\r\n");
        }
        models.put("errorDetail", errorDetail);
        String[] receiverMailList = emails.split(",");

        DingtalkWebHookUtil.sendDingtalkPrivateMsgBymq("DevOps系统异常警告", FreemakerUtils.generateByModelAndTemplate(models, dingTalkError), "",
                Arrays.asList(receiverMailList), MsgProfileType.mergeApply,sendRabbitMq);
    }



}
