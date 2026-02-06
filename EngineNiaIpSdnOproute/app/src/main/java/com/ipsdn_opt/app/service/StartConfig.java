package com.ipsdn_opt.app.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.ipsdn_opt.app.model.Settings;
import com.ipsdn_opt.app.repository.SettingsRepository;

import lombok.extern.slf4j.Slf4j;

@Profile("prod")
@Component
@Slf4j
public class StartConfig implements CommandLineRunner {
    @Autowired
    ScheduledJob job;
    @Autowired
    SettingsRepository settingsRepository;
    @Autowired
    ApplicationArguments applicationArguments;
    @Autowired
    CollectSvc collectSvc;

    @Override
    public void run(String... args) {
        log.info("[NodeFactor Flow] StartConfig.run() 시작");
        Settings settings = settingsRepository.findAll().get(0);
        log.info("[NodeFactor Flow] Settings 조회 완료 - automeasurement: {}, measurementperiod: {}초",
                settings.getAutomeasurement(), settings.getMeasurementperiod());
        if (settings.getAutomeasurement()) {
            log.info("[NodeFactor Flow] automeasurement=true 확인, Timer 시작 (주기: {}초)",
                    settings.getMeasurementperiod());
            Timer jobScheduler = new Timer();
            jobScheduler.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    try {
                        LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
                        log.info("[NodeFactor Flow] Timer 실행 - latencyCheckAllNode() 호출 시작 (시간: {})", currentDateTime);
                        collectSvc.latencyCheckAllNode(currentDateTime);
                        // collectSvc.requestLinkTraffic(currentDateTime);
                    } catch (Exception e) {
                        log.error("[NodeFactor Flow] Auto-Collection Error. => " + e.getMessage(), e);
                    }
                }
            },
                    0,
                    settings.getMeasurementperiod() * 1000);
        } else {
            log.warn("[NodeFactor Flow] automeasurement=false, Timer 시작 안 함");
        }
    }
}
