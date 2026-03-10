package com.codej.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codej.base.dto.AppDto;

@Service
public class UserService {

    @Autowired
    private DbUserService userService;

    @Autowired
    private AnonymousUserService anonymouseUserService;
    
    @Autowired
    private AppDto appDto;
    
    public BaseUserService get() {
        if(appDto.getAuthUse() == false) {
            return anonymouseUserService;
        } 
        return userService;
    }
}
