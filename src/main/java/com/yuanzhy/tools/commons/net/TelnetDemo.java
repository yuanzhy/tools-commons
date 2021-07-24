package com.yuanzhy.tools.commons.net;

import org.junit.Test;

import java.io.IOException;

public class TelnetDemo {
    @Test
    public void weatherTelnet() throws IOException {
//        final TelnetClient telnet;
//        telnet = new TelnetClient();
//        try {
//            telnet.connect("rainmaker.wunderground.com", 3000);
//            this.readWrite(telnet.getInputStream(), telnet.getOutputStream(),
//                    System.in, System.out);
//        } finally {
//            telnet.disconnect();
//        }
//        System.exit(0);
    }

//    public void readWrite(final InputStream remoteInput,
//                                 final OutputStream remoteOutput,
//                                 final InputStream localInput,
//                                 final OutputStream localOutput) {
//        final Thread reader;
//        final Thread writer;
//        reader = new Thread(() -> {
//            int ch;
//            try {
//                while (!Thread.interrupted() && (ch = localInput.read()) != -1) {
//                    remoteOutput.write(ch);
//                    remoteOutput.flush();
//                }
//            } catch (final IOException e) {
//                //e.printStackTrace();
//            }
//        });
//        writer = new Thread(() -> {
//            try {
//                Util.copyStream(remoteInput, localOutput);
//            } catch (final IOException e) {
//                e.printStackTrace();
//                System.exit(1);
//            }
//        });
//        writer.setPriority(Thread.currentThread().getPriority() + 1);
//
//        writer.start();
//        reader.setDaemon(true);
//        reader.start();
//
//        try {
//            writer.join();
//            reader.interrupt();
//        } catch (final InterruptedException e) {
//            // Ignored
//        }
//    }
}
