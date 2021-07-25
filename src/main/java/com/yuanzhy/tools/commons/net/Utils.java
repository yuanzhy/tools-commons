package com.yuanzhy.tools.commons.net;

import org.apache.commons.net.util.KeyManagerUtils;
import org.apache.commons.net.util.SSLContextUtils;
import org.apache.commons.net.util.SubnetUtils;
import org.apache.commons.net.util.TrustManagerUtils;
import org.junit.Test;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;


public class Utils {

    @Test
    public void subnetUtils() {
        // 以下为两种初始化subnet工具类的方式
        // 1. 使用IP和子网掩码
        SubnetUtils subnet = new SubnetUtils("192.168.1.113", "255.255.255.0");
        // 2. 使用CIDR格式地址
        // SubnetUtils subnet = new SubnetUtils("192.168.1.113/24");

        // 获取网络各种参数
        SubnetUtils.SubnetInfo si = subnet.getInfo();
        // 获取IP地址
        si.getAddress(); // 192.168.1.113
        // 获取网络地址（IP地址和子网掩码进行与运算，结果是网络地址，即主机号全0是网络地址）
        si.getNetworkAddress(); // 192.168.1.0
        // 获取广播地址（专门用于同时向网络中所有工作站进行发送的一个地址）
        si.getBroadcastAddress(); // 192.168.1.255
        // 获取子网下的起始地址
        si.getLowAddress(); // 192.168.1.1
        // 获取子网下的终止地址
        si.getHighAddress(); // 192.168.1.254
        // 获取上一个地址
        si.getPreviousAddress(); // 192.168.1.112
        // 获取下一个地址
        si.getNextAddress(); // 192.168.1.114
        // 获取子网掩码
        si.getNetmask(); // 255.255.255.0
        // 获取CIDR格式的网络地址
        si.getCidrSignature(); // 192.168.1.113/24
        // 获取子网最大IP个数
        si.getAddressCountLong(); // 254
        // 获取所有IP地址数组
        si.getAllAddresses(); // [192.168.1.1, ..., 192.168.1.254]
    }

    public void trustManagerUtils() {
        // 获取不执行检查的 TrustManager
        X509TrustManager aatm = TrustManagerUtils.getAcceptAllTrustManager();
        // 生成一个 TrustManager 来检查服务器证书的有效性，但不执行任何检查。
        X509TrustManager vsctm = TrustManagerUtils.getValidateServerCertificateTrustManager();
        try {
            // 返回 JVM 提供的默认 TrustManager
            X509TrustManager dtm = TrustManagerUtils.getDefaultTrustManager(null);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    public void keyManagerUtils() throws IOException, GeneralSecurityException {
        String keyStorePath = "c:/client.jks";
        String password = "123456"; // key密码
        File keyFile = new File(keyStorePath);
        KeyManager km = KeyManagerUtils.createClientKeyManager(keyFile, password);
        // 还有多个重载方法
//        KeyManagerUtils.createClientKeyManager(KeyStore ks, String keyAlias, String keyPass);
//        KeyManagerUtils.createClientKeyManager(File storePath, String storePass, String keyAlias);
//        KeyManagerUtils.createClientKeyManager(String storeType, String storePath, String storePass, String keyAlias, String keyPass);
    }

    // SSL相关写法
    public void sslContext() throws Exception {
        String key = "c:/client.jks";
        String password = "123456"; // key密码

        // 使用jdk原生写法
        KeyStore keyStore = KeyStore.getInstance("JKS");  //创建一个keystore来管理密钥库
        keyStore.load(new FileInputStream(key), password.toCharArray());
        //创建jkd密钥访问库
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(keyStore);
        KeyManager[] kms = kmf.getKeyManagers();
        TrustManager[] tms = tmf.getTrustManagers();
        SSLContext ctx = SSLContext.getInstance("TLSv1"); // 支持SSLv2, SSLv3, TLSv1, TLSv1.1, SSLv2Hello
        ctx.init(kms, tms,null);

        // 使用commons-net相关工具类写法
        File keyFile = new File(key);
        KeyManager km = KeyManagerUtils.createClientKeyManager(keyFile, password);
        TrustManager tm = TrustManagerUtils.getDefaultTrustManager(keyStore);
        SSLContext ctx2 = SSLContextUtils.createSSLContext("TLSv1", km, tm);
    }
}
