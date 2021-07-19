package com.yuanzhy.tools.commons.vfs;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.util.FileObjectUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class Utils {

    public void fileObjectUtils() throws IOException {
        FileSystemManager fsMgr = VFS.getManager();
        String path = "E:\\yuanzhy\\test.txt";
        FileObject fo = fsMgr.toFileObject(new File(path));
        // 是否存在
        FileObjectUtils.exists(fo);
        // 读取为byte数组
        FileObjectUtils.getContentAsByteArray(fo);
        // 读取为字符串
        FileObjectUtils.getContentAsString(fo, "UTF-8");
        // 读取properties文件
        Properties props = FileObjectUtils.readProperties(fo);
        // 写入输出流
        FileObjectUtils.writeContent(fo, new ByteArrayOutputStream());
        // 写入另一个FileObject
        FileObjectUtils.writeContent(fo, fsMgr.toFileObject(new File("newTest.txt")));
    }
}
