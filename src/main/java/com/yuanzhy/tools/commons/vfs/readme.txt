## 缓存策略
CacheStrategy.ON_RESOLVE：每次从 {@link FileSystemManager#resolveFile} 请求文件时刷新数据。默认策略
CacheStrategy.ON_CALL   ：每次在 fileObject 上调用方法时刷新数据。仅当您确实需要最新信息时才使用此设置，因为此设置会造成重大的性能损失。
CacheStrategy.MANUAL    ：手动处理缓存数据。调用 {@link FileObject#refresh()} 来刷新对象数据

-----------------------------------------------------------------------------------------------------------------------

## 缓存方式
SoftRefFilesCache：这个实现会缓存每个文件，只要它可以被 JVM 强访问。一旦 JVM 需要内存 - 每个软可访问的文件都将被丢弃。默认方式
DefaultFilesCache：一个简单的FileCache实现。
                   此实现缓存每个文件，没有过期或限制。
                   此实现在主缓存映射中包含文件系统特定的 {@linkplain ConcurrentHashMap ConcurrentHashMaps} 列表。
LRUFilesCache    ：此实现使用 {@link LRUMap} 缓存每个文件。 默认构造函数使用每个文件系统的 LRU 大小为 100
WeakRefFilesCache：这个实现会缓存每个文件，只要它可以被 java vm 强访问。
                   一旦对象不再可达它将被丢弃。与 SoftRefFilesCache 相比，此实现可能会更快地释放资源，因为它不会等到内存限制。
NullFilesCache   ：此实现从不缓存单个文件

-----------------------------------------------------------------------------------------------------------------------

## 自定义配置
在类路径下创建"META-INF/vfs-providers.xml"文件，可以添加额外的配置。可以配置自己的实现，如亚马逊S3文件，阿里云文件等。
配置文件是XML文件。配置文件的根元素应该是<providers>元素。<providers>元素可能包含：
        零个或多个<provider>元素。
        可选的<default provider>元素。
        零个或多个<extension map>元素。
        零个或多个<mime type map>元素。

<provider>：元素定义了一个文件提供者。它必须具有class name属性，该属性指定提供程序类的完全限定名。provider类必须是public的，
            并且必须有一个带有FileSystemManager参数的公共构造函数，该参数允许系统传递所使用的文件系统管理器。
            <provider>元素可以包含零个或多个<scheme>元素，以及零个或多个<if available>元素。
            元素定义提供者将处理的URI方案。它必须有一个name属性，用于指定URI方案。
            如果类路径中不存在某些类，<if available>元素用于禁用提供程序。它必须具有class name属性，该属性指定要测试的类的完全限定名。如果找不到类，则未注册提供程序。
<default-provider>：元素定义默认提供者。它的格式与<provider>元素相同。
<extension-map>：元素定义了从文件扩展名到应该处理具有该扩展名的文件的提供程序的映射。
                 它必须有一个extension属性（指定扩展）和一个scheme属性（指定提供程序的URI方案）。

<mime-map>：元素定义了从文件的mime类型到应该处理该mime类型文件的提供程序的映射。
            它必须有一个mime type属性（指定mime类型）和一个scheme属性（指定提供程序的URI方案）。

下面是一个配置文件示例：
    <providers>
        <provider class-name="org.apache.commons.vfs2.provider.zip.ZipFileProvider">
            <scheme name="zip"/>
        </provider>
        <extension-map extension="zip" scheme="zip"/>
        <mime-type-map mime-type="application/zip" scheme="zip"/>
        <provider class-name="org.apache.commons.vfs2.provider.ftp.FtpFileProvider">
            <scheme name="ftp"/>
            <if-available class-name="org.apache.commons.net.ftp.FTPFile"/>
        </provider>
        <default-provider class-name="org.apache.commons.vfs2.provider.url.UrlFileProvider"/>
    </providers>

-----------------------------------------------------------------------------------------------------------------------

## 文件实现
============================================================
#### 名称 ####
所有文件名都被视为URI。这样做的后果之一是必须使用%25对“%”字符进行编码。
根据文件系统的不同，如果需要，还会对其他字符进行编码。这是自动完成的，但可能会反映在文件名中。
示例
    file:///somedir/some%25file.txt
许多文件系统接受用户ID和密码作为url的一部分。但是，在文件中以明文形式存储密码通常是不可接受的。为了帮助解决这个问题，Commons VFS提供了一种加密密码的机制。不过，应该注意的是，这并不是完全安全的，因为在Commons VFS可以使用密码之前，需要对密码进行解密。
要创建加密密码，请执行以下操作：
  $ java-cp commons-vfs-2.0.jar org.apache.commons.vfs2.util.EncryptUtil encrypt mypassword
其中mypassword是要加密的密码。结果将是一行包含大写十六进制字符的输出。例如
  $ java-cp commons-vfs-2.0.jar org.apache.commons.vfs2.util.EncryptUtil encrypt WontUBee9 D7B82198B272F5C93790FEB38A73C7B8
然后剪切返回的输出并将其粘贴到URL中，如下所示：
    https://testuser:{D7B82198B272F5C93790FEB38A73C7B8}@myhost.com/svn/repos/vfstest/trunk
VFS将{}中包含的密码视为已加密，并将在使用密码之前对其进行解密。
============================================================
#### 本地文件 ####
提供对本地物理文件系统上的文件的访问。
URI格式
    [file//]absolute-path
其中绝对路径是本地平台的有效绝对文件名。Windows下支持UNC名称。
示例
    file:///home/someuser/somedir
    file:///C:/Documents and Settings
    file://///somehost/someshare/afile.txt
    /home/someuser/somedir
    c:\program files\some dir
    c:/program files/some dir
============================================================
#### Zip, Jar and Tar ####
提供对Zip、Jar和Tar文件内容的只读访问
需要单独引入commons-compress
URI格式
    zip://arch-file-uri[!absolute-path]
    jar://arch-file-uri[!absolute-path]
    tar://arch-file-uri[!absolute-path]
    tgz://arch-file-uri[!absolute-path]
    tbz2://arch-file-uri[!absolute-path]
其中，arch文件uri是指任何受支持类型的文件，包括其他zip文件。注意：如果您想使用！作为普通字符，必须使用%21进行转义。
tgz和tbz2对于tar:gz和tar:bz2是方便的
示例
    jar:../lib/classes.jar!/META-INF/manifest.mf
    zip:http://somehost/downloads/somefile.zip
    jar:zip:outer.zip!/nested.jar!/somedir
    jar:zip:outer.zip!/nested.jar!/some%21dir
    tar:gz:http://anyhost/dir/mytar.tar.gz!/mytar.tar!/path/in/tar/README.txt
    tgz:file://anyhost/dir/mytar.tgz!/somepath/somefile
============================================================
#### gzip and bzip2 ####
提供对gzip和bzip2文件内容的只读访问
需要单独引入commons-compress
URI格式
    gz://compressed-file-uri
    bz2://compressed-file-uri
其中，压缩文件uri是指任何受支持类型的文件。没有必要再加一个！如果你读取文件的内容，你总是会得到未压缩的版本。
示例
    gz:/my/gz/file.gz
============================================================
#### HDFS ####
提供对apachehadoop文件系统（HDFS）中文件的（只读）访问。在Windows上，集成测试在默认情况下是禁用的，因为它需要二进制文件
URI格式
    hdfs://hostname[:port][ absolute-path]
示例
    hdfs://somehost:8080/downloads/some_dir
    hdfs://somehost:8080/downloads/some_file.ext
需要单独引入HDFS相关依赖
<dependency>
    <groupId>org.apache.hadoop</groupId>
    <artifactId>hadoop-hdfs-client</artifactId>
    <version>3.3</version>
    </dependency>
<dependency>
    <groupId>org.apache.hadoop</groupId>
    <artifactId>hadoop-common</artifactId>
    <version>3.3</version>
</dependency>
<dependency>
    <groupId>org.apache.hadoop</groupId>
    <artifactId>hadoop-hdfs</artifactId>
    <version>3.3</version>
</dependency>
============================================================
#### HTTP and HTTPS ####
提供对HTTP服务器上文件的访问
URI格式
    http://[username[:password]@]hostname[:port][absolute-path]
    https://[username[:password]@]hostname[:port][absolute-path]
文件系统选项
    proxyHost：            要连接的代理主机。
    proxyPort：            要使用的代理端口。
    proxyScheme：          要使用的代理方案（http/https）。
    cookies：              要添加到请求的cookies数组。
    maxConnectionsPerHost：允许连接到特定主机和端口的最大连接数。默认值为5。
    maxTotalConnections：  所有主机允许的最大连接数。默认值为50。
    keystoreFile：         SSL连接的密钥库文件。
    keystorePass：         密钥库密码。
    keystoreType：         密钥库类型。
示例
    http://somehost:8080/downloads/somefile.jar
    http://myusername@somehost/index.html
依赖httpClient
============================================================
#### WebDAV ####
通过commons-vfs2-jackrabbit1和commons-vfs2-jackrabbit2模块提供对WebDAV服务器上文件的访问。
URI格式
    webdav://[username[:password]@]hostname[:port][absolute-path]
文件系统选项
    versioning ：如果应启用版本控制，则版本控制为true
    creatorName：要通过更改文件来标识的用户名。如果未设置，将使用用于身份验证的用户名。
示例
    webdav://somehost:8080/dist
============================================================
#### FTP ####
提供对FTP服务器上文件的访问。
依赖commons-net
URI格式
    ftp://[username[:password]@]hostname[:port][relative-path]
示例
    ftp://myusername:mypassword@somehost/pub/downloads/somefile.tgz
默认情况下，路径相对于用户的主目录。可通过以下方式进行更改：
FtpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(options, false);
============================================================
#### FTPS ####
通过SSL提供对FTP服务器上文件的访问
依赖commons-net
URI格式
    ftps://[username[:password]@]hostname[:port][absolute-path]
示例
    ftps://myusername:mypassword@somehost/pub/downloads/somefile.tgz
============================================================
#### SFTP ####
提供对SFTP服务器（即SSH或SCP服务器）上的文件的访问
URI格式
    sftp://[username[:password]@]hostname[:port][relative-path]
示例
    sftp://myusername:mypassword@somehost/pub/downloads/somefile.tgz
默认情况下，路径相对于用户的主目录。可通过以下方式进行更改：
FtpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(options, false);
依赖
<dependency>
    <groupId>com.jcraft</groupId>
    <artifactId>jsch</artifactId>
    <version>0.1.55</version>
</dependency>
============================================================
#### Temporary Files ####
提供对临时文件系统（scratchpad）的访问，该文件系统在Commons VFS关闭时被删除。临时文件系统由本地文件系统支持
URI格式
    tmp://[absolute-path]
示例
    tmp://dir/somefile.txt
============================================================
#### Resource ####
这实际上不是一个文件系统，它只是尝试使用javas ClassLoader.getResource（）查找资源，并创建一个VFS url以供进一步处理
URI格式
    res://[path]
示例
    res://path/in/classpath/image.png
    将会转换为 -> jar:file://my/path/to/images.jar!/path/in/classpath/image.png
============================================================
#### RAM ####
在内存中存储所有数据的文件系统（每个文件内容一个字节数组）
URI格式
    ram://[path]
文件系统选项
    maxsize：最大文件系统大小（所有文件内容的总字节数）
示例
    ram:///any/path/to/file.txt
============================================================

%%%%%%%%%%%%%%%%%%%%%% 以下正在开发中 %%%%%%%%%%%%%%%%%%%%%%

============================================================
#### CIFS ####（开发中）
CIFS（沙盒）文件系统提供对CIFS服务器（如Samba服务器或Windows共享）的访问
URI格式
    smb://[username[:password]@]hostname[:port][absolute-path]
示例
    smb://somehost/home
============================================================
#### MIME #### （开发中）
这个（沙盒）文件系统可以读取邮件及其附件，比如归档文件。
如果已解析邮件中的某个部分没有名称，则将生成一个伪名称。虚拟名称是： _body_part_X，其中X将被零件号替换。
URI格式
    mime://mime-file-uri[!absolute-path]
示例
    mime:file:///your/path/mail/anymail.mime!/
    mime:file:///your/path/mail/anymail.mime!/filename.pdf
    mime:file:///your/path/mail/anymail.mime!/_body_part_0