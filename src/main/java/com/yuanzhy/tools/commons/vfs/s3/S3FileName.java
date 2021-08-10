package com.yuanzhy.tools.commons.vfs.s3;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.provider.GenericFileName;

public class S3FileName extends GenericFileName {

    private final String bucketName;
    private final String s3path;

    protected S3FileName(String hostName, int port, int defaultPort, String userName, String password, String path, FileType type) {
        super("s3", hostName, port, defaultPort, userName, password, path, type);
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (StringUtils.isEmpty(path)) {
            this.bucketName = "";
            this.s3path = "";
        } else {
            this.bucketName = path.substring(0, path.indexOf("/"));
            path = path.substring(path.indexOf("/"));
            if (type == FileType.FOLDER && !path.equals("/")) {
                path = path.concat("/");
            }
            this.s3path = path;
        }
    }

    @Override
    public FileName createName(String absolutePath, FileType fileType) {
        return new S3FileName(getHostName(), getPort(), getDefaultPort(), getUserName(), getPassword(), getPath(), getType());
    }

    @Override
    protected void appendRootUri(StringBuilder buffer, boolean addPassword) {
        buffer.append(getScheme());
        buffer.append("://");
        appendCredentials(buffer, addPassword);
        buffer.append(getHostName());
        buffer.append(':');
        buffer.append(getPort());
    }

    public String getBucketName() {
        return bucketName;
    }

    public String getS3path() {
        return s3path;
    }
}
