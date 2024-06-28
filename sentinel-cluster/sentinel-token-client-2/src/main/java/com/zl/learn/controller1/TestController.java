package com.zl.learn.controller1;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    @SentinelResource("flow-test-1")
    public String test(){
        System.out.println("hello");
        return "hello";
    }
}
