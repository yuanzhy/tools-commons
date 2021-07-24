package com.yuanzhy.tools.commons.net;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.NtpUtils;
import org.apache.commons.net.ntp.NtpV3Impl;
import org.apache.commons.net.ntp.NtpV3Packet;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.commons.net.ntp.TimeStamp;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.NumberFormat;

public class NtpDemo {
    @Test
    public void ntp() throws IOException {
        NTPUDPClient ntpClient = new NTPUDPClient();
        try {
            ntpClient.setDefaultTimeout(10000);
            ntpClient.open();
            // 局域网请自定搭建ntp服务器
            InetAddress hostAddr = InetAddress.getByName("ntp.aliyun.com");
            TimeInfo info = ntpClient.getTime(hostAddr);
            NtpV3Packet message = info.getMessage();
            // RFC-1305中定义的层
            int stratum = message.getStratum();
            String refType;
            if (stratum <= 0) {
                refType = "(Unspecified or Unavailable)";
            } else if (stratum == 1) {
                refType = "(Primary Reference; e.g., GPS)"; // GPS, radio clock, etc.
            } else {
                refType = "(Secondary Reference; e.g. via NTP or SNTP)";
            }
            // stratum should be 0..15...
            System.out.println(" Stratum: " + stratum + " " + refType);
            int version = message.getVersion();
            int li = message.getLeapIndicator();
            System.out.println(" leap=" + li + ", version="
                    + version + ", precision=" + message.getPrecision());

            System.out.println(" mode: " + message.getModeName() + " (" + message.getMode() + ")");
            int poll = message.getPoll();
            // poll 值通常是 MINPOLL (4) 和 MAXPOLL (14)
            System.out.println(" poll: " + (poll <= 0 ? 1 : (int) Math.pow(2, poll))
                    + " seconds" + " (2 ** " + poll + ")");
            double disp = message.getRootDispersionInMillisDouble();

            NumberFormat numberFormat = new java.text.DecimalFormat("0.00");
            System.out.println(" rootdelay=" + numberFormat.format(message.getRootDelayInMillisDouble())
                    + ", rootdispersion(ms): " + numberFormat.format(disp));
            // 返回RFC-1305中定义的引用ID，它是一个 32 位整数，其值取决于多个条件
            int refId = message.getReferenceId();
            // 引用ID转换为IPv4字符串格式
            String refAddr = NtpUtils.getHostAddress(refId);
            String refName = null;
            if (refId != 0) {
                if (refAddr.equals("127.127.1.0")) {
                    refName = "LOCAL"; // 这是本地时钟的参考地址
                } else if (stratum >= 2) {
                    // 引用ID具有127.127前缀，则它使用自己的参考时钟
                    // 以 127.127.clock-type.unit-num 形式定义（例如 127.127.8.0 模式 5
                    // 用于 GENERIC DCF77 AM；请参阅 NTP 软件中的 refclock.htm分配。
                    if (!refAddr.startsWith("127.127")) {
                        try {
                            InetAddress addr = InetAddress.getByName(refAddr);
                            String name = addr.getHostName();
                            if (name != null && !name.equals(refAddr)) {
                                refName = name;
                            }
                        } catch (UnknownHostException e) {
                            // 一些第 2 层服务器同步到参考时钟设备，但伪造层级更高...（例如 2）
                            // 参考无效主机也许它是参考时钟名称？
                            // 否则只显示引用 IP 地址。
                            // some stratum-2 servers sync to ref clock device but fudge stratum level higher... (e.g. 2)
                            // ref not valid host maybe it's a reference clock name?
                            // otherwise just show the ref IP address.
                            refName = NtpUtils.getReferenceClock(message);
                        }
                    }
                } else if (version >= 3 && (stratum == 0 || stratum == 1)) {
                    refName = NtpUtils.getReferenceClock(message);
                    // refname 通常至少有 3 个字符 (例： GPS, WWV, LCL, etc.)
                }
            }
            if (refName != null && refName.length() > 1) {
                refAddr += " (" + refName + ")";
            }
            System.out.println(" Reference Identifier:\t" + refAddr);

            TimeStamp refNtpTime = message.getReferenceTimeStamp();
            System.out.println(" Reference Timestamp:\t" + refNtpTime + "  " + refNtpTime.toDateString());

            // Originate Time is time request sent by client (t1)
            TimeStamp origNtpTime = message.getOriginateTimeStamp();
            System.out.println(" Originate Timestamp:\t" + origNtpTime + "  " + origNtpTime.toDateString());

            long destTimeMillis = info.getReturnTime();
            // Receive Time is time request received by server (t2)
            TimeStamp rcvNtpTime = message.getReceiveTimeStamp();
            System.out.println(" Receive Timestamp:\t" + rcvNtpTime + "  " + rcvNtpTime.toDateString());

            // Transmit time is time reply sent by server (t3)
            TimeStamp xmitNtpTime = message.getTransmitTimeStamp();
            System.out.println(" Transmit Timestamp:\t" + xmitNtpTime + "  " + xmitNtpTime.toDateString());

            // Destination time is time reply received by client (t4)
            TimeStamp destNtpTime = TimeStamp.getNtpTime(destTimeMillis);
            System.out.println(" Destination Timestamp:\t" + destNtpTime + "  " + destNtpTime.toDateString());
            // 如果尚未完成，则计算偏移/延迟
            info.computeDetails();
            Long offsetMillis = info.getOffset();
            Long delayMillis = info.getDelay();
            String delay = delayMillis == null ? "N/A" : delayMillis.toString();
            String offset = offsetMillis == null ? "N/A" : offsetMillis.toString();

            System.out.println(" Roundtrip delay(ms)=" + delay
                    + ", clock offset(ms)=" + offset); // offset in ms
        } finally {
            ntpClient.close();
        }

    }
}

class SimpleNTPServer implements Runnable {

    private int port;
    private volatile boolean running;
    private boolean started;
    private DatagramSocket socket;

    /**
     * Creates SimpleNTPServer listening on default NTP port.
     */
    public SimpleNTPServer()
    {
        this(NtpV3Packet.NTP_PORT);
    }

    /**
     * Creates SimpleNTPServer.
     *
     * @param port the local port the server socket is bound to, or
     *             <code>zero</code> for a system selected free port.
     * @throws IllegalArgumentException if port number less than 0
     */
    public SimpleNTPServer(final int port)
    {
        if (port < 0) {
            throw new IllegalArgumentException();
        }
        this.port = port;
    }

    public int getPort()
    {
        return port;
    }

    /**
     * Returns state of whether time service is running.
     *
     * @return true if time service is running
     */
    public boolean isRunning()
    {
        return running;
    }

    /**
     * Returns state of whether time service is running.
     *
     * @return true if time service is running
     */
    public boolean isStarted()
    {
        return started;
    }

    /**
     * Connects to server socket and listen for client connections.
     *
     * @throws IOException if an I/O error occurs when creating the socket.
     */
    public void connect() throws IOException
    {
        if (socket == null)
        {
            socket = new DatagramSocket(port);
            // port = 0 is bound to available free port
            if (port == 0) {
                port = socket.getLocalPort();
            }
            System.out.println("Running NTP service on port " + port + "/UDP");
        }
    }

    /**
     * Starts time service and provide time to client connections.
     *
     * @throws IOException if an I/O error occurs when creating the socket.
     */
    public void start() throws IOException
    {
        if (socket == null)
        {
            connect();
        }
        if (!started)
        {
            started = true;
            new Thread(this).start();
        }
    }

    /**
     * Main method to service client connections.
     */
    @Override
    public void run()
    {
        running = true;
        final byte buffer[] = new byte[48];
        final DatagramPacket request = new DatagramPacket(buffer, buffer.length);
        do {
            try {
                socket.receive(request);
                final long rcvTime = System.currentTimeMillis();
                handlePacket(request, rcvTime);
            } catch (final IOException e) {
                if (running)
                {
                    e.printStackTrace();
                }
                // otherwise socket thrown exception during shutdown
            }
        } while (running);
    }

    /**
     * Handles incoming packet. If NTP packet is client-mode then respond
     * to that host with a NTP response packet otherwise ignore.
     *
     * @param request incoming DatagramPacket
     * @param rcvTime time packet received
     *
     * @throws IOException  if an I/O error occurs.
     */
    protected void handlePacket(final DatagramPacket request, final long rcvTime) throws IOException
    {
        final NtpV3Packet message = new NtpV3Impl();
        message.setDatagramPacket(request);
        System.out.printf("NTP packet from %s mode=%s%n", request.getAddress().getHostAddress(),
                NtpUtils.getModeName(message.getMode()));
        if (message.getMode() == NtpV3Packet.MODE_CLIENT) {
            final NtpV3Packet response = new NtpV3Impl();

            response.setStratum(1);
            response.setMode(NtpV3Packet.MODE_SERVER);
            response.setVersion(NtpV3Packet.VERSION_3);
            response.setPrecision(-20);
            response.setPoll(0);
            response.setRootDelay(62);
            response.setRootDispersion((int) (16.51 * 65.536));

            // originate time as defined in RFC-1305 (t1)
            response.setOriginateTimeStamp(message.getTransmitTimeStamp());
            // Receive Time is time request received by server (t2)
            response.setReceiveTimeStamp(TimeStamp.getNtpTime(rcvTime));
            response.setReferenceTime(response.getReceiveTimeStamp());
            response.setReferenceId(0x4C434C00); // LCL (Undisciplined Local Clock)

            // Transmit time is time reply sent by server (t3)
            response.setTransmitTime(TimeStamp.getNtpTime(System.currentTimeMillis()));

            final DatagramPacket dp = response.getDatagramPacket();
            dp.setPort(request.getPort());
            dp.setAddress(request.getAddress());
            socket.send(dp);
        }
        // otherwise if received packet is other than CLIENT mode then ignore it
    }

    /**
     * Closes server socket and stop listening.
     */
    public void stop()
    {
        running = false;
        if (socket != null)
        {
            socket.close();  // force closing of the socket
            socket = null;
        }
        started = false;
    }

    public static void main(final String[] args)
    {
        int port = NtpV3Packet.NTP_PORT;
        if (args.length != 0)
        {
            try {
                port = Integer.parseInt(args[0]);
            } catch (final NumberFormatException nfe) {
                nfe.printStackTrace();
            }
        }
        final SimpleNTPServer timeServer = new SimpleNTPServer(port);
        try {
            timeServer.start();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

}