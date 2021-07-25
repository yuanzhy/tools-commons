package com.yuanzhy.tools.commons.net;

import org.apache.commons.net.whois.WhoisClient;
import org.junit.Test;

import java.io.IOException;

public class WhoisDemo {

    @Test
    public void whois() throws IOException {
        // 基于TCP协议
        WhoisClient whoisClient = new WhoisClient();
        try {
            // 基于TCP使用connect
            whoisClient.connect(WhoisClient.DEFAULT_HOST);
            String host = whoisClient.query("baidu.com");
            System.out.println(host);
        } finally {
            // 基于TCP需要主动断开连接
            whoisClient.disconnect();
        }
    }
}
