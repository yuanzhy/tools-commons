package com.yuanzhy.tools.commons.daemon;

import java.time.LocalDateTime;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonController;
import org.apache.commons.daemon.DaemonInitException;
import org.apache.commons.daemon.DaemonUserSignal;

public class LinuxDaemon implements Daemon, Runnable, DaemonUserSignal {
    private Thread thread=null;
    private volatile boolean stopping=false;
    public LinuxDaemon() {
        System.err.println("LinuxDaemon: instance "+this.hashCode()+" created");
    }
    
    @Override
    public void run() {
        System.err.println("LinuxDaemon: started");
        while (!this.stopping) {
            // 模拟业务处理
            System.err.println("运行中：" + LocalDateTime.now().toString());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void init(DaemonContext context) throws DaemonInitException, Exception {
        // context可以获取启动参数做一些定制化处理
        String[] args = context.getArguments();
        // controller可以主动触发服务的刷新和停止
        DaemonController controller = context.getController();
        // 刷新服务进程
        // controller.reload();
        // 停止服务进程
        // controller.shutdown();
        // 停止服务进程并记录失败消息
        // controller.fail();

        this.thread=new Thread(this);

        System.err.println("LinuxDaemon: instance "+this.hashCode()+" init");
    }

    @Override
    public void start() throws Exception {
        // 开启
        System.err.println("LinuxDaemon: starting");

        this.thread.start();
    }

    @Override
    public void stop() throws Exception {
        // 停止
        System.err.println("LinuxDaemon: stopping");
        /* 通知线程终止 */
        this.stopping=true;
        /* 等待主线程退出并转储一条消息 */
        this.thread.join(3000);
        System.err.println("SimpleDaemon: stopped");
    }

    @Override
    public void destroy() {
        // 销毁
        System.err.println("LinuxDaemon: instance "+this.hashCode()+
                " destroy");
    }
    @Override
    public void signal() {
        // 接收到操作系统的SIGUSR2信号
        System.err.println("LinuxDaemon: instance "+this.hashCode()+
                " signal");
    }

    @Override
    protected void finalize() {
        System.err.println("LinuxDaemon: instance "+this.hashCode()+
                " garbage collected");
    }
}
