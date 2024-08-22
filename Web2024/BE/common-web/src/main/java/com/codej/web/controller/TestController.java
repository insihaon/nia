package com.codej.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {
    @ResponseBody
    @GetMapping(value = "/now")
    public ResponseEntity<?> now() throws Exception {
        return new ResponseEntity<>("CurrentTime = " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), HttpStatus.OK);
    }
}
