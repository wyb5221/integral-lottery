package com.dachen.util;

import com.qiniu.processing.OperationManager;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class QiniuUtil {
	private static final Logger logger = LoggerFactory.getLogger(QiniuUtil.class);

	public static final String QRCODE_BUCKET = "qrcode";
	public static final String DEFAULT_BUCKET = "default";
	public static final String VOICE_BUCKET = "patient";


	public static final String getQiniuDomain() {
		return PropertiesUtil.getContextProperty("qiniu.domain");
	}

    public static final String getQiniuUrlPattern(){
        return "http://{0}." + getQiniuDomain() + "/" + "{1}";// {0}:空间名; // {1}:key
    }

	private static Auth auth = null;
    public static Auth auth() {
        if (null == auth) {
            synchronized (QiniuUtil.class) {
                if (null == auth) {
                    String access_key = PropertiesUtil.getContextProperty("qiniu.access.key");
                    String secret_key = PropertiesUtil.getContextProperty("qiniu.secret.key");
                    auth = Auth.create(access_key, secret_key);
                }
            }
        }
        return auth;
    }

	private static OperationManager operater;
	public static OperationManager getOperationManager() {
		if (operater == null) {
			operater = new OperationManager(auth(), new Configuration());
		}
		return operater;
	}

}