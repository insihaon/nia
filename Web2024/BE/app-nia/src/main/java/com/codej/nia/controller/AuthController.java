package com.codej.nia.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codej.base.dto.AppDto;
import com.codej.base.dto.BaseUser;
import com.codej.base.dto.DbUser;
import com.codej.base.dto.model.Data;
import com.codej.base.dto.response.BaseResponse;
import com.codej.base.dto.response.SingleResponse;
import com.codej.base.exception.CSigninFailedException;
import com.codej.base.property.GlobalConstants;
import com.codej.base.utils.EncryptUtil;
import com.codej.base.utils.JsonUtil;
import com.codej.nia.provider.NiaJwtTokenProvider;
import com.codej.nia.service.NiaService;
import com.codej.nia.service.NiaUserService;
import com.codej.web.controller.AbsAuthController;
import com.codej.web.service.ResponseService;
import com.google.gson.JsonObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController("authController")
@RequestMapping(value = "/")
public class AuthController extends AbsAuthController {
    @Autowired
    private NiaUserService niaUserService;

    @Autowired
    private NiaService niaService;

    @Autowired
    private ResponseService responseService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private NiaJwtTokenProvider niaJwtTokenProvider;

    @Autowired
    private AppDto appDto;

    protected static final String ipsdnTokenKey = "ipsdnToken";

    @Override
    protected NiaUserService getService() {
        return niaUserService;
    }

    public BaseResponse signin(String uid, String password, String address, JsonObject json) throws Exception {
        log.debug(String.format("signin: %s, %s, %s", uid, password, json.toString()));
        BaseUser user = null;
        if (appDto.isDevProfile()) {
            log.debug(passwordEncoder.encode(password));
        }

        user = getService().loginFromDb(uid, password);
        if (appDto.getAuthUse() != false) {
            if (!passwordEncoder.matches(password, user.getPassword())) {
                log.debug("Password is wrong: enc={}, dec={}", user.getPassword(), password);
                throw new CSigninFailedException();
            } else {
                log.debug("Password is match: enc={}, dec={}", user.getPassword(), password);
                getService().updateLoginDate(uid);
            }
        }

        updateUser(user, json);

        user.setPassword(null);
        user.setIpAddress(address);

        String token = niaJwtTokenProvider.createToken(user, address);

        // User 정보와 토큰 정보를 반환
        HashMap<String, Object> mapUser = JsonUtil.convertObjectToMap(user);
        Data data = new Data(mapUser);
        data.set(tokenKey, token);

        Map<String, Object> ipsdnToken = niaService.getIpsdnToken();
        data.set(ipsdnTokenKey, ipsdnToken);

        Boolean otpShow = isOtpShow(uid);
        data.set("otpShow", otpShow);

        if (otpShow) {
            String otp = generateOtp(6, 1);
            otpNumber = otp;
            sendOtp(uid, otp);
        }
        SingleResponse<Map<String, Object>> response = responseService.createSingleDataResponse(data);
        return SingleResponse.createResult(response, true);
    }

    @PostMapping(value = "/nia/insertUser")
    public BaseResponse signup(
        HttpServletRequest request,
        @RequestBody HashMap<String, Object> body)
        throws Exception {
        
        try {
            HashMap<String, Object> decryptBody = getDecryptBody(body);
            JsonObject json = decryptRequestParameter(decryptBody);
            String password = json.get("password").getAsString();

            DbUser user = DbUser.builder()
                .uid(json.get("uid").getAsString())
                .password(passwordEncoder.encode(password))
                .name(json.get("name").getAsString())
                .mobile(json.get("phone").getAsString())
                .email(json.get("email").getAsString())
                .agencyName(json.get("agency_name").getAsString())
                .build();

            getService().INSERT_USER(user);
            return responseService.createSuccessResponse();
        } catch (Exception e) {
            log.error("insertUser fail ===>{}", e.toString());
        }
        return responseService.createFailResponse();
    }

    @PostMapping(value = "/nia/upsertUser")
    public BaseResponse upsertUser(
            HttpServletRequest request,
            @RequestBody HashMap<String, Object> body)
            throws Exception {
        try {
            HashMap<String, Object> decryptBody = getDecryptBody(body);
            JsonObject json = decryptRequestParameter(decryptBody);
            String token = niaJwtTokenProvider.resolveToken(request);
            DbUser currentUser = niaJwtTokenProvider.getUserDetails(token);
            BaseUser dbUser = getService().loginFromDb(currentUser.getUid(), currentUser.getPassword());
            
            String orgPassword = json.get("orgpassword").getAsString();

            if (!passwordEncoder.matches(orgPassword, dbUser.getPassword())) {
                return responseService.createFailResponse(-9999, "password mismatch");
            } else {
                String newPassword = json.get("password").getAsString();

                BaseUser user = DbUser.builder()
                    .uid(json.get("uid").getAsString())
                    .password(passwordEncoder.encode(newPassword))
                    .name(json.get("name").getAsString())
                    .mobile(json.get("phone").getAsString())
                    .email(json.get("email").getAsString())
                    .agencyName(json.get("agency_name").getAsString())
                    .build();
                getService().UPSERT_USER(user);
                return responseService.createSuccessResponse();
            }
        } catch (Exception e) {
            log.error("upsertUser fail ===>{}", e.toString());
        }
        return responseService.createFailResponse();

    }

    @PostMapping(value = "/deleteAccount")
    public BaseResponse postDeleteAccount(
            HttpServletRequest request,
            @RequestBody HashMap<String, Object> body)
            throws Exception {
        try {
            HashMap<String, Object> decryptBody = getDecryptBody(body);
            JsonObject json = decryptRequestParameter(decryptBody);
            String uid = json.get("uid").getAsString();
            String password = json.get("password").getAsString();
    
            return deleteUser(uid, password);
        } catch (Exception e) {
            log.error("deleteAccount fail ===>{}", e.toString());
        }
        return responseService.createFailResponse();
    }

    private BaseResponse deleteUser(String uid, String password) {
        try {
            BaseUser user = getService().getUser(uid);
            if (passwordEncoder.matches(password, user.getPassword())) {
                getService().deleteUserByUid(uid);
                return responseService.createSuccessResponse();
            }
        } catch (Exception e) {
            log.error("deleteUser fail ===>{}", e.toString());
        }
        return responseService.createFailResponse(-9999, "password mismatch");
    }

    private HashMap<String, Object> getDecryptBody(HashMap<String, Object> body) {
        HashMap<String, Object> decryptBody = null;
        try {
            String encryptData = (String) body.get(GlobalConstants.Common.DATA);
            decryptBody = EncryptUtil.decrypt(encryptData);

            if (decryptBody == null) {
                decryptBody = new HashMap<String, Object>(body);
            }
            
        } catch (Exception e) {
            log.error(e.toString());
        }
        return decryptBody;
    }
}