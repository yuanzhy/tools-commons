package com.yuanzhy.tools.commons.net;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.imap.IMAPClient;
import org.apache.commons.net.pop3.POP3Client;
import org.apache.commons.net.pop3.POP3MessageInfo;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

public class MailDemo {

    private static final String USERNAME = "529164645@qq.com";
    private static final String PASSWORD = "woshishen000";

    @Test
    public void pop3() throws IOException {
        POP3Client pop3 = new POP3Client();
        try {
            pop3.setDefaultTimeout(60000);
            pop3.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true));
            pop3.connect("pop.qq.com");
            if (!pop3.isConnected()) {
                System.err.println("连接失败");
                return;
            }

            boolean suc = pop3.login(USERNAME, PASSWORD);
            if (!suc) {
                System.err.println("登录失败");
                return;
            }
            POP3MessageInfo status = pop3.status();
            if (status == null) {
                System.err.println("获取状态失败");
                return;
            }
            System.out.println("Status: " + status);
            POP3MessageInfo[] messages = pop3.listMessages();
            if (messages == null) {
                System.err.println("获取列表失败");
                return;
            } else if (messages.length == 0) {
                System.out.println("没有消息");
                return;
            }
            for (POP3MessageInfo msgInfo : messages) {
                // 获取邮件信息（不含内容）
                try (BufferedReader reader = (BufferedReader) pop3.retrieveMessageTop(msgInfo.number, 0)) {
//                Reader reader = pop3.retrieveMessage(msgInfo.number); // 获取邮件所有内容
                    if (reader == null) {
                        System.err.println("无法获取邮件标题");
                        return;
                    }
                    String from = "", subject = "", line;
                    while ((line = reader.readLine()) != null) {
                        final String lower = line.toLowerCase(Locale.ENGLISH);
                        if (lower.startsWith("from: ")) {
                            from = line.substring(6).trim();
                        } else if (lower.startsWith("subject: ")) {
                            subject = line.substring(9).trim();
                        }
                    }
                    System.out.println(msgInfo.number + " From: " + from + "  Subject: " + subject);
                }
            }
            pop3.logout();
        } finally {
            pop3.disconnect();
        }
    }

    @Test
    public void imap() throws IOException {
        IMAPClient imap = new IMAPClient();
        try {
            imap.setDefaultTimeout(10000);
            imap.addProtocolCommandListener(new PrintCommandListener(System.out, true));
            imap.connect("imap.qq.com");
            if (!imap.isConnected()) {
                System.err.println("连接失败");
                return;
            }
            boolean suc = imap.login(USERNAME, PASSWORD);
            if (!suc) {
                System.out.println("登录失败");
                return;
            }
            // 支持哪些能力
            imap.capability(); // IMAP4 IMAP4rev1 XLIST MOVE IDLE XAPPLEPUSHSERVICE NAMESPACE CHILDREN ID UIDPLUS
            // 选择邮箱名称：inbox，Sent Messages，Drafts，Deleted Messages，Junk等
            imap.select("inbox");
            // 检查收件箱状态
            imap.examine("inbox"); // 数量，最新邮件，未读数等
            // 获取收件箱未读数
            imap.status("inbox", new String[]{"UNSEEN"});
            // 显示文件夹
            imap.list("", "*");
            imap.logout();
        } finally {
            imap.disconnect();
        }
    }
    @Test
    public void smtp() throws IOException {
//        SMTPClient smtp = new SMTPClient();
//        try {
//            smtp.setDefaultTimeout(10000);
//            smtp.addProtocolCommandListener(new PrintCommandListener(System.out, true));
//            smtp.connect("smtp.qq.com");
//            if (!smtp.isConnected()) {
//                System.err.println("连接失败");
//                return;
//            }
//            smtp.logout();
//        } finally {
//            smtp.disconnect();
//        }
    }
}
