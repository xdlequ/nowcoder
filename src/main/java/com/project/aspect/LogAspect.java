package com.project.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by ql on 2019/5/3.
 */
@Aspect
@Component
public class LogAspect {


    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);
    @Before("execution(* com.project.controller.*Controller.*(..))")
    public void beforeMethod(JoinPoint joinPoint){
        StringBuilder sb=new StringBuilder();
        for (Object arg:joinPoint.getArgs()){
            if (arg!=null){
                sb.append("arg:"+arg.toString()+"|");
            }
        }
        logger.info("before method"+sb.toString());
    }

    @After("execution(* com.project.controller.IndexController.*(..))")
    public void afterMethod(){
        logger.info("after mothod"+new Date());
    }
}
