package com.yuanzhy.tools.commons.compress;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class otherDemo {

    public void factory() throws Exception {
        // 使用factory动态获取归档流
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        String archiveName = ArchiveStreamFactory.TAR;
        InputStream is = new FileInputStream("/in.tar");
        OutputStream os = new FileOutputStream("/out.tar");
        // 动态获取实现类，此时ais实际上是TarArchiveOutPutStream
        ArchiveInputStream ais = factory.createArchiveInputStream(archiveName, is);
        ArchiveOutputStream aos = factory.createArchiveOutputStream(archiveName, os);
        ais.close();
        aos.close();
    }

    public void factory2() throws Exception {
        // 使用factory动态获取压缩流
        CompressorStreamFactory factory = new CompressorStreamFactory();
        String compressName = CompressorStreamFactory.GZIP;
        InputStream is = new FileInputStream("/in.gz");
        OutputStream os = new FileOutputStream("/out.gz");
        // 动态获取实现类，此时ais实际上是TarArchiveOutPutStream
        CompressorInputStream cis = factory.createCompressorInputStream(compressName, is);
        CompressorOutputStream cos = factory.createCompressorOutputStream(compressName, os);
        cis.close();
        cos.close();
    }

    public void archiveCompress() throws IOException {
        // 解压解包test.tar.gz文件
        String outPath = "/test";
        InputStream is = new FileInputStream("/test.tar.gz");
        CompressorInputStream gis = new GzipCompressorInputStream(is);
        try (ArchiveInputStream tgis = new TarArchiveInputStream(gis)) {
            ArchiveEntry nextEntry;
            while ((nextEntry = tgis.getNextEntry()) != null) {
                String name = nextEntry.getName();
                File file = new File(outPath, name);
                //如果是目录，创建目录
                if (nextEntry.isDirectory()) {
                    file.mkdir();
                } else {
                    //文件则写入具体的路径中
                    FileUtils.copyToFile(tgis, file);
                    file.setLastModified(nextEntry.getLastModifiedDate().getTime());
                }
            }
        }
    }
}
