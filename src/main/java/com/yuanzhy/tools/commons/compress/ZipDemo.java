package com.yuanzhy.tools.commons.compress;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

/**
 *
 * @author yuanzhy
 * @date 2021-07-09
 */
public class ZipDemo {

    @Test
    public void zip() throws IOException {
        InputStream is = new FileInputStream("/home/yuanzhy/文档/啊.zip");
        String outPath = "/home/yuanzhy/文档";
        ZipInputStream zipIs = new ZipInputStream(is);
        ZipEntry nextEntry;
        while ((nextEntry = zipIs.getNextEntry()) != null) {
            String name = nextEntry.getName();
            File file = new File(outPath, name);
            //如果是目录，创建目录
            if (name.endsWith("/")) {
                file.mkdir();
            } else {
                //文件则写入具体的路径中
                FileUtils.copyToFile(zipIs, file);
            }
            //关闭当前布姆
            zipIs.closeEntry();
        }

    }
}
