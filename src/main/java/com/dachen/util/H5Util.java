package com.dachen.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author liangcs
 * @desc
 * @date:2017/5/18 11:22
 * @Copyright (c) 2017, DaChen All Rights Reserved.
 */
@Service
public class H5Util {


	private static final Logger logger = LoggerFactory.getLogger(H5Util.class);
	private static final String TEMPLATE_FILE_PATH = "/template/content.html";


	public static void createMainBodyFile(String content, String fileKey) {
		try {
			replaceMainBodyTemplateFile(content, fileKey);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}
	public static void createMainBodyFile(String content,String title, String fileKey) {
		try {
			replaceMainBodyTemplateFile(content,title, fileKey);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private static void replaceMainBodyTemplateFile(String content, String key) throws IOException {
		InputStream inputStream = new ClassPathResource(TEMPLATE_FILE_PATH).getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		StringBuffer sf = new StringBuffer();
		String str = "";
		while ((str = br.readLine()) != null) {

			if (str.contains("{{content}}")) {
				str = str.replace("{{content}}", content);
			}

			if (str.contains("{{JSBridge}}")) {
				str = str.replace("{{JSBridge}}", PropertiesUtil.getContextProperty("dachen.JSBridge"));
			}

			sf.append(str).append("\n");
		}
//		QiniuUtil.upload(sf.toString().getBytes(), key);
	}
	
	private static void replaceMainBodyTemplateFile(String content,String title, String key) throws IOException {
		InputStream inputStream = new ClassPathResource(TEMPLATE_FILE_PATH).getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		StringBuffer sf = new StringBuffer();
		String str = "";
		while ((str = br.readLine()) != null) {

			if (str.contains("{{content}}")) {
				str = str.replace("{{content}}", content);
			}

			if (str.contains("{{JSBridge}}")) {
				str = str.replace("{{JSBridge}}", PropertiesUtil.getContextProperty("dachen.JSBridge"));
			}
			if (str.contains("{{title}}")) {
				str = str.replace("{{title}}", title);
			}

			sf.append(str).append("\n");
		}
//		QiniuUtil.upload(sf.toString().getBytes(), key);
	}

}
