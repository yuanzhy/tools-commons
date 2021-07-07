package com.yuanzhy.tools.commons.io;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.AutoCloseInputStream;
import org.apache.commons.io.input.CountingInputStream;
import org.apache.commons.io.input.MessageDigestCalculatingInputStream;
import org.apache.commons.io.input.ObservableInputStream;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

/**
 *
 * @author yuanzhy
 * @date 2021-07-06
 */
public class StreamDemo {
    /**
     * AutoCloseInputStream是一个过滤流，用来包装其他流，读取完后流会自动关掉
     * 实现原理很简单，当读取完后将底层的流关闭，然后创建一个ClosedInputStream赋值给它包装的输入流。
     * 注：如果输入流没有全部读取是不会关掉底层流的
     */
    public void autoClose() throws Exception {
        InputStream is = new FileInputStream("test.txt");
        AutoCloseInputStream acis = new AutoCloseInputStream(is);
        IOUtils.toByteArray(acis); // 将流全部读完
        // 可以省略关闭流的逻辑了
    }

    @Test
    public void reversedLinesFileReader() throws IOException {
        // 从后向前按行读取
        try (ReversedLinesFileReader reader = new ReversedLinesFileReader(
                new File("test.txt"),
                Charset.forName("UTF-8"))) {
            String lastLine = reader.readLine(); // 读取最后一行
            List<String> line5 = reader.readLines(5); // 再读5行
        }
    }

    /**
     * 大家都知道只给一个输入流咱们是没办法准确的知道它的大小的，虽说流提供了available()方法
     * 但是这个方法只有在ByteArrayInputStream的情况下拿到的是准确的大小，其他如文件流网络流等都是不准确的
     * （当然用野路子也可以实现，比如写入临时文件通过File.length()方法获取，然后在将文件转换为文件流）
     * 下面这个流可以实现计数功能，当把文件读完大小也就计算出来了
     */
    @Test
    public void countingInputStream() {
        try (CountingInputStream cis = new CountingInputStream(new FileInputStream("test.txt"))) {
            String txt = IOUtils.toString(cis, "UTF-8");// 文件内容
            long size = cis.getByteCount();// 读取的字节数
        } catch (IOException e) {
            // 异常处理
        }
    }

    /**
     * 可观察的输入流（典型的观察者模式），可实现边读取边处理
     * 比如将某些字节替换为另一个字节，计算md5摘要等
     * 当然你也可以完全写到文件里后在做处理，这样相当于做了两次遍历，性能较差
     * @throws IOException
     */
    @Test
    public void observableInputStream() {
        // ​这是一个基类，使用时需要继承它来扩展自己的流，示例如下：
    }
    private class MyObservableInputStream extends ObservableInputStream {
        class MyObserver extends Observer {
            @Override
            public void data(final int input) throws IOException {
                // 做自定义处理
            }
            @Override
            public void data(final byte[] input, final int offset, final int length) throws IOException {
                // 做自定义处理
            }
        }
        public MyObservableInputStream(InputStream inputStream) {
            super(inputStream);
        }
    }

    @Test
    public void testMD5() throws Exception {
        MessageDigestCalculatingInputStream md5InputStream = new MessageDigestCalculatingInputStream(new FileInputStream("index.html"));
        IOUtils.copy(md5InputStream, new FileOutputStream("index2.html"));
        System.out.println(Hex.encodeHexString(md5InputStream.getMessageDigest().digest()));
    }

    /**
     * 其他的还有很多，不做过多介绍了
     */
    public void others() {
        // BOMInputStream: 同时读取文本文件的bom头
        // BoundedInputStream：有界的流，控制只允许读取前x个字节
        // BrokenInputStream: 一个错误流，永远抛出IOException
        // CharSequenceInputStream: 支持StringBuilder,StringBuffer等读取
        // LockableFileWriter: 带锁的Writer，同一个文件同时只允许一个流写入，多余的写入操作会跑出IOException
        // StringBuilderWriter: StringBuilder的Writer
        // ... ...
    }
}
