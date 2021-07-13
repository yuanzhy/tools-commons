package com.yuanzhy.tools.commons.compress;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipParameters;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Deflater;

/**
 * GzipCompressorOutputStream：压缩"*.gz"文件
 * GzipCompressorInputStream：解压"*.gz"文件
 *
 * BZip2CompressorOutputStream：压缩"*.bz2"文件
 * BZip2CompressorInputStream：解压"*.bz2"文件
 *
 * XZCompressorOutputStream：压缩"*.xz"文件
 * XZCompressorInputStream：解压"*.xz"文件
 *
 * FramedLZ4CompressorOutputStream：压缩"*.lz4"文件
 * FramedLZ4CompressorInputStream：解压"*.lz4"文件
 *
 * BlockLZ4CompressorOutputStream：压缩"*.block_lz4"文件
 * BlockLZ4CompressorInputStream：解压"*.block_lz4"文件
 *
 * Pack200CompressorOutputStream：压缩"*.pack"文件
 * Pack200CompressorInputStream：解压"*.pack"文件
 *
 * DeflateCompressorOutputStream：压缩"*.deflate"文件
 * DeflateCompressorInputStream：解压"*.deflate"文件
 *
 * LZMACompressorOutputStream：压缩"*.lzma"文件
 * LZMACompressorInputStream：解压"*.lzma"文件
 *
 * FramedSnappyCompressorOutputStream：压缩"*.sz"文件
 * FramedSnappyCompressorInputStream：解压"*.sz"文件
 *
 * ZCompressorInputStream：解压"*.Z"文件​
 */
public class CompressorDemo {

    @Test
    public void ungzip() throws IOException {
        String gzFile = "/test.js.gz";
        FileInputStream is = new FileInputStream(gzFile);
        try (GzipCompressorInputStream gis = new GzipCompressorInputStream(is)) {
            GzipParameters p = gis.getMetaData();
            File targetFile = new File(FilenameUtils.getFullPath(gzFile), p.getFilename());
            FileUtils.copyToFile(gis, targetFile);
            targetFile.setLastModified(p.getModificationTime());
        }
    }
    @Test
    public void gzip() throws IOException {
        // gzip压缩
        String file = "/test.js";
        GzipParameters parameters = new GzipParameters();
        parameters.setCompressionLevel(Deflater.BEST_COMPRESSION);
        parameters.setOperatingSystem(3);
        parameters.setFilename(FilenameUtils.getName(file));
        parameters.setComment("Test file");
        parameters.setModificationTime(System.currentTimeMillis());
        FileOutputStream fos = new FileOutputStream(file + ".gz");
        try (GzipCompressorOutputStream gzos = new GzipCompressorOutputStream(fos, parameters);
            InputStream is = new FileInputStream(file)) {
            IOUtils.copy(is, gzos);
        }
    }

    public void unbz2() throws IOException {
        // 解压bz2
        String bzFile = "/test.tar.bz2";
        FileInputStream is = new FileInputStream(bzFile);
        try (BZip2CompressorInputStream bzis = new BZip2CompressorInputStream(is)) {
            File targetFile = new File("test.tar");
            FileUtils.copyToFile(bzis, targetFile);
        }
    }

    public void bz2() throws IOException {
        // 压缩bz2
        String srcFile = "/test.tar";
        String targetFile = "/test.tar.bz2";
        FileOutputStream os = new FileOutputStream(targetFile);
        try (BZip2CompressorOutputStream bzos = new BZip2CompressorOutputStream(os);
            InputStream is = new FileInputStream(srcFile)) {
            IOUtils.copy(is, bzos);
        }
    }

//    public void unDeflate() throws IOException {
//        String srcFile = "/test.deflate";
//        File targetFile = new File("test");
//        FileInputStream is = new FileInputStream(srcFile);
//        try (BlockLZ4CompressorInputStream dis = new BlockLZ4CompressorInputStream(is)) {
//            FileUtils.copyToFile(dis, targetFile);
//        }
//    }
}
