package com.fuhao.esb.core.util;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
/**
 * package name is  com.fuhao.esb.core.util
 * Created by fuhao on 14-2-11.
 * Project Name esb-java
 */
public class IPUtils {
    private static String serverIP ;
    /**
     * 判断系统版本，是linux还是windows
     */
    public static boolean isWindowsOS() {
        boolean isWindowsOS = false;
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().indexOf("windows") > -1) {
            isWindowsOS = true;
        }
        return isWindowsOS;
    }
    /**
     *
     *根据操作系统不同，采用不同的方式获得本机IP
     *目前只支持windows和linux
     */
    public static String getLocalIP() throws Exception {
        if(serverIP!=null){
            return serverIP;
        }
        if (isWindowsOS()) {
            // 如果是Windows操作系统
            serverIP = InetAddress.getLocalHost().getHostAddress();
        }else {
            // 如果是Linux操作系统
            boolean bFindIP = false;
            Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface
                    .getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                if (bFindIP) {
                    break;
                }
                NetworkInterface ni = (NetworkInterface) netInterfaces
                        .nextElement();
                // ----------特定情况，可以考虑用ni.getName判断
                // 遍历所有ip
                Enumeration<InetAddress> ips = ni.getInetAddresses();
                while (ips.hasMoreElements()) {
                    InetAddress	ip = (InetAddress) ips.nextElement();
                    // 127.开头的都是lookback地址
                    //删除ip.isSiteLocalAddress()判断条件
                    if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
                        bFindIP = true;
                        serverIP = ip.getHostAddress();
                        break;
                    }
                }
            }
        }
        return serverIP;
    }
}
