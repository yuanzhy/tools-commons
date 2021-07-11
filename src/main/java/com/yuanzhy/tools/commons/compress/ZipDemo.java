package com.yuanzhy.tools.commons.compress;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author yuanzhy
 * @date 2021-07-09
 */
public class ZipDemo {

    @Test
    public void javaZip() throws IOException {
        // 早期java版本解压zip中文文件名会乱码，目测Java8是正正常的
        InputStream is = new FileInputStream("/home/yuanzhy/文档/啊.zip");
        String outPath = "/home/yuanzhy/文档";
        try (ZipInputStream zipIs = new ZipInputStream(is)) {
            ZipEntry nextEntry;
            while ((nextEntry = zipIs.getNextEntry()) != null) {
                String name = nextEntry.getName();
                File file = new File(outPath, name);
                //如果是目录，创建目录
                if (nextEntry.isDirectory()) {
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
    @Test
    public void commonsZip() throws IOException {
        // commons不需要手动关闭nextEntry
        InputStream is = new FileInputStream("/home/yuanzhy/文档/啊.zip");
        String outPath = "/home/yuanzhy/文档";
        try (ZipArchiveInputStream zis = new ZipArchiveInputStream(is)) {
            ZipArchiveEntry nextEntry;
            while ((nextEntry = zis.getNextZipEntry()) != null) {
                String name = nextEntry.getName();
                File file = new File(outPath, name);
                //如果是目录，创建目录
                if (nextEntry.isDirectory()) {
                    file.mkdir();
                } else {
                    //文件则写入具体的路径中
                    FileUtils.copyToFile(zis, file);
                }
            }
        }
    }
}
