package com.yuanzhy.tools.commons.compress;

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
// 压缩
public class CompressorDemo {
    @Test
    public void ungzip() throws IOException {
        String gzFile = "/test.js.gz";
        FileInputStream is = new FileInputStream(gzFile);
        try (GzipCompressorInputStream gis = new GzipCompressorInputStream(is)) {
            GzipParameters p = gis.getMetaData();
            File srcFile = new File(FilenameUtils.getFullPath(gzFile), p.getFilename());
            FileUtils.copyToFile(gis, srcFile);
            srcFile.setLastModified(p.getModificationTime());
        }
    }

    @Test
    public void gzip() throws IOException {
        String file = "/test.js";
        GzipParameters parameters = new GzipParameters();
        parameters.setCompressionLevel(Deflater.BEST_COMPRESSION);
        parameters.setOperatingSystem(3);
        parameters.setFilename(FilenameUtils.getName(file));
        parameters.setComment("Test file");
        parameters.setModificationTime(System.currentTimeMillis());
        FileOutputStream fos = new FileOutputStream(file + ".gz");
        try (GzipCompressorOutputStream os = new GzipCompressorOutputStream(fos, parameters);
            InputStream is = new FileInputStream(file)) {
            IOUtils.copy(is, os);
        }
    }

    public void unbz2() {
        // TODO
//        BZip2CompressorInputStream bzis = new BZip2CompressorInputStream(new FileInputStream(""));
    }
}
