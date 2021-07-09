package com.yuanzhy.tools.commons.exec;

import java.io.InputStream;

/**
 *
 * @author yuanzhy
 * @date 2021-07-09
 */
public class ExecDemo {

    public void runtime() throws Exception {
        Process process = Runtime.getRuntime().exec("ls -l");
        InputStream is = process.getInputStream();
        InputStream errIs = process.getErrorStream();
        process.waitFor();
        // TODO runtime demo
    }
}
