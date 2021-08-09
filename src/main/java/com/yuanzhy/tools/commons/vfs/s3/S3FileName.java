//package com.yuanzhy.tools.commons.vfs.s3;
//
//import org.apache.commons.vfs2.FileName;
//import org.apache.commons.vfs2.FileType;
//import org.apache.commons.vfs2.provider.GenericFileName;
//
//public class S3FileName extends GenericFileName {
//
//    private final String bucketName;
//    private final String s3path;
//
//    protected S3FileName(String hostName, int port, int defaultPort, String userName, String password, String path, FileType type) {
//        super("s3", hostName, port, defaultPort, userName, password, path, type);
//        this.bucketName = path.substring(0, path.indexOf("/"));
//        this.s3path = path.substring(path.indexOf("/"));
//    }
//
//    @Override
//    public FileName createName(String absolutePath, FileType fileType) {
//        return new S3FileName(getHostName(), getPort(), getUserName(), getPassword(), getPath(), getType());
//    }
//
//    public String getBucketName() {
//        return bucketName;
//    }
//
//    public String getS3path() {
//        return s3path;
//    }
//}
