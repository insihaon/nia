package com.codej.nia.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codej.base.dto.AppDto;
import com.codej.base.dto.BaseUser;
import com.codej.base.dto.DbUser;
import com.codej.base.provider.JwtTokenProvider;
import com.codej.base.utils.JsonUtil;
import com.codej.web.controller.AbsAuthController;
import com.codej.web.service.ResponseService;
import com.codej.web.service.UserService;
import com.codej.web.social.service.KakaoService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController("authController")
@RequestMapping(value = "/") 
public class AuthController extends AbsAuthController {
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
    
    @Autowired
    private NiaApiController niaApi;

    @Override
    protected BaseUser updateUser(BaseUser user, JsonObject json) throws Exception {
        if(json != null && "datahub".equals(appDto.getProject()) ) {
            String menuId = null;
            String userId = null;
            // {"username":91187283,"password":"anonymous","ctrlKey":false,"MENU_ID":"O0000183","USER_ID":"T021239112","id":91187283}
            if (!appDto.isDevProfile()) {
                JsonElement menuIdElement = json.get("MENU_ID");
                if(menuIdElement != null) {
                    menuId = menuIdElement.getAsString();
                }
                JsonElement userIdElement = json.get("USER_ID");
                if(userIdElement != null) {
                    userId = userIdElement.getAsString();
                }
            } else {
                menuId = "O0000183";
                userId = "T021239112";
            }

            if(menuId == null || userId == null ) {
                String s = String.format("권한 정보를 가져올 수 없습니다. blackDtlAuth, %s, %s", menuId, userId);
                log.error(s);
                // return responseService.createFailResponse(s);
            }
            
            HashMap<String, Object> param = new HashMap<String, Object>();
            // param.put("LOGIN_ID", uid);
            param.put("USER_ID", userId);
            param.put("MENU_ID", menuId);
            ResponseEntity<String> response = null;
            
            if(appDto.isDevProfile()) {
                response = niaApi.getAuthDev(param);
            } else {
                response = niaApi.getAuth(param);
            }
            
            try {
                String body = response.getBody();
                JsonObject jsonObject = JsonUtil.convertJsonToJsonObject(body);
                JsonObject resObject = jsonObject.get("RES").getAsJsonObject();
                Map<String, Object> res = JsonUtil.fromJson(resObject);
                String USER_ID = (String) res.get("USER_ID");
                String MENU_ID = (String) res.get("MENU_ID");
                String USER_NM = (String) res.get("USER_NM");
                String LOGIN_ID = (String) res.get("LOGIN_ID");
                String EMAIL = (String) res.get("EMAIL");
                List<Map<String, Object>> LIST_NDH_PROFILE_LIST = (List<Map<String, Object>>)res.get("NDH_PROFILE_LIST");
                List<String> listBlackDtlAuthNames = Arrays.asList(new String[]{});
                if (LIST_NDH_PROFILE_LIST != null) {
                    listBlackDtlAuthNames = LIST_NDH_PROFILE_LIST
                            .stream()
                            .map(item -> (String) item.get("BLACK_DTL_AUTH_NM"))
                            .collect(Collectors.toList());
                } 

                DbUser dbUser = (DbUser)user;
                dbUser.setName(USER_NM);
                dbUser.setEmail(EMAIL);
                dbUser.setReserved0(LOGIN_ID);
                dbUser.setReserved1(USER_ID);
                dbUser.setReserved2(MENU_ID);
                boolean isAdmin = listBlackDtlAuthNames.contains("btnManualDelete");
                dbUser.setRoles(isAdmin ? "ROLE_ADMIN" : "ROLE_USER");
                dbUser.setBlackDtlList(listBlackDtlAuthNames);
                getService().setUser(user);
            } catch (Exception e) {
                log.error(e.toString());
            }
        }

        return user;
    }

}