package com.ipsdn_opt.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ipsdn_opt.app.model.Response;
import com.ipsdn_opt.app.model.ShellCommand;
import com.ipsdn_opt.app.service.RoutingTableSvc;

@RestController
public class RoutingTableController {
    @Autowired
    RoutingTableSvc routingTableSvc;

    @PostMapping("/ipsdn/opt/routingtable")
    public @ResponseBody List<Response> collectRoutingTable(@RequestBody ShellCommand shcmd) {
        return routingTableSvc.collectRoutingTable(shcmd);
    }

    @PostMapping("/ipsdn/opt/ospfpath")
    public @ResponseBody Response calculateOspfPath() {
        return routingTableSvc.calculateOspfPath();
    }
    @GetMapping("/ipsdn/opt/ospfpath")
    public @ResponseBody Response getOspfPath(@RequestParam(name = "snode", required = false) String sNode, @RequestParam(name = "rnode", required = false) String rNode) {
        return routingTableSvc.getOspfPath(sNode, rNode);
    }
}
