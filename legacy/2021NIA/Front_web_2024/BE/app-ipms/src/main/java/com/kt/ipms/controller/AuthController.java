package com.kt.ipms.controller;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codej.base.dto.AppDto;
import com.codej.base.dto.model.Data;
import com.codej.base.dto.response.BaseResponse;
import com.codej.base.dto.response.SingleResponse;
import com.codej.base.utils.JsonUtil;
import com.codej.web.controller.AbsAuthController;
import com.codej.web.service.ResponseService;
import com.google.gson.JsonObject;
import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.service.ConfigPropertieService;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.opermgmt.loginmgmt.service.LoginMgmtService;
import com.kt.ipms.legacy.opermgmt.loginmgmt.service.LoginMgmtTxService;
import com.kt.ipms.legacy.opermgmt.loginmgmt.vo.LoginInfoVo;
import com.kt.ipms.legacy.opermgmt.operstdmgmt.service.TbCmnMsgMstService;
import com.kt.ipms.legacy.opermgmt.usermgmt.adapter.UserMgmtAdapterService;
import com.kt.ipms.provider.IpmsJwtTokenProvider;
import com.kt.ipms.service.IpmsUserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController("authController")
@RequestMapping(value = "/")
public class AuthController extends AbsAuthController {

    @Lazy @Autowired
	private UserMgmtAdapterService userMgmtAdapterService;

    @Autowired
    private IpmsUserService ipmsUserService;
    // @Autowired
	// private LoginMgmtService loginMgmtService;
    // @Autowired
	// private LoginMgmtController loginMgmtController;
    
    @Autowired
	private LoginMgmtTxService loginMgmtTxService;
    @Autowired
	private LoginMgmtService loginMgmtService;
    @Autowired
	protected TbCmnMsgMstService tbCmnMstService;
    @Autowired
	private ConfigPropertieService configPropertieService;
    @Autowired
    private ResponseService responseService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IpmsJwtTokenProvider ipmsJwtTokenProvider;

    @Autowired
    private HttpSession session;

    @Autowired
    private AppDto appDto;

    @Override
    protected IpmsUserService getService() {
        return ipmsUserService;
    }
    @PostMapping(value = "/signin")
    public BaseResponse postSignin(
            HttpServletRequest request,
            @RequestBody HashMap<String, Object> body)
            throws Exception {
        String address = getAddress(request);
        JsonObject json = decryptRequestParameter(body);
        String id = json.get("id").getAsString();
        String password = json.get("password").getAsString();
        return signin(request, id, password, address, json);
    }
    public BaseResponse signin(HttpServletRequest request, String uid, String password, String address, JsonObject json) throws Exception {
       
        LoginInfoVo resultLoginVo = null;

        log.debug(String.format("signin: %s, %s, %s", uid, password, json.toString()));
        
        // 레거시 로직 추가
        boolean isOtpCheck = appDto.getOtpUse(); // 레거시 프로파일 값과 맞게 설정
		boolean isTodayLogin = false;

        try {
            LoginInfoVo loginVo = new LoginInfoVo();
            loginVo.setSuserId(uid);
            loginVo.setsUserPw(URLDecoder.decode(password, "UTF-8"));

            isTodayLogin = loginMgmtService.checkTodayLogin(request,loginVo);
            loginMgmtTxService.setHostInfomation(request, loginVo);
            boolean isIpLogin = loginMgmtService.checkIPLogin(loginVo);

            if(isOtpCheck && !isTodayLogin){ // OTP 로직
				resultLoginVo = loginMgmtService.newLogin(request, loginVo);
			}else{ // 일반 로직
				resultLoginVo = loginMgmtService.login(request, loginVo);
			}

            if(resultLoginVo.getsConnRsltCD().equals(CommonCodeUtil.LDAP_RESULT_FAIL)) {
				ServiceException e = new ServiceException("CMN.INFO.00035");
				String msgDesc = tbCmnMstService.selectMsgDesc(e);
                return responseService.createFailResponse(msgDesc);
			} else {
                Boolean isRun = configPropertieService.getBoolean("Mail.isRun");
				if ((isOtpCheck && !isTodayLogin) || (isRun && isIpLogin)){
                // otp 전송
					Map<String,Object> otpRetMap = loginMgmtService.callOtpSend(request,loginVo);					
					otpRetMap.putAll(otpRetMap);
                    if(otpRetMap.get("result").equals("fail")) {
                        String commonMsg = (String)otpRetMap.get("commonMsg");
                        return responseService.createFailResponse(commonMsg);
                    }
                }
            }
        }  catch(ServiceException e)	{
			
			e.printStackTrace();
            responseService.createFailResponse();
			
		} catch (Exception e) {
			
			e.printStackTrace();
            responseService.createFailResponse();
		}

        String token = ipmsJwtTokenProvider.createToken(resultLoginVo, session.getId(), address);
        // // User 정보와 토큰 정보를 반환
        HashMap<String, Object> mapUser = JsonUtil.convertObjectToMap(resultLoginVo);
        Data data = new Data(mapUser);
        data.set(tokenKey, token);
        
        Boolean otpShow = isOtpShow(uid);
        data.set("otpShow", otpShow);

        SingleResponse<Map<String, Object>> response = responseService.createSingleDataResponse(data);
        return SingleResponse.createResult(response, true);
    }
    private String getAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
}