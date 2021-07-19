package com.yuanzhy.tools.commons.vfs;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Configs {

    @Test
    public void config() throws IOException {
        File config = new File("providers.xml");
        FileSystemManager mgr;
        if (config.exists()) {
            URL providersUrl = config.toURI().toURL();
            mgr = new StandardFileSystemManager();
            System.out.println("Custom providers configuration used: " + providersUrl);
            ((StandardFileSystemManager) mgr).setConfiguration(providersUrl);
            ((StandardFileSystemManager) mgr).init();
        } else {
            mgr = VFS.getManager();
        }
        FileObject fo = mgr.toFileObject(new File(System.getProperty("user.dir")));
        fo.close();
        mgr.close();
    }
}
