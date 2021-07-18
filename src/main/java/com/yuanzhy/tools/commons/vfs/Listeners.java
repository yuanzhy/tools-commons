package com.yuanzhy.tools.commons.vfs;

import org.apache.commons.vfs2.FileChangeEvent;
import org.apache.commons.vfs2.FileListener;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class Listeners {
    @Test
    public void listener() throws IOException {
        // 监听文件创建，修改或删除
        FileSystemManager fsMgr = VFS.getManager();
        String path = "E:\\yuanzhy\\test.txt";
        FileObject fo = fsMgr.toFileObject(new File(path));
        // 添加监听器
        fo.getFileSystem().addListener(fo, new MyListener());
        if (!fo.exists()) {
            fo.createFile();
        }
        fo.setWritable(false, false);
//        fo.delete();
        fo.close();
        fsMgr.close();
    }

    private class MyListener implements FileListener {
        @Override
        public void fileCreated(FileChangeEvent event) throws Exception {
            System.out.println("fileCreated："+event.getFileObject().getName());
        }
        @Override
        public void fileDeleted(FileChangeEvent event) throws Exception {
            System.out.println("fileDeleted："+event.getFileObject().getName());
        }
        @Override
        public void fileChanged(FileChangeEvent event) throws Exception {
            System.out.println("fileChanged："+event.getFileObject().getName());
        }
    }
}
