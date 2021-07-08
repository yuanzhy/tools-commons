package com.yuanzhy.tools.commons.codec;

import org.apache.commons.codec.net.URLCodec;
import org.junit.Test;

/**
 *
 * @author yuanzhy
 * @date 2021-07-08
 */
public class NetDemo {

    @Test
    public void URLCodec() throws Exception {
        URLCodec urlCodec = new URLCodec();
        // url编码
        String encUrl  = urlCodec.encode("http://x.com?f=哈");
        // url解码
        String url = urlCodec.decode(encUrl);
    }
}
