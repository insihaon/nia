package com.nia.alarm.ip.preprocessor.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("AsyncService")
public class AsyncService {
    @Async("alarmPasingThreadPoolTaskExecutor")
    public void run(Runnable runnable) {
        runnable.run();
    }
}
