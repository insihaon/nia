package com.nia.tsdn.service.result.config;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

@Configuration
public class RestfulConfig {
    @Value("${spring.restTemplate.factory.readTimeout}")
    private int READ_TIMEOUT;

    @Value("${spring.restTemplate.factory.connectTimeout}")
    private int CONNECT_TIMEOUT;

    @Value("${spring.restTemplate.httpClient.maxConnTotal}")
    private int MAX_CONN_TOTAL;

    @Value("${spring.restTemplate.httpClient.maxConnPerRoute}")
    private int MAX_CONN_PER_ROUTE;

    @Bean(name = "ServiceResult_RestTemplate")
//    public RestTemplate restTemplate() {
//
//        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
//        factory.setReadTimeout(READ_TIMEOUT);
//        factory.setConnectTimeout(CONNECT_TIMEOUT);
//
//        HttpClient httpClient = HttpClientBuilder.create()
//                .setMaxConnTotal(MAX_CONN_TOTAL)
//                .setMaxConnPerRoute(MAX_CONN_PER_ROUTE)
//                .build();
//        factory.setHttpClient(httpClient);
//
//        return new RestTemplate(factory);
//    }
    public RestTemplate restTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build();

        SSLConnectionSocketFactory scsf = new SSLConnectionSocketFactory(
             SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build(),
                NoopHostnameVerifier.INSTANCE);
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(scsf).build();

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

        requestFactory.setHttpClient(httpclient);

        RestTemplate restTemplate = new RestTemplate(requestFactory);
        return restTemplate;
    }
}
