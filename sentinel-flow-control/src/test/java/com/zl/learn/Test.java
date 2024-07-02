package com.zl.learn;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;

public class Test {

    @org.junit.Test
    public void test() throws NacosException {
        ConfigService configService = NacosFactory.createConfigService("127.0.0.1:8848");
        System.out.println(configService.getServerStatus());
    }
}
