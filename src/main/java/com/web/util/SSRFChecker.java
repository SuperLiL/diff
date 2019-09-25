package com.web.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;


public class SSRFChecker {
    private ArrayList<String> blacklistIpArray = new ArrayList<String>(Arrays.asList("127.0.0.0/8", "10.0.0.0/8", "172.16.0.0/12","192.168.0.0/16")); //ip blacklist array.
    private ArrayList<String> whitelistIpArray = new ArrayList<String>(Arrays.asList("10.179.21.213")); //ip whitelist array.
    private ArrayList<String> whitelistProtocolArray = new ArrayList<String>(Arrays.asList("http", "https")); //protocol whitelist array.
    ResourceBundle resourceBundle = ResourceBundle.getBundle("diff");
    public boolean checkURL(String urlString) {
        try {
            //String[] whiteLists = resourceBundle.getStringArray("whiteList");
            String whiteList = resourceBundle.getString("whiteList");
            String[] whiteLists = whiteList.split(",");
            URL url = new URL(urlString);
            String protocol = url.getProtocol();
            if (!whitelistProtocolArray.contains(protocol)) {
                System.out.println("Unsupported protocol~ Error:" + url);
                return false;
            }

            String host = url.getHost();
//            System.out.println(host);
//            InetAddress address = InetAddress.getByName(host);
//            System.out.println(address);
//            String ip = address.getHostAddress();
//            System.out.println(ip);

            boolean contains = Arrays.asList(whiteLists).contains(host);
            //resourceBundle.containsKey(ip)
            if(contains) {
                System.out.println("Good Ip~ Success:" + host + "," + url);
                return true;
            }else{
                System.out.println("Bad Ip~ Failed:" + host + "," + url);
                return false;
            }
            /*for (String blacklistIp : blacklistIpArray) {
                if (equalIpSubnet(ip, blacklistIp)) {
                    System.out.println("Bad IP~ Error:" + ip + ", " + host + "," + url);
                    return false;
                }
            }
            System.out.println("Good Ip~ Success:" + ip + ", " + host + "," + url);
            return true;*/
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("Bad URL~ Error:" + urlString);
            return false;
        }
    }
    
    //支持以下内网IP形式:
    //10.0.0.0/8
    //172.16.0.0/12
    //192.168.0.0/16
    private boolean equalIpSubnet(String ip, String blacklistIp) {

        long ipAddr = IpUtil.ip2long(ip);
        int type = Integer.parseInt(blacklistIp.replaceAll(".*/", ""));
        int mask = 0xFFFFFFFF << (32 - type);
        String cidrIp = blacklistIp.replaceAll("/.*", "");
        long cidrIpAddr = IpUtil.ip2long(cidrIp);
        return (ipAddr & mask) == (cidrIpAddr & mask);

    }

    public ArrayList<String> getBlacklistIpArray() {
        return blacklistIpArray;
    }

    public void setBlacklistIpArray(ArrayList<String> blacklistIpArray) {
        this.blacklistIpArray = blacklistIpArray;
    }

    public ArrayList<String> getWhitelistIpArray() {
        return whitelistIpArray;
    }

    public void setWhitelistIpArray(ArrayList<String> whitelistIpArray) {
        this.whitelistIpArray = whitelistIpArray;
    }

    public ArrayList<String> getWhitelistProtocolArray() {
        return whitelistProtocolArray;
    }

    public void setWhitelistProtocolArray(ArrayList<String> whitelistProtocolArray) {
        this.whitelistProtocolArray = whitelistProtocolArray;
    }
    public static void main(String[] args){
        SSRFChecker ssrfChecker = new SSRFChecker();
        //String url = "http://127.0.0.1.xip.io"; //默认支持302跳转
        String url = "ftp://baidu.com";
//        String url = "https://10.179.21.213";
//        String url = "http://127.0.0.1";
        System.out.println(ssrfChecker.checkURL(url));
//        ssrfChecker.checkURL(url);
    }
}
