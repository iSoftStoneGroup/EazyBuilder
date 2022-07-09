package com.eazybuilder.ci.auth.methodAuthentication;


import com.eazybuilder.ci.constant.RoleEnum;
import com.eazybuilder.ci.entity.Role;
import com.eazybuilder.ci.entity.User;
import com.eazybuilder.ci.exception.CIException;
import com.eazybuilder.ci.util.AuthUtils;
import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Aspect
@Component
@Order(-10)
public class MethodAuthenticationAspect {

    private static Logger logger = LoggerFactory.getLogger(MethodAuthenticationAspect.class);


    @Pointcut("@annotation(authentication)")
    public void AuthenticationService(HaveRole authentication) {
    }

    @Around("AuthenticationService(authentication)")
    public Object doAround(ProceedingJoinPoint joinPoint, HaveRole authentication) throws Throwable{
        final List<RoleEnum> accessRoleEnums = Arrays.asList(authentication.roleEnums());
        User user = AuthUtils.getCurrentUser();
        List<RoleEnum> allowRoles = null;
        if(user!=null){
            List<RoleEnum> enumList =  user.getRoles().stream().map(Role::getRoleEnum).collect(Collectors.toList());
            //取交集
            allowRoles = enumList.stream().filter(roleEnum -> accessRoleEnums.contains(roleEnum)).collect(Collectors.toList());
        }
        if(allowRoles== null || CollectionUtils.isEmpty(allowRoles)){
            throw new CIException(HttpStatus.FORBIDDEN.value(),"角色权限不足,无法访问！");
        }
        return joinPoint.proceed();
    }







}