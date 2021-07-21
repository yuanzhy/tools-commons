package com.yuanzhy.tools.commons.mail;

import java.io.File;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailConstants;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.junit.Test;

/**
 * commons mail实现的发送带有附件和图片的邮件
 *
 * @author maoning
 * @date 2021/7/21
 */
public class ComplexCommonsMailDemo {
    @Test
    public void sendEmail() throws EmailException {
        HtmlEmail email = new HtmlEmail();
        // 设置基本信息
        email.setHostName("smtp.qq.com");
        email.setAuthenticator(new DefaultAuthenticator("demo@qq.com", "xxxxxxxx"));
        email.addTo("demo@163.com");
        email.setFrom("demo@qq.com");
        email.setSubject("commons mail发送的比较全面的带有附件和带图片及正文的邮件");
        // 生成图片标识
        String cid = email.embed(new File("D:\\commons mail\\images\\5.jpg"));
        email.setCharset(EmailConstants.UTF_8);
        email.setHtmlMsg("<html>简单的头像：<br/><img src=\"cid:" + cid + "\"></html>");
        // 额外提示
        email.setTextMsg("您的邮件不支持html格式");
        // 附件
        EmailAttachment attachment = new EmailAttachment();
        attachment.setPath("D:\\commons mail\\files\\test.docx");
        EmailAttachment attachment2 = new EmailAttachment();
        attachment2.setPath("D:\\commons mail\\files\\test2.txt");
        email.attach(attachment);
        email.attach(attachment2);
        // 发送
        email.send();
    }
}
