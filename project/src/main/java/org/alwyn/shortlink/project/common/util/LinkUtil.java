package org.alwyn.shortlink.project.common.util;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Date;
import java.util.Optional;

import static org.alwyn.shortlink.project.common.constant.LinkConstant.DEFAULT_CACHE_VALID_TIME;

public class LinkUtil {

    public static long getLinkCacheValidTime(Date validDate) {
        return Optional.ofNullable(validDate).map(each -> DateUtil.between(new Date(), each, DateUnit.MS)).orElse(DEFAULT_CACHE_VALID_TIME);
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forward-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
}
