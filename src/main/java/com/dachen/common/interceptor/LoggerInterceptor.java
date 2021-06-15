package com.dachen.common.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class LoggerInterceptor implements HandlerInterceptor {

	static Logger logger = LoggerFactory.getLogger(LoggerInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("请求：" + request.getRequestURI());
		sb.append("\n");

		Map<String, String[]> paramMap = request.getParameterMap();
		if (!paramMap.isEmpty()) {
			sb.append("?");

			for (String key : paramMap.keySet()) {
				sb.append(key).append("=").append(paramMap.get(key)[0]).append("&");
			}

			sb.setLength(sb.length() - 1);
			sb.append("\n");
		}
		sb.append("Method：" + request.getMethod());
		sb.append("\n");

		sb.append("Content-Type：" + request.getContentType());
		sb.append("\n");

		sb.append("User-Agent：" + request.getHeader("User-Agent"));
		sb.append("\n");

		sb.append("**************************************************");
		sb.append("\n");

		if (logger.isInfoEnabled()) {
			logger.info(sb.toString());
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
