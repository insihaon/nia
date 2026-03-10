package com.ipsdn_opt.app.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ipsdn_opt.app.model.RequestMetricConfig;
import com.ipsdn_opt.app.model.Response;
import com.ipsdn_opt.app.service.OptimizeRouteSvc;

@RestController
public class OptimizeRouteController {
    @Autowired
    OptimizeRouteSvc optimizeRouteSvc;

    @PostMapping("/ipsdn/opt/daydata")
    public Response calculateDayData(@RequestParam(name = "sourcedate", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate sourceDate) {
        if(sourceDate==null) sourceDate = LocalDate.now();
        return optimizeRouteSvc.calculateDayData(sourceDate);
    }

    @PostMapping("/ipsdn/opt/optimization")
    public Response optimizeRoute(@RequestParam(name = "sourcedate", required = true) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate sourceDate,
            @RequestParam(name = "faultlink_id", required = false) Long faultLink_id,
            @RequestParam(name = "faultnode_id", required = false) Long faultNode_id) {
        return optimizeRouteSvc.optimizeRoute(sourceDate, faultLink_id, faultNode_id);
    }

    
    @GetMapping("/ipsdn/opt/optimization/pathcheck")
    public Response optimizedPathCheck(@RequestParam(name = "factors", required = false) Boolean isFactors, 
            @RequestParam(name = "interface", required = false) Boolean isInterface, 
            @RequestParam(name = "sourcedate", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate sourceDate) {
        if(isFactors==null) isFactors = false;
        if(isInterface==null) isInterface = false;
        return optimizeRouteSvc.optimizedPathCheck(isFactors, isInterface, sourceDate);
    }
    @GetMapping("/ipsdn/opt/optimization/path/verification")
    public Response optimizedPathVerify(
            @RequestParam(name = "sourcedate", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate sourceDate) {
        return optimizeRouteSvc.optimizedPathVerify(sourceDate);
    }
    @GetMapping("/ipsdn/opt/link/factors/{link_id}")
    public Response findLinkFactors(@PathVariable("link_id") long link_id, 
        @RequestParam(name = "measureddate", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate measuredDate) {
        return optimizeRouteSvc.findLinkfactors(link_id, measuredDate);
    }

    @PostMapping("/ipsdn/opt/optimization/config")
    public Response configOptimizedPath(@RequestParam("sourcedate") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate sourceDate) {
        return optimizeRouteSvc.configOptimizedPath(sourceDate);
    }
    @PostMapping("/ipsdn/opt/optimization/config/restore")
    public Response configMetricRestore(@RequestParam("sourcedate") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate sourceDate) {
        return optimizeRouteSvc.configMetricRestore(sourceDate);
    }
    
    // @PostMapping("/ipsdn/opt/optimization/netconf/metric")
    // public Response requestNetconfMetricModify(@RequestBody List<RequestMetricConfig> reqMetrics) {
    //     return optimizeRouteSvc.requestNetconfMetricModify(reqMetrics);
    // }

    @GetMapping("/ipsdn/opt/cur_route/all")
    public Response getCurrentPathAll() {
        return optimizeRouteSvc.getCurrentPath(null);
    }
    @GetMapping("/ipsdn/opt/cur_route/{e2enode_id}")
    public Response getCurrentPath(@PathVariable("e2enode_id") long e2enode_id) {
        return optimizeRouteSvc.getCurrentPath(e2enode_id);
    }
    @GetMapping("/ipsdn/opt/opt_route/all")
    public Response getOptimizedPathAll(@RequestParam(name = "firstdate", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate firstDate,
                @RequestParam(name = "lastdate", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate lastDate) {
        return optimizeRouteSvc.getOptimizedPath(null, firstDate, lastDate);
    }
    @GetMapping("/ipsdn/opt/opt_route/{e2enode_id}")
    public Response getOptimizedPath(@PathVariable("e2enode_id") long e2enode_id,
                @RequestParam(name = "firstdate", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate firstDate,
                @RequestParam(name = "lastdate", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate lastDate) {
        return optimizeRouteSvc.getOptimizedPath(e2enode_id, firstDate, lastDate);
    }
}
