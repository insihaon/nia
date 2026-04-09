package com.ipsdn_opt.app.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Profile("prod")
@Service
public class Schedular {
    @Autowired
    OptimizeRouteSvc optimizeRouteSvc;
    @Autowired
    CollectSvc collectSvc;
    @Autowired
    RestSvc restSvc;

    // 매일 새벽 1시 실행
    @Scheduled(cron="0 0 1 * * ?")
    //@Scheduled(cron="0 * * * * *")
    public void calculateDayDataAndOptimizeRoute() {
        restSvc.setCookies(restSvc.controllerLogin("http://203.255.249.31:8088"));
        collectSvc.interfaceUpdateFromIpsdn();
        collectSvc.linkUpdateFromIpsdn();
        LocalDate optDate = LocalDate.now().minusDays(1);
        optimizeRouteSvc.calculateDayData(optDate);
        optimizeRouteSvc.optimizeRoute(optDate, null, null);
        collectSvc.checkCollecting(optDate);
    }

    // 1분마다 실행
    @Scheduled(cron="0 */1 * * * *")
    public void trafficRead() {
        // collectSvc.getLinkTrafficFromControllerDB();

        collectSvc.requestLinkTraffic(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
    }
}
