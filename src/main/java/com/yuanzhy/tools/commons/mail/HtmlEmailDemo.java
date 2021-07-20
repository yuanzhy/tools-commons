package com.yuanzhy.tools.commons.mail;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailConstants;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.ImageHtmlEmail;
import org.apache.commons.mail.resolver.DataSourceUrlResolver;
import org.junit.Test;

/**
 * html格式内容电子邮件
 *
 * @author maoning
 * @date 2021/7/17
 */
public class HtmlEmailDemo {
    @Test
    public void sendHtmlEmailDemo() throws EmailException, MalformedURLException {
        // Create the email message
        HtmlEmail email = new HtmlEmail();
        email.setHostName("smtp.qq.com");
        email.setAuthenticator(new DefaultAuthenticator("demo@qq.com", "xxxxxxxxx"));
        email.addTo("demo@163.com", "上进的筱筱");
        email.setFrom("demo@qq.com", "我是发件人024-2");
        email.setSubject("拥有内联图片的html格式邮件");
        // embed the image and get the content id
        URL url = new URL("http://yuanzhy.com/static/img/tool/redhat.png");
        // 随机生成的图片标识
        String cid = email.embed(url, "yuanzhy redhat");
        // 内容包含中文需要设置编码
        email.setCharset(EmailConstants.UTF_8);
        // 设置html格式内容
        email.setHtmlMsg("<html>yuanzhy redhat logo - <img src=\"cid:" + cid + "\">" +
                "<br/><br/>" +
                "图片之后输入一段中文文字吧~~" +
                "</html>");
        // set the alternative message
        email.setTextMsg("Your email client does not support HTML messages");
        // 在html格式消息后设置,则原有html格式会乱码,其中的图片将放到附件中
        // email.setMsg("这是一封html格式的邮件里的msg");

        // send the email
        email.send();
    }

    @Test
    public void sendImageHtmlDemo() throws MalformedURLException, EmailException {
        // load your HTML email template
        String htmlEmailTemplate = ".... <img src=\"http://yuanzhy.com/static/img/tool/redhat.png\"> ....";

        // define you base URL to resolve relative resource locations
        URL url = new URL("http://yuanzhy.com");


        // create the email message
        ImageHtmlEmail email = new ImageHtmlEmail();
        email.setDataSourceResolver(new DataSourceUrlResolver(url));
        email.setHostName("smtp.qq.com");
        email.setAuthenticator(new DefaultAuthenticator("demo@qq.com", "xxxxxxxxx"));
        email.addTo("demo@163.com", "筱筱");
        email.setFrom("demo@qq.com", "筱筱的qq");
        email.setSubject("Test email with inline image");

        // set the html message
        email.setHtmlMsg(htmlEmailTemplate);

        // set the alternative message
        email.setTextMsg("Your email client does not support HTML messages");

        // send the email
        email.send();
    }
}
