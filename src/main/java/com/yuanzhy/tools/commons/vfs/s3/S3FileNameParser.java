package com.yuanzhy.tools.commons.vfs.s3;

import org.apache.commons.vfs2.provider.HostFileNameParser;

public class S3FileNameParser extends HostFileNameParser {

    private static final S3FileNameParser INSTANCE = new S3FileNameParser();

    private static final int PORT = 9000;

    public static S3FileNameParser getInstance() {
        return INSTANCE;
    }

    public S3FileNameParser() {
        super(PORT);
    }

}
