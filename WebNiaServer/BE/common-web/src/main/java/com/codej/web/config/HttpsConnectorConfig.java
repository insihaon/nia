package com.codej.web.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * HTTPS 커넥터 설정
 * 기존 HTTP 포트를 유지하면서 추가 HTTPS 포트를 활성화한다.
 * myconf.https.enabled=true 일 때만 동작한다.
 */
@Slf4j
@Configuration
@ConditionalOnExpression("'${myconf.https.enabled:false}' == 'true'")
public class HttpsConnectorConfig {

    @Value("${myconf.https.port:8081}")
    private int httpsPort;

    @Value("${myconf.https.key-store}")
    private Resource keyStore;

    @Value("${myconf.https.key-store-password}")
    private String keyStorePassword;

    @Value("${myconf.https.key-alias:nia-https}")
    private String keyAlias;

    @Value("${myconf.https.key-store-type:JKS}")
    private String keyStoreType;

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> httpsCustomizer() {
        return factory -> {
            try {
                Connector httpsConnector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
                httpsConnector.setScheme("https");
                httpsConnector.setSecure(true);
                httpsConnector.setPort(httpsPort);

                Http11NioProtocol protocol = (Http11NioProtocol) httpsConnector.getProtocolHandler();
                protocol.setSSLEnabled(true);
                protocol.setKeystoreFile(keyStore.getURL().toString());
                protocol.setKeystorePass(keyStorePassword);
                protocol.setKeystoreType(keyStoreType);
                protocol.setKeyAlias(keyAlias);

                factory.addAdditionalTomcatConnectors(httpsConnector);
                log.info("HTTPS 커넥터 활성화: port={}, keyAlias={}", httpsPort, keyAlias);
            } catch (IOException e) {
                log.error("HTTPS 커넥터 초기화 실패: {}", e.getMessage(), e);
            }
        };
    }
}
