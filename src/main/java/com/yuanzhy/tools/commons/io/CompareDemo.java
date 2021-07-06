package com.yuanzhy.tools.commons.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.comparator.CompositeFileComparator;
import org.apache.commons.io.comparator.DirectoryFileComparator;
import org.apache.commons.io.comparator.NameFileComparator;
import org.apache.commons.io.input.MessageDigestCalculatingInputStream;
import org.junit.Test;

/**
 *
 * @author yuanzhy
 * @date 2021-07-06
 */
public class CompareDemo {
    /**
     * 现成的文件比较器
     * DefaultFileComparator   // 默认文件比较器，直接使用File的compare方法。（文件集合排序（  Collections.sort()  ）时传此比较器和不传效果一样）
     * DirectoryFileComparator  // 目录排在文件之前
     * ExtensionFileComparator  // 扩展名比较器，按照文件的扩展名的ascii顺序排序，无扩展名的始终排在前面
     * LastModifiedFileComparator // 按照文件的最后修改时间排序
     * NameFileComparator // 按照文件名称排序
     * PathFileComparator // 按照路径排序，父目录优先排在前面
     * SizeFileComparator // 按照文件大小排序，小文件排在前面（目录会计算其总大小）
     * CompositeFileComparator // 组合排序，将以上排序规则组合在一起
     */
    @Test
    public void compare() {
//        List<File> files = Files.list(Paths.get("/home/yuanzhy/图片")).map(Path::toFile).collect(Collectors.toList());
        List<File> files = Arrays.asList(new File[]{
                new File("/foo/def"),
                new File("/foo/test.txt"),
                new File("/foo/abc"),
                new File("/foo/hh.txt")});
        // 排序目录在前
        Collections.sort(files, DirectoryFileComparator.DIRECTORY_COMPARATOR); // ["/foo/def", "/foo/abc", "/foo/test.txt", "/foo/hh.txt"]
        // 排序目录在后
        Collections.sort(files, DirectoryFileComparator.DIRECTORY_REVERSE); // ["/foo/test.txt", "/foo/hh.txt", "/foo/def", "/foo/abc"]
        // 组合排序，首先按目录在前排序，其次再按照名称排序
        Comparator dirAndNameComp = new CompositeFileComparator(
                    DirectoryFileComparator.DIRECTORY_COMPARATOR,
                    NameFileComparator.NAME_COMPARATOR);
        Collections.sort(files, dirAndNameComp); // ["/foo/abc", "/foo/def", "/foo/hh.txt", "/foo/test.txt"]
    }

    @Test
    public void t() throws Exception {
        MessageDigestCalculatingInputStream md5InputStream = new MessageDigestCalculatingInputStream(new FileInputStream("/home/yuanzhy/文档/index.html"));
        IOUtils.copy(md5InputStream, new FileOutputStream("/home/yuanzhy/文档/index2.html"));
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(FileUtils.readFileToByteArray(new File("/home/yuanzhy/文档/index2.html")));
        System.out.println(bytesToHex(md.digest()));
//        System.out.println(bytesToHex(md5InputStream.getMessageDigest().digest()));
    }


    public static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if(hex.length() < 2){
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
