package com.codej.base.utils;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.codej.base.exception.CHttpRelayServiceFail;
import com.fasterxml.jackson.databind.ObjectMapper;


public class HttpUtil {

    // @formatter:off      
    public static RestTemplate restTemplate = new RestTemplate(); 
    public static RestTemplate sslRestTemplate = new RestTemplate();    
    // @formatter:on

    private HttpUtil() {
    }

    public static HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.setAccessControlAllowOrigin("*");
        return httpHeaders;
    }

    public static HttpHeaders getHeaders(@Nullable MultiValueMap<String, String> headers) {
        HttpHeaders httpHeaders = getHeaders();
        httpHeaders.putAll(headers);
        return httpHeaders;
    }

    public static byte[] download(String url) {
        HttpHeaders headers = HttpUtil.getHeaders();
        ResponseEntity<byte[]> exchange = HttpUtil.restTemplate.exchange(url, HttpMethod.GET,
                new HttpEntity<MultiValueMap<String, Object>>(headers), byte[].class);
        return exchange.getBody();
    }

    public static ResponseEntity<?> get(String url)
            throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CHttpRelayServiceFail,
            Exception {
        String json = "";
        CloseableHttpClient httpClient = createHttpClient(url);
        try {
            HttpGet request = new HttpGet(url);
            request.addHeader("content-type", "application/json;charset=UTF-8");

            CloseableHttpResponse response = httpClient.execute(request);
            json = EntityUtils.toString(response.getEntity());

            request.releaseConnection();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new CHttpRelayServiceFail("Failed to execute HTTP Relay Service.");
        } finally {
            httpClient.close();
        }

        return new ResponseEntity<String>(json, HttpStatus.OK);
    }

    public static ResponseEntity<?> get(String url,
            Map<String, String> headers)
            throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CHttpRelayServiceFail, Exception {
        String json = "";
        CloseableHttpClient httpClient = createHttpClient(url);
        try {
            HttpGet request = new HttpGet(url);

            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    request.addHeader(entry.getKey(), entry.getValue());
                }
            }

            CloseableHttpResponse response = httpClient.execute(request);
            json = EntityUtils.toString(response.getEntity());

            request.releaseConnection();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new CHttpRelayServiceFail("Failed to execute HTTP Relay Service.");
        } finally {
            httpClient.close();
        }

        return new ResponseEntity<String>(json, HttpStatus.OK);
    }

    public static ResponseEntity<?> post(String url, Map<String, Object> param)
            throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CHttpRelayServiceFail,
            Exception {
        String json = "";
        CloseableHttpClient httpClient = createHttpClient(url);
        try {
            HttpPost request = new HttpPost(url);
            request.addHeader("content-type", "application/json;charset=UTF-8");
            request.setEntity(new StringEntity(new ObjectMapper().writeValueAsString(param).toString(), "UTF-8"));

            CloseableHttpResponse response = httpClient.execute(request);
            json = EntityUtils.toString(response.getEntity());

            request.releaseConnection();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new CHttpRelayServiceFail("Failed to execute HTTP Relay Service. -findUserByUid");
        } finally {
            httpClient.close();
        }
        ;
        return new ResponseEntity<String>(json, HttpStatus.OK);
    }

    public static ResponseEntity<?> post(String url, Map<String, Object> param,
            Map<String, String> headers)
            throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CHttpRelayServiceFail, Exception {
        String json = "";
        CloseableHttpClient httpClient = createHttpClient(url);
        try {
            HttpPost request = new HttpPost(url);

            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    request.addHeader(entry.getKey(), entry.getValue());
                }
            }
            request.setEntity(new StringEntity(new ObjectMapper().writeValueAsString(param).toString(), "UTF-8"));

            CloseableHttpResponse response = httpClient.execute(request);
            json = EntityUtils.toString(response.getEntity());

            request.releaseConnection();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new CHttpRelayServiceFail("Failed to execute HTTP Relay Service. -findUserByUid");
        } finally {
            httpClient.close();
        }

        return new ResponseEntity<String>(json, HttpStatus.OK);
    }

    public static CloseableHttpClient createHttpClient(String url) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        return createHttpClient(url, 30000);    // 데이터 읽기 타임아웃 
    }


    public static CloseableHttpClient createHttpClient(String url, int timeout) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        final int connectionTimeout = 3000;         // 서버에 연결할 때의 타임아웃
        final int connectionRequestTimeout = 3000;    // 커넥션 풀에서 커넥션을 할당받을 때의 타임아웃
        
        CloseableHttpClient httpClient = null;
        if (url.startsWith("https")) {
            SSLContext sslcontext = SSLContexts.custom()
                .useProtocol("SSL")
                .loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString) throws CertificateException {
                    return true;
                }
            }).build();

            final RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(connectionTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setSocketTimeout(timeout)
                .build();

            HttpClientBuilder httpClientBuilder = HttpClients.custom();
            httpClientBuilder
                .setDefaultRequestConfig(requestConfig)
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .setSSLContext(sslcontext);
            httpClient = httpClientBuilder.build();
        } else {
            RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(connectionTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setSocketTimeout(timeout).build();

            httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        }
        return httpClient;
    }

}
