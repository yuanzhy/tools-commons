package com.yuanzhy.tools.commons.vfs.s3;

import io.minio.CopyObjectArgs;
import io.minio.CopySource;
import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.messages.Item;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.provider.AbstractFileObject;
import org.apache.commons.vfs2.provider.GenericFileName;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class S3FileObject extends AbstractFileObject<S3FileSystem> {

    private final S3FileSystem fs;
    private final GenericFileName fileName;
    protected S3FileObject(GenericFileName name, S3FileSystem fileSystem) {
        super(name, fileSystem);
        this.fileName = name;
        this.fs = fileSystem;
//        this.hdfs = hdfs;
//        this.path = p;
    }

    @Override
    protected long doGetContentSize() throws Exception {
        GetObjectArgs args = GetObjectArgs.builder()./*bucket(this.fileName.bucketName).*/object(fileName.getPath()).build();
        return getClient().getObject(args).headers().size();
    }

    @Override
    protected FileType doGetType() throws Exception {
        return fileName.getType();
    }

    @Override
    protected String[] doListChildren() throws Exception {
        ListObjectsArgs args = ListObjectsArgs.builder().prefix(fileName.getPath()).build();
        Iterator<Result<Item>> ite = getClient().listObjects(args).iterator();
        List<String> result = new ArrayList<>();
        while (ite.hasNext()) {
            Result<Item> r = ite.next();
            result.add(r.get().objectName());
        }
        return result.toArray(new String[0]);
    }

    @Override
    protected void doRename(FileObject newFile) throws Exception {
        CopyObjectArgs args = CopyObjectArgs.builder().source(CopySource.builder().object(fileName.getPath()).build()).object(newFile.getName().getPath()).build();
        getClient().copyObject(args);
    }

    @Override
    protected void doDelete() throws Exception {
        RemoveObjectArgs args = RemoveObjectArgs.builder().object(fileName.getPath()).build();
        getClient().removeObject(args);
    }

    private MinioClient getClient() {
        return getAbstractFileSystem().getClient();
    }
}
