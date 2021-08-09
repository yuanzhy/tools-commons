package com.yuanzhy.tools.commons.vfs.s3;

import org.apache.commons.vfs2.FileContent;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.VFS;

import java.io.IOException;
import java.util.Arrays;

public class S3Demo {

    public void s3() throws IOException {
        FileSystemManager fsMgr = VFS.getManager();
        FileObject fo = fsMgr.resolveFile("s3://172.18.1.1:9000/cache/files/abc.docx");
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
        System.out.println("type："+fo.getType());
        if (fo.getType().hasChildren()) {
            System.out.println("child："+fo.getChild("test.txt"));
            System.out.println("children："+ Arrays.toString(fo.getChildren()));
        }
        if (fo.getType().hasContent()) {
            FileContent fc = fo.getContent();
            // InputStream is = fc.getInputStream();
            // byte[] bytes = fc.getByteArray();
            System.out.println(fc.getString("UTF-8"));
        }
        if (fo.isWriteable()) {
            int suc = fo.delete(Selectors.EXCLUDE_SELF);
            System.out.println(suc);
        }
        // 会同时关闭FileContent并释放FileObject
        fo.close();
        // 关闭文件系统，释放连接，清除缓存等
        fsMgr.close();
    }

}
