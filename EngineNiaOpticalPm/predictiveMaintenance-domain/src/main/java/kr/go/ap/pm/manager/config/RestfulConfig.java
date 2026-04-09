package kr.go.ap.pm.manager.config;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.util.Timeout;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import java.time.Duration;

@Configuration
public class RestfulConfig {

    @Value("${client.connect-timeout-ms:3000}")
    private int connectTimeoutMs;

    @Value("${client.read-timeout-ms:5000}")
    private int readTimeoutMs;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {

        // HttpClient 5용 RequestConfig
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofMilliseconds(connectTimeoutMs))          // 연결 타임아웃
                .setConnectionRequestTimeout(Timeout.ofMilliseconds(connectTimeoutMs))// 커넥션 풀에서 빌려오는 타임아웃
                .setResponseTimeout(Timeout.ofMilliseconds(readTimeoutMs))           // 응답 수신 타임아웃
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        HttpComponentsClientHttpRequestFactory factory =
                new HttpComponentsClientHttpRequestFactory(httpClient);

        return builder
                .requestFactory(() -> factory)
                .connectTimeout(Duration.ofMillis(connectTimeoutMs))
                .readTimeout(Duration.ofMillis(readTimeoutMs))
                .build();
    }
}

//@Configuration
//public class RestfulConfig {
//    @Value("${client.connect-timeout-ms:3000}")
//    private int connectTimeoutMs;
//
//    @Value("${client.read-timeout-ms:5000}")
//    private int readTimeoutMs;
//
//    @Bean
//    public RestTemplate restTemplate(RestTemplateBuilder builder) {
//        RequestConfig requestConfig = RequestConfig.custom()
//                .setConnectTimeout(connectTimeoutMs)
//                .setConnectionRequestTimeout(connectTimeoutMs)
//                .setSocketTimeout(readTimeoutMs)
//                .build();
//
//        HttpComponentsClientHttpRequestFactory factory =
//                new HttpComponentsClientHttpRequestFactory(
//                        HttpClients.custom().setDefaultRequestConfig(requestConfig).build());
//
//        return builder
//                .requestFactory(() -> factory)
//                .setConnectTimeout(Duration.ofMillis(connectTimeoutMs))
//                .setReadTimeout(Duration.ofMillis(readTimeoutMs))
//                .build();
//    }
//}





