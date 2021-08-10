package com.yuanzhy.tools.commons.vfs.s3;

import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.errors.ErrorResponseException;
import io.minio.messages.Item;
import kotlin.Pair;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.vfs2.FileContent;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.provider.AbstractFileObject;
import org.apache.commons.vfs2.util.MonitorOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class S3FileObject extends AbstractFileObject<S3FileSystem> {

    private final S3FileName fileName;
    protected S3FileObject(S3FileName name, S3FileSystem fileSystem) {
        super(name, fileSystem);
        this.fileName = name;
    }

    @Override
    protected OutputStream doGetOutputStream(boolean bAppend) throws Exception {
        if (bAppend) {
            throw new FileSystemException("vfs.provider/write-append-not-supported.error", this.getName().getURI());
        }
        return new S3OutputStream(new File(FileUtils.getTempDirectoryPath() + "/s3", RandomStringUtils.random(10)));
    }

    @Override
    protected InputStream doGetInputStream(int bufferSize) throws Exception {
        return getObject();
    }

    @Override
    protected long doGetContentSize() throws Exception {
        return getStat().size();
    }

    @Override
    protected FileType doGetType() throws Exception {
        try {
            if (getStat() == null) {
                return FileType.IMAGINARY;
            }
        } catch (Exception e) {
            return FileType.IMAGINARY;
        }
        return fileName.getType();
    }

    private StatObjectResponse getStat() throws Exception {
        try {
            StatObjectArgs args = StatObjectArgs.builder().bucket(fileName.getBucketName()).object(fileName.getS3path()).build();
            return getClient().statObject(args);
        } catch (ErrorResponseException e) {
            if (e.response().code() == 404) {
                return null;
            }
            throw e;
        }
    }

    private GetObjectResponse getObject() throws Exception {
//        StatObjectArgs argss = StatObjectArgs.builder().bucket(fileName.getBucketName()).object(fileName.getS3path()).build();
//        StatObjectResponse rr = getClient().statObject(argss);
//        System.out.println(rr);
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
                throw new FileNotFoundException(fileName.getURI());
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
    protected FileContent doCreateFileContent() throws FileSystemException {
        return super.doCreateFileContent();
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

    private class S3OutputStream extends MonitorOutputStream {
        private File tempFile;
        public S3OutputStream(File tempFile) throws IOException {
            super(FileUtils.openOutputStream(tempFile));
            this.tempFile = tempFile;
        }

        /**
         * Called after this stream is closed.
         */
        @Override
        protected void onClose() throws IOException {
            try {
                InputStream is = new FileInputStream(tempFile);
                PutObjectArgs args = PutObjectArgs.builder().bucket(fileName.getBucketName()).object(fileName.getS3path()).stream(is, tempFile.length(), -1).build();
                ObjectWriteResponse owr = getClient().putObject(args);
                if (owr.versionId() == null) {
                    throw new FileSystemException("vfs.provider.ftp/finish-put.error", getName());
                }
            } catch (Exception e) {
                throw new FileSystemException("vfs.provider.ftp/finish-put.error", getName(), e);
            } finally {
                FileUtils.deleteQuietly(tempFile);
            }
        }
    }
}
