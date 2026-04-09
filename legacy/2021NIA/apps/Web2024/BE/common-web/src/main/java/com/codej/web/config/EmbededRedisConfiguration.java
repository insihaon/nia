package com.codej.web.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import redis.embedded.RedisServer;

/**
 * 로컬 환경일경우 내장 레디스가 실행된다.
 */
@Slf4j
@ConditionalOnExpression("'${spring.redis.embedded-server-start}' == 'true'")
@Configuration
public class EmbededRedisConfiguration {

    @Value("${spring.redis.port:6379}")
    private int redisPort;

    @Value("${spring.redis.maxheap:128M}")
    private String springRedisMaxheap;

    private RedisServer redisServer;

    @PostConstruct
    public void redisServer() throws IOException, InterruptedException{

        // case 1. isActive 가 정상적으로 안되서 사용가능한 port를 찾는 방식으로 변경.
        // int port = isRedisRunning() ? findAvailablePort() : redisPort;

        // case 2. 6379 포트를 Kill 하고 실행
        if(isRedisRunning()) {
            killRedisServerProcessCommand();
        }

        int port = redisPort;
        // redisServer = new RedisServer(port);
        redisServer = RedisServer.builder()
            .port(port)
            .setting("maxmemory " + springRedisMaxheap) //maxmemory 128M
            .build();
        
        try {
            if(redisServer.isActive()) {
                stopRedis();
            }
            redisServer.start();
            log.info(">>> EmbededRedisServer started <<< : port={}", port);
        } catch (Exception e) {
            log.error("EmbededRedisServer error: port={}, message={}", port, e.getMessage());
        }
    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }

    /**
     * Embedded Redis가 현재 실행중인지 확인
     */
    private boolean isRedisRunning() throws IOException {
        return isRunning(executeGrepProcessCommand(redisPort));
    }

    /**
     * 현재 PC/서버에서 사용가능한 포트 조회
     */
    public int findAvailablePort() throws IOException {

        for (int port = 7000; port <= 65535; port++) {
            Process process = executeGrepProcessCommand(port);
            if (!isRunning(process)) {
                return port;
            }
        }

        throw new IllegalArgumentException("Not Found Available port: 6380 ~ 65535");
    }

    private void killRedisServerProcessCommand() throws IOException, InterruptedException {
        // 운영체제 구분 (window, window 가 아니면 무조건 linux 로 판단)
        if (System.getProperty("os.name").indexOf("Windows") > -1) {
            String command = String.format("taskkill /f /im redis-server*");
            String[] shell = {"cmd", "/c", command};
            Runtime.getRuntime().exec(shell);
            Thread.sleep(1000);
        } else {
            String command = String.format("kill -9 $(lsof -t -i:%d)", redisPort);
            String[] shell = {"/bin/sh", "-c", command};
            Runtime.getRuntime().exec(shell);
            Thread.sleep(1000);
        }
    }

    /**
     * 해당 port를 사용중인 프로세스 확인하는 sh 실행
     */
    private Process executeGrepProcessCommand(int port) throws IOException {
        Process process;

        // 운영체제 구분 (window, window 가 아니면 무조건 linux 로 판단)
        if (System.getProperty("os.name").indexOf("Windows") > -1) {
            String command = String.format("netstat -nao | findstr LISTEN | findstr :%d", port);
            String[] shell = {"cmd", "/c", command};
            process = Runtime.getRuntime().exec(shell);
        } else {
            String command = String.format("netstat -nat | grep LISTEN | grep %d", port);
            String[] shell = {"/bin/sh", "-c", command};
            process = Runtime.getRuntime().exec(shell);
        }
        return process;
    }

    /**
     * 해당 Process가 현재 실행중인지 확인
     */
    private boolean isRunning(Process process) {
        String line;
        StringBuilder pidInfo = new StringBuilder();

        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            while ((line = input.readLine()) != null) {
                pidInfo.append(line);
            }
        } catch (Exception e) {
            // 예외 처리
        }

        return !StringUtils.isEmpty(pidInfo.toString());
    }
}

