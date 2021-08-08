package com.yuanzhy.tools.commons.vfs.s3;

import io.minio.MinioClient;
import org.apache.commons.vfs2.Capability;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.provider.AbstractFileName;
import org.apache.commons.vfs2.provider.AbstractFileSystem;
import org.apache.commons.vfs2.provider.GenericFileName;

import java.util.Collection;

public class S3FileSystem extends AbstractFileSystem {

    private MinioClient client;

    protected S3FileSystem(GenericFileName rootFileName, MinioClient client, FileSystemOptions fileSystemOptions) {
        super(rootFileName, (FileObject)null, fileSystemOptions);
        this.client = client;
    }

    @Override
    protected FileObject createFile(AbstractFileName name) throws Exception {
        return new S3FileObject(name, this);
    }

    @Override
    protected void addCapabilities(Collection<Capability> caps) {
        caps.addAll(S3FileProvider.CAPABILITIES);
    }

    /**
     * Creates an FTP client to use.
     *
     * @return An FTPCleint.
     * @throws FileSystemException if an errorurs.
     */
    MinioClient getClient() {
        return client;
    }
}
