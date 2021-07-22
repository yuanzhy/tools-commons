package com.yuanzhy.tools.commons.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.junit.Test;

/**
 *
 * @author yuanzhy
 * @date 2021-07-09
 */
public class FtpDemo {

    public FTPClient connect(String host, int port, String username, String password) throws IOException {
        FTPClient ftpClient = new FTPClient();
        /**设置文件传输的编码*/
        ftpClient.setControlEncoding("UTF-8");

        /**连接 FTP 服务器
         * 如果连接失败，则此时抛出异常，如ftp服务器服务关闭时，抛出异常：
         * java.net.ConnectException: Connection refused: connect*/
        ftpClient.connect(host, port);
        /**登录 FTP 服务器
         * 1）如果传入的账号为空，则使用匿名登录，此时账号使用 "Anonymous"，密码为空即可*/
        if (username == null && password == null) {
            ftpClient.login("Anonymous", "");
        } else {
            ftpClient.login(username, password);
        }

        /** 设置传输的文件类型
         * BINARY_FILE_TYPE：二进制文件类型
         * ASCII_FILE_TYPE：ASCII传输方式，这是默认的方式
         * ....
         */
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

        /**
         * 确认应答状态码是否正确完成响应
         * 凡是 2开头的 isPositiveCompletion 都会返回 true，因为它底层判断是：
         * return (reply >= 200 && reply < 300);
         */
        int reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            /**
             * 如果 FTP 服务器响应错误 中断传输、断开连接
             * abort：中断文件正在进行的文件传输，成功时返回 true,否则返回 false
             * disconnect：断开与服务器的连接，并恢复默认参数值
             */
            ftpClient.abort();
            ftpClient.disconnect();
        } else {
            System.out.println("连接成功");
        }

        return ftpClient;
    }

    /**
     * 关闭FTP方法
     * @param ftpClient ftp客户端
     * @return
     */
    public void disconnect(FTPClient ftpClient) throws IOException {
        try {
            ftpClient.logout();
        } finally {
            if (ftpClient.isConnected()) {
                ftpClient.disconnect();
            }
        }
    }


    /**
     * 下载FTP下指定文件
     * @param ftp FTPClient对象
     * @param filePath FTP文件路径
     * @param fileName 文件名
     * @param downPath 下载保存的目录
     * @return
     */
    public void download(FTPClient ftp, String filePath, String fileName, String downPath) throws IOException {
        // 跳转到文件目录
        ftp.changeWorkingDirectory(filePath);
        // 获取目录下文件集合
        ftp.enterLocalPassiveMode();
        FTPFile[] files = ftp.listFiles();
        for (FTPFile file : files) {
            // 取得指定文件并下载
            if (file.getName().equals(fileName)) {
                File downFile = new File(downPath + File.separator + file.getName());
                try (OutputStream out = new FileOutputStream(downFile)) {
                    // 绑定输出流下载文件,需要设置编码集，不然可能出现文件为空的情况
                    boolean suc = ftp.retrieveFile(
                            new String(file.getName().getBytes("UTF-8"), "ISO-8859-1"), out);
                    // 下载成功删除文件,看项目需求
                    // ftp.deleteFile(new String(fileName.getBytes("UTF-8"),"ISO-8859-1"));
                    if (suc) {
                        System.out.println("下载成功");
                    } else {
                        System.err.println("下载失败");
                    }
                }
            }
        }
    }

    /**
     * FTP文件上传工具类
     * @param ftpClient ftp客户端
     * @param filePath  文件在本地的路径
     * @param ftpPath   ftp上的路径
     * @return
     */
    public void uploadFile(FTPClient ftpClient, String filePath, String ftpPath) {
        //上传文件
        File file = new File(filePath);
        try (InputStream in = new FileInputStream(file)) {
            // 设置PassiveMode传输
            ftpClient.enterLocalPassiveMode();
            //设置二进制传输，使用BINARY_FILE_TYPE，ASC容易造成文件损坏
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            //判断FPT目标文件夹时候存在不存在则创建
            if (!ftpClient.changeWorkingDirectory(ftpPath)) {
                ftpClient.makeDirectory(ftpPath);
            }
            //跳转目标目录
            ftpClient.changeWorkingDirectory(ftpPath);
            String tempName = ftpPath + File.separator + file.getName();
            boolean suc = ftpClient.storeFile(new String(tempName.getBytes("UTF-8"), "ISO-8859-1"), in);
            if (suc) {
                System.out.println("上传成功");
            } else {
                System.err.println("上传失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("上传失败");
        }
    }

    /**
     * FPT上文件的复制
     * @param ftpClient  FTPClient对象
     * @param olePath    原文件地址
     * @param newPath    新保存地址
     * @param fileName   文件名
     * @return
     */
    public void copyFile(FTPClient ftpClient, String olePath, String newPath, String fileName) {
        try {
            // 跳转到文件目录
            ftpClient.changeWorkingDirectory(olePath);
            //设置连接模式，不设置会获取为空
            ftpClient.enterLocalPassiveMode();
            // 获取目录下文件集合
            FTPFile[] files = ftpClient.listFiles();
            for (FTPFile file : files) {
                // 取得指定文件并下载
                if (file.getName().equals(fileName)) {
                    // 读取文件，使用下载文件的方法把文件写入内存,绑定到out流上
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    ftpClient.retrieveFile(
                            new String(file.getName().getBytes("UTF-8"), "ISO-8859-1"), out);
                    ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
                    //创建新目录
                    ftpClient.makeDirectory(newPath);
                    //文件复制，先读，再写
                    //二进制
                    ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                    boolean suc = ftpClient.storeFile(
                            newPath + File.separator + (new String(file.getName().getBytes("UTF-8"),
                                    "ISO-8859-1")), in);
                    out.flush();
                    if (suc) {
                        System.out.println("转存成功");
                    } else {
                        System.err.println("复制失败");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 实现文件的移动，这里做的是一个文件夹下的所有内容移动到新的文件，
     * 如果要做指定文件移动，加个判断判断文件名
     * 如果不需要移动，只是需要文件重命名，可以使用ftp.rename(oleName,newName)
     * @param ftpClient
     * @param oldPath
     * @param newPath
     * @return
     */
    public void moveFile(FTPClient ftpClient, String oldPath, String newPath) {
        try {
            ftpClient.changeWorkingDirectory(oldPath);
            ftpClient.enterLocalPassiveMode();
            //获取文件数组
            FTPFile[] files = ftpClient.listFiles();
            //新文件夹不存在则创建
            if(!ftpClient.changeWorkingDirectory(newPath)){
                ftpClient.makeDirectory(newPath);
            }
            //回到原有工作目录
            ftpClient.changeWorkingDirectory(oldPath);
            for (FTPFile file : files) {
                //转存目录
                boolean suc = ftpClient.rename(new String(file.getName().getBytes("UTF-8"), "ISO-8859-1"),
                        newPath + File.separator + new String(file.getName().getBytes("UTF-8"),
                                "ISO-8859-1"));
                if (suc) {
                    System.out.println(file.getName() + "移动成功");
                } else {
                    System.err.println(file.getName() + "移动失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除FTP上指定文件夹下文件及其子文件方法，添加了对中文目录的支持
     * @param ftpClient FTPClient对象
     * @param ftpFolder 需要删除的文件夹
     * @return
     */
    public void deleteByFolder(FTPClient ftpClient, String ftpFolder) {
        try {
            ftpClient.changeWorkingDirectory(new String(ftpFolder.getBytes("UTF-8"), "ISO-8859-1"));
            ftpClient.enterLocalPassiveMode();
            FTPFile[] files = ftpClient.listFiles();
            for (FTPFile file : files) {
                //判断为文件则删除
                if(file.isFile()){
                    ftpClient.deleteFile(new String(file.getName().getBytes("UTF-8"), "ISO-8859-1"));
                }
                //判断是文件夹
                if(file.isDirectory()){
                    String childPath = ftpFolder + File.separator+file.getName();
                    //递归删除子文件夹
                    deleteByFolder(ftpClient,childPath);
                }
            }
            //循环完成后删除文件夹
            boolean suc = ftpClient.removeDirectory(new String(ftpFolder.getBytes("UTF-8"), "ISO-8859-1"));
            if (suc) {
                System.out.println(ftpFolder + "文件夹删除成功");
            } else {
                System.err.println(ftpFolder + "文件夹删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 遍历解析文件夹下所有文件
     * @param folderPath 需要解析的的文件夹
     * @param ftp FTPClient对象
     */
    public void readFileByFolder(FTPClient ftp,String folderPath) throws IOException {
        ftp.changeWorkingDirectory(new String(folderPath.getBytes("UTF-8"), "ISO-8859-1"));
        //设置FTP连接模式
        ftp.enterLocalPassiveMode();
        //获取指定目录下文件文件对象集合
        FTPFile files[] = ftp.listFiles();
        for (FTPFile file : files) {
            //判断为txt文件则解析
            if (file.isFile()) {
                String fileName = file.getName();
                if (!fileName.endsWith(".txt")) {
                    continue;
                }
                try (InputStream in = ftp.retrieveFileStream(
                        new String(file.getName().getBytes("UTF-8"), "ISO-8859-1"))) {
                    //这里就把一个txt文件完整解析成了个字符串，就可以调用实际需要操作的方法
                    System.out.println(IOUtils.toString(in, "UTF-8"));
                    //ftp.retrieveFileStream使用了流，需要释放一下，不然会返回空指针
                    ftp.completePendingCommand();
                }
            }
            //判断为文件夹，递归
            else if (file.isDirectory()) {
                String path = folderPath + File.separator + file.getName();
                readFileByFolder(ftp, path);
            }
        }
    }

    @Test
    public void test() {
        try {
            FTPClient ftpClient = this.connect("192.168.1.11", 21, "user","password");
            // this.download(ftpClient, "/file", "你好.jpg", "C:\\下载");
            // this.copyFile(ftpClient, "/file", "/txt/temp", "你好.txt");
            // this.uploadFile(ftpClient, "C:\\下载\\你好.jpg", "/");
            // this.moveFile(ftpClient, "/file", "/txt/temp");
            // this.deleteByFolder(ftpClient, "/txt");
            this.readFileByFolder(ftpClient, "/");
            this.disconnect(ftpClient);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
