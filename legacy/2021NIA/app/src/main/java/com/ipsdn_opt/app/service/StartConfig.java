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

import lombok.extern.java.Log;
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
        Settings settings = settingsRepository.findAll().get(0);
        if(settings.getAutomeasurement()) {
            Timer jobScheduler = new Timer();
            jobScheduler.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
                            collectSvc.latencyCheckAllNode(currentDateTime);
                            //collectSvc.requestLinkTraffic(currentDateTime);
                        }
                        catch(Exception e) {
                            log.info("Auto-Collection Error. => " + e.getMessage());
                        }
                    }
                },
                0,
                settings.getMeasurementperiod()*1000
            );
        }
    }
}
