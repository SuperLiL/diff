package com.web.util;

public class IpUtil {
    public static long ip2long(String ip) {
        ip = ip.trim();
        String[] ips = ip.split("\\.");

        long ip2long = 0L;
        for (int i = 0; i < ips.length; i++) {
            ip2long = ip2long << 8 | Integer.parseInt(ips[i]);
        }
        return ip2long;
    }
}

