commons-net库实现了许多基本Internet协议的客户端。
库的目的是提供基本的协议访问，而不是更高级别的抽象。
因此，有些设计违反了面向对象的设计原则。
我们的理念是尽可能使协议的全局功能可访问（例如，TFTP发送文件和接收文件），但也提供对基本协议的访问（如果适用），以便程序员可以构建自己的自定义实现（例如，TFTP分组类和TFTP分组发送和接收方法被公开）。


支持的协议包括：
    FTP/FTPS：FTP（File Transfer Protocol，文件传输协议） 是 TCP/IP 协议组中的协议之一。FTP协议包括两个组成部分，其一为FTP服务器，其二为FTP客户端。其中FTP服务器用来存储文件，用户可以使用FTP客户端通过FTP协议访问位于FTP服务器上的资源。在开发网站的时候，通常利用FTP协议把网页或程序传到Web服务器上。此外，由于FTP传输效率非常高，在网络上传输大的文件时，一般也采用该协议。
    FTP over HTTP (experimental)：由于FTP工作在被动模式时不仅需要将21作为FTP的控制（命令）端口，还要将20作为FTP的数据端口，因此在配置防火墙时比较麻烦，不如用http协议传输文件。因此可以利用原有的网站结合Alias的方法加目录访问控制来实现。
    NNTP：网络新闻组传输协议
    SMTP(S)：SMTP（Simple Mail Transfer Protocol）是一种提供可靠且有效的电子邮件传输的协议。SMTP是建立在FTP文件传输服务上的一种邮件服务，主要用于系统之间的邮件信息传递，并提供有关来信的通知
    POP3(S)：POP3，全名为“Post Office Protocol - Version 3”，即“邮局协议版本3”。POP3协议允许电子邮件客户端下载服务器上的邮件，但是在客户端的操作（如移动邮件、标记已读等），不会反馈到服务器上，比如通过客户端收取了邮箱中的3封邮件并移动到其他文件夹，邮箱服务器上的这些邮件是没有同时被移动的
    IMAP(S)：IMAP（Internet Message Access Protocol）以前称作交互邮件访问协议（Interactive Mail Access Protocol），是一个应用层协议。它的主要作用是邮件客户端可以通过这种协议从邮件服务器上获取邮件的信息，下载邮件等。IMAP提供webmail 与电子邮件客户端之间的双向通信，客户端的操作都会反馈到服务器上，对邮件进行的操作，服务器上的邮件也会做相应的动作
    Telnet：Telnet协议是TCP/IP协议族中的一员，是Internet远程登录服务的标准协议和主要方式。它为用户提供了在本地计算机上完成远程主机工作的能力。在终端使用者的电脑上使用telnet程序，用它连接到服务器。终端使用者可以在telnet程序中输入命令，这些命令会在服务器上运行，就像直接在服务器的控制台上输入一样。可以在本地就能控制服务器。要开始一个telnet会话，必须输入用户名和密码来登录服务器。Telnet是常用的远程控制Web服务器的方法
    TFTP：TFTP（Trivial File Transfer Protocol,简单文件传输协议）是TCP/IP协议族中的一个用来在客户机与服务器之间进行简单文件传输的协议，提供不复杂、开销不大的文件传输服务。端口号为69。
    Finger：显示有关运行 Finger 服务或 Daemon 的指定远程计算机（通常是运行 UNIX 的计算机）上用户的信息。该远程计算机指定显示用户信息的格式和输出。如果不使用参数，Finger 将显示帮助。
    Whois：whois（读作“Who is”，非缩写）是用来查询域名的IP以及所有者等信息的传输协议。简单说，whois就是一个用来查询域名是否已经被注册，以及注册域名的详细信息的数据库（如域名所有人、域名注册商）。通过whois来实现对域名信息的查询。早期的whois查询多以命令列接口存在，但是现在出现了一些网页接口简化的线上查询工具，可以一次向不同的数据库查询。网页接口的查询工具仍然依赖whois协议向服务器发送查询请求，命令列接口的工具仍然被系统管理员广泛使用。whois通常使用TCP协议43端口。每个域名/IP的whois信息由对应的管理机构保存。
    rexec/rcmd/rlogin：是一个Unix命令，远程执行，远程登录，起源于BSD
    Time (rdate) and Daytime：DAYTIME协议是基于TCP的应用，是一种有用的调试工具，它的作用是返回当前时间和日期，格式是字符串格式。时间协议（英语：TIME protocol）是一个在RFC 868内定义的网络传输协议。它用作提供机器可读的日期时间信息。
    Echo：echo是一个计算机命令，它可以基于TCP协议，服务器就在TCP端口7检测有无消息，如果使用UDP协议，基本过程和TCP一样，检测的端口也是7。 是路由也是网络中最常用的数据包，可以通过发送echo包知道当前的连接节点有那些路径，并且通过往返时间能得出路径长度。
    Discard：它的作用就是接收到什么抛弃什么，它对调试网络状态的一定的用处。基于TCP的抛弃服务，如果服务器实现了抛弃协议，服务器就会在TCP端口9检测抛弃协议请求，在建立连接后并检测到请求后，就直接把接收到的数据直接抛弃，直到用户中断连接。而基于UDP协议的抛弃服务和基于TCP差不多，检测的端口是UDP端口9，功能也一样
    NTP/SNTP：NTP服务器【Network Time Protocol（NTP）】是用来使计算机时间同步化的一种协议，它可以使计算机对其服务器或时钟源（如石英钟，GPS等等)做同步化，它可以提供高精准度的时间校正（LAN上与标准间差小于1毫秒，WAN上几十毫秒），且可介由加密确认的方式来防止恶毒的协议攻击。时间按NTP服务器的等级传播。按照离外部UTC源的远近把所有服务器归入不同的Stratum（层）中。

背景：
Commons-Net最初是一个名为NetComponents的商业Java库，最初由ORO，Inc.在Java早期开发的。
在1998发布了1.3.8版本后，源代码被捐赠给Apache软件基金会，并在APACHE许可证下可用。
从那时起，许多程序员为Commons-Net的持续发展做出了贡献。当前版本的编号方案与旧版本无关。
也就是说，Commons net 1.0成功地取代了NetComponents 1.3.8。

org.apache.commons.net
org.apache.commons.net.bsd
org.apache.commons.net.chargen
org.apache.commons.net.daytime
org.apache.commons.net.discard
org.apache.commons.net.echo
org.apache.commons.net.finger
org.apache.commons.net.ftp
org.apache.commons.net.imap
org.apache.commons.net.io
org.apache.commons.net.nntp
org.apache.commons.net.ntp
org.apache.commons.net.pop3
org.apache.commons.net.smtp
org.apache.commons.net.telnet
org.apache.commons.net.tftp
org.apache.commons.net.time
org.apache.commons.net.util
org.apache.commons.net.whois