package com.nia.rca.cluster.preprocessor.config;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

@Configuration
@EnableAsync(proxyTargetClass = true)
public class AsyncConfig {

    private final static Logger LOGGER = Logger.getLogger(AsyncConfig.class);

    @Bean(name = "alarmPasingThreadPoolTaskExecutor")
    public Executor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(15); // Runtime.getRuntime().availableProcessors() CPU 코어의 수만큼 최대 스레드를 사용하는 스레드풀을 생성
        taskExecutor.setQueueCapacity(50);
        taskExecutor.setThreadNamePrefix("AlarmPasingExecutor-");
        taskExecutor.initialize();
        return new HandlingExecutor(taskExecutor);
    }

    public class HandlingExecutor implements AsyncTaskExecutor {

        private AsyncTaskExecutor executor;

        public HandlingExecutor(AsyncTaskExecutor executor) {
            this.executor = executor;
        }

        @Override
        public void execute(Runnable task) {
            executor.execute(createWrappedRunnable(task));
        }

        @Override
        public void execute(Runnable task, long startTimeout) {
            executor.execute(createWrappedRunnable(task), startTimeout);
        }

        @Override
        public Future<?> submit(Runnable task) {
            return executor.submit(createWrappedRunnable(task));
        }

        @Override
        public <T> Future<T> submit(final Callable<T> task) {
            return executor.submit(createCallable(task));
        }

        private <T> Callable<T> createCallable(final Callable<T> task) {
            return new Callable<T>() {
                @Override
                public T call() throws Exception {
                    try {
                        return task.call();
                    } catch (Exception ex) {
                        handle(ex);
                        throw ex;
                    }
                }
            };
        }

        private Runnable createWrappedRunnable(final Runnable task) {
            return new Runnable() {
                @Override
                public void run() {
                    try {
                        task.run();
                    } catch (Exception ex) {
                        handle(ex);
                    }
                }
            };
        }

        private void handle(Exception ex) {
            LOGGER.error(">>>>>>>>>>Failed to execute task. : {"+ExceptionUtils.getStackTrace(ex) + "} <<<<<<<<<<<<<<<<<");
        }

        public void destroy() {
            if(executor instanceof ThreadPoolTaskExecutor){
                ((ThreadPoolTaskExecutor) executor).shutdown();
            }
        }
    }
}
