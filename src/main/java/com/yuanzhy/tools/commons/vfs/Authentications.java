package com.yuanzhy.tools.commons.vfs;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.auth.StaticUserAuthenticator;
import org.apache.commons.vfs2.impl.DefaultFileSystemConfigBuilder;

public class Authentications {

    public void auth() throws FileSystemException {
        StaticUserAuthenticator auth = new StaticUserAuthenticator("domain", "username", "password");
        FileSystemOptions opts = new FileSystemOptions();
        DefaultFileSystemConfigBuilder.getInstance().setUserAuthenticator(opts, auth);

        FileObject fo = VFS.getManager().resolveFile("smb://host/anyshare/dir", opts);
        // ... ...
    }
}
