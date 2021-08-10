package com.yuanzhy.tools.commons.vfs.s3;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.vfs2.Capability;
import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileSystem;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.UserAuthenticationData;
import org.apache.commons.vfs2.provider.AbstractOriginatingFileProvider;
import org.apache.commons.vfs2.provider.GenericFileName;
import org.apache.commons.vfs2.util.UserAuthenticatorUtils;

import io.minio.MinioClient;

import static org.apache.commons.vfs2.UserAuthenticationData.PASSWORD;
import static org.apache.commons.vfs2.UserAuthenticationData.USERNAME;

public class S3FileProvider extends AbstractOriginatingFileProvider {
    public static final UserAuthenticationData.Type[] AUTHENTICATOR_TYPES = new UserAuthenticationData.Type[] {
            UserAuthenticationData.USERNAME, UserAuthenticationData.PASSWORD };
    // TODO
    static final Collection<Capability> CAPABILITIES = Collections
            .unmodifiableCollection(Arrays.asList(Capability.GET_TYPE, Capability.READ_CONTENT,
                    Capability.CREATE,
                    Capability.DELETE,
                    Capability.RENAME,
                    Capability.WRITE_CONTENT,
                    Capability.URI, Capability.GET_LAST_MODIFIED,
                    Capability.SET_LAST_MODIFIED_FILE,
                    Capability.ATTRIBUTES, Capability.RANDOM_ACCESS_READ, Capability.DIRECTORY_READ_CONTENT,
                    Capability.LIST_CHILDREN));

    public S3FileProvider() {
        this.setFileNameParser(S3FileNameParser.getInstance());
    }

    @Override
    protected FileSystem doCreateFileSystem(FileName rootName, FileSystemOptions fileSystemOptions) throws FileSystemException {
        UserAuthenticationData authData = UserAuthenticatorUtils.authenticate(fileSystemOptions, AUTHENTICATOR_TYPES);
        MinioClient client = createClient((GenericFileName) rootName, authData);
        return new S3FileSystem((GenericFileName) rootName, client, fileSystemOptions);
    }

    private MinioClient createClient(GenericFileName rootName, UserAuthenticationData authData) {
        String accessKey = new String(UserAuthenticatorUtils.getData(authData, USERNAME, UserAuthenticatorUtils.toChar(rootName.getUserName())));
        String secretKey = new String(UserAuthenticatorUtils.getData(authData, PASSWORD, UserAuthenticatorUtils.toChar(rootName.getUserName())));
        MinioClient client = new MinioClient.Builder()
                .endpoint("http://" + rootName.getHostName() + ":" + rootName.getPort() + "/")
                .credentials(accessKey, secretKey)
                .build();
        return client;
    }

    @Override
    public Collection<Capability> getCapabilities() {
        return CAPABILITIES;
    }
}
