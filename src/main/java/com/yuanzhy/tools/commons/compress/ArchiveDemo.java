package com.yuanzhy.tools.commons.compress;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
// 归档
public class ArchiveDemo {

    public void tar() throws IOException {
        // ar，arj，cpio，dump，zip，jar 使用方法类似，就不做代码示例了
        InputStream is = new FileInputStream("/home/yuanzhy/文档/啊.zip");
        String outPath = "/home/yuanzhy/文档";
        try (TarArchiveInputStream tis = new TarArchiveInputStream(is)) {
            TarArchiveEntry nextEntry;
            while ((nextEntry = tis.getNextTarEntry()) != null) {
                String name = nextEntry.getName();
                File file = new File(outPath, name);
                //如果是目录，创建目录
                if (nextEntry.isDirectory()) {
                    file.mkdir();
                } else {
                    //文件则写入具体的路径中
                    FileUtils.copyToFile(tis, file);
                    file.setLastModified(nextEntry.getLastModifiedDate().getTime());
                }
            }
        }
//        TarArchiveOutputStream tos = new TarArchiveOutputStream(new FileOutputStream(""));
    }
    @Test
    public void un7z() throws IOException {
        // 7z解压
        String outPath = "/home/yuanzhy/文档";
        try (SevenZFile archive = new SevenZFile(new File("test.7z"))) {
            SevenZArchiveEntry entry;
            while ((entry = archive.getNextEntry()) != null) {
                File file = new File(outPath, entry.getName());
                if (entry.isDirectory()) {
                    file.mkdirs();
                }
                if (entry.hasStream()) {
                    final byte [] buf = new byte [1024];
                    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    for (int len = 0; (len = archive.read(buf)) > 0;) {
                        baos.write(buf, 0, len);
                    }
                    FileUtils.writeByteArrayToFile(file, baos.toByteArray());
                }
            }
        }
    }

    @Test
    public void _7z() throws IOException {
        // 7z压缩
        try (SevenZOutputFile outputFile = new SevenZOutputFile(new File("/test.7z"))) {
            File srcFile = new File("/test");
            _7zRecusive(outputFile, srcFile, "");
        }
    }
    private void _7zRecusive(SevenZOutputFile _7zFile, File srcFile, String basePath) throws IOException {
        if (srcFile.isDirectory()) {
            File[] files = srcFile.listFiles();
            String nextBasePath = basePath + srcFile.getName() + "/";
            // 空目录
            if (ArrayUtils.isEmpty(files)) {
                SevenZArchiveEntry entry = _7zFile.createArchiveEntry(srcFile, nextBasePath);
                _7zFile.putArchiveEntry(entry);
                _7zFile.closeArchiveEntry();
            } else {
                for (File file : files) {
                    _7zRecusive(_7zFile, file, nextBasePath);
                }
            }
        } else {
            SevenZArchiveEntry entry = _7zFile.createArchiveEntry(srcFile, basePath + srcFile.getName());
            _7zFile.putArchiveEntry(entry);
            byte[] bs = FileUtils.readFileToByteArray(srcFile);
            _7zFile.write(bs);
            _7zFile.closeArchiveEntry();
        }
    }
}
