package com.yuanzhy.tools.commons.vfs;

import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs2.FileContent;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.RandomAccessContent;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.util.RandomAccessMode;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;

public class LocalFiles {
    // 总demo
    @Test
    public void localFiles() throws IOException {
        FileSystemManager fsMgr = VFS.getManager();
//        FileObject jarFile = fsMgr.resolveFile("jar:lib/aJarFile.jar");
        // 本地文件, 只需要关闭FileObject
        String path = "E:\\yuanzhy\\ai";
        FileObject fo = fsMgr.toFileObject(new File(path));
        System.out.println(fo.getFileSystem());
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

    @Test
    public void readFileContent() throws IOException {
        // 读取文件内容
        // 支持获取字符串，流，字节数组等
        FileSystemManager fsMgr = VFS.getManager();
        String path = "E:\\yuanzhy\\yyhc.py";
        FileObject fo = fsMgr.toFileObject(new File(path));
        if (fo.isFile()) {
            FileContent fc = fo.getContent();
            // fc.getInputStream();
            // fc.getByteArray();
            // 获取内容 - 字符串形式
            String content = fc.getString("UTF-8");
            System.out.println(content);
        }
        // 会同时关闭FileContent并释放FileObject
        // 应该放在finally或者try-resources中关闭，此处只是示例
        fo.close();
        // 关闭文件系统，释放连接，清除缓存等
        fsMgr.close();
    }

    @Test
    public void readFileAttr() throws IOException {
        // 读取文件只读的属性信息
        // 只有本地Jar文件 和 HDFS文件支持此功能,其他类型文件获取的为空
        // jar属性就是manifest中的属性

        // HDFS支持的属性都在HdfsFileAttributes枚举中
        // 访问时间：HdfsFileAttributes.LAST_ACCESS_TIME
        // 块大小：HdfsFileAttributes.BLOCK_SIZE
        // 用户组：HdfsFileAttributes.GROUP
        // 所有者：HdfsFileAttributes.OWNER
        // 操作权限：HdfsFileAttributes.PERMISSIONS
        // 文件大小：HdfsFileAttributes.LENGTH
        // 修改时间：HdfsFileAttributes.MODIFICATION_TIME
        FileSystemManager fsMgr = VFS.getManager();
        String path = "E:\\yuanzhy\\yyhc.py";
        FileObject fo = fsMgr.toFileObject(new File(path));
        if (fo.isFile()) {
            FileContent fc = fo.getContent();
            // 获取只读的文件属性
            Map<String, Object> attrs = fc.getAttributes();
            System.out.println(fc.getAttributes());
        }
        // 会同时关闭FileContent并释放FileObject
        // 应该放在finally或者try-resources中关闭，此处只是示例
        fo.close();
        // 关闭文件系统，释放连接，清除缓存等
        fsMgr.close();
    }

    @Test
    public void readFile() throws IOException {
        // 读取文件只读的属性信息
        // 只有本地Jar文件 和 HDFS文件支持此功能,其他类型文件获取的为空
        // jar属性就是manifest中的属性

        // HDFS支持的属性都在HdfsFileAttributes枚举中
        // 访问时间：HdfsFileAttributes.LAST_ACCESS_TIME
        // 块大小：HdfsFileAttributes.BLOCK_SIZE
        // 用户组：HdfsFileAttributes.GROUP
        // 所有者：HdfsFileAttributes.OWNER
        // 操作权限：HdfsFileAttributes.PERMISSIONS
        // 文件大小：HdfsFileAttributes.LENGTH
        // 修改时间：HdfsFileAttributes.MODIFICATION_TIME
        FileSystemManager fsMgr = VFS.getManager();
        String path = "E:\\yuanzhy\\yyhc.py";
        FileObject fo = fsMgr.toFileObject(new File(path));
        if (fo.isFile()) {
            FileContent fc = fo.getContent();
            // 获取只读的文件属性
            Map<String, Object> attrs = fc.getAttributes();
            String[] attrNames = fc.getAttributeNames();
            Object attr = fc.getAttribute("attrName");
        }
        // 会同时关闭FileContent并释放FileObject
        // 应该放在finally或者try-resources中关闭，此处只是示例
        fo.close();
        // 关闭文件系统，释放连接，清除缓存等
        fsMgr.close();
    }

    public void setFilePermissions() throws IOException {
        // 设置文件属性，如可读可写可执行等
        // 只有本地文件和SFTP上的文件支持此功能
        FileSystemManager fsMgr = VFS.getManager();
        String path = "E:\\yuanzhy\\yyhc.py";
        FileObject fo = fsMgr.toFileObject(new File(path));
        if (fo.isFile()) {
            fo.setWritable(true, true);
            fo.setExecutable(true, true);
        }
        // 会同时关闭FileContent并释放FileObject
        // 应该放在finally或者try-resources中关闭，此处只是示例
        fo.close();
        // 关闭文件系统，释放连接，清除缓存等
        fsMgr.close();
    }

    public void readFolder() throws IOException {
        // 主要是获取子文件
        FileSystemManager fsMgr = VFS.getManager();
        String path = "E:\\yuanzhy";
        FileObject fo = fsMgr.toFileObject(new File(path));
        if (fo.isFolder()) {
            FileObject[] foArr = fo.getChildren();
            FileObject test = fo.getChild("test");
        }
        // 会同时关闭FileContent并释放FileObject
        // 应该放在finally或者try-resources中关闭，此处只是示例
        fo.close();
        // 关闭文件系统，释放连接，清除缓存等
        fsMgr.close();
    }

    public void delete() throws IOException {
        // 目录可通过参数删除部分子文件
        // 只有本地文件，内存文件，FTP，SFTP，HDFS支持删除
        FileSystemManager fsMgr = VFS.getManager();
        String path = "E:\\yuanzhy\\text.txt";
        FileObject fo = fsMgr.toFileObject(new File(path));
        if (fo.isFolder()) {
            // 删除此文件和所有子文件, 返回删除的数量
            fo.deleteAll(); // 同fo.delete(Selectors.SELECT_ALL);
            // 只删除所有子文件
            fo.delete(Selectors.EXCLUDE_SELF);
            // 只删除直接子文件和空目录
            fo.delete(Selectors.SELECT_CHILDREN);
            // 只删除文件
            fo.delete(Selectors.SELECT_FILES);
            // 只删除空的子目录
            fo.delete(Selectors.SELECT_FOLDERS);
            // 删除目录本身（如果包含子文件则删除失败返回0）
            fo.delete(Selectors.SELECT_SELF);
            // 目录不为空则删除失败返回false
            boolean suc = fo.delete();
        } else if (fo.isFile()) {
            // 删除文件本身
            boolean suc = fo.delete();
        }
        // 会同时关闭FileContent并释放FileObject
        // 应该放在finally或者try-resources中关闭，此处只是示例
        fo.close();
        // 关闭文件系统，释放连接，清除缓存等
        fsMgr.close();
    }

    public void writeTo() throws IOException {
        // 将文件内容写入其他地方
        // 目录可通过参数删除部分子文件
        // 只有本地文件，内存文件，FTP，SFTP，HDFS支持删除
        FileSystemManager fsMgr = VFS.getManager();
        String path = "E:\\yuanzhy\\text.txt";
        FileObject fo = fsMgr.toFileObject(new File(path));
        if (fo.isFile()) {
            FileContent fc = fo.getContent();
            // 支持写入输出流，FileContent和FileObject中
//            fc.write(FileObject);
//            fc.write(FileContent);
//            fc.write(OutputStream);
        }
        // 会同时关闭FileContent并释放FileObject
        // 应该放在finally或者try-resources中关闭，此处只是示例
        fo.close();
        // 关闭文件系统，释放连接，清除缓存等
        fsMgr.close();
    }
    @Test
    public void write() throws IOException {
        // 新增or修改内容
        // 只有本地文件，内存文件，FTP，SFTP，GZIP，BZ2，HDFS支持
        // 其中只有本地文件，内存文件，FTP，SFTP支持追加写。GZIP，BZ2，HDFS只支持覆盖写
        FileSystemManager fsMgr = VFS.getManager();
        String path = "E:\\yuanzhy\\text.txt";
        FileObject fo = fsMgr.toFileObject(new File(path));
        fo.createFile();
        if (fo.isFile()) {
            FileContent fc = fo.getContent();
            OutputStream os = fc.getOutputStream();
            IOUtils.write("测试", os, "UTF-8");
            os.close();
            // 追加写
            try {
                os = fc.getOutputStream(true);
                IOUtils.write("追加数据", os, "UTF-8");
            } catch (FileSystemException e) {
                // 不支持追加写入
                System.err.println("不支持追加写入");
            }
        }
        // 会同时关闭FileContent并释放FileObject
        // 应该放在finally或者try-resources中关闭，此处只是示例
        fo.close();
        // 关闭文件系统，释放连接，清除缓存等
        fsMgr.close();
    }

    @Test
    public void randomAccess() throws IOException {
        // 随机只读：本地文件，内存文件，FTP，SFTP，HDFS，HTTP支持
        // 随机读写：本地文件，内存文件支持
        FileSystemManager fsMgr = VFS.getManager();
        String path = "E:\\yuanzhy\\text.txt";
        FileObject fo = fsMgr.toFileObject(new File(path));
        if (fo.isFile()) {
            FileContent fc = fo.getContent();
            try {
                RandomAccessContent rac = fc.getRandomAccessContent(RandomAccessMode.READ);
                // ... ...
            } catch (FileSystemException e) {
                // 不支持RandomAccessMode.READ
            }
        }
        // 会同时关闭FileContent并释放FileObject
        // 应该放在finally或者try-resources中关闭，此处只是示例
        fo.close();
        // 关闭文件系统，释放连接，清除缓存等
        fsMgr.close();
    }
}
