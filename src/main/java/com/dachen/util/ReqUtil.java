package com.dachen.util;

import com.alibaba.fastjson.JSON;
import com.dachen.common.auth.data.AccessToken;
import com.dachen.common.exception.ServiceException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author liangcs
 * @desc
 * @date:2017/5/11 14:47
 * @Copyright (c) 2017, DaChen All Rights Reserved.
 */
@Component
public class ReqUtil implements InitializingBean {

    public static ReqUtil instance;
    private ReqUtil() {
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        instance = this;
    }

    public HttpServletRequest getRequest() {
        if (Objects.isNull(RequestContextHolder.getRequestAttributes())) {
            return null;
        }
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
            .getRequest();
    }

    public Integer getUserId() {
        HttpServletRequest request = getRequest();
        if (request != null && request.getHeader("userID") != null) {
            return Integer.valueOf(request.getHeader("userID"));
        }
        return null;
    }

    public Integer getUserType() {
        return null != getAccessToken().getUserType() ? getAccessToken().getUserType() : 0;
    }

    public AccessToken getAccessToken() {

        HttpServletRequest request = getRequest();
        if (Objects.isNull(request)) {
            throw new ServiceException("获取HttpServletRequest失败");
        }

        String token = getRequest().getHeader("token");
        if (StringUtils.isEmpty(token)) {
            return new AccessToken();
        }

        AccessToken accessToken = JSON
            .parseObject(token, AccessToken.class);
        if (Objects.isNull(accessToken)) {
            throw new ServiceException("获取token信息异常");
        }

        return accessToken;
    }



    /**
     * 获取请求ip
     * @param request
     * @return
     */
	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

    public UserAgentInfo getUserAgent() {
        HttpServletRequest req = getRequest();
        if (req != null) {
            String header = req.getHeader("User-Agent");
            return UserAgent.resolveAgentInfo(header);
        }
        return null;
    }

    public String getClientIp() {
        HttpServletRequest req = getRequest();
        if (req != null) {
            String ip = req.getHeader("x-forwarded-for");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = req.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = req.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = req.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = req.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = req.getRemoteAddr();
            }
            //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if(ip!=null && ip.length()>15){
                //"***.***.***.***".length() = 15
                if(ip.indexOf(",")>0){
                    ip = ip.substring(0,ip.indexOf(","));
                }
            }
            return ip;
        }
        return null;
    }


    /**
     * 版本比较
     */
    public static int compareTo(String a[], String b[]) {
        int len1 = a.length;
        int len2 = b.length;
        int max = Math.max(len1, len2);
        int k = 0;
        while (k < max) {
            String str1 = k > (len1 - 1) ? "0" : a[k];
            String str2 = k > (len2 - 1) ? "0" : b[k];
            Integer v1, v2;
            try {
                v1 = Integer.parseInt(str1);
            } catch (Exception e) {
                v1 = 0;
            }
            try {
                v2 = Integer.parseInt(str2);
            } catch (Exception e) {
                v2 = 0;
            }
            int t = v1.compareTo(v2);
            if (t != 0) {
                return t;
            }
            k++;
        }
        return 0;
    }

}
