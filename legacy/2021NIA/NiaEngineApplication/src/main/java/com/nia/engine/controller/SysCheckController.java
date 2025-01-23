package com.nia.engine.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class SysCheckController implements HealthIndicator {
    private static final Logger LOGGER = LoggerFactory.getLogger(SysCheckController.class);

    @Override
    public Health health() {
        int Status = 200;

        if (Status == HttpStatus.OK.value()){
            LOGGER.info("서버 상태는 UP 입니다.");
            return Health.up().build();
        }else{
            LOGGER.info("서버 상태는 DOWN 입니다.");
        }
        return Health.down().build();
    }


    @Override
    public Health getHealth(boolean includeDetails) {
        return HealthIndicator.super.getHealth(includeDetails);
    }
}
