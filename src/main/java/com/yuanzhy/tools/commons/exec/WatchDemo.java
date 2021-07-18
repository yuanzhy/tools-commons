package com.yuanzhy.tools.commons.exec;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class WatchDemo {

    @Test
    public void watch() throws IOException, InterruptedException {
        String command = "ping 192.168.1.10";
        ByteArrayOutputStream susStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errStream = new ByteArrayOutputStream();
        CommandLine commandLine = CommandLine.parse(command);
        DefaultExecutor exec = new DefaultExecutor();
        //设置一分钟超时
        ExecuteWatchdog watchdog = new ExecuteWatchdog(60);
        exec.setWatchdog(watchdog);
        PumpStreamHandler streamHandler = new PumpStreamHandler(susStream, errStream);
        exec.setStreamHandler(streamHandler);
        try {
            int code = exec.execute(commandLine);
            System.out.println("result code: " + code);
            // 不同操作系统注意编码，否则结果乱码
            String suc = susStream.toString("GBK");
            String err = errStream.toString("GBK");
            System.out.println(suc+err);
        } catch (ExecuteException e) {
            if (watchdog.killedProcess()) {
                // 被watchdog故意杀死
                System.err.println("超时了");
            }
        }
    }
}
