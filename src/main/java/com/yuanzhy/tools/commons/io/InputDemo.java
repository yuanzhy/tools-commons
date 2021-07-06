package com.yuanzhy.tools.commons.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.AutoCloseInputStream;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.io.input.CountingInputStream;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.junit.Test;

/**
 *
 * @author yuanzhy
 * @date 2021-07-06
 */
public class InputDemo {
    /**
     * AutoCloseInputStream是一个过滤流，用来包装其他流，读取完后流会自动关掉
     * 实现原理很简单，当读取完后将底层的流关闭，然后创建一个ClosedInputStream赋值给它包装的输入流。感兴趣的可以研究下源码
     * 注：如果输入流没有全部读取是不会关掉底层流的
     * @throws Exception
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
     * @throws IOException
     */
    @Test
    public void countingInputStream() throws IOException {
        try (CountingInputStream cis = new CountingInputStream(new FileInputStream("test.txt"))) {
            String txt = IOUtils.toString(cis, "UTF-8");// 文件内容
            long size = cis.getByteCount();// 读取的字节数
        }
    }

}
