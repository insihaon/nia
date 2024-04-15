package com.codej.base.dto;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Data
public class AppDto {
    @PostConstruct
    void log() throws JsonProcessingException {
        String context = this.toString();
        context = context.replace(this.getClass().getSimpleName() + "(", "");
        context = context.replaceAll("\\)$", "\n");
        context = context.replace(", ", "\n");

        log.info("\n" + context);
    }

    @Value("${myconf.project:}")
    private String project;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.servlet.context-path:/}")
    private String contextPath;

    @Value("${server.port}")
    private String port;

    @Value("${server.error.redirect:}")
    private String errorRedirect;

    @Value("${spring.datasource.url:}")
    private String db;
    @Value("${spring.datasource.username:}")
    private String dbUser;

    @Value("${spring.datasource2nd.url:}")
    private String db2;
    @Value("${spring.datasource2nd.username:}")
    private String db2User;

    @Value("${spring.datasource3rd.url:}")
    private String db3;
    @Value("${spring.datasource3rd.username:}")
    private String db3User;

    @Value("${spring.datasource4th.url:}")
    private String db4;
    @Value("${spring.datasource4th.username:}")
    private String db4User;

    @Value("${spring.datasource5th.url:}")
    private String db5;
    @Value("${spring.datasource5th.username:}")
    private String db5User;

    @Value("${spring.profiles.active:default}")
    private String profile;

    @Value("${spring.profiles.dev:true}")
    private Boolean profileDev;

    @Value("${myconf.websocket.url:/ws-stomp}")
    private String wsUrl;

    @Value("${myconf.websocket.enabled:true}")
    private String wsEnabled;

    @Value("${myconf.websocket.auth-same-network:true}") // true: redis를 이용해 사용자 정보를 공유 할 수 있는 환경이다.
    private Boolean wsCanAccessUser;

    @Value("${spring.redis.host:127.0.0.1}")
    private String redisHost;

    @Value("${spring.redis.port:6379}")
    private Integer redisPort;

    @Value("${spring.redis.database:}")
    private Integer redisDb;

    @Value("${spring.redis.embedded-server-start}")
    private Boolean redisEmbededServerStart; // true: 자체 redis 서버를 실행한다

    @Value("${spring.redis.enabled:true}")
    private Boolean redisEnabled; // false: redis 를 사용 하지 않는다

    @JsonIgnore
    @Value("${spring.redis.password:}") // 패스워드 설정 시 에러가 떨어지는 케이스가 있다. 원인분석이 필요하다
    private String redisPassword;

    @JsonIgnore
    @Value("${spring.redis.timeout:3000}")
    private Integer redisTimeout;

    @Value("${myconf.auth.use:true}") // false: 인증을 사용하지 않는다
    private Boolean authUse;

    @Value("${myconf.auth.single-only:false}") // false: 다중사용자를 허용한다.
    private Boolean authSingleOnly;

    @Value("${myconf.auth.expired-seconds:86400}") // default: 60*60*24, 24hours
    private long authExpiredSeconds;

    @Value("${myconf.auth.otp.use:false}") // false: OTP 인증을 사용하지 않는다
    private Boolean otpUse;

    @Value("${myconf.auth.login:true}") // false: LOGIN UI를 사용하지 않는다
    private Boolean authLogin;

    @Value("${myconf.encrypt:true}")
    private Boolean encrypt;

    private Boolean isDevProfile = true;

    @Value("${myconf.api-server.ipsdn:http://203.255.249.31:8088}")
    private String ipsdnUrl;

    @Value("${myconf.api-server.user:code}")
    private String ipsdnId;

    @Value("${myconf.api-server.pw:codej!@#}")
    private String ipsdnPass;

    @Value("${myconf.api-server.kong:http://192.168.0.222:8000}")
    private String kongUrl;

    @Value("${myconf.api-server.kong-admin:http://192.168.0.222:8001}")
    private String kongAdminUrl;

    @Value("${myconf.api-server.kong-use:true}")
    private Boolean kongApiUse;

    // @Value("${myconf.api-server.hub-api:http://10.220.178.184:28000}")
    // private String requestApiUrl;

    @Value("${myconf.api-server.tcping:false}")
    private Boolean tcping;

    @Value("${myconf.dhub.auth-url:http://127.0.0.1:8070/dh/profile}")
    private String dhubAuthUrl;

    @Value("${spring.rabbitmq.embedded-server-start:false}")
    private Boolean rabbitmqEmbeddedEnabled;

    @Value("${spring.rabbitmq.enabled:true}")
    private Boolean rabbitmqEnabled; // false: rabbitmq 를 사용 하지 않는다

    @Value("${spring.rabbitmq.address:10.81.208.123}")
    private String rabbitmqAddress;

    @Value("${spring.rabbitmq.port:5675}")
    private int rabbitmqPort;

    @Value("${spring.rabbitmq.username:rcaadmin}")
    private String rabbitmqUserName;

    @JsonIgnore
    @Value("${spring.rabbitmq.password:rcaadmin12#$}")
    private String rabbitmqPassword;

    @Value("${spring.rabbitmq.coreAlarmQueue:TOPAS/COREALARM}")
    private String rabbitmqCoreAlarmQueue;

    @Value("${spring.rabbitmq.aamAlarmQueue:wasIndexAAM.exchange.wasIndexAAM-group}")
    private String rabbitmqAamAlarmQueue;

    @Value("${myconf.api-server.grafana-url:127.0.0.1}")
    private String grafanaUrl;

    @Value("${myconf.api-server.hub-api:http://127.0.0.1:8070/dh}")
    private String hubUrl;

    @Value("${myconf.snapshot-Url:http://incodej-lab.iptime.org:24888/download/}")
    private String snapshotUrl;

    @Value("${myconf.servermonitor-Url:http://incodej-lab.iptime.org:3000/download/}")
    private String serverMonitorUrl;

    @Value("${myconf.aimonitor-Url:http://incodej-lab.iptime.org:82/download/}")
    private String aiMonitorUrl;

    @Value("${myconf.ssh.host:incodej-lab.iptime.org}")

    private String sshHost;

    @Value("${myconf.ssh.port:16891}")
    private int sshPort;

    @Value("${myconf.ssh.user:codej8888}")
    private String sshUser;

    @Value("${myconf.ssh.password:zhfps!}")
    private String sshPassword;

    private Object time;

    @PostConstruct
    public void init() {
        isDevProfile = isDevProfile();
    }

    public Boolean isDevProfile() {
        // List<String> array = Arrays.asList(new String[] { "oper", "test" });
        // return array.contains(this.getProfile()) == false;
        return this.getProfileDev();
    }

    public Object update() {
        this.time = new Date().getTime();
        return this;
    }
}
