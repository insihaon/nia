package com.codej.nia.controller;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codej.base.dto.response.BaseResponse;
import com.codej.base.provider.JwtTokenProvider;
import com.codej.base.utils.CryptUtil;
import com.codej.base.utils.JsonUtil;
import com.codej.web.controller.AbsDataController;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;

@Slf4j
@RestController
@RequestMapping(value = { "/dh" })

public class NiaApiController extends AbsDataController {

    private static final OkHttpClient client = new OkHttpClient();


    @Autowired
    private JwtTokenProvider jwtTokenProvider;



}
