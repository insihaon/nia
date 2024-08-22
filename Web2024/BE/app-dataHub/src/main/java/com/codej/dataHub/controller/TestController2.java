package com.codej.dataHub.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codej.base.controller.BaseController;

// @Controller
@RestController
public class TestController2 extends BaseController{
    // @ResponseBody
    @GetMapping(value = "/now1")
    public ResponseEntity<?> now1() throws Exception {
        return new ResponseEntity<>("now1 = " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), HttpStatus.OK);
    }
}
