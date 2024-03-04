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



}