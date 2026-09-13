package com.ne.srm.auth.util;


import com.ne.srm.auth.dto.ClientIpInfo;
import jakarta.servlet.http.HttpServletRequest;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.regex.Pattern;


/**
 * 客户端IP解析工具

 * 用途:
 * 1. 登录设备记录
 * 2. 登录日志
 * 3. 用户行为展示

 * 不用于:
 * 1. 权限判断
 * 2. IP白名单
 * 3. 安全认证
 */
public final class IpUtils {

    private IpUtils() {}

    /**
     * 获取客户端IP信息
     */
    public static ClientIpInfo resolve(HttpServletRequest request){
        ClientIpInfo info = new ClientIpInfo();

        if(request == null){
            return info;
        }

        String raw = getRawIpHeader(request);

        info.setRaw(cleanRaw(raw));

        String ip = extractClientIp(request);

        ip = normalize(ip);

        /*
         * normalize后再次验证
         */
        if(!isIp(ip)){
            ip=null;
        }

        info.setClientIp(ip);

        if(isIpv4(ip)){
            info.setIpv4(ip);
        }

        if(isIpv6(ip)){
            info.setIpv6(ip);
        }
        return info;
    }

    /**
     * 获取原始IP Header
     */
    private static String getRawIpHeader(HttpServletRequest request){

        String[] headers={
                "X-Forwarded-For",
                "X-Real-IP",
                "CF-Connecting-IP",
                "True-Client-IP"
        };

        for(String header:headers){
            String value = request.getHeader(header);

            if(value!=null && !value.isBlank()){
                return value;
            }

        }

        return request.getRemoteAddr();

    }


    /**
     * 提取客户端IP
     */
    private static String extractClientIp(HttpServletRequest request){

        String[] headers={

                "X-Forwarded-For",
                "X-Real-IP",
                "CF-Connecting-IP",
                "True-Client-IP",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP"
        };

        for(String header:headers){
            String value = request.getHeader(header);

            String ip = parseFirst(value);

            ip = removePort(ip);

            if(isIp(ip)){
                return ip;
            }
        }

        return request.getRemoteAddr();

    }

    /**
     * 处理:
     * X-Forwarded-For:
     * client,proxy1,proxy2
     */
    private static String parseFirst(String value){

        if(value==null || value.isBlank()){
            return null;
        }

        String ip=value.trim();

        if(ip.contains(",")){
            ip = ip.split(",")[0].trim();

        }

        return ip;
    }

    /**
     * 去除端口
     * 支持:
     * 192.168.1.1:8080
     * [2001:db8::1]:8080
     */
    private static String removePort(String ip){

        if(ip==null){
            return null;
        }

        /*
         * IPv6 bracket形式
         */
        if(ip.startsWith("[") && ip.contains("]")){

            return ip.substring(1, ip.indexOf("]"));
        }

        /*
         * IPv4:port
         *
         * IPv6内部也有:
         *
         * :
         *
         * 所以只处理只有一个冒号情况
         */
        int index = ip.lastIndexOf(":");

        if(index>0 && ip.indexOf(":")==index){
            return ip.substring(0, index);
        }

        return ip;

    }

    /**
     * 判断是否IPv4
     */
    public static boolean isIpv4(String ip) {
        if(ip == null || ip.isBlank()){
            return false;
        }
        String[] parts = ip.split("\\.", -1);

        /*
         * 必须4段
         */
        if(parts.length != 4){
            return false;

        }

        for(String part : parts){
            /*
             * 空段
             */
            if(part.isEmpty()){
                return false;

            }

            /*
             * IPv4每段最大3位
             *
             * 防止:
             *
             * 12345678901
             */
            if(part.length() > 3){
                return false;

            }

            /*
             * 只允许数字
             */
            for(char c : part.toCharArray()){

                if(!Character.isDigit(c)){
                    return false;
                }
            }

            /*
             * 严格模式:
             *
             * 001
             * 01
             *
             * 不允许
             */
            if(part.length()>1 && part.startsWith("0")){
                return false;
            }
            try {
                int value = Integer.parseInt(part);

                if(value < 0 || value > 255){
                    return false;
                }
            }catch(NumberFormatException e){
                /*
                 * 防御异常输入
                 */
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否IPv6
     */
    public static boolean isIpv6(String ip){
        if(ip==null || ip.isBlank()){
            return false;

        }

        if(!ip.contains(":")){
            return false;

        }

        try{
            InetAddress address = InetAddress.getByName(ip);

            return address instanceof Inet6Address;
        }catch(Exception e){

            return false;
        }

    }

    /**
     * 判断IP
     */
    private static boolean isIp(String ip){
        return isIpv4(ip) || isIpv6(ip);

    }

    /**
     * IP标准化
     */
    private static String normalize(String ip){

        if(ip==null){
            return null;
        }

        ip=ip.trim();

        if(ip.isEmpty()){
            return null;

        }

        /*
         * IPv4 mapped IPv6
         *
         * ::ffff:192.168.1.1
         */
        if(ip.startsWith("::ffff:")){
            ip = ip.substring(7);

        }

        /*
         * IPv6 localhost
         */
        if("::1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)){
            return "127.0.0.1";

        }

        /*
         * IPv6 zone id
         *
         * fe80::1%eth0
         */
        if(ip.contains("%")){
            ip = ip.substring(0, ip.indexOf("%"));

        }
        return ip;

    }

    /**
     * 防止日志注入
     */
    private static String cleanRaw(String raw){

        if(raw==null){
            return null;
        }

        return raw
                .replace("\r","")
                .replace("\n","");

    }

    /**
     * IPv4严格校验
     */
    private static final Pattern IPV4_PATTERN =
            Pattern.compile(
                    "^(25[0-5]|2[0-4]\\d|1?\\d?\\d)\\."
                            + "(25[0-5]|2[0-4]\\d|1?\\d?\\d)\\."
                            + "(25[0-5]|2[0-4]\\d|1?\\d?\\d)\\."
                            + "(25[0-5]|2[0-4]\\d|1?\\d?\\d)$"
            );

}