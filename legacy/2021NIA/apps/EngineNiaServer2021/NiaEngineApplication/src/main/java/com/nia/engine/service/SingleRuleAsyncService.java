package com.nia.engine.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("SingleRuleAsyncService")
public class SingleRuleAsyncService {
    @Async("singleRuleThreadPoolTaskExecutor")
    public void run(Runnable runnable) {
        runnable.run();
    }
}
