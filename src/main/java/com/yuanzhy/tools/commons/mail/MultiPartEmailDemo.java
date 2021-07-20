package com.yuanzhy.tools.commons.mail;

import java.net.MalformedURLException;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.junit.Test;

/**
 * 带有附件的邮件测试类
 *
 * @author maoning
 * @date 2021/7/17
 */
public class MultiPartEmailDemo {
    @Test
    public void sendAttachmentEmail() throws EmailException, MalformedURLException {
        // Create the attachment
        EmailAttachment attachment1 = new EmailAttachment();
        attachment1.setPath("D:\\commons mail\\images\\4.jpeg");
        attachment1.setDisposition(EmailAttachment.INLINE);
        attachment1.setDescription("Picture of mouse");
//        attachment1.setName("a lovely mouse.jpeg");

        EmailAttachment attachment = new EmailAttachment();
        // 可以是路径
        attachment.setPath("D:\\commons mail\\files\\test.docx");
        // 也可以是URL
//        attachment.setURL(new URL("http://yuanzhy.com/static/img/tool/timestamp.png"));
//        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        attachment.setDescription("attach");
//        attachment.setName("timestamp.png");

        // Create the email message
        MultiPartEmail email = new MultiPartEmail();
        email.setHostName("smtp.qq.com");
        email.setAuthenticator(new DefaultAuthenticator("demo@qq.com", "xxxxxxxxx"));
        email.addTo("demo@163.com", "");
        email.setFrom("demo@qq.com", "Me");
        email.setSubject("The picture3");
        email.setMsg("第四次发送图片附件中设置URL,名称带后缀");

        // add the attachment
        email.attach(attachment);
        email.attach(attachment1);

        // send the email
        email.send();
    }
}
