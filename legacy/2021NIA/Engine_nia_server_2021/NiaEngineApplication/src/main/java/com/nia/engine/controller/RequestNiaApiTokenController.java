package com.nia.engine.controller;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RequestNiaApiTokenController {
    private static final Logger LOGGER = Logger.getLogger(RequestNiaApiTokenController.class);

    @RequestMapping(value = "RemoteToken", method = RequestMethod.POST)
    public String remoteTokenData(@RequestBody String remoteTokenData){

        LOGGER.info("=====> [RequestNiaApiTokenController] remoteTokenData : " + remoteTokenData + " <=====");

        return null;

    }

}

