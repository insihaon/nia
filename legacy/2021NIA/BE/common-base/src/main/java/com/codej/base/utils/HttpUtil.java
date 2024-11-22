package com.codej.base.utils;

import java.util.Map;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
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

    public static ResponseEntity<?> get(String url) throws CHttpRelayServiceFail, Exception {
        String json = "";
        int timeout = 30;
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * 3000)
                .setConnectionRequestTimeout(timeout * 3000)
                .setSocketTimeout(timeout * 1000).build();

        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
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
            Map<String, String> headers) throws CHttpRelayServiceFail, Exception {
        String json = "";
        int timeout = 30;
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * 3000)
                .setConnectionRequestTimeout(timeout * 3000)
                .setSocketTimeout(timeout * 1000).build();

        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
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
            throws CHttpRelayServiceFail, Exception {
        String json = "";
        int timeout = 30;
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * 3000)
                .setConnectionRequestTimeout(timeout * 3000)
                .setSocketTimeout(timeout * 1000)
                .build();

        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
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

        return new ResponseEntity<String>(json, HttpStatus.OK);
    }

    public static ResponseEntity<?> post(String url, Map<String, Object> param,
            Map<String, String> headers)
            throws CHttpRelayServiceFail, Exception {
        String json = "";
        int timeout = 30;
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * 3000)
                .setConnectionRequestTimeout(timeout * 3000)
                .setSocketTimeout(timeout * 1000)
                .build();

        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
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

}
