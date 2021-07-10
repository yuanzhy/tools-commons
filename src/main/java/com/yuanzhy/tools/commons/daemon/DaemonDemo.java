package com.yuanzhy.tools.commons.daemon;

public class DaemonDemo {

    public void deamon() {
//        DaemonLoader.start();
    }
}

//class ServiceLauncher extends DaemonWrapper {
//    private static ServiceLauncher launcher;
//    private Service  service;
//
//    @Override
//    public void destroy() {
//        System.out.println("destroy");
//    }
//
//    @Override
//    public void init(DaemonContext dc) {
//        System.out.println(Arrays.toString(dc.getArguments()));
//    }
//    @Override
//    public void start() throws Exception {
//        service = new Service();
//        Thread t = new Thread(service);
//        t.setDaemon(true);
//        t.start();
//        t.join();
//    }
//
//    @Override
//    public void stop() throws Exception {
//        service.finish();
//    }
//
//    public static void startService(String[] args) throws Exception {
//        launcher = new ServiceLauncher(args);
//        launcher.start();
//    }
//
//    public static void stopService(String[] args) throws Exception {
//        launcher.stop();
//    }
//}
