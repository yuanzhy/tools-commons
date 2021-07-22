**Apache Commons Email**是用于从Java发送电子邮件的类库，提供了几种发送电子邮件的API。它构建在Java Mail API 之上，主要是为了简化它。

Commons Email目前的最新版本是1.5，在Java Mail 1.5.6版本上构建。

maven坐标如下：

``````xml
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-email</artifactId>
    <version>1.5</version>
</dependency>
``````
使用程序发送邮件前需要开启POP3/SMTP服务，获取对应邮箱的授权码，不同的邮箱开启方式和位置不甚相同，这里介绍下qq邮箱、163邮箱的开启方式：

- qq邮箱

  进入邮箱首页，点击设置，切换到账户页签，将页面滑动到“POP3/IMAP/SMTP/Exchange/CardDAV/CalDAV服务 ”部分，点击“POP3/SMTP服务”后的【开启】，会弹出页面提醒用QQ绑定的手机号发送短信，发送完毕后，点击“我已发送”，即可获得授权码。

  ![](.\images\开启POP3 SMTP服务.png)

- 163邮箱

  进入邮箱首页，点击设置，选择下拉选项的POP3/SMTP/IMAP，点击进入“POP3/SMTP/IMAP”部分 ，点击“POP3/SMTP服务”后的【开启】，弹出页面，用绑定手机扫码发送短信，发送完毕后可获得授权码。

  ![](.\images\163邮箱开启SMTP服务.png)

  ![](.\images\163邮箱开启SMTP服务2.png)

*Tips*：QQ邮箱修改密码或设置独立密码均需要重新发送短信生成授权码，163邮箱未进行试验，可自行尝试。