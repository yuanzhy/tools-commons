package com.yuanzhy.tools.commons.vfs.s3;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.provider.HostFileNameParser;
import org.apache.commons.vfs2.provider.UriParser;
import org.apache.commons.vfs2.provider.VfsComponentContext;

public class S3FileNameParser extends HostFileNameParser {

    private static final S3FileNameParser INSTANCE = new S3FileNameParser();

    private static final int PORT = 9000;

    public static S3FileNameParser getInstance() {
        return INSTANCE;
    }

    public S3FileNameParser() {
        super(PORT);
    }

    @Override
    public FileName parseUri(final VfsComponentContext context, final FileName base, final String fileName)
            throws FileSystemException {
        // FTP URI are generic URI (as per RFC 2396)
        final StringBuilder name = new StringBuilder();

        // Extract the scheme and authority parts
        final Authority auth = extractToPath(context, fileName, name);

        // Decode and normalize the file name
        UriParser.canonicalizePath(name, 0, name.length(), this);
        UriParser.fixSeparators(name);

        if (StringUtils.isEmpty(FilenameUtils.getExtension(name.toString()))
                && name.charAt(name.length() - 1) != '/') {
            name.append("/");
        }
        FileType fileType = UriParser.normalisePath(name);
        final String path = name.toString();

        return new S3FileName(auth.getHostName(), auth.getPort(), PORT, auth.getUserName(), auth.getPassword(),
                path, fileType);
    }

}
