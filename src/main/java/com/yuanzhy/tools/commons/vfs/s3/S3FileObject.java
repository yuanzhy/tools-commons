package com.yuanzhy.tools.commons.vfs.s3;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.provider.AbstractFileName;
import org.apache.commons.vfs2.provider.AbstractFileObject;

public class S3FileObject extends AbstractFileObject<S3FileSystem> {

    private final S3FileSystem fs;

    protected S3FileObject(AbstractFileName name, S3FileSystem fileSystem) {
        super(name, fileSystem);
        this.fs = fileSystem;
//        this.hdfs = hdfs;
//        this.path = p;
    }

    @Override
    protected long doGetContentSize() throws Exception {
        // TODO
        return getClient().getObject(GetObjectArgs.builder().build()).headers().size();
    }

    @Override
    protected FileType doGetType() throws Exception {
        return null;
    }

    @Override
    protected String[] doListChildren() throws Exception {
        return new String[0];
    }

    private MinioClient getClient() {
        return getAbstractFileSystem().getClient();
    }
}
