package com.yuanzhy.tools.commons.lang;

import org.apache.commons.lang3.SystemUtils;
import org.junit.Test;

import java.io.File;

public class SystemDemo {
    @Test
    public void os() {
        // 判断操作系统类型
        boolean isWin = SystemUtils.IS_OS_WINDOWS;
        boolean isWin10 = SystemUtils.IS_OS_WINDOWS_10;
        boolean isWin2012 = SystemUtils.IS_OS_WINDOWS_2012;
        boolean isMac = SystemUtils.IS_OS_MAC;
        boolean isLinux = SystemUtils.IS_OS_LINUX;
        boolean isUnix = SystemUtils.IS_OS_UNIX;
        boolean isSolaris = SystemUtils.IS_OS_SOLARIS;
        // ... ...

        // 判断java版本
        boolean isJava6 = SystemUtils.IS_JAVA_1_6;
        boolean isJava8 = SystemUtils.IS_JAVA_1_8;
        boolean isJava11 = SystemUtils.IS_JAVA_11;
        boolean isJava14 = SystemUtils.IS_JAVA_14;
        // ... ...

        // 获取java相关目录
        File javaHome = SystemUtils.getJavaHome();
        File userHome = SystemUtils.getUserHome();// 操作系统用户目录
        File userDir = SystemUtils.getUserDir();// 项目所在路径
        File tmpDir = SystemUtils.getJavaIoTmpDir();
    }
}
