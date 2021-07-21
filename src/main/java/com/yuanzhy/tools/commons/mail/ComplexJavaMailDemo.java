package com.yuanzhy.tools.commons.mail;

import java.io.IOException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.junit.Test;

/**
 * javamail实现的发送带有附件和图片的邮件
 *
 * @author maoning
 * @date 2021/7/21
 */
public class ComplexJavaMailDemo {
    @Test
    public void sendEmail() throws MessagingException, IOException {
        Properties prop = new Properties();
        prop.setProperty("mail.host", "smtp.qq.com");
        prop.setProperty("mail.transport.protocol", "smtp");
        prop.setProperty("mail.smtp.auth", "true");
        // 创建session
        Session session = Session.getInstance(prop);
        Transport ts = session.getTransport();
        ts.connect("smtp.qq.com", "demo@qq.com", "xxxxxxxx");
        MimeMessage message = new MimeMessage(session);
        // 设置基本信息
        message.setFrom(new InternetAddress("demo@qq.com"));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress("demo@163.com"));
        message.setSubject("javamail发送的比较全面的带有附件和带图片及正文的邮件");
        // 内容
        MimeBodyPart text = new MimeBodyPart();
        text.setContent("简单的头像：<br/><img src='cid:portrait.jpg'>", "text/html;charset=UTF-8");
        // 图片
        MimeBodyPart image = new MimeBodyPart();
        image.setDataHandler(new DataHandler(new FileDataSource("D:\\commons mail\\images\\5.jpg")));
        image.setContentID("portrait.jpg");
        // 附件1
        MimeBodyPart attachment = new MimeBodyPart();
        DataHandler handler = new DataHandler(new FileDataSource("D:\\commons mail\\files\\test.docx"));
        attachment.setDataHandler(handler);
        attachment.setFileName(handler.getName());
        // 附件2
        MimeBodyPart attachment2 = new MimeBodyPart();
        DataHandler handler2 = new DataHandler(new FileDataSource("D:\\commons mail\\files\\test2.txt"));
        attachment2.setDataHandler(handler2);
        attachment2.setFileName(MimeUtility.encodeText(handler2.getName()));
        // 构建
        MimeMultipart part = new MimeMultipart();
        part.addBodyPart(text);
        part.addBodyPart(image);
        part.setSubType("related");
        MimeMultipart part2 = new MimeMultipart();
        part2.addBodyPart(attachment);
        part2.addBodyPart(attachment2);
        MimeBodyPart content = new MimeBodyPart();
        content.setContent(part);
        part2.addBodyPart(content);
        part2.setSubType("mixed");
        message.setContent(part2);
        message.saveChanges();
        // 发送
        ts.sendMessage(message, message.getAllRecipients());
        ts.close();
    }
}
