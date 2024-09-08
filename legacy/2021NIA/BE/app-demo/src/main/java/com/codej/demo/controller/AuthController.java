package com.codej.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codej.web.controller.AbsAuthController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController("authController")
@RequestMapping(value = "/")
public class AuthController extends AbsAuthController {
}