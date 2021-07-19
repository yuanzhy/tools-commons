package com.yuanzhy.tools.commons.vfs;

import org.apache.commons.vfs2.CacheStrategy;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.cache.LRUFilesCache;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;

import java.io.IOException;

public class Caches {

    public void cache() throws IOException {
        StandardFileSystemManager fsMgr = new StandardFileSystemManager();
        // 手动处理缓存数据。调用 {@link FileObject#refresh()} 来刷新对象数据
        fsMgr.setCacheStrategy(CacheStrategy.MANUAL);
        // 设置缓存实现为LRU
        fsMgr.setFilesCache(new LRUFilesCache());
        fsMgr.init();
        // 本地文件, 只需要关闭FileObject
        FileObject fo = fsMgr.resolveFile("files://E/yuanzhy/test.txt");
        String c = fo.getContent().getString("UTF-8");
        System.out.println(c);
        fo.close();
        fsMgr.close();
    }
}
