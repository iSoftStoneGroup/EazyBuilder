package com.eazybuilder.ci;

import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.eazybuilder.ci.entity.SysLog;
import com.eazybuilder.ci.entity.User;
import com.eazybuilder.ci.service.SysLogService;
import com.eazybuilder.ci.util.AuthUtils;

import cn.hutool.extra.servlet.ServletUtil;


/**
 * 切面处理类，操作日志异常日志记录处理
 *
 */
@Aspect
@Component
public class SysLogAspect {


    @Autowired
    private SysLogService operationLogService;


    /**
     * 设置操作日志切入点 记录操作日志 在注解的位置切入代码
     */
    @Pointcut("@annotation(com.eazybuilder.ci.OperLog)")
    public void operLogPoinCut() {
    }


    /**
     * 正常返回通知，拦截用户操作日志，连接点正常执行完成后执行， 如果连接点抛出异常，则不会执行
     *
     * @param joinPoint 切入点
     * @param keys      返回结果
     */
    @AfterReturning(value = "operLogPoinCut()", returning = "keys")
    public void saveOperLog(JoinPoint joinPoint, Object keys) {
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes
                .resolveReference(RequestAttributes.REFERENCE_REQUEST);

        SysLog operlog = new SysLog();
        try {
            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            // 获取切入点所在的方法
            Method method = signature.getMethod();
            // 获取操作
            OperLog opLog = method.getAnnotation(OperLog.class);
            if (opLog != null) {
                String operModul = opLog.module();
                String operType = opLog.opType();
                String operDesc = opLog.opDesc();
                operlog.setModule(operModul);
                operlog.setOpType(operType);
                operlog.setOpDesc(operDesc);
            }

            operlog.setHttpMethod(request.getMethod());
            operlog.setAccessTime(new Date());
            operlog.setAccessUrl(request.getRequestURI());
            operlog.setRemoteAddr(ServletUtil.getClientIP(request));
            operlog.setOpResult("success");
            User user=AuthUtils.getCurrentUser();
            if(user!=null) {
            	operlog.setUserId(user.getId());
            	operlog.setUserName(user.getName());
            }
            operationLogService.save(operlog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 异常返回通知，用于拦截异常日志信息 连接点抛出异常后执行
     *
     * @param joinPoint 切入点
     * @param e         异常信息
     */
    @AfterThrowing(pointcut = "operLogPoinCut()", throwing = "e")
    public void saveExceptionLog(JoinPoint joinPoint, Throwable e) {
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes
                .resolveReference(RequestAttributes.REFERENCE_REQUEST);

        SysLog operlog = new SysLog();
        try {
            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            // 获取切入点所在的方法
            Method method = signature.getMethod();
            // 获取操作
            OperLog opLog = method.getAnnotation(OperLog.class);
            if (opLog != null) {
                String operModul = opLog.module();
                String operType = opLog.opType();
                String operDesc = opLog.opDesc();
                operlog.setModule(operModul);
                operlog.setOpType(operType);
                operlog.setOpDesc(operDesc);
            }

            operlog.setHttpMethod(request.getMethod());
            operlog.setAccessTime(new Date());
            operlog.setAccessUrl(request.getRequestURI());
            operlog.setRemoteAddr(ServletUtil.getClientIP(request));
            User user=AuthUtils.getCurrentUser();
            if(user!=null) {
            	operlog.setUserId(user.getId());
            	operlog.setUserName(user.getName());
            }
            operlog.setOpResult(e.getMessage());
            operationLogService.save(operlog);
        } catch (Exception e2) {
            e2.printStackTrace();
        }

    }

}