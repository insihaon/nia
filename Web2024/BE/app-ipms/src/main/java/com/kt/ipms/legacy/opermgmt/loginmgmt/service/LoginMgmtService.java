package com.kt.ipms.legacy.opermgmt.loginmgmt.service;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.framework.utils.StringUtils;
import com.kt.ipms.legacy.cmn.service.CommonService;
import com.kt.ipms.legacy.cmn.service.ConfigPropertieService;
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.util.PrintLogUtil;
import com.kt.ipms.legacy.opermgmt.loginmgmt.vo.LoginInfoVo;
import com.kt.ipms.legacy.opermgmt.operstdmgmt.service.TbCmnMsgMstService;
import com.kt.ipms.legacy.opermgmt.usermgmt.vo.TbUserBasVo;


@Component
public class LoginMgmtService {
	
	@Lazy @Autowired
	private LoginMgmtTxService loginMgmtTxService;

	@Lazy @Autowired
	private CommonService commonService;

	@Lazy @Autowired
	private ConfigPropertieService configPropertieService;

	@Autowired
	protected TbCmnMsgMstService tbCmnMstService;

	String sErrMsgInfo = "";
	int failCnt = 0;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public LoginInfoVo login(HttpServletRequest request, LoginInfoVo loginInfoVo)  {
		try {
			
			
			/** 사용자 정보 조회 **/
			TbUserBasVo searchUserBasVo = new TbUserBasVo();
			searchUserBasVo.setSuserId(loginInfoVo.getSuserId());
			PrintLogUtil.printLogInfo("LOGIN USER SELECT START ");
			TbUserBasVo resultUserBasVo = loginMgmtTxService.selectUserBasInfo(searchUserBasVo);
			PrintLogUtil.printLogInfoValueObject(resultUserBasVo);
			PrintLogUtil.printLogInfo("LOGIN USER SELECT END ");
			if (resultUserBasVo == null || !StringUtils.hasText(resultUserBasVo.getSuserId())) {
				PrintLogUtil.printLogInfo("LOGIN USER  SELECT ERROR ");
				//sErrMsgInfo = "CMN.INFO.00039";
				//throw new ServiceException("CMN.INFO.00039");
				
				sErrMsgInfo = "CMN.INFO.00035";
				throw new ServiceException("CMN.INFO.00035");
			}
			
			/** HOST 정보 설정 **/
			loginMgmtTxService.setHostInfomation(request, loginInfoVo);
			
			failCnt = resultUserBasVo.getNloginFailTmscnt().intValue();
			/** LDAP 정보 체크 **/
			boolean isLdapCheck = configPropertieService.getBoolean("Auth.isLdapCheck");
			if (isLdapCheck) {
				PrintLogUtil.printLogInfo("LDAP CHECK START");
				boolean isLdapSuccess = loginMgmtTxService.isLdapSuccess(loginInfoVo);
				PrintLogUtil.printLogInfo("LDAP CHECK END");
				if (!isLdapSuccess) {
					PrintLogUtil.printLogInfo("LOGIN LDAP FAIL");
					failCnt = failCnt+1;
					
					TbUserBasVo  updateUserVo = new TbUserBasVo();
					updateUserVo.setSuserId(loginInfoVo.getSuserId());
					updateUserVo.setSmodifyId(loginInfoVo.getSuserId());
					updateUserVo.setNloginFailTmscnt(BigInteger.valueOf(failCnt));
					loginMgmtTxService.updateUserbas(updateUserVo);
					sErrMsgInfo = "CMN.INFO.00035";
					loginInfoVo.setsConnRsltCD(CommonCodeUtil.LDAP_RESULT_FAIL);
					return loginInfoVo;
					//throw new ServiceException("CMN.INFO.00035");
				}
			}
			
			
			/** 사용자 접속 정보 저장 **/
			Date sysDate = commonService.selectSysDate();
			loginInfoVo.setDloginDt(sysDate);
			loginInfoVo.setDcreateDt(sysDate);
			loginInfoVo.setDmodifyDt(sysDate);
			loginInfoVo.setScreateId(loginInfoVo.getSuserId());
			loginInfoVo.setSmodifyId(loginInfoVo.getSuserId());
			loginInfoVo.setNhndsetApySeq(1);
			PrintLogUtil.printLogInfo("LOGIN HIST INSERT START");
			loginMgmtTxService.insertUserConnHist(loginInfoVo);
			PrintLogUtil.printLogInfo("LOGIN HIST INSERT END");
			
			/** 퇴직상태 정보 체크 **/
			if ((StringUtils.hasText(resultUserBasVo.getSuserSttusCd()) ) && !resultUserBasVo.getSuserSttusCd().equals(CommonCodeUtil.USER_STTUS_NORMAL)) {
				
				PrintLogUtil.printLogInfo("LOGIN USER STTUS FAIL");
					//sErrMsgInfo = "CMN.INFO.00037";
					//throw new ServiceException("CMN.INFO.00037");
				sErrMsgInfo = "CMN.INFO.00035";
				throw new ServiceException("CMN.INFO.00035");
			}
			
			/** 비밀번호 실패 횟수 체크 **/
			if (resultUserBasVo.getNloginFailTmscnt() != null) {
				
				int failMaxCnt = configPropertieService.getInt("Auth.failMaxCnt");
				if (failCnt > failMaxCnt) {
					// 등록되지 않은 사용자 입니다 패스워드 실패 횟수 초과 하였습니다. 관리자에게 실패횟수 초기화를 요청 하셔야 합니다.
					//sErrMsgInfo = "CMN.INFO.00036";
					//throw new ServiceException("CMN.INFO.00036");
					PrintLogUtil.printLogInfo("LOGIN FAILCNT FAIL");
					sErrMsgInfo = "CMN.INFO.00035";
					throw new ServiceException("CMN.INFO.00035");
				}
			}
			
			/** 접속 IP 유효성 체크 **/
			boolean isIpCheck = configPropertieService.getBoolean("Auth.isIpCheck");
			if (isIpCheck && !loginMgmtTxService.isUserIpValidation(loginInfoVo) ) {
				
					// 등록되지 않은 사용자 입니다. 인증되지 않은 IP로 접속을 시도 하였습니다. 관리자에게 IP등록 신청 하셔야 합니다."
					//sErrMsgInfo = "CMN.INFO.00038";
					//throw new ServiceException("CMN.INFO.00038");
				
				PrintLogUtil.printLogInfo("LOGIN IP CHECK FAIL");
				sErrMsgInfo = "CMN.INFO.00035";
				throw new ServiceException("CMN.INFO.00035");
				
			}
			
			CloneUtil.copyObjectInformation(resultUserBasVo, loginInfoVo);
			
			//성공시   로그인 실패 카운트 초기화 
			TbUserBasVo  updateUserVo = new TbUserBasVo();
			updateUserVo.setSuserId(loginInfoVo.getSuserId());
			updateUserVo.setSmodifyId(loginInfoVo.getSuserId());
			updateUserVo.setNloginFailTmscnt(BigInteger.valueOf(0));
			loginMgmtTxService.updateUserbas(updateUserVo);
			
			loginInfoVo.setsConnRsltCD(CommonCodeUtil.LOGIN_SUCCESS);
			/** 사용자 계위 정보 설정 **/
			loginMgmtTxService.setGradeLevelInfo(loginInfoVo);
			
			/** 사용자 메뉴 정보 설정 **/
			loginMgmtTxService.setMenuInfoByAuth(loginInfoVo);
			
		} catch (ServiceException e) {
			e.printStackTrace();
			if (sErrMsgInfo.equals("")){
				throw e;
			}else{
				throw new ServiceException(sErrMsgInfo);
			}
		} catch (Exception e) {
			// 로그인 중 알수 없는 Exception 발생
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00000");
		}
		return loginInfoVo;
	}
	
	@Transactional (propagation = Propagation.REQUIRED)
	public void logout(LoginInfoVo loginInfoVo)  {
		try {
			Date sysDate = commonService.selectSysDate();
			loginInfoVo.setDmodifyDt(sysDate);
			loginInfoVo.setSmodifyId(loginInfoVo.getSuserId());
			loginInfoVo.setDlogoutDt(sysDate);
			loginMgmtTxService.updateUserConnHist(loginInfoVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			// 로그아웃 중 알수 없는 Exception 발생
			throw new ServiceException("CMN.HIGH.00000");
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public LoginInfoVo newLogin(HttpServletRequest request, LoginInfoVo loginInfoVo)  {
		try {
			
			/** 사용자 정보 조회 **/
			TbUserBasVo searchUserBasVo = new TbUserBasVo();
			searchUserBasVo.setSuserId(loginInfoVo.getSuserId());
			PrintLogUtil.printLogInfo("LOGIN USER SELECT START ");
			PrintLogUtil.printLogInfoValueObject(searchUserBasVo);
			TbUserBasVo resultUserBasVo = loginMgmtTxService.selectUserBasInfo(searchUserBasVo);
			PrintLogUtil.printLogInfo("LOGIN USER SELECT END ");
			PrintLogUtil.printLogInfo(resultUserBasVo.getSuserId());
			if (resultUserBasVo == null || !StringUtils.hasText(resultUserBasVo.getSuserId())) {
				PrintLogUtil.printLogInfo("LOGIN USER  SELECT ERROR ");
				//sErrMsgInfo = "CMN.INFO.00039";
				//throw new ServiceException("CMN.INFO.00039");
				
				sErrMsgInfo = "CMN.INFO.00035";
				throw new ServiceException("CMN.INFO.00035");
			}
			
			/** HOST 정보 설정 **/
			loginMgmtTxService.setHostInfomation(request, loginInfoVo);
			
			failCnt = resultUserBasVo.getNloginFailTmscnt().intValue();
			/** LDAP 정보 체크 **/
			boolean isLdapCheck = configPropertieService.getBoolean("Auth.isLdapCheck");
			if (isLdapCheck) {
				PrintLogUtil.printLogInfo("LDAP CHECK START");
				boolean isLdapSuccess = loginMgmtTxService.isLdapSuccess(loginInfoVo);
				PrintLogUtil.printLogInfo("LDAP CHECK END");
				if (!isLdapSuccess) {
					PrintLogUtil.printLogInfo("LOGIN LDAP FAIL");
					failCnt = failCnt+1;
					
					TbUserBasVo  updateUserVo = new TbUserBasVo();
					updateUserVo.setSuserId(loginInfoVo.getSuserId());
					updateUserVo.setSmodifyId(loginInfoVo.getSuserId());
					updateUserVo.setNloginFailTmscnt(BigInteger.valueOf(failCnt));
					loginMgmtTxService.updateUserbas(updateUserVo);
					sErrMsgInfo = "CMN.INFO.00035";
					loginInfoVo.setsConnRsltCD(CommonCodeUtil.LDAP_RESULT_FAIL);
					return loginInfoVo;
					//throw new ServiceException("CMN.INFO.00035");
				}
			}
						
			/** 퇴직상태 정보 체크 **/
			if ((StringUtils.hasText(resultUserBasVo.getSuserSttusCd()) ) && !resultUserBasVo.getSuserSttusCd().equals(CommonCodeUtil.USER_STTUS_NORMAL)) {
				
				PrintLogUtil.printLogInfo("LOGIN USER STTUS FAIL");
					//sErrMsgInfo = "CMN.INFO.00037";
					//throw new ServiceException("CMN.INFO.00037");
				sErrMsgInfo = "CMN.INFO.00035";
				throw new ServiceException("CMN.INFO.00035");
			}
			
			/** 비밀번호 실패 횟수 체크 **/
			if (resultUserBasVo.getNloginFailTmscnt() != null) {
				
				int failMaxCnt = configPropertieService.getInt("Auth.failMaxCnt");
				if (failCnt > failMaxCnt) {
					// 등록되지 않은 사용자 입니다 패스워드 실패 횟수 초과 하였습니다. 관리자에게 실패횟수 초기화를 요청 하셔야 합니다.
					//sErrMsgInfo = "CMN.INFO.00036";
					//throw new ServiceException("CMN.INFO.00036");
					PrintLogUtil.printLogInfo("LOGIN FAILCNT FAIL");
					sErrMsgInfo = "CMN.INFO.00035";
					throw new ServiceException("CMN.INFO.00035");
				}
			}
			
			/** 접속 IP 유효성 체크 **/
			boolean isIpCheck = configPropertieService.getBoolean("Auth.isIpCheck");
			if (isIpCheck && !loginMgmtTxService.isUserIpValidation(loginInfoVo) ) {
				
					// 등록되지 않은 사용자 입니다. 인증되지 않은 IP로 접속을 시도 하였습니다. 관리자에게 IP등록 신청 하셔야 합니다."
					//sErrMsgInfo = "CMN.INFO.00038";
					//throw new ServiceException("CMN.INFO.00038");
				
				PrintLogUtil.printLogInfo("LOGIN IP CHECK FAIL");
				sErrMsgInfo = "CMN.INFO.00035";
				throw new ServiceException("CMN.INFO.00035");
				
			}
			
			loginInfoVo.setsConnRsltCD(CommonCodeUtil.LOGIN_SUCCESS);
			
		} catch (ServiceException e) {
			e.printStackTrace();
			if (sErrMsgInfo.equals("")){
				throw e;
			}else{
				throw new ServiceException(sErrMsgInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 로그인 중 알수 없는 Exception 발생
			throw new ServiceException("CMN.HIGH.00000");
		}
		return loginInfoVo;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public LoginInfoVo newLoginSuccess(HttpServletRequest request, LoginInfoVo loginInfoVo)  {
		try {
			
			
			/** 사용자 정보 조회 **/
			TbUserBasVo searchUserBasVo = new TbUserBasVo();
			searchUserBasVo.setSuserId(loginInfoVo.getSuserId());
			PrintLogUtil.printLog("LOGIN USER SELECT START ");
			TbUserBasVo resultUserBasVo = loginMgmtTxService.selectUserBasInfo(searchUserBasVo);
			PrintLogUtil.printLog("LOGIN USER SELECT END ");
			if (resultUserBasVo == null || !StringUtils.hasText(resultUserBasVo.getSuserId())) {
				PrintLogUtil.printLog("LOGIN USER  SELECT ERROR ");
				//sErrMsgInfo = "CMN.INFO.00039";
				//throw new ServiceException("CMN.INFO.00039");
				
				sErrMsgInfo = "CMN.INFO.00035";
				throw new ServiceException("CMN.INFO.00035");
			}
			
			/** HOST 정보 설정 **/
			loginMgmtTxService.setHostInfomation(request, loginInfoVo);
			
			failCnt = resultUserBasVo.getNloginFailTmscnt().intValue();
			/** LDAP 정보 체크 **/
			boolean isLdapCheck = configPropertieService.getBoolean("Auth.isLdapCheck");
			isLdapCheck = false;
			if (isLdapCheck) {
				PrintLogUtil.printLog("LDAP CHECK START");
				boolean isLdapSuccess = loginMgmtTxService.isLdapSuccess(loginInfoVo);
				PrintLogUtil.printLog("LDAP CHECK END");
				if (!isLdapSuccess) {
					PrintLogUtil.printLog("LOGIN LDAP FAIL");
					failCnt = failCnt+1;
					
					TbUserBasVo  updateUserVo = new TbUserBasVo();
					updateUserVo.setSuserId(loginInfoVo.getSuserId());
					updateUserVo.setSmodifyId(loginInfoVo.getSuserId());
					updateUserVo.setNloginFailTmscnt(BigInteger.valueOf(failCnt));
					loginMgmtTxService.updateUserbas(updateUserVo);
					sErrMsgInfo = "CMN.INFO.00035";
					loginInfoVo.setsConnRsltCD(CommonCodeUtil.LDAP_RESULT_FAIL);
					return loginInfoVo;
					//throw new ServiceException("CMN.INFO.00035");
				}
			}
			
			
			/** 사용자 접속 정보 저장 **/
			Date sysDate = commonService.selectSysDate();
			loginInfoVo.setDloginDt(sysDate);
			loginInfoVo.setDcreateDt(sysDate);
			loginInfoVo.setDmodifyDt(sysDate);
			loginInfoVo.setScreateId(loginInfoVo.getSuserId());
			loginInfoVo.setSmodifyId(loginInfoVo.getSuserId());
			loginInfoVo.setNhndsetApySeq(1);
			PrintLogUtil.printLog("LOGIN HIST INSERT START");
			loginMgmtTxService.insertUserConnHist(loginInfoVo);
			PrintLogUtil.printLog("LOGIN HIST INSERT END");
								
			CloneUtil.copyObjectInformation(resultUserBasVo, loginInfoVo);
			
			//성공시   로그인 실패 카운트 초기화 
			TbUserBasVo  updateUserVo = new TbUserBasVo();
			updateUserVo.setSuserId(loginInfoVo.getSuserId());
			updateUserVo.setSmodifyId(loginInfoVo.getSuserId());
			updateUserVo.setNloginFailTmscnt(BigInteger.valueOf(0));
			loginMgmtTxService.updateUserbas(updateUserVo);
			
			loginInfoVo.setsConnRsltCD(CommonCodeUtil.LOGIN_SUCCESS);
			/** 사용자 계위 정보 설정 **/
			loginMgmtTxService.setGradeLevelInfo(loginInfoVo);
			
			/** 사용자 메뉴 정보 설정 **/
			loginMgmtTxService.setMenuInfoByAuth(loginInfoVo);
			
		} catch (ServiceException e) {
			if (sErrMsgInfo.equals("")){
				throw e;
			}else{
				throw new ServiceException(sErrMsgInfo);
			}
		} catch (Exception e) {
			// 로그인 중 알수 없는 Exception 발생
			throw new ServiceException("CMN.HIGH.00000");
		}
		return loginInfoVo;
	}
	
	@Transactional
	public boolean checkTodayLogin(HttpServletRequest request, LoginInfoVo loginInfoVo){
		boolean retVal = loginMgmtTxService.checkTodayLogin(loginInfoVo);
		return retVal;
	}

	/***
	 * OTP 발송 로직
	 * @param request
	 * @param loginVo
	 * @return
	 */
	@Transactional
	public Map<String, Object> callOtpSend(HttpServletRequest request, LoginInfoVo loginVo) {

		Map<String,Object> apiMap = new HashMap<String,Object>();
		Map<String,Object> apiRetMap = new HashMap<String,Object>();
		Map<String,Object> retMap = new HashMap<String,Object>();
		
		try{
			
			// Step1. 사용자 전화번호 조회
			Map<String,Object> userPhoneMap = loginMgmtTxService.selectUserPhoneNo(loginVo);
			
			String userPhoneNo = (String) userPhoneMap.get("handphone_no");
			PrintLogUtil.printLog("USER PHONE : "+userPhoneNo);
			
			if (null == userPhoneMap.get("handphone_no")){
				throw new ServiceException("CMN.INFO.00054", new String[]{"핸드폰 번호가 존재하지 않습니다."});
			}
			
			// Step2. api_hist 등록
			int histSeq = loginMgmtTxService.insertApiHistory(loginVo,"SEND");
			PrintLogUtil.printLog(">>>>>>>>>>>>>>> HIST SEQ : "+histSeq);
			
			// Step3. api 호출
			apiMap.put("sequenceno", histSeq);
			apiMap.put("phone_number", userPhoneNo);
			apiRetMap = otpApiCall("rest/mobileSmsAuth/authenticationBySMSWithPhoneNo",apiMap);
			
			// Step4. api_hist 업데이트
			if((Boolean) apiRetMap.get("result")){ // API 성공
				apiRetMap.put("apiHistSeq", histSeq);
				apiRetMap.put("suserId",loginVo.getSuserId());
				loginMgmtTxService.updateApiHistory(apiRetMap);
				
				if("1".equals(apiRetMap.get("returncode"))){// Step4-1. API 결과 성공
					PrintLogUtil.printLog(">>>>>>>>>>>> SUCCESS API");
					retMap.put("result", "otp");
				}else{ // Step4-2. API 결과 실패
					PrintLogUtil.printLog(">>>>>>>>>>>> FAIL API");
					retMap.put("result", "fail");
					retMap.put("apiErrorCode", otpApiError(apiRetMap.get("errorcode").toString()));
				}
				
			}else{ // API 실패
				retMap.put("result", "fail");
				retMap.put("commonMsg", "API 호출 실패");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return retMap;
	}
	
	/***
	 * OTP 발송 로직
	 * @param request
	 * @param loginVo
	 * @return
	 */
	@Transactional
	public Map<String, Object> callOtpConfirm(HttpServletRequest request, LoginInfoVo loginVo, Map<String,Object> reqMap) {

		Map<String,Object> apiMap = new HashMap<String,Object>();
		Map<String,Object> apiRetMap = new HashMap<String,Object>();
		Map<String,Object> retMap = new HashMap<String,Object>();
		try{
			
			// Step1. api_hist 등록
			int histSeq = loginMgmtTxService.insertApiHistory(loginVo,"CONFIRM");
			PrintLogUtil.printLog(">>>>>>>>>>>>>>> HIST SEQ : "+histSeq);
			
			// Step2. API 호출
			Map<String,Object> userPhoneMap = loginMgmtTxService.selectUserPhoneNo(loginVo);
			
			String userPhoneNo = (String) userPhoneMap.get("handphone_no");
			PrintLogUtil.printLog("USER PHONE : "+userPhoneNo);
			
			if (null == userPhoneMap.get("handphone_no")){
				throw new ServiceException("CMN.INFO.00054", new String[]{"핸드폰 번호가 존재하지 않습니다."});
			}
			
			// Step3. api 호출
			apiMap.put("sequenceno", histSeq);
			apiMap.put("phone_number", userPhoneNo);
			apiMap.put("auth_no", reqMap.get("authNo").toString());
			
			apiRetMap = otpApiCall("rest/mobileSmsAuth/authenticationBySMSAuthConfirm",apiMap);
			
			// Step4. api_hist 업데이트
			if((Boolean) apiRetMap.get("result")){ // API 성공
				apiRetMap.put("apiHistSeq", histSeq);
				apiRetMap.put("suserId",loginVo.getSuserId());
				loginMgmtTxService.updateApiHistory(apiRetMap);
				
				if("1".equals(apiRetMap.get("returncode"))){// Step4-1. API 결과 성공
					PrintLogUtil.printLog(">>>>>>>>>>>> SUCCESS API");
					loginVo = newLoginSuccess(request, loginVo);
					retMap.put("result", "success");
					// 로그인 히스토리 등록
					Map<String,Object> paraMap = new HashMap<String,Object>();
					paraMap.put("suserId", loginVo.getSuserId());
					paraMap.put("ssuccessYn", "Y");
					paraMap.put("sfailCnt", 0);
					Map<String,Object> selMap = loginMgmtTxService.selectOtpLogin(paraMap);
					if(selMap.get("checkcount").equals(0)){
						loginMgmtTxService.insertOtpLogin(paraMap);
					}else{
						loginMgmtTxService.updateOtpLogin(paraMap);
					}
					
					
				}else{ // Step4-2. API 결과 실패
					PrintLogUtil.printLog(">>>>>>>>>>>> FAIL API");
					retMap.put("result", "fail");
					
					Map<String,Object> paraMap = new HashMap<String,Object>();
					paraMap.put("suserId", loginVo.getSuserId());
					paraMap.put("ssuccessYn", "N");
					paraMap.put("sfailCnt", 1);
					Map<String,Object> selMap = loginMgmtTxService.selectOtpLogin(paraMap);
					paraMap.put("checkcount",selMap.get("checkcount"));
					if(selMap.get("checkcount").equals(0)){
						loginMgmtTxService.insertOtpLogin(paraMap);
					}else{
						loginMgmtTxService.updateOtpLogin(paraMap);
					}
					
					retMap.put("apiErrorCode", otpApiError(apiRetMap.get("errorcode").toString()));
				}
				
			}else{ // API 실패
				retMap.put("result", "fail");
				retMap.put("commonMsg", "API 호출 실패");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return retMap;
	}
		

	public Map<String,Object> otpApiCall(String reqUrl, Map<String,Object> reqParam){
		
		PrintLogUtil.printLog(">>>>>>>>>>>>>>> OTP API CALL START");
		
		Map<String,Object> retObj = new HashMap<String,Object>();
				
//		HttpURLConnection conn = null;
		
		HttpsURLConnection conn = null;
        
		OutputStreamWriter out = null;
		String reqData = "";
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		
		
		try{

			String requestUrl = configPropertieService.getString("Auth.apiOtpUrl");
			String apiId = configPropertieService.getString("Auth.apiOtpId");
			String apiPwd = configPropertieService.getString("Auth.apiOtpPassword");
			String unionStr = apiId+":"+apiPwd;
			String fullUrl = requestUrl + reqUrl;
			
			byte[] encodedBytes = Base64.encodeBase64(unionStr.getBytes());
			String apikey = new String(encodedBytes);
			
			PrintLogUtil.printLogInfo(">>>>>>>>>>>>>>> OTP API CALL APIKEY : "+apikey);
			PrintLogUtil.printLogInfo(">>>>>>>>>>>>>>> OTP API CALL FULL URL : "+fullUrl);
			
			SSLContext sslContext2 = SSLContext.getDefault();
			
			String[] test = sslContext2.getSupportedSSLParameters().getProtocols();
			
			String version = System.getProperty("java.version");
			PrintLogUtil.printLogInfo("JAVA Version : "+version); 
			
			for (int i=0;i<test.length;i++){
				PrintLogUtil.printLogInfo(test[i]);
			}
			
			SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
			
	        sslContext.init(null, null, new SecureRandom());
	        
			URL url = new URL(fullUrl);
//			conn = (HttpURLConnection) url.openConnection();
			conn = (HttpsURLConnection) url.openConnection();
			
			conn.setSSLSocketFactory(sslContext.getSocketFactory());
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json;chartset=utf-8");
			conn.setRequestProperty("authorization", "Basic "+apikey);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			
			ObjectMapper oReq = new ObjectMapper();
			reqData = oReq.writerWithDefaultPrettyPrinter().writeValueAsString(reqParam);
			
			out = new OutputStreamWriter(conn.getOutputStream());
			out.write(reqData);
			out.flush();
			
			// 응답 헤더의 정보를 모두 출력
	        for (Entry<String, List<String>> tempheader : conn.getHeaderFields().entrySet()) {
	            for (String value : tempheader.getValue()) {
	            	PrintLogUtil.printLog(">>>> Response HEADER KEY : "+tempheader.getKey() + " : " + value);
	            }
	        }
	        
	        PrintLogUtil.printLog(">>>> Response Code : "+conn.getResponseCode());
	        PrintLogUtil.printLog(">>>> Response Message : "+conn.getResponseMessage());
	        PrintLogUtil.printLog(">>>> Response ContentType : "+conn.getContentType());
	        
	        BufferedReader brtemp = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			
	        StringBuilder sbtemp = new StringBuilder();
			String linetemp;
			while((linetemp = brtemp.readLine()) != null) {
				sbtemp.append(linetemp).append("\n");
			}
			brtemp.close();
			String resDataTemp = sbtemp.toString();
			
			PrintLogUtil.printLog(">>>> Response Data : "+resDataTemp);
			
	        
			// Response
			int responseCode = conn.getResponseCode();
			
			if(responseCode == HttpURLConnection.HTTP_OK) {
				br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
				
				String line;
				while((line = br.readLine()) != null) {
					sb.append(line).append("\n");
				}
				br.close();
				
				String resData = sb.toString();
				ObjectMapper oRes = new ObjectMapper();
				Map<String,Object> mapRes = oRes.readValue(resDataTemp, Map.class);
				
				retObj.putAll(mapRes);
				retObj.put("result", true);
				
			} else{
				retObj.put("result", false);
			}
	
			PrintLogUtil.pringLogHashMap(retObj);
			
			PrintLogUtil.printLog(">>>>>>>>>>>>>>> OTP API CALL END");
		
		}catch(Exception e){
			PrintLogUtil.printLog(">>>>>>>>>>>>>>> OTP API CALL Exception");
			e.printStackTrace();
			retObj.put("result", false);
		}
				
		return retObj;
	}
	
	public String otpApiError(String errorCode){
		String errorMsg = "";
		
		if(errorCode.equals("90")){
			errorMsg = "1 분 이내 요청불가";
		}else if(errorCode.equals("91")){
			errorMsg = "인증번호 5 회오류횟수 초과(일일잠금)";
		}else if(errorCode.equals("92")){
			errorMsg = "인증번호 불일치";
		}else if(errorCode.equals("93")){
			errorMsg = "인증번호만료(3분 이내 확인)";
		}else if(errorCode.equals("94")){
			errorMsg = "CTN 을 찾을 수없습니다";
		}else if(errorCode.equals("200001")){
			errorMsg = "SHUB 자체 오류(연동/인증/규격)";
		}else if(errorCode.equals("200002")){
			errorMsg = "Request 파라미터값 error";
		}else if(errorCode.equals("200006")){
			errorMsg = "SHUB 자체오류";
		}else if(errorCode.equals("200020")){
			errorMsg = "TCP/IP adapter error";
		}else{
			errorMsg = "알수없는에러";
		}
		
		return errorMsg;
	}

	@Transactional
	public boolean checkIPLogin(LoginInfoVo loginInfoVo){
		boolean result = false;
		int count= loginMgmtTxService.checkIPLogin(loginInfoVo);
		if(count == 0){
			result = true;
		}
		return result;
	}
}
