package com.nia.korenrca.server;

import com.nia.korenrca.shared.ConfigProperties;
import com.nia.korenrca.system.DesktopSystem;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Server {
    private static Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {

        new Server().start();
    }

    private void start() {
        try {
            new DesktopSystem().initialize();
        } catch (Exception e) {
            
            LOGGER.error("시스템 초기화 과정에 오류가 발생하였습니다.", e);
            return;
        }

        try {

            VertxOptions options = new VertxOptions();
            options.setMaxEventLoopExecuteTime(Long.MAX_VALUE);
            options.setMaxWorkerExecuteTime(Long.MAX_VALUE);
            options.setBlockedThreadCheckInterval(200000000);
            Vertx vertx = Vertx.vertx(options);

            if (ConfigProperties.get().getSocketServerStart()) {
                SocketServer.start();
                if (ConfigProperties.get().getProxyServerStart()) {
                    Thread.sleep(2000);
                }
            }

            DeploymentOptions deploymentOptions = new DeploymentOptions();
            deploymentOptions.setWorker(true);
            deploymentOptions.setWorkerPoolName("vertx-worker");
            vertx.deployVerticle(HttpServer.class.getName(), deploymentOptions);

            //스케쥴러 로드
            // 2019-08-16 차트 기능 언로드
            //CronTriggerRunner qRunner = new CronTriggerRunner();
            //qRunner.task();

        } catch (Exception e) {
            LOGGER.error("서버를 시작 중 오류가 발생하였습니다.", e);
        }
    }
}
