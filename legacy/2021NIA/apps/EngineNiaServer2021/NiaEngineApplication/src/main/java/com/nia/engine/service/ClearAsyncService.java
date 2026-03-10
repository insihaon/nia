package com.nia.engine.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("ClearAsyncService")
public class ClearAsyncService {
    @Async("clearThreadPoolTaskExecutor")
    public void run(Runnable runnable) {
        runnable.run();
    }
}
