package com.zl.learn;

import com.alibaba.csp.sentinel.init.InitExecutor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class TokenClientApplication2
{
    static {
        InitExecutor.doInit();
    }
    public static void main( String[] args )
    {
        SpringApplication.run(TokenClientApplication2.class, args);
    }
}
