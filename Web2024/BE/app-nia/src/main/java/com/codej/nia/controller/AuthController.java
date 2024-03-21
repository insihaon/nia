package com.codej.nia.controller;

import java.util.HashMap;

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
import com.codej.base.dto.response.BaseResponse;
import com.codej.web.controller.AbsAuthController;
import com.codej.web.service.ResponseService;
import com.codej.nia.service.NiaUserService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController("authController")
@RequestMapping(value = "/") 
public class AuthController extends AbsAuthController {
    @Autowired
    private NiaUserService niaUserService;
    @Autowired
    private ResponseService responseService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AppDto appDto;

    @Override
    protected NiaUserService getService() {
        return niaUserService;
    }

    @PostMapping(value = "/nia/upserUser")
    public BaseResponse signup(
            HttpServletRequest request,
            @RequestBody HashMap<String, Object> body) 
            throws Exception {
        JsonObject json = decryptRequestParameter(body);
        String password = json.get("password").getAsString();

        BaseUser user = DbUser.builder()
                .uid(json.get("uid").getAsString())
                .password(passwordEncoder.encode(password))
                .name(json.get("name").getAsString())
                .mobile(json.get("phone").getAsString())
                .email(json.get("email").getAsString())
                .agencyName(json.get("agency_name").getAsString())
                .build();

        getService().UPSERT_USER(user);
        return responseService.createSuccessResponse();
    }

    @PostMapping(value = "/deleteAccount")
    public BaseResponse postDeleteAccount(
            HttpServletRequest request,
            @RequestBody HashMap<String, Object> body)
            throws Exception {
        JsonObject json = decryptRequestParameter(body);
        String uid = json.get("uid").getAsString();
        String password = json.get("password").getAsString();

        return deleteUser(uid, password);
    }

    private BaseResponse deleteUser(String uid, String password) {
        try {
            BaseUser user = getService().getUser(uid);
            if(passwordEncoder.matches(password, user.getPassword())) {
                getService().deleteUserByUid(uid);
                return responseService.createSuccessResponse();
            }
        } catch (Exception e) {
            log.error("deleteUser fail ===>{}", e.toString());
        }
        return responseService.createFailResponse(-9999, "password mismatch");
    }

}