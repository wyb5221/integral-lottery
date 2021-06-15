package com.dachen.integral.biz.service.remote;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.dachen.common.exception.ServiceException;
import com.dachen.commons.micro.comsume.RibbonManager;
import com.dachen.integral.data.constant.MicroUrlConstants;
import com.dachen.integral.data.param.CreditTransferBatchParam;
import com.dachen.integral.data.vo.LiveDurationVO;
import com.dachen.integral.data.vo.UserInfo;
import com.dachen.integral.data.vo.UserInfoVO;
import com.dachen.util.RemoteServiceResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 钟良
 * @desc
 * @date:2017/11/29 15:13 Copyright (c) 2017, DaChen All Rights Reserved.
 */
@Service
public class RemoteCallService {

	private static final Logger logger = LoggerFactory.getLogger(RemoteCallService.class);

	@Autowired
	protected RibbonManager ribbonManager;


	private void checkRemoteResult(RemoteServiceResult remoteServiceResult) {
		if (remoteServiceResult == null) {
			throw new ServiceException("远程调用无响应。");
		}

		if (remoteServiceResult.getResultCode() != RemoteServiceResult.SUCCESS) {
			logger.error("remoteServiceResult：{}", JSON.toJSONString(remoteServiceResult));
			if (StringUtils.isNotEmpty(remoteServiceResult.getDetailMsg())) {
				throw new ServiceException(remoteServiceResult.getDetailMsg());
			} else {
				throw new ServiceException(remoteServiceResult.getResultMsg());
			}
		}
	}


	/**
	 * 发起远程调用并验证返回结果
	 *
	 * @param url
	 * @param map
	 * @return
	 */
	public String sendPost(String url, Map<String, Object> map) {
		logger.info("远程接口调用，url：{}，参数：{}", url, JSON.toJSONString(map));

		String responseStr = ribbonManager.post(url, map);

		logger.info("远程接口调用，返回值：{}", responseStr);

		RemoteServiceResult response = JSON.parseObject(responseStr, RemoteServiceResult.class);

		//验证远程调用参数
		checkRemoteResult(response);

		return JSON.toJSONString(response.getData());
	}

	/**
	 * 发起远程调用并验证返回结果
	 *
	 * @param url
	 * @param
	 * @return
	 */
	public String sendPost(String url, Object objectMap, Map<String, String> headerMap) {
		logger.info("远程接口调用，url：{}，参数：{}", url, JSON.toJSONString(objectMap));

		String responseStr = ribbonManager.post(url, objectMap, headerMap);

		RemoteServiceResult response = JSON.parseObject(responseStr, RemoteServiceResult.class);
		//验证远程调用参数
		checkRemoteResult(response);

		return JSON.toJSONString(response.getData());
	}

	public String sendGet(String url, Object... param) {
		logger.info("远程接口调用，URL：{}，param：{}", url, param);
		String responseStr = ribbonManager.get(url, param);
		logger.info("responseStr : {}", responseStr);
		RemoteServiceResult response = JSON.parseObject(responseStr, RemoteServiceResult.class);
		//验证远程调用参数
		checkRemoteResult(response);

		return JSON.toJSONString(response.getData());
	}


	/**
	 * 发送body方式远程调用
	 *
	 * @param url
	 * @param body
	 * @return
	 */
	public String sendBodyPost(String url, Object body) {
		logger.info("远程接口调用，URL：{}，param：{}", url, JSON.toJSONString(body));
		String responseStr = ribbonManager.post(url, body);
		RemoteServiceResult response = JSON.parseObject(responseStr, RemoteServiceResult.class);
		//验证远程调用参数
		checkRemoteResult(response);
		return JSON.toJSONString(response.getData());
	}


	/**
	 * 发送get请求
	 *
	 * @param url
	 * @param obj
	 * @return
	 */
	public String sendGet(String url, Object obj) {
		logger.info("远程接口调用，url：{}，参数：{}", url, obj);

		String responseStr = ribbonManager.get(url, obj);
		// logger.info("远程接口调用，返回值：{}",responseStr);
		RemoteServiceResult response = JSON.parseObject(responseStr, RemoteServiceResult.class);
		//验证远程调用参数
		checkRemoteResult(response);
		return JSON.toJSONString(response.getData());
	}



	/**
	 * 从auth2获取简要用户信息
	 * @param userIds
	 * @return
	 */
	public Map<Integer, UserInfo> getAuth2UsersByIds(List<String> userIds) {
		Map<Integer, UserInfo> userMap = new LinkedHashMap<>();
		if(CollectionUtils.isEmpty(userIds)){
			return userMap;
		}

		List<String> authUserIds = new LinkedList<>(userIds);
		Map<String, Object> param = new HashMap<>(1);
		param.put("userId",  authUserIds);
		try {
			String response = sendPost(MicroUrlConstants.AUTH_GET_USERS_BY_IDS, param);
			List<UserInfo> userInfos = JSONArray.parseArray(response, UserInfo.class);
			userMap = userInfos.stream().collect(Collectors.toMap(UserInfo :: getUserId, Function.identity()));

		}catch(Exception ex){
			logger.error("###获取auth2用户信息error:{}", JSON.toJSONString(ex));
		}

		return userMap;
	}

	/**
	 * 获取医生详情信息
	 * @param userId
	 * @return
	 */
	public UserInfoVO getDoctorInfo(Integer userId) {

		try {
			String response = sendGet(MicroUrlConstants.HEALTH_GET_DOCTOR_INFO, userId);
			if (Objects.isNull(response)){
				logger.info("获取医生详情信息失败 userId:{}", userId);
				throw new ServiceException("获取医生详情信息失败");
			}
			UserInfoVO userInfo = JSON.parseObject(response, UserInfoVO.class);
			return userInfo;
		}catch(Exception ex){
			logger.error("###获取health用户信息error:{}", JSON.toJSONString(ex));
		}
		return null;
	}


	/**
	 * 学币转账
	 * @param businessCode
	 * @param businessId
	 * @param creator
	 * @param fromReason
	 * @param remark
	 * @param sourceId
	 * @param sourceType
	 * @param targetId
	 * @param targetType
	 * @param toReason
	 * @param value
	 */
	public void transfer(String businessCode,String businessId,String creator,String fromReason,String remark,String sourceId,Integer sourceType,String targetId, Integer targetType,String toReason, Long value) {

		CreditTransferBatchParam param = new CreditTransferBatchParam();
		param.setBusinessCode(businessCode);
		param.setBusinessId(businessId);
		param.setCreator(creator);
		param.setFromReason(fromReason);
		param.setRemark(StringUtils.isEmpty(remark) ? toReason : remark);
		param.setSourceId(sourceId);
		param.setSourceType(sourceType);
		param.setTargetId(targetId);
		param.setTargetType(targetType);
		param.setToReason(toReason);
		param.setValue(value);
		sendBodyPost(MicroUrlConstants.TRANSFER_NEW, param);
	}

	/**
	 * 获取用户参加会议的时长列表
	 * @param userId
	 * @param meetingId
	 */
	public LiveDurationVO getLiveDuration(Integer userId, String meetingId){
		try {
			Map<String, Object> param = new HashMap<>();
			param.put("meetingIdSet",  Arrays.asList(meetingId));
			param.put("userId",  userId);
			String response = sendPost(MicroUrlConstants.GET_JOIN_MEETING_DURATION, param);
			if (Objects.isNull(response)){
				logger.info("获取用户参加会议的时长失败:{}, meetingId:{}", userId, meetingId);
				throw new ServiceException("获取用户参加会议的时长失败");
			}
			LiveDurationVO liveDurationVO = JSON.parseObject(response,LiveDurationVO.class);
			return liveDurationVO;
		}catch (Exception e){
			logger.info("获取用户参加会议的时长异常:{}, meetingId:{}", userId, meetingId);
			logger.info("获取用户参加会议的时长异常:{}", e);
		}
		return null;
	}

}
