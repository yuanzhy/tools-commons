package com.yuanzhy.tools.commons.io;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.file.Counters;
import org.apache.commons.io.file.PathUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

public class FileUtilsDemo {

    public void readWriteDemo() throws IOException {
        File readFile = new File("test.txt");
        // 读取文件
        String str = FileUtils.readFileToString(readFile, "UTF-8");
        // 读取文件为字节数组
        byte[] bytes = FileUtils.readFileToByteArray(readFile);
        // 按行读取文件
        List<String> lines =  FileUtils.readLines(readFile, "UTF-8");

        File writeFile = new File("out.txt");
        // 将字符串写入文件
        FileUtils.writeStringToFile(writeFile, "测试文本", "UTF-8");
        // 将字节数组写入文件
        FileUtils.writeByteArrayToFile(writeFile, bytes);
        // 将字符串列表一行一行写入文件
        FileUtils.writeLines(writeFile, lines, "UTF-8");
    }

    public void moveCopyDemo() throws IOException {
        File srcFile = new File("src.txt");
        File destFile = new File("dest.txt");
        File srcDir = new File("/srcDir");
        File destDir = new File("/destDir");
        // 移动/拷贝文件
        FileUtils.moveFile(srcFile, destFile);
        FileUtils.copyFile(srcFile, destFile);
        // 移动/拷贝文件到目录
        FileUtils.moveFileToDirectory(srcFile, destDir, true);
        FileUtils.copyFileToDirectory(srcFile, destDir);
        // 移动/拷贝目录
        FileUtils.moveDirectory(srcDir, destDir);
        FileUtils.copyDirectory(srcDir, destDir);
        // 拷贝网络资源到文件
        FileUtils.copyURLToFile(new URL("http://xx"), destFile);
        // 拷贝流到文件
        FileUtils.copyInputStreamToFile(new FileInputStream("test.txt"), destFile);
        // ... ...
    }

    public void others() throws IOException {
        File file = new File("test.txt");
        File dir = new File("/test");
        // 删除文件
        FileUtils.delete(file);
        // 删除目录
        FileUtils.deleteDirectory(dir);
        // 文件大小，如果是目录则递归计算总大小
        long s = FileUtils.sizeOf(file);
        // 则递归计算目录总大小，参数不是目录会抛出异常
        long sd = FileUtils.sizeOfDirectory(dir);
        // 递归获取目录下的所有文件
        Collection<File> files = FileUtils.listFiles(dir, null, true);
        // 获取jvm中的io临时目录
        FileUtils.getTempDirectory();
        // ... ...
    }

    @Test
    public void name() throws Exception {
        // 获取名称，后缀等
        String name = "/home/xxx/test.txt";
        FilenameUtils.getName(name); // "test.txt"
        FilenameUtils.getBaseName(name); // "test"
        FilenameUtils.getExtension(name); // "txt"
        FilenameUtils.getPath(name); // "/home/xxx/"

        // 将相对路径转换为绝对路径
        FilenameUtils.normalize("/foo/bar/.."); // "/foo"
    }

    public void path() throws IOException {
        // path既可以表示目录也可以表示文件

        // 获取当前路径
        Path path = PathUtils.current();
        // 删除path
        PathUtils.delete(path);
        // 路径或文件是否为空
        PathUtils.isEmpty(path);
        // 设置只读
        PathUtils.setReadOnly(path, true);
        // 复制
        PathUtils.copyFileToDirectory(Paths.get("test.txt"), path);
        PathUtils.copyDirectory(Paths.get("/srcPath"), Paths.get("/destPath"));
        // 统计目录内文件数量
        Counters.PathCounters counters = PathUtils.countDirectory(path);
        counters.getByteCounter(); // 字节大小
        counters.getDirectoryCounter(); // 目录个数
        counters.getFileCounter(); // 文件个数
        // ... ...
    }
}
