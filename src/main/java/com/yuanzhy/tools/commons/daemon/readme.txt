apache commons daemon软件是一组实用程序和Java支持类，用于将Java应用程序作为服务器进程运行。
在Unix术语中，这些进程通常被称为“守护进程”（daemon）（因此得名）。
在Windows上，它们被称为“服务”。

它分为两个部分，一部分是用C写的，与操作系统交互，另一部分是用java写的，提供Daemon接口。
Apache commons daemon对Win32和Unix平台都有支持，Win32平台使用procrun, Unix平台使用jsvc。

==============================================================================================

Unix使用介绍：
下载源码包：http://commons.apache.org/proper/commons-daemon/download_daemon.cgi
$ tar -zxf commons-daemon-1.2.4-src.tar.gz
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

==============================================================================================

Windows使用介绍：
获取二进制包的两种方式
  一. 编译 -------------------------------------------------------
    安装VS，将nmake命令配置到环境变量
    Windows X64 Build
      "C:\Program Files (x86)\Microsoft Visual Studio\2019\Community\VC\Auxiliary\Build\vcvars64.bat"
      nmake CPU=X64
    Windows X86 Build
      nmake CPU=X86
      nmake CPU=X86 PREFIX=c:\desired\path\of\daemon install

    Release builds are build with Mladen Turk's (mturk) Custom Microsoft Compiler
    Toolkit Compilation. This can be obtained from:
    https://github.com/mturk/cmsc
    Hash: cb6be932c8c95a46262a64a89e68aae620dfdcee
    Compile as per <cmsc-root>/tools/README.txt

    A detailed description of the full environment used for recent release builds is
    provided at:
    https://cwiki.apache.org/confluence/display/TOMCAT/Common+Native+Build+Environment

    下载源码包
    The steps to produce the Windows binaries is then:

    1. cd $GIT_CLONE_DIR\src\native\windows\apps\prunmgr
    2. $CMSC_ROOT\setenv.bat /x86
    3. nmake -f Makefile
    4. cd ..\prunsrv
    5. nmake -f Makefile
    6. $CMCS_ROOT\setenv.bat /x64
    7. nmake -f Makefile

    It is not necessary to build a 64-bit version of prunmgr since the 32-bit
    version works with both 32-bit and 64-bit services.

  二. 直接下载现成的二进制应用 -------------------------------------
    https://downloads.apache.org/commons/daemon/binaries/windows/

