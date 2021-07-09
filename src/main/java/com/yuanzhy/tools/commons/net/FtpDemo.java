package com.yuanzhy.tools.commons.net;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.junit.Test;

/**
 *
 * @author yuanzhy
 * @date 2021-07-09
 */
public class FtpDemo {
    @Test
    public void ftp() {
        FTPClient ftpClient = new FTPClient();
        try {
            /**设置文件传输的编码*/
            ftpClient.setControlEncoding("UTF-8");

            /**连接 FTP 服务器
             * 如果连接失败，则此时抛出异常，如ftp服务器服务关闭时，抛出异常：
             * java.net.ConnectException: Connection refused: connect*/
            ftpClient.connect("172.18.11.11", 21);
            /**登录 FTP 服务器
             * 1）如果传入的账号为空，则使用匿名登录，此时账号使用 "Anonymous"，密码为空即可*/
            ftpClient.login("Anonymous", "");
            // ftpClient.login(username, password);

            /** 设置传输的文件类型
             * BINARY_FILE_TYPE：二进制文件类型
             * ASCII_FILE_TYPE：ASCII传输方式，这是默认的方式
             * ....
             */
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

            /**
             * 确认应答状态码是否正确完成响应
             * 凡是 2开头的 isPositiveCompletion 都会返回 true，因为它底层判断是：
             * return (reply >= 200 && reply < 300);
             */
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                /**
                 * 如果 FTP 服务器响应错误 中断传输、断开连接
                 * abort：中断文件正在进行的文件传输，成功时返回 true,否则返回 false
                 * disconnect：断开与服务器的连接，并恢复默认参数值
                 */
                ftpClient.abort();
                ftpClient.disconnect();
            } else {
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(">>>>>FTP服务器连接登录失败，请检查连接参数是否正确，或者网络是否通畅*********");
        }
//        return ftpClient;
    }
}
