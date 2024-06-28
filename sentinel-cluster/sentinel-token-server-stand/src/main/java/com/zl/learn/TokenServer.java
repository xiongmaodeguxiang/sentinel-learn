package com.zl.learn;

import com.alibaba.csp.sentinel.cluster.server.SentinelDefaultTokenServer;

/**
 * Hello world!
 *
 */
public class TokenServer
{
    public static void main( String[] args ) throws Exception {
        SentinelDefaultTokenServer tokenServer = new SentinelDefaultTokenServer();
        //启动
        tokenServer.start();
    }
}
