package com.shayne.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取客户端IP
 * @Author WY
 * @Date 2017年12月27日
 */
public class IpUtil {
    
    public IpUtil() {
        
    }

    /**
     * 获取ip地址
     * @param request
     * @return
     * String
     */
    public static String getHost(HttpServletRequest request) {
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
}