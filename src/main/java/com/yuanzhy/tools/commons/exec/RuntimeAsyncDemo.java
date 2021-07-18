package com.yuanzhy.tools.commons.exec;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Consumer;

/**
 * 简易Runtime异步执行demo, 很多细节不是很严谨，只看大体思路即可。
 */
public class RuntimeAsyncDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("1. 开始执行");
        String cmd = "cmd /c dir"; // 假设是一个耗时的操作
        execAsync(cmd, processResult -> {
            System.out.println("3. 异步执行完成，success=" + processResult.success + "; msg=" + processResult.result);
            System.exit(0);
        });
        // 做其他操作 ... ...
        System.out.println("2. 做其他操作");
        // 避免主线程退出导致程序退出
        Thread.currentThread().join();
    }
    private static void execAsync(String command, Consumer<ProcessResult> callback) throws IOException {
        final Process process = Runtime.getRuntime().exec(command);
        new Thread(() -> {
            StringBuilder successMsg = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"))) {
                // 存放临时结果
                String line;
                while ((line = br.readLine()) != null) {
                    try {
                        successMsg.append(line).append("\r\n");
                        int exitCode = process.exitValue();
                        ProcessResult pr = new ProcessResult();
                        if (exitCode == 0) {
                            pr.success = true;
                            pr.result = successMsg.toString();
                        } else {
                            pr.success = false;
                            pr.result = IOUtils.toString(process.getErrorStream());
                        }
                        callback.accept(pr); // 回调主线程注册的函数
                        break; // exitValue没有异常表示进程执行完成，退出循环
                    } catch (IllegalThreadStateException e) {
                        // 异常代表进程没有执行完
                    }
                    try {
                        // 等待100毫秒在检查是否完成
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private static class ProcessResult {
        boolean success;
        String result;
    }
}
