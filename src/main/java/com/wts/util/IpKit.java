package com.wts.util;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

public class IpKit {
    private static Logger logger = Logger.getLogger(IpKit.class);
    public static String getRealIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String getRealIpV2(HttpServletRequest request) {
        String accessIP = request.getHeader("x-forwarded-for");
        if (null == accessIP)
            return request.getRemoteAddr();
        return accessIP;
    }

    public static void setIP(String ip) {
        try {
            Runtime.getRuntime().exec("netsh    interface    ip    set    addr    \"本地连接\"    static    "
                    + ip + "    255.255.255.0     10.153.73.254     1");
            logger.info("切换内网成功");
        } catch (Exception e) {
            logger.info("切换内网失败");
        }
    }

    public static void setIP() {
        try {
            Runtime.getRuntime().exec("netsh interface ip set address name=\"本地连接\" source=dhcp");
            logger.info("切换外网成功");
        } catch (Exception e) {
            logger.info("切换外网失败");
        }
    }
    public static void setIP2(String ip) {
        try {
            Runtime.getRuntime().exec("netsh    interface    ip    set    addr    \"本地连接\"    static    "
                    + ip + "    255.255.255.0     192.168.2.1     1");
            logger.info("切换外网成功");
        } catch (Exception e) {
            logger.info("切换外网失败");
        }
    }
    public static void main(String[] args) throws Exception {
//        setIP("10.153.73.166");
        setIP();
    }
}
