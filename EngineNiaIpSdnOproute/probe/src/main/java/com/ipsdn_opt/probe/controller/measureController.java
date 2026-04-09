package com.ipsdn_opt.probe.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ipsdn_opt.probe.model.NetIp;
import com.ipsdn_opt.probe.model.RegexTest;
import com.ipsdn_opt.probe.model.Response;
import com.ipsdn_opt.probe.service.measureSvc;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class measureController {
    @Autowired
    measureSvc measureSvc;

    @GetMapping("/ipsdn/opt/probe/arguments")
    public void printArguments() {
        measureSvc.argumentPrint();
    }

    @GetMapping("/ipsdn/opt/probe/linklatency")
    public Response linkLatency(@RequestParam(name = "measured_time", required = false) String strMeasuredTime) {
        log.info("[NodeFactor Flow] measureController.linkLatency() 호출 - measured_time: {}", strMeasuredTime);
        LocalDateTime measuredTime = LocalDateTime.parse(strMeasuredTime);
        Response response = measureSvc.measureFactors(measuredTime);
        log.info("[NodeFactor Flow] measureController.linkLatency() 완료 - status: {}", response.isStatus());
        return response;
    }
    @GetMapping("/ipsdn/opt/probe/e2elatency")
    public Response e2eLatency(@RequestParam(name = "measured_time") String strMeasuredTime, @RequestParam(name = "destnode_id", required = false) Long destNode_id) {
        LocalDateTime measuredTime = LocalDateTime.parse(strMeasuredTime);
        return measureSvc.e2eLatency(measuredTime, destNode_id);
    }
    @GetMapping("/ipsdn/opt/probe/servercheck")
    public Response serverCheck() {
        return measureSvc.owampCheck();
    }

    @GetMapping("/ipsdn/opt/probe/regexTest")
    public Response regexTest(@RequestBody RegexTest regex) {
        return measureSvc.regexTest(regex);
    }

    @PostMapping("/ipsdn/opt/probe/pingcheck")
    public Response pingCheck(@RequestBody List<String> netipList) {
        return measureSvc.pingCheck(netipList);
    }

}
