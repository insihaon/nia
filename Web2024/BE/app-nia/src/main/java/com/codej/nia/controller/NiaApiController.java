package com.codej.nia.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codej.base.dto.response.ResultResponse;
import com.codej.base.utils.CryptUtil;
import com.codej.base.utils.cipher.rsa.RSA;
import com.codej.web.controller.AbsDataController;
import com.codej.nia.service.NiaService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = { "/nia" })
public class NiaApiController extends AbsDataController {


    @Autowired
    private NiaService niaService;


    public static HashMap<String, RSA.RSAKey> RsaMAP = new HashMap<>();

    @PostMapping(value = "/send/{serviceName}")
    public ResultResponse<?> processService(
        @PathVariable(value = "serviceName", required = true) String serviceName,
        HttpServletRequest request,
        @RequestBody HashMap<String, Object> param) 
        throws Exception {

        try {
            param = CryptUtil.decryptToMap(param);
            switch (serviceName) {
                case "sendMail":
                    return niaService.mailProcessing(param);
                case "dataSnapshot":
                    niaService.send(param);
                    break;
                default:
                    break;
            }
            return responseService.createSuccessResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseService.createFailResponse();
    }
}
