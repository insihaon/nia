package com.codej.web.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codej.base.controller.BaseController;
import com.codej.base.dto.response.ResultResponse;
import com.codej.base.dto.response.SingleResponse;
import com.codej.base.utils.KongUtil;
import com.codej.web.service.ResponseService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = { "/kong" })
public class KongApiController extends BaseController {

    @Autowired(required = false)
    private KongUtil kongUtil;

    @Autowired(required = false)
    private ResponseService responseService; // 결과를 처리할 Service

    // @PostMapping(value = "/addService")
    public SingleResponse<Object> addService(Map<String, Object> map) throws Exception {
        String serviceName = (String)map.get("serviceName");
        String serviceUrl = (String)map.get("serviceUrl");
        return responseService.createSingleResponse(addService(serviceName, serviceUrl));
    }

    // @PostMapping(value = "/addUser")
    public SingleResponse<Object> addUser(Map<String, Object> map) throws Exception {
        String userName = (String)map.get("userName");
        String apiKey = addUser(userName);
        return responseService.createSingleResponse(apiKey);
    }

    // @PostMapping(value = "/addLinkUserToService")
    public SingleResponse<Object> addLinkUserToService(Map<String, Object> map) throws Exception {
        String userName = (String)map.get("userName");
        String serviceName = (String)map.get("serviceName");
        return responseService.createSingleResponse(addLinkUserToService(userName, serviceName));
    }

    // @PostMapping(value = "/deleteLinkUserToService")
    public SingleResponse<Object> deleteLinkUserToService(Map<String, Object> map) throws Exception {
        String userName = (String)map.get("userName");
        String serviceName = (String)map.get("serviceName");
        return responseService.createSingleResponse(deleteLinkUserToService(userName, serviceName));
    }

    // @PostMapping(value = "/deleteService")
    public SingleResponse<Object> deleteService(Map<String, Object> map) throws Exception {
        String serviceName = (String)map.get("serviceName");
        return responseService.createSingleResponse(deleteService(serviceName));
    }

    // @PostMapping(value = "/deleteUser")
    public SingleResponse<Object> deleteUser(Map<String, Object> map) throws Exception {
        String userName = (String)map.get("userName");
        return responseService.createSingleResponse(deleteUser(userName));
    }

    @PostMapping(value = "/reset")
    public ResultResponse<Boolean> reset(Map<String, Object> map) throws Exception {
        reset();
        return responseService.createSuccessResponse();
    }

    public void reset() throws IOException {
        deleteAllUser();
        deleteAllServices();
    }

    public void countdownTimer(int in_seconds) {
        int seconds = in_seconds;
        log.info("처리 중...\t");
        while (seconds > 0) {
            log.info(seconds + "... ");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            seconds--;
        }
    }

    public String addService(String serviceName, String serviceUrl) throws Exception {
        if (kongUtil.existService(serviceName) == true) {
            return null;
        }

        kongUtil.createService(serviceName, serviceUrl);

        String hostName = getHostName(serviceName);
        String routeKey = kongUtil.createHost(serviceName, hostName);

        kongUtil.enableKeyAuthPlugin(serviceName);
        kongUtil.enableKeyAuthPluginForRoute(routeKey);
        return hostName;
    }

    public boolean deleteService(String serviceName) throws IOException {
        try {
            kongUtil.deleteHost(serviceName);
            kongUtil.deleteService(serviceName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void deleteAllServices() throws IOException {
        kongUtil.deleteAllServices();
    }

    public boolean deleteUser(String userName) throws Exception {
        String userId = kongUtil.getConsumerIdByName(userName);
        kongUtil.deleteConsumer(userId);
        return true;
    }

    public String addUser(String userName) throws Exception {
        kongUtil.createConsumer(userName);
        String api_key = kongUtil.createConsumerApiKey(userName);
        return api_key;
    }

    public void deleteAllUser() throws IOException {
        kongUtil.deleteAllConsumers();
    }

    public String addLinkUserToService(String userName, String serviceName) {
        String aclGroupName = getAclGroupName(serviceName);
        try {
            kongUtil.createAclGroup(userName, aclGroupName);
            kongUtil.addLinkConsumerWithAclGroup(userName, aclGroupName);
            kongUtil.addAclPluginToService(serviceName, aclGroupName);
            return aclGroupName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String deleteLinkUserToService(String userName, String serviceName) {
        String aclGroupName = getAclGroupName(serviceName);
        try {
            kongUtil.deleteLinkConsumerWithAclGroup(userName, aclGroupName);
            return aclGroupName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getHostName(String serviceName) {
        return "HOST_" + serviceName;
    }

    public String getAclGroupName(String serviceName) {
        return "ACL_" + serviceName;
    }
  
 }
