package com.yuanzhy.tools.commons.codec;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.junit.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author yuanzhy
 * @date 2021-07-08
 */
public class DigestDemo {

    public void digestUtils() {
        // 如果使用Java自带的api需要十多行才能实现md5摘要

        // 对数据做md5，参数支持字符串，字节数据，输入流
        String md5 = DigestUtils.md5Hex("测试");
        // 对数据做sha摘要
        String sha1 = DigestUtils.sha1Hex("测试");
        String sha256 = DigestUtils.sha256Hex("测试");
        String sha384 = DigestUtils.sha384Hex("测试");
        String sha512 = DigestUtils.sha512Hex("测试");
        String sha3_256 = DigestUtils.sha3_256Hex("测试");
        String sha3_384 = DigestUtils.sha3_384Hex("测试");
        String sha3_512 = DigestUtils.sha3_512Hex("测试");
        // ... ... 基本支持所有的sha算法

        // 获取Java原生的digest对象
        MessageDigest md5Md = DigestUtils.getMd5Digest();
        MessageDigest sha256Md = DigestUtils.getSha256Digest();
    }

    // 如果不使用commons工具类，md5操作需要如下实现
    @Test
    public void javaMd5() {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update("测试".getBytes());
            byte[] md5Bytes = md.digest();
            String md5 = byte2Hex(md5Bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private String byte2Hex(byte[] b) {
        StringBuilder r = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            r.append(hex);
        }
        return r.toString();
    }

    // hmac相关摘要算法
    public void hmacUtils() {
        String key = "asdf3234asdf3234asdf3234asdf3234";
        String valueToDigest = "测试数据";

        HmacUtils.getHmacSha256(valueToDigest.getBytes());
        // 做HMAC-MD5摘要
        String hmacMd5 = new HmacUtils(HmacAlgorithms.HMAC_MD5, key).hmacHex(valueToDigest);
        // 做HMAC-sha摘要
        String hmacSha256 = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, key).hmacHex(valueToDigest);
        String hmacSha384 = new HmacUtils(HmacAlgorithms.HMAC_SHA_384, key).hmacHex(valueToDigest);
        String hmacSha512 = new HmacUtils(HmacAlgorithms.HMAC_SHA_512, key).hmacHex(valueToDigest);
    }

}
