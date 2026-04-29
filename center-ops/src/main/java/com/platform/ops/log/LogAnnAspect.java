package com.platform.ops.log;

import com.alibaba.nacos.shaded.com.google.gson.Gson;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
public class LogAnnAspect {
    private static final Logger log = LoggerFactory.getLogger(LogAnnAspect.class);

    @Pointcut("@annotation(com.platform.ops.log.LogAnn)")
    public void pc() {

    }

    @Around("pc()")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        //• @Around 环绕通知需要调 ProceedingJoinPoint.proceed() 来让原始方法执行,其他通知不需要考虑目标方法执行.
        //• @Around 环绕通知方法的返回值,必须指定为Object,来接收原始方法的返回值,否则原始方法执行完毕,是获取不到返回值的.


        LogAnn logAnn = ((MethodSignature) joinPoint.getSignature())
                .getMethod().getAnnotation(LogAnn.class);
        for (Object arg : joinPoint.getArgs()) {
            log.info(logAnn.key() + " input:" + arg);
        }


        long st = System.currentTimeMillis();
        Object output = joinPoint.proceed();  // 执行目标方法
        long runtime = System.currentTimeMillis() - st;

        log.info("output:" + output + " runtime:" + runtime);
    }
}
