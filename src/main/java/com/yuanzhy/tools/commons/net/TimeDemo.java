package com.yuanzhy.tools.commons.net;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.net.time.TimeUDPClient;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;

public class TimeDemo {

    @Test
    public void time() throws IOException {
        // 基于UDP协议
        TimeUDPClient timeClient = new TimeUDPClient();
        timeClient.setDefaultTimeout(3000);
        InetAddress addr = InetAddress.getByName("time.nist.gov");
        // UDP使用open
        timeClient.open();
        Date d = timeClient.getDate(addr);
        System.out.println(DateFormatUtils.format(d, "yyyy-MM-dd HH:mm:ss.SSS"));
        // UDP使用close关闭连接
        timeClient.close();
    }
}
