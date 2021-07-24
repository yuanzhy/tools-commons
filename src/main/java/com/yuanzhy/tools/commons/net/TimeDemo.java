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
        TimeUDPClient timeClient = new TimeUDPClient();
        timeClient.setDefaultTimeout(3000);
        InetAddress addr = InetAddress.getByName("time.nist.gov");
        timeClient.open();
//        InetAddress addr = InetAddress.getLocalHost();
        Date d = timeClient.getDate(addr);
        System.out.println(DateFormatUtils.format(d, "yyyy-MM-dd HH:mm:ss.SSS"));
        timeClient.close();
    }
}
