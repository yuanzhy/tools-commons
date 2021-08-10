package com.yuanzhy.tools.commons.vfs.s3;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.apache.commons.vfs2.FileContent;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.auth.StaticUserAuthenticator;
import org.apache.commons.vfs2.impl.DefaultFileSystemConfigBuilder;
import org.junit.Test;

public class S3Demo {

    @Test
    public void s3() throws IOException {
        FileSystemManager fsMgr = VFS.getManager();
        StaticUserAuthenticator auth = new StaticUserAuthenticator("", "username", "password");
        FileSystemOptions opts = new FileSystemOptions();
        DefaultFileSystemConfigBuilder.getInstance().setUserAuthenticator(opts, auth);
        FileObject fo = fsMgr.resolveFile("s3://172.18.11.12:9000/bucket/files/-1003241317", opts);
        if (!fo.exists()) {
            System.out.println("fo not exists");
            return;
        }
        System.out.println("parent："+fo.getParent().toString());
        System.out.println("name："+fo.getName());
        System.out.println("path："+fo.getPath());
        System.out.println("pubURI："+fo.getPublicURIString());
        System.out.println("URI："+fo.getURI().toString());
        System.out.println("URL："+fo.getURL());
        boolean isFile = fo.isFile();
        boolean isFolder = fo.isFolder();
        // 是否符号链接
        boolean isSymbolic = fo.isSymbolicLink();
        boolean executable = fo.isExecutable();
        boolean isHidden = fo.isHidden();
        boolean isReadable = fo.isReadable();
        boolean isWriteable = fo.isWriteable();
        System.out.println("type："+fo.getType());
        if (fo.getType().hasChildren()) {
            System.out.println("child："+fo.getChild("Editor.bin"));
            System.out.println(fo.getChild("Editor.bin").getContent().getSize());
            System.out.println("children："+ Arrays.toString(fo.getChildren()));
        }
        if (fo.getType().hasContent()) {
            FileContent fc = fo.getContent();
            InputStream is = fc.getInputStream();
            FileUtils.copyInputStreamToFile(is, new File("/home/yuanzhy/文档/editor.bin"));
            // byte[] bytes = fc.getByteArray();
//            System.out.println(fc.getString("UTF-8"));
        }
//        if (fo.isWriteable()) {
//            int suc = fo.delete(Selectors.EXCLUDE_SELF);
//            System.out.println(suc);
//        }
        // 会同时关闭FileContent并释放FileObject
        fo.close();
        // 关闭文件系统，释放连接，清除缓存等
        fsMgr.close();
    }

}
