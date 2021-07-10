package com.yuanzhy.tools.commons.exec;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteResultHandler;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author yuanzhy
 * @date 2021-07-09
 */
public class ExecDemo {
    @Test
    public void runtime() throws Exception {
        // 同步调用其他进程

        // 不使用工具类的写法
//        Process process = Runtime.getRuntime().exec("ls -l");
        Process process = Runtime.getRuntime().exec("cmd /c dir");
        int exitCode = process.waitFor(); // 阻塞等待完成
        if (exitCode == 0) { // 状态码0表示执行成功
            String result = IOUtils.toString(process.getInputStream()); // "IOUtils" commons io中的工具类，详情可以参见前续文章介绍
            System.out.println(result);
        } else {
            String errMsg = IOUtils.toString(process.getErrorStream());
            System.out.println(errMsg);
        }
        // 等等，这么写其实有坑。如果执行一个安装脚本会在控制台输出大量内容，这时可能会导致进程卡死（其实是一直阻塞状态）。
        // 这是由于缓冲区等缓冲区满了，无法写入数据，导致线程阻塞，对外现象就是进程无法停止，也不占资源，什么反应也没有
        // 这种情况可以单独启动一个线程去读取输入流的内容，避免缓冲区占满，示例如下：
    }

    public void runtimeThread() throws Exception {
        final Process process = Runtime.getRuntime().exec("cmd /c dir");
        new Thread(() -> {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    try {
                        process.exitValue();
                        break; // exitValue没有异常表示进程执行完成，退出循环
                    } catch (IllegalThreadStateException e) {
                        // 异常代表进程没有执行完
                    }
                    System.out.println(line);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    // JDK自带的Runtime不支持异步执行，如果要异步拿到执行结果需要自己单独创建线程不断轮询进程状态然后通知主线程，
    // 实现起来较为复杂，这里就不做示例了，想要示例可以留言。
    // 下面来看看使用commons-exec如何简单的实现这些功能
    @Test
    public void execSync() {
        try {
            String command = "ping 192.168.1.10";
            //接收正常结果流
            ByteArrayOutputStream susStream = new ByteArrayOutputStream();
            //接收异常结果流
            ByteArrayOutputStream errStream = new ByteArrayOutputStream();
            CommandLine commandLine = CommandLine.parse(command);
            DefaultExecutor exec = new DefaultExecutor();
//            exec.setExitValues(null);
            //设置一分钟超时
//            ExecuteWatchdog watchdog = new ExecuteWatchdog(60*1000);
//            exec.setWatchdog(watchdog);
            PumpStreamHandler streamHandler = new PumpStreamHandler(susStream, errStream);
            exec.setStreamHandler(streamHandler);
            int code = exec.execute(commandLine);
            System.out.println("result code: " + code);
            // 不同操作系统注意编码，否则结果乱码
            String suc = susStream.toString("GBK");
            String err = errStream.toString("GBK");
            System.out.println(suc);
            System.out.println(err);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void execAsync() {
        try {
            String command = "ping 192.168.1.10";
            //接收正常结果流
            ByteArrayOutputStream susStream = new ByteArrayOutputStream();
            //接收异常结果流
            ByteArrayOutputStream errStream = new ByteArrayOutputStream();
            CommandLine commandLine = CommandLine.parse(command);
            DefaultExecutor exec = new DefaultExecutor();
            //设置一分钟超时
//            ExecuteWatchdog watchdog = new ExecuteWatchdog(60*1000);
//            exec.setWatchdog(watchdog);

            PumpStreamHandler streamHandler = new PumpStreamHandler(susStream, errStream);
            exec.setStreamHandler(streamHandler);
            ExecuteResultHandler erh = new ExecuteResultHandler(){
                @Override
                public void onProcessComplete(int exitValue) {
                    try {
                        String suc = susStream.toString("GBK");
                        System.out.println(suc);
                    } catch (UnsupportedEncodingException uee) {
                        uee.printStackTrace();
                    }
                }
                @Override
                public void onProcessFailed(ExecuteException e) {
                    try {
                        String err = errStream.toString("GBK");
                        System.out.println(err);
                    } catch (UnsupportedEncodingException uee) {
                        uee.printStackTrace();
                    }
                }
            };
            exec.execute(commandLine, erh);
            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
