package com.codej.nia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codej.base.dto.AppDto;

@RestController
@RequestMapping("/api")
public class ProfileController {

    @Autowired
    private AppDto appDto;

    @GetMapping("/profile")
    public String getProfile() {
        return appDto.getProfile();
    }
}
