package com.dachen.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;


public class PropertiesUtil {

    public static Environment env;

    private static String fileDownLoadPrex = null;

    public static String getContextProperty(String name) {
        return env.getProperty(name);
    }

    public static String getHeaderPrefix() {
        if (fileDownLoadPrex == null) {
            fileDownLoadPrex = getFileDownloadPrefix();
        }
        StringBuffer sb = new StringBuffer(fileDownLoadPrex);
        if (!StringUtils.isEmpty(getContextProperty("fileserver.basepath"))) {
            sb.append("/");
            sb.append(getContextProperty("fileserver.basepath"));
        }
        return sb.toString();
    }

    public static String getFileDownloadPrefix() {
        if (fileDownLoadPrex == null) {
            StringBuffer sb = new StringBuffer();
            sb.append(getContextProperty("fileserver.protocol")).append("://");
            sb.append(getContextProperty("fileserver.host")).append(":")
                .append(getContextProperty("fileserver.port"));
            fileDownLoadPrex = sb.toString();
        }
        return fileDownLoadPrex;
    }


    //移除全路径的主机，端口，访问根路径
    public static String removeUrlPrefix(String url) {
        if (StringUtils.isEmpty(url)) {
            return url;
        } else {
            if (url.indexOf(PropertiesUtil.getHeaderPrefix()) > -1) {
                url = url.replace(PropertiesUtil.getHeaderPrefix(), "");
            }
            return url;
        }
    }

}
