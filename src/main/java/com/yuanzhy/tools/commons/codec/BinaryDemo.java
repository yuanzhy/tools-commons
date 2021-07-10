package com.yuanzhy.tools.commons.codec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
        System.out.println(base64);
        // base64解码
        byte[] src = Base64.decodeBase64(base64);
        System.out.println(new String(src));
        // 字符串是否是base64
        Base64.isBase64(base64);

        // Base32 Base16 同理
    }
    @Test
    public void hex() throws Exception {
        // 十六进制用途一般是将二进制以更简短的方式展示，比如MD5值一般就用16进制来展现

        // byte数组转为十六进制字符串
        String hex = Hex.encodeHexString("123".getBytes());
        System.out.println(hex);
        // 16进制字符串解码
        byte[] src = Hex.decodeHex(hex);
        System.out.println(new String(src));

//        String s = new Base16().encodeToString("123".getBytes());
//        System.out.println(s);
    }


    @Test
    public void base64Stream() throws IOException {
        // 以流方式提供Base64编码和解码
        // 附："123"的base64编码为"MTIz"

        // 对输入流做base64编码
        InputStream is = new ByteArrayInputStream("123".getBytes());
        Base64InputStream ebis = new Base64InputStream(is, true);
        String enc = IOUtils.toString(ebis, "UTF-8"); // MTIz

        // 对base64数据流做解码
        is = new ByteArrayInputStream(enc.getBytes());
        Base64InputStream dbis = new Base64InputStream(is, false);
        String dec = IOUtils.toString(dbis, "UTF-8"); // 123

        // -----------------------

        // 将数据做base64编码写入输出流
        final String data = "123";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Base64OutputStream ebos = new Base64OutputStream(baos, true);
        IOUtils.write(data, ebos, "UTF-8");
        String enc2 = baos.toString(); // MTIz

        // 将base64数据做解码写入输出流
        baos = new ByteArrayOutputStream();
        Base64OutputStream dbos = new Base64OutputStream(baos, false);
        IOUtils.write(data, dbos, "UTF-8");
        String dec2 = dbos.toString(); // 123
    }
}
