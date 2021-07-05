package com.yuanzhy.tools.commons.io;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;

/**
 *
 */
public class IOUtilsDemo {

    /**
     * closeQuietly: 安静的关闭流，不需要判空和捕获异常，同时支持关闭多个流
     * @throws Exception
     */
    public void closeDemo() {
        InputStream inputStream = null;
        InputStream outputStream = null;
        // 原生写法
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // commons写法(可以传任意数量的流)
        IOUtils.closeQuietly(inputStream, outputStream);
    }

    public void readAsBytesDemo() throws IOException {
        // 输入流转换为byte数组
        // 原生写法
        InputStream is = new FileInputStream("foo.txt");
        byte[] buf = new byte[1024];
        int len;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while ((len = is.read(buf)) != -1) {
            out.write(buf, 0, len);
        }
        byte[] result = out.toByteArray();
        // commons写法
        byte[] result2 = IOUtils.toByteArray(is);
    }

    public void readAsString() throws IOException {
        // 输入流转换为字符串

        InputStream is = new FileInputStream("foo.txt");
        // 原生写法
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        String result = sb.toString();
        // commons写法
        String result2 = IOUtils.toString(is, "UTF-8");


        // IOUtils.toString 还有很多重载方法，保证有你想要的
        // 将reader转换为字符串
        // String toString(Reader reader, String charset) throws IOException;
        // 将uri转换为字符串，也就是可以直接将网络上的内容下载为字符串
        // String toString(URI uri, String charset) throws IOException;
        // 将uri转换为字符串，也就是可以直接将网络上的内容下载为字符串
        // String toString(URL url, String charset) throws IOException;
    }

    public void readLinesDemo() {

//        IOUtils.readLines()
    }

}
