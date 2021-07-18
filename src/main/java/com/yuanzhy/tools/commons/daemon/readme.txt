apache commons daemon软件是一组实用程序和Java支持类，用于将Java应用程序作为服务器进程运行。
在Unix术语中，这些进程通常被称为“守护进程”（daemon）（因此得名）。
在Windows上，它们被称为“服务”。

它分为两个部分，一部分是用C写的，与操作系统交互，另一部分是用java写的，提供Daemon接口。
Apache commons daemon对Win32和Unix平台都有支持，Win32平台使用procrun, Unix平台使用jsvc。

Unix使用介绍：
下载源码包：http://commons.apache.org/proper/commons-daemon/download_daemon.cgi
$ tar -zxvf commons-daemon-1.2.4-src.tar.gz
$ cd commons-daemon-1.2.4-src/src/native/unix
$ ./support/buildconf.sh
  -- 得到以下输出：
        ./support/buildconf.sh: configure script generated successfully
$ ./configure
  -- 得到以下输出：
      ... ...
      *** Writing output files ***
      configure: creating ./config.status
      config.status: creating Makefile
      config.status: creating Makedefs
      config.status: creating native/Makefile
      *** All done ***
      Now you can issue "make"

$ make
检查jsvc是否安装成功，出现相关的帮助信息则安装成功
$ ./jsvc -help