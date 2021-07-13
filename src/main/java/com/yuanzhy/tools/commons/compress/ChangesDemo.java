package com.yuanzhy.tools.commons.compress;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.changes.ChangeSet;
import org.apache.commons.compress.changes.ChangeSetPerformer;
import org.apache.commons.compress.changes.ChangeSetResults;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ChangesDemo {
    @Test
    public void changes() throws IOException {
        String tarFile = "/test.tar";
        InputStream is = new FileInputStream(tarFile);
        // 替换后会覆盖原test.tar，如果是windows可能会由于文件被访问而覆盖报错
        OutputStream os = new FileOutputStream(tarFile);
        try (TarArchiveInputStream tais = new TarArchiveInputStream(is);
             TarArchiveOutputStream taos = new TarArchiveOutputStream(os)) {
            ChangeSet changes = new ChangeSet();
            // 删除"test.tar中"的"dir/1.txt"文件
            changes.delete("dir/1.txt");
            // 删除"test.tar"中的"t"目录
            changes.delete("t");
            // 添加文件，如果已存在则替换
            File addFile = new File("/a.txt");
            ArchiveEntry addEntry = taos.createArchiveEntry(addFile, addFile.getName());
            // add可传第三个参数：true: 已存在则替换(默认值)， false: 不替换
            changes.add(addEntry, new FileInputStream(addFile));
            // 执行修改
            ChangeSetPerformer performer = new ChangeSetPerformer(changes);
            ChangeSetResults result = performer.perform(tais, taos);
        }
    }
}
