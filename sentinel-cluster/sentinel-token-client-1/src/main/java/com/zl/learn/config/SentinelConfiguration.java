package com.zl.learn.config;

import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SentinelConfiguration {
    @Bean
    SentinelResourceAspect sentinelResourceAspect(){
        return new SentinelResourceAspect();
    }

}
