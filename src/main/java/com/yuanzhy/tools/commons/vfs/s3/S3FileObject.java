package com.yuanzhy.tools.commons.vfs.s3;

import io.minio.CopyObjectArgs;
import io.minio.CopySource;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.GetObjectTagsArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.errors.ErrorResponseException;
import io.minio.messages.Item;
import io.minio.messages.Tags;
import kotlin.Pair;

import org.apache.commons.vfs2.Capability;
import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileNotFolderException;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.NameScope;
import org.apache.commons.vfs2.provider.AbstractFileObject;
import org.apache.commons.vfs2.provider.GenericFileName;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class S3FileObject extends AbstractFileObject<S3FileSystem> {

    private final S3FileSystem fs;
    private final S3FileName fileName;
    protected S3FileObject(S3FileName name, S3FileSystem fileSystem) {
        super(name, fileSystem);
        this.fileName = name;
        this.fs = fileSystem;
//        this.hdfs = hdfs;
//        this.path = p;
    }

    @Override
    protected InputStream doGetInputStream(int bufferSize) throws Exception {
        return getObject();
    }

    @Override
    protected long doGetContentSize() throws Exception {
        StatObjectArgs args = StatObjectArgs.builder().bucket(fileName.getBucketName()).object(fileName.getS3path()).build();
        StatObjectResponse res = getClient().statObject(args);
        return res.size();
    }

    @Override
    protected FileType doGetType() throws Exception {
        if (fileName.getType() == FileType.FILE) {
            if (getObject() == null) {
                return FileType.IMAGINARY;
            }
        }
        return fileName.getType();
    }

    private GetObjectResponse getObject() throws Exception {
        StatObjectArgs argss = StatObjectArgs.builder().bucket(fileName.getBucketName()).object(fileName.getS3path()).build();
        StatObjectResponse rr = getClient().statObject(argss);
        System.out.println(rr);


        GetObjectArgs args = GetObjectArgs.builder().bucket(this.fileName.getBucketName()).object(fileName.getS3path()).build();
        try {
            GetObjectResponse res = getClient().getObject(args);
            Iterator<Pair<String, String>> ite =  res.headers().iterator();
            while (ite.hasNext()) {
                Pair<String, String> pair = ite.next();
                System.out.println(pair.toString());
            }
            return res;
        } catch (ErrorResponseException e) {
            if (e.response().code() == 404) {
                return null;
            }
            throw e;
        }
    }

    @Override
    protected String[] doListChildren() throws Exception {
        ListObjectsArgs args = ListObjectsArgs.builder().bucket(this.fileName.getBucketName()).prefix(fileName.getS3path()).build();
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
        RemoveObjectArgs args = RemoveObjectArgs.builder().bucket(fileName.getBucketName()).object(fileName.getS3path()).build();
        getClient().removeObject(args);
    }

    @Override
    public Path getPath() {
        return null;
    }

    @Override
    protected FileObject[] doListChildrenResolved() throws Exception {
        if (this.doGetType() != FileType.FOLDER) {
            return null;
        }
        final String[] children = doListChildren();
        final FileObject[] fo = new FileObject[children.length];
        for (int i = 0; i < children.length; i++) {
            String name = fileName.getRootURI() + fileName.getBucketName() + children[i];
            fo[i] = getFileSystem().getFileSystemManager().resolveFile(name, getFileSystem().getFileSystemOptions());
        }
        return fo;
    }

    private MinioClient getClient() {
        return getAbstractFileSystem().getClient();
    }
}
