package com.dachen.util;

import com.dachen.im.server.enums.AppEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by gl on 2014/7/24.
 */
public class UserAgent {
	
	private static final  Logger logger=LoggerFactory.getLogger(UserAgent.class);
	
	/**
	 * 
	 * @param userAgentString  DGroupBusiness/1.6.170316.01/clientAppId/(ios/android/web)/iPhone; iOS 10.2.1
	 * @return
	 */
	public static String[] parser(String userAgentString) {
		if (StringUtils.isEmpty(userAgentString)) {
			return null;
		}

		String appClientId = null;
		String[] s = StringUtils.split(userAgentString, "/", 5);
		if (s.length <= 2) {
			return null;
		}
		String appClientVersion = s[1];
		if (s.length > 2) {
			if (AppEnum.isValid((s[2]))) {
				appClientId = s[2];
			}
		}
		if (StringUtils.isEmpty(appClientId) && StringUtils.isEmpty(appClientVersion)) {
			return null;
		}
		if (StringUtils.isEmpty(appClientVersion)) {
			return new String[] { appClientId };
		}

		return new String[] { appClientId, appClientVersion };

	}

	/*
	 * public static void main(String[]args) { String agent =
	 * "1.6.0.2017031602/android_Dalvik/2.1.0 (Linux; U; Android 5.0.2; vivo Y35A Build/LRX22G)"
	 * ; // parser(agent); // System.out.println("====="); // agent =
	 * "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36"
	 * ; // parser(agent); // System.out.println("====="); agent =
	 * "DGroupBusiness/1.6.170316.01/iPhone; iOS 10.2.1"; agent =
	 * "DGroupShop/1.3030201.127/android_Dalvik/1.6.0 (Linux; U; Android 4.4.4; HM 2A MIUI/V8.1.2.0.KHLCNDI)"
	 * ; agent=
	 * "DGroupDoctor/1.9.021401.507/android_Dalvik/1.6.0 (Linux; U; Android 4.4.4; HM 2A MIUI/V8.2.1.0.KHLCNDL)"
	 * ; // parser(agent);
	 * 
	 * System.out.println(isVersion("0.6.0170316.01q")); }
	 */

	// user-agent:
	// MedicalCircle/1.8.1.04091727/031/android/Honor_Che1-CL20/EmotionUI_3.0/mobile

	public static UserAgentInfo resolveAgentInfo(String userAgentInfo) {
		if (StringUtils.isEmpty(userAgentInfo)) {
			return null;
		}
		UserAgentInfo agentInfo = new UserAgentInfo();
		String[] itemArray = userAgentInfo.split("/");
		if (itemArray.length > 0) {
			agentInfo.setAppType(itemArray[0].trim().toLowerCase());
		}
		if (itemArray.length > 1) {
			String versionStr = itemArray[1].trim();
			agentInfo.setVersionStr(versionStr);
		}
		if (itemArray.length > 2) {
			agentInfo.setClientAppId(itemArray[2].trim().toLowerCase());
		}
		if (itemArray.length > 3) {
			agentInfo.setDeviceType(itemArray[3].trim().toLowerCase());
		}
		return agentInfo;
	}
}
