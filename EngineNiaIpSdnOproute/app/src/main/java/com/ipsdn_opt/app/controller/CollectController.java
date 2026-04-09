package com.ipsdn_opt.app.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ipsdn_opt.app.model.Response;
import com.ipsdn_opt.app.service.CollectSvc;

@RestController
public class CollectController {
    @Autowired
    CollectSvc collectSvc;

    @PostMapping("/ipsdn/opt/link")
    public Response linkUpdateFromIpsdn() {
        return collectSvc.linkUpdateFromIpsdn();
    }
    @PostMapping("/ipsdn/opt/interface")
    public Response interfaceUpdateFromIpsdn() {
        return collectSvc.interfaceUpdateFromIpsdn();
    }
    @GetMapping("/ipsdn/opt/latency")
    public Response latencyCheck(@RequestParam(name = "node_id") long node_id, @RequestParam(name = "destnode_id", required = false) Long destNode_id) {
        return collectSvc.latencyCheck(node_id, destNode_id);
    }
    @GetMapping("/ipsdn/opt/latency/allnode")
    public Response latencyCheckAllNode() {
        LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        return collectSvc.latencyCheckAllNode(currentDateTime);
    }
    @GetMapping("/ipsdn/opt/measuredfactors")
    public Response measuredFactors(@RequestParam(name = "nodename", required = false) String nodename) {
        return collectSvc.measuredFactors(nodename);
    }
    // 컨트롤러에게 요청
    @GetMapping("/ipsdn/opt/linktraffic")
    public Response requestLinkTraffic() {
        LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        return collectSvc.requestLinkTraffic(currentDateTime);
    }
    @GetMapping("/ipsdn/opt/factormatrix")
    public Response factorMatrix(@RequestParam(name = "factorname", required = false) String factorName) {
        return collectSvc.requestFactorMatrix(factorName);
    }
    // // DB에서 가져오는 로직
    // @GetMapping("/ipsdn/opt/linktraffic")
    // public Response getLinkTrafficFromControllerDB() {
    //     return collectSvc.getLinkTrafficFromControllerDB();
    // }


    
    @GetMapping("/ipsdn/opt/node/factor/collection/check")
    public Response collectDataCheck(@RequestParam("measureddate") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate measuredDate) {
        return collectSvc.checkCollecting(measuredDate);
    }

    @PostMapping("/ipsdn/opt/nms/id/conversion")
    public Response conversionNmsId() {
        return collectSvc.conversionNmsId();
    }

    @GetMapping("/ipsdn/opt/factor/linktraffic")
    public Response getLinkTraffic(@RequestParam("start_time") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start_time,
                @RequestParam("end_time") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end_time) {
        return collectSvc.getLinkTraffic(start_time, end_time);
    }
    @GetMapping("/ipsdn/opt/factor/linkfactor")
    public Response getLinkFactor(@RequestParam("start_time") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start_time,
                @RequestParam("end_time") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end_time) {
        return collectSvc.getLinkFactor(start_time, end_time);
    }
    @GetMapping("/ipsdn/opt/factor/nodefactor")
    public Response getNodeFactor(@RequestParam("start_time") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start_time,
                @RequestParam("end_time") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end_time) {
        return collectSvc.getNodeFactor(start_time, end_time);
    }
    @GetMapping("/ipsdn/opt/nodefactor/series")
    public Response getNodeFactorForSeries(@RequestParam("factorname") String factorName, 
                @RequestParam("sourcedate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate sourceDate) {
        return collectSvc.getNodeFactorForSeries(factorName, sourceDate);
    }
}
