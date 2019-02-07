package com.seungwoo.controller;

import com.seungwoo.dto.ParamDto;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;


/**
 * Created by Leo.
 * User: ssw
 * Date: 2019-02-07
 * Time: 16:00
 */
@Aspect
@Configuration
public class TestAspect {

    private static final Logger logger = LoggerFactory.getLogger(TestAspect.class);

    @Before("execution(* com.seungwoo.controller.*.* (..)) && args(paramDto)")
    public void setUUID(ParamDto paramDto) {
        paramDto.setTrId(UUID.randomUUID().toString().replace("-", ""));
        logger.info("============aspect=========");
    }
}
