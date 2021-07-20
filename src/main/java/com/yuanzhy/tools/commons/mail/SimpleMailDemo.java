package com.yuanzhy.tools.commons.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.junit.Test;

/**
 * 简单邮件测试类
 *
 * @author maoning
 * @date 2021/7/15
 */
public class SimpleMailDemo {
    /**
     * 发送一封简单的邮件
     *
     * @throws EmailException
     */
    @Test
    public void sendSimpleMail() throws EmailException {
        Email email = new SimpleEmail();
        // 发送邮件的SMTP服务器，如果不设置，默认查询系统变量mail.smtp.host的值，没有则会抛出异常
        // org.apache.commons.mail.EmailException: Cannot find valid hostname for mail session
        email.setHostName("smtp.qq.com");
        email.setSmtpPort(465);
        // 一个授权码只能发送一次邮件:QQ邮箱每次修改密码或者修改独立密码时需要重新发送短信获取授权码
        // javax.mail.AuthenticationFailedException: 535 Login fail. Authorization code is expired
        email.setAuthenticator(new DefaultAuthenticator("demo@qq.com", "xxxxxxxxx"));
        email.setSSLOnConnect(true);
        email.setFrom("demo@qq.com");
        email.setSubject("TestMail");
        email.setMsg("这是一封简单的邮件This is the third test mail ... :-)");
        email.addTo("demo@163.com");
        email.send();
    }

    @Test
    public void sendSimpleMail2() throws MessagingException {
        Properties prop = new Properties();
        prop.setProperty("mail.host", "smtp.qq.com");
        prop.setProperty("mail.transport.protocol", "smtp");
        prop.setProperty("mail.smtp.auth", "true");
        // 创建session
        Session session = Session.getInstance(prop);
        // Session的debug模式，设置为true能够查看到程序发送Email的运行状态
        // session.setDebug(true);
        Transport ts = session.getTransport();
        // 以qq邮箱为例,需要开启POP3/SMTP服务,生成授权码供connect时使用
        ts.connect("demo@qq.com", "xxxxxxxxx");
        // 创建邮件对象
        MimeMessage message = new MimeMessage(session);
        message.setFrom("demo@qq.com");
        message.setRecipient(Message.RecipientType.TO, new InternetAddress("demo@163.com"));
        message.setSubject("javamail简单文本邮件");
        message.setContent("这是一封javamail发送的简单邮件", "text/html;charset=UTF-8");

        // 发送
        ts.sendMessage(message, message.getAllRecipients());
        ts.close();
    }
}
