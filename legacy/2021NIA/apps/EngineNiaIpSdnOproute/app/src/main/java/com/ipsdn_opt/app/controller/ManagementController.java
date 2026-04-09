package com.ipsdn_opt.app.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ipsdn_opt.app.model.Response;
import com.ipsdn_opt.app.model.User;
import com.ipsdn_opt.app.service.ManagementSvc;

@RestController
public class ManagementController {
    @Autowired
    private ManagementSvc managementSvc;

    Map<String, String> map = new HashMap<>();
    
    @PostMapping("/ipsdn/opt/signup")
    public Response signUpUser(@RequestBody User user){
        return managementSvc.signUpUser(user);
    }
    
    @PostMapping("/ipsdn/opt/probe/distribution")
    public Response dockerDistributeProbe() {
        return managementSvc.dockerDistributeProbe();
    }

}
