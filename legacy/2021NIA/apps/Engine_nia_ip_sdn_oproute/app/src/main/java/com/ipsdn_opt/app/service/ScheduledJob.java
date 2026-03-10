package com.ipsdn_opt.app.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduledJob extends TimerTask {
    @Autowired
    public CollectSvc collectSvc;

    public void run() {
        LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        collectSvc.latencyCheckAllNode(currentDateTime);
        collectSvc.requestLinkTraffic(currentDateTime);
    }
}