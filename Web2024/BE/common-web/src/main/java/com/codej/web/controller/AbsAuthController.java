package com.codej.web.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codej.base.controller.BaseController;
import com.codej.base.dto.AppDto;
import com.codej.base.dto.BaseUser;
import com.codej.base.dto.DbUser;
import com.codej.base.dto.model.Data;
import com.codej.base.dto.response.BaseResponse;
import com.codej.base.dto.response.SingleResponse;
import com.codej.base.exception.CSigninFailedException;
import com.codej.base.property.GlobalConstants;
import com.codej.base.provider.JwtTokenProvider;
import com.codej.base.utils.Base64;
import com.codej.base.utils.JsonUtil;
import com.codej.base.utils.cipher.rsa.RSA;
import com.codej.base.utils.cipher.tea.TEA;
import com.codej.web.service.BaseUserService;
import com.codej.web.service.ResponseService;
import com.codej.web.service.UserService;
import com.codej.web.social.dto.KakaoProfile;
import com.codej.web.social.service.KakaoService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public abstract class AbsAuthController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private ResponseService responseService;
    @Autowired
    private KakaoService kakaoService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AppDto appDto;

    protected static final String tokenKey = "accessToken";
    protected HashMap<String, LocalDateTime> userOtpInfo = new HashMap<String, LocalDateTime>();
    protected String otpNumber = null;
    public static HashMap<String, RSA.RSAKey> RsaMAP = new HashMap<>();

    protected BaseUserService getService() {
        return userService.get();
    }

    @GetMapping(value = "/getkey")
    public SingleResponse<?> getKey(HttpServletRequest request) throws Exception {
        return postKey(request);
    }

    @PostMapping(value = "/getkey")
    public SingleResponse<?> postKey(HttpServletRequest request) throws Exception {
        javax.servlet.http.HttpSession session = request.getSession();
        if (session.getAttribute(GlobalConstants.KEY.RSA_KEY) == null) {
            session.setAttribute(GlobalConstants.KEY.RSA_KEY, RSA.createRsa());
        }
        //RSA.RSAKey rsaKey = (RSA.RSAKey) session.getAttribute(GlobalConstants.KEY.RSA_KEY);

        RSA.RSAKey rsaKey = RSA.createRsa();
        AbsAuthController.RsaMAP.put(rsaKey.getPublicKeyModulus(), rsaKey);
        Data data = new Data();
        data.set("m", rsaKey.getPublicKeyModulus());
        data.set("e", rsaKey.getPublicKeyExponent());
        return SingleResponse.createResult(responseService.createSingleDataResponse(data), true);
    }

    @GetMapping(value = "/currentkey")
    public Data currentKey(HttpServletRequest request) throws Exception {
        javax.servlet.http.HttpSession session = request.getSession();
        RSA.RSAKey rsaKey = (RSA.RSAKey) session.getAttribute(GlobalConstants.KEY.RSA_KEY);
        Data data = new Data();
        if (rsaKey != null) {
            data.set("m", rsaKey.getPublicKeyModulus());
            data.set("e", rsaKey.getPublicKeyExponent());
        }
        return data;
    }

    @PostMapping("/loginform")
    public BaseResponse formSignin(HttpServletRequest request, @RequestParam HashMap<String, Object> body) throws Exception {
        return postSignin(request, body);
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
        return signin(id, password, address, json);
    }

    @GetMapping(value = "/signin")
    @Operation(operationId = "로그인_Get", description = "로그인을 한다.")
    public BaseResponse getSignin(
            HttpServletRequest request,
            @RequestParam String id,
            @RequestParam String password)
            throws Exception {
        return signin(id, password, getAddress(request), null);
    }
    
    @PostMapping(value = "/otpCheck")
    @Operation(operationId = "otp체크", description = "otp 일치 여부를 확인한다.")
    public BaseResponse postOtpCheck(
            HttpServletRequest request,
            @RequestBody HashMap<String, Object> body)
            throws Exception {

        String otp = (String) body.get("otp");
        return otpCheck(otp);
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
        
        String token = jwtTokenProvider.createToken(user, address);

        // User 정보와 토큰 정보를 반환
        HashMap<String, Object> mapUser = JsonUtil.convertObjectToMap(user);
        Data data = new Data(mapUser);
        data.set(tokenKey, token);
        
        Boolean otpShow = isOtpShow(uid);
        data.set("otpShow", otpShow);
        
        if (otpShow) {
            String otp = generateOtp(6, 1);
            otpNumber = otp;
            sendOtp(uid, otp);
            /* 
             * 기존방식
             * boolean sended = sendOtp(user, otp);
             * data.set("otp", sended ? AesCryptUtil.encrypt(otp) : null);
             */
        }
        SingleResponse<Map<String, Object>> response = responseService.createSingleDataResponse(data);
        return SingleResponse.createResult(response, true);
    }

    protected BaseUser updateUser(BaseUser user, JsonObject json) throws Exception {
        return user;
    }

    public boolean isOtpShow(String id) {
        boolean otpShow = false;
        if(appDto.getOtpUse()){
            if(userOtpInfo.containsKey(id)) {
                LocalDateTime otpSendTime = userOtpInfo.get(id);
                Duration duration = Duration.between(otpSendTime, LocalDateTime.now());
                if(duration.toHours() < 24) {
                    otpShow = false;
                } else if(duration.toHours() > 24) {
                    otpShow = true;
                }
            } else {
                otpShow = true;
            }
        } else {
            otpShow = false;
        }
        return otpShow;
    }
    public BaseResponse otpCheck(String otpInput) {
        if(otpNumber.equals(otpInput)) {
            return responseService.createSuccessResponse();
        } else {
            return responseService.createFailResponse(-9999, "OTP authentication failed");
        }
    }

    public boolean sendOtp(String id, String number) {
        // String mobile = user.getMobile();
        // if (mobile == null || mobile.length() < 8) {
        //     return false;
        // }
        
        /* db user정보 insert -> agent otp send */
        userOtpInfo.put(id, LocalDateTime.now());
        return true;
    }


    public void save(String id, String password, String name) throws Exception {
        getService().INSERT_USER(
                DbUser.builder()
                        .uid(id)
                        .password(passwordEncoder.encode(password))
                        .name(name).build());
    }

    @PostMapping(value = "/signout")
    public SingleResponse<Map<String, Object>> signout(
            @RequestHeader HashMap<String, Object> header) throws Exception {
        String token = jwtTokenProvider.resolveToken(header);
        jwtTokenProvider.removeToken(token);
        Data data = new Data();
        return responseService.createSingleDataResponse(data);
    }

    @PostMapping(value = "/signin/{provider}")
    public SingleResponse<String> signinByProvider(HttpServletRequest request,
                                                   @PathVariable String provider,
                                                   @RequestParam String accessToken) throws Exception {
        String address = getAddress(request);
        KakaoProfile profile = kakaoService.getKakaoProfile(accessToken);
        DbUser user = (DbUser) getService().findUserByUidAndProvider(String.valueOf(profile.getId()), provider);
        user.setIpAddress(address);
        return responseService.createSingleResponse(
                jwtTokenProvider.createToken(user, address));
    }
    
     /**
     * 전달된 파라미터에 맞게 난수를 생성한다
     * @param len : 생성할 난수의 길이
     * @param dupCd : 중복 허용 여부 (1: 중복허용, 2:중복제거)
     */
    public static String generateOtp(int len, int dupCd) {
        Random rand = new Random();
        StringBuffer numStr = new StringBuffer();   // 난수가 저장될 변수

        for (int i = 0; i < len; i++) {
            // 0~9 까지 난수 생성
            String ran = Integer.toString(rand.nextInt(10));

            if (dupCd == 1) {
                // 중복 허용시 numStr에 append
                numStr.append(ran);
            } else if (dupCd == 2) {
                // 중복을 허용하지 않을시 중복된 값이 있는지 검사한다
                if (numStr.indexOf(ran) < 0) {
                    // 중복된 값이 없으면 numStr에 append
                    numStr.append(ran);
                } else {
                    // 생성된 난수가 중복되면 루틴을 다시 실행한다
                    i -= 1;
                }
            }
        }
        return numStr.toString();
    }

    @PostMapping(value = "/rms/signup")
    public BaseResponse postSignup(
            HttpServletRequest request,
            @RequestBody HashMap<String, Object> body) 
            throws Exception {
        JsonObject json = decryptRequestParameter(body);
        String password = json.get("password").getAsString();

        HashMap<String, Object> user = new HashMap<String, Object>();
        user.put("login_id", (String) json.get("login_id").getAsString());
        user.put("password", (String) passwordEncoder.encode(password));
        user.put("user_id", (String) json.get("user_id").getAsString());
        user.put("user_name", (String) json.get("user_name").getAsString());
        user.put("tel_num", (String) json.get("tel_num").getAsString());
        user.put("email_addr", (String) json.get("email_addr").getAsString());
        user.put("auth_id", "GUEST");
        user.put("approval_yn", "n"); /* 승인여부는 회원가입 시 무조건 n => 관리자가 승인 시 y */


        getService().INSERT_USER(user);
        return responseService.createSuccessResponse();
    }
    
    // @PostMapping(value = "/signup")
    public BaseResponse _signup(
            @RequestParam String id,
            @RequestParam String password,
            @RequestParam String name) throws Exception {
        save(id, password, name);
        return responseService.createSuccessResponse();
    }

    @PostMapping(value = "/signup/{provider}")
    public BaseResponse signupProvider(
            @PathVariable String provider,
            @RequestParam String accessToken,
            @RequestParam String name) throws Exception {
        KakaoProfile profile = kakaoService.getKakaoProfile(accessToken);
        DbUser user = (DbUser) getService().findUserByUidAndProvider(String.valueOf(profile.getId()), provider);
        log.info("signupProvider(): id={}, password={}", user.getUid(), user.getPassword());

        DbUser inUser = (DbUser) DbUser.builder()
                .uid(String.valueOf(profile.getId()))
                .name(name).build();
        getService().INSERT_USER(inUser);
        return responseService.createSuccessResponse();
    }

    @PostMapping(value = "/rms/changepwd")
    public BaseResponse postFindUser(
            HttpServletRequest request,
            @RequestBody HashMap<String, Object> body) 
            throws Exception {
        JsonObject json = decryptRequestParameter(body);
        String password = json.get("login_password").getAsString();

        HashMap<String, Object> user = new HashMap<String, Object>();
        user.put("login_id", (String) json.get("login_id").getAsString());
        user.put("user_id", (String) json.get("user_id").getAsString());
        user.put("user_name", (String) json.get("user_name").getAsString());
        user.put("login_password", (String) passwordEncoder.encode(password));

        getService().changeUserPassword(user);
        return responseService.createSuccessResponse();
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

    private JsonObject decryptRequestParameter(HashMap<String, Object> body) {
        // RequestBody 에서 파라미터 추출
        // log.info("signin(): id={}, password={}", body.get("id"),
        HashMap encData = (HashMap)body.get("data");

        String encryptKey = (String)encData.get("key");
        String encryptValue = Base64.decodeString((String)encData.get("value"));
        String m = (String)encData.get("m");

        RSA.RSAKey rsaKey = RsaMAP.get(m);

        /* TEA 암호화에 사용된 키를 구한다 */
        String teaKey = null;
        try {
            teaKey = RSA.decryptRsa(rsaKey, encryptKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* 암호화된 text를 TEA키를 이용하여 decrypt 한다. */
        TEA tea = new TEA(teaKey);
        String data = tea.decrypt(encryptValue);
        return (JsonObject) JsonParser.parseString(data);
    }

    @PostMapping(value = "/kickout/put/{uid}")
    public void putKickout(
            @PathVariable String uid)
            throws Exception {
        jwtTokenProvider.putKickout(uid);
    }

    @PostMapping(value = "/kickout/pop/{uid}")
    public void popKickout(
            @PathVariable String uid)
            throws Exception {
        jwtTokenProvider.popKickout(uid);
    }
}
