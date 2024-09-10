package com.kt.ipms.config;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.ajp.AbstractAjpProtocol;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;

// @Configuration
// 리버스프록시로 대체
public class AjpConfiguration {

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> customizer() {
        return factory -> {
            Connector ajpConnector = new Connector("AJP/1.3");
            ajpConnector.setPort(8009);
            ajpConnector.setSecure(false);
            ajpConnector.setAllowTrace(false);
            ajpConnector.setScheme("http");

            // Configure the AJP protocol
            AbstractAjpProtocol<?> protocol = (AbstractAjpProtocol<?>) ajpConnector.getProtocolHandler();
            protocol.setSecretRequired(true);
            protocol.setSecret("ajp-secret-value"); 

            factory.addAdditionalTomcatConnectors(ajpConnector);
        };
    }
}
