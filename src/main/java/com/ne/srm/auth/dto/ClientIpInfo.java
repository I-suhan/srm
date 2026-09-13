package com.ne.srm.auth.dto;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

@Data
public class ClientIpInfo {


    /**
     * 最终客户端IP
     */
    private String clientIp;


    /**
     * IPv4
     */
    private String ipv4;


    /**
     * IPv6
     */
    private String ipv6;


    /**
     * 原始Header
     */
    private String raw;

}
