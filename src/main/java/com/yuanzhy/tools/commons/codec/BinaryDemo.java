package com.yuanzhy.tools.commons.codec;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

/**
 *
 * @author yuanzhy
 * @date 2021-07-08
 */
public class BinaryDemo {

    public void base64() {
        // base64一般用户网络传输数据，不可见字符无法在网络上传输，可以使用base64编码将其展现出来

        // base64编码
        String base64 = Base64.encodeBase64String("测试数据".getBytes());
        // base64解码
        byte[] src = Base64.decodeBase64(base64);
        // 字符串是否是base64
        Base64.isBase64(base64);

        // Base32 Base16 同理
    }
    @Test
    public void hex() throws Exception {
        // 十六进制用途一般是将二进制以更简短的方式展示，比如MD5值一般就用16进制来展现

        // byte数组转为十六进制字符串
        String hex = Hex.encodeHexString("测试数据".getBytes());
        // 16进制字符串解码
        byte[] src = Hex.decodeHex(hex);
    }
}
