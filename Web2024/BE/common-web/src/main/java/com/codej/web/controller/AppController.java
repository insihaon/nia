package com.codej.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codej.base.controller.BaseController;
import com.codej.base.dto.AppDto;
import com.codej.base.dto.response.BaseResponse;
import com.codej.base.dto.response.SingleResponse;
import com.codej.web.service.ResponseService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class AppController extends BaseController {

    @Autowired
    private ResponseService responseService; // 결과를 처리할 Service

    @Autowired 
    CacheManager cacheManager;

    @Autowired
    private AppDto appDto;

    @PostMapping(value = "/setting")
    public BaseResponse postSetting() throws Exception {
        log.info("/setting PostMapping");
        // return responseService.createSingleResponse(appDto);
        return SingleResponse.createResult(responseService.createSingleResponse(appDto), true);
    }

    @GetMapping(value = "/setting")
    public ResponseEntity<?> getSetting() throws Exception {
        log.info("/setting GetMapping");
        return new ResponseEntity<>(appDto, HttpStatus.OK);
    }

    @GetMapping(value = "/cache")
    public ResponseEntity<?> getCache(@RequestParam("name") String name) throws Exception {
        // http://localhost:8070/cache?name=user
        
        org.springframework.cache.Cache cache = cacheManager.getCache(name);

        // System.out.println("Cache Contents:");
        // ((HashMap<String, Object>) cache.getNativeCache()).forEach((key, value) -> {
        //     System.out.println("Key: " + key);
        //     System.out.println("Value: " + value);
        // });
        
        return new ResponseEntity<>(cache.getNativeCache(), HttpStatus.OK);
    }
}
