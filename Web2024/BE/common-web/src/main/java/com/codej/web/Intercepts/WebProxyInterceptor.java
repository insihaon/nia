package com.codej.web.Intercepts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.HandlerInterceptor;

import com.codej.base.dto.AppDto;
import com.codej.base.exception.CServiceIncorrectUse;
import com.codej.base.exception.CUntrustedRequestException;
import com.codej.base.property.GlobalConstants;
import com.codej.base.utils.EncryptUtil;
import com.codej.base.utils.HttpUtil;
import com.codej.base.utils.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WebProxyInterceptor implements HandlerInterceptor {

    @Autowired
    protected AppDto appDto;
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    /*
     * IPMS v4 서버로 요청 전달
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (appDto.getWebProxyEnable() == false)
            return true;  // 컨트롤러로 요청 전달

        HttpMethod httpMethod = HttpMethod.valueOf(request.getMethod());
        if (HttpMethod.OPTIONS.equals(httpMethod))
            return true;  // 컨트롤러로 요청 전달

        // 원본 요청의 URI 및 쿼리 파라미터 가져오기
        String originalQuery = request.getQueryString();
        String requestUri = request.getRequestURI();
        String fullExternalUrl = appDto.getWebProxyUrl() + requestUri + (originalQuery == null || originalQuery.isEmpty() ? "" : "?" + originalQuery);

        // 요청 Body에서 encrypt 값을 확인
        Boolean encrypt = isEncrypt(request);

        // 요청 Body 변경 (POST, PUT 경우만)
        String requestBody = null;
        if (HttpMethod.POST.equals(httpMethod) || HttpMethod.PUT.equals(httpMethod)) {
            requestBody = getRequestBody(request);

            if (encrypt) {
                // JSON 문자열을 HashMap 으로 변환
                Map<String, Object> requestMap = objectMapper.readValue(requestBody, HashMap.class);
                String encryptData = (String) requestMap.get(GlobalConstants.Common.DATA);
                String decryptData = EncryptUtil.decryptText(encryptData);
                Map<String, Object> decryptMap = objectMapper.readValue(decryptData, HashMap.class);
                decryptMap.put(GlobalConstants.Common.ENCRYPT, EncryptUtil.encrypt(Boolean.toString(false)));

                requestBody = objectMapper.writeValueAsString(decryptMap);
            }
        }

        // 기존 HttpUtil.createHttpClient 대신 SSL 인증서 적용 HttpClient 생성
        CloseableHttpClient httpClient;

        int timeout = 60000;
        if (appDto.getWebProxySslEnable()) {
            if (appDto.getWebProxySslP12() != null) {
                httpClient = createHttpClientWithP12(timeout);
            } else {
                httpClient = createHttpClientWithPem(timeout);
            }
        } else {
            httpClient = HttpUtil.createHttpClient(fullExternalUrl, timeout);
        }
        HttpRequestBase requestRelay = null;
        try {
            if (HttpMethod.GET.equals(httpMethod)) {
                requestRelay = new HttpGet(fullExternalUrl);
            } else if (HttpMethod.POST.equals(httpMethod)) {
                requestRelay = new HttpPost(fullExternalUrl);
                ((HttpPost) requestRelay).setEntity(new StringEntity(requestBody, StandardCharsets.UTF_8));
            }

            copyRequestHeaders(request, requestRelay);

            CloseableHttpResponse externalResponse = httpClient.execute(requestRelay);
            org.apache.http.HttpEntity entity = externalResponse.getEntity();
            String responseBody = entity != null ? EntityUtils.toString(entity, StandardCharsets.UTF_8) : "";

            // ** 응답 설정 **
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=UTF-8");
            response.setStatus(externalResponse.getStatusLine().getStatusCode());

            // ** 응답 본문 전송 **
            if (!responseBody.isEmpty()) {
                if (encrypt) {
                    // 문자열을 UTF-8 인코딩의 바이트 배열로 변환
                    byte[] utf8Response = responseBody.getBytes(StandardCharsets.UTF_8);
                    Map<String, Object> requestMap = objectMapper.readValue(utf8Response, HashMap.class);

                    ModelMap resultModel = new ModelMap();
                    String json = objectMapper.writeValueAsString(requestMap.get(GlobalConstants.Common.RESULT));
                    resultModel.addAttribute(GlobalConstants.Common.RESULT, EncryptUtil.encrypt(json));
                    resultModel.addAttribute(GlobalConstants.Common.ENCRYPT, true);
                    response.getWriter().write(objectMapper.writeValueAsString(resultModel));
                    response.getWriter().flush();
                } else {
                    byte[] utf8Response = responseBody.getBytes(StandardCharsets.UTF_8);
                    response.getOutputStream().write(utf8Response);
                    response.getOutputStream().flush();
                }
            } else {
                response.getOutputStream().flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(response, 500, String.format("Internal Server Error : %s", e.getMessage()));
        }

        return false; // 컨트롤러로 요청을 전달하지 않음
    }

        /**
     * PKCS12 형식의 인증서를 로드하여 SSL 인증서를 적용한 HttpClient를 생성한다.
     *
     * @param timeout 타임아웃(ms)
     * @return CloseableHttpClient
     * @throws Exception 인증서 로드 또는 SSLContext 생성 오류 발생 시 예외 발생
     */
    public CloseableHttpClient createHttpClientWithP12(int timeout) throws Exception {
        String password = appDto.getWebProxySslPassword();
        // 1. PKCS12 형식의 클라이언트 인증서 파일 로드
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        // 아래 경로와 비밀번호를 실제 환경에 맞게 수정하세요.
        try (FileInputStream keyStoreStream = new FileInputStream(new File(appDto.getWebProxySslP12()))) {
            keyStore.load(keyStoreStream, password.toCharArray());
        }

        // 2. SSLContext 생성 : KeyStore에서 클라이언트 인증서(key material) 로드
        SSLContext sslContext = SSLContextBuilder.create()
                .loadKeyMaterial(keyStore, password.toCharArray())
                .build();

        // 3. SSLConnectionSocketFactory 생성 : TLS 버전 및 호스트명 검증 설정
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(
                sslContext,
                new String[]{"TLSv1.2"},    // 사용하고자 하는 TLS 프로토콜 버전
                null,                       // 기본 cipher suites 사용
                SSLConnectionSocketFactory.getDefaultHostnameVerifier()
        );

        // 4. 타임아웃 설정 (연결, 요청, 소켓)
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(3000)
                .setConnectionRequestTimeout(3000)
                .setSocketTimeout(timeout)
                .build();

        // 5. HttpClient 생성
        return HttpClients.custom()
                .setSSLSocketFactory(sslSocketFactory)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    /**
     * PEM 형식의 인증서와 개인키를 로드하여 SSL 인증서를 적용한 HttpClient를 생성한다.
     *
     * @param timeout 타임아웃(ms)
     * @return CloseableHttpClient
     * @throws Exception 인증서 로드 또는 SSLContext 생성 오류 발생 시 예외 발생
     */
    public CloseableHttpClient createHttpClientWithPem(int timeout) throws Exception {
        // 1. PEM 형식의 클라이언트 인증서 로드 (X.509 형식)
        File certFile = new File(appDto.getWebProxySslCrt());
        X509Certificate clientCert;
        try (FileInputStream certInputStream = new FileInputStream(certFile)) {
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            clientCert = (X509Certificate) certFactory.generateCertificate(certInputStream);
        }

        // 2. PEM 형식의 개인키 로드 (BouncyCastle 사용)
        
        File keyFile = new File(appDto.getWebProxySslKey());
        PrivateKey privateKey;
        try (PEMParser pemParser = new PEMParser(new FileReader(keyFile))) {
            Object object = pemParser.readObject();
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
            if (object instanceof PEMKeyPair) {
                PEMKeyPair pemKeyPair = (PEMKeyPair) object;
                privateKey = converter.getKeyPair(pemKeyPair).getPrivate();
            } else if (object instanceof PrivateKeyInfo) {
                PrivateKeyInfo privateKeyInfo = (PrivateKeyInfo) object;
                privateKey = converter.getPrivateKey(privateKeyInfo);
            } else {
                throw new IllegalArgumentException("지원되지 않는 PEM 형식: " + object.getClass().getName());
            }
        }

        // 3. KeyStore 생성 후 인증서와 개인키 등록 (PKCS12 형식 사용)
        String password = appDto.getWebProxySslPassword();
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(null, null);
        keyStore.setKeyEntry("client", privateKey, password.toCharArray(), new java.security.cert.Certificate[]{clientCert});

        // 4. SSLContext 빌드 : KeyStore에서 클라이언트 인증서(key material) 로드
        SSLContext sslContext = SSLContextBuilder.create()
                .loadKeyMaterial(keyStore, password.toCharArray())
                .build();

        // 5. SSLConnectionSocketFactory 생성 : TLS 버전 및 호스트 검증 설정
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(
                sslContext,
                new String[]{"TLSv1.2"},    // 사용하고자 하는 TLS 프로토콜 버전
                null,                       // 기본 cipher suites 사용
                SSLConnectionSocketFactory.getDefaultHostnameVerifier()
        );

        // 6. 타임아웃 설정 (연결, 요청, 소켓)
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(3000)
                .setConnectionRequestTimeout(3000)
                .setSocketTimeout(timeout)
                .build();

        // 7. HttpClient 생성
        return HttpClients.custom()
                .setSSLSocketFactory(sslSocketFactory)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }


    
    @SuppressWarnings("unchecked")
    private Boolean isEncrypt(HttpServletRequest request) throws IOException {
        Boolean encrypt = true;
        String requestBody = getRequestBody(request);
        try {

            if (appDto.isDevProfile() == false) {
                // 타임스탬프 보안 체크
                long now = new Date().getTime();
                String tsHeader = request.getHeader("_t");
                String ts = EncryptUtil.decryptText(tsHeader);

                if (ts == null || Math.abs((now - Long.valueOf(ts)) / 1000) > 10) {
                    throw new CUntrustedRequestException();
                }
            }

            if (requestBody == null) {
                throw new CServiceIncorrectUse("요청 데이터가 비어 있습니다.");
            }

            Map<String, Object> mapBody = JsonUtil.convertJsonToClass(requestBody, Map.class);
            Object value = mapBody.get(GlobalConstants.Common.ENCRYPT);
            if (value == null) {
                throw new NullPointerException();
            }

            String decryptText = EncryptUtil.decryptText((String) value);
            if (decryptText == null) {
                throw new CServiceIncorrectUse("요청 데이터가 규격에 맞지 않습니다.");
            }
            encrypt = "true".equals(decryptText);
        } catch (Exception e) {
            String msg = "REQUEST ENCRYPT 위변조";
            log.error(msg, e);
            throw new CUntrustedRequestException(msg);
        }
        return encrypt;
    }

    private String getRequestBody(HttpServletRequest request) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }

    // 요청 헤더 복사 (content-length 제외)
    private void copyRequestHeaders(HttpServletRequest src, HttpRequestBase dest) {
        Enumeration<String> headerNames = src.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = src.getHeader(headerName);
            if (!headerName.equals("content-length")) {
                dest.setHeader(headerName, headerValue);
            }
        }
        // 기본 Content-Type 설정 (없을 경우)
        if (dest.getFirstHeader("Content-Type") == null) {
            dest.setHeader("Content-Type", "application/json; charset=UTF-8");
        }
    }

    private void sendErrorResponse(HttpServletResponse response, int statusCode, String message) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=UTF-8");
            response.setStatus(statusCode);
            response.getWriter().write("{\"error\": \"" + message + "\"}");
            response.getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 요청의 Query Parameter를 변경하는 메서드
     * @param queryString   예: user=John&targetKey=123&lang=en
     * @return  변경된 쿼리 스트링, 예: user=John&targetKey=modifiedValue&lang=en
     */
    private String modifyQueryParams(String queryString) {
        if (queryString == null || queryString.isEmpty()) {
            return "";
        }

        Map<String, String> queryParams = new LinkedHashMap<>();

        // 기존 쿼리 스트링을 파싱하여 Map으로 변환
        for (String param : queryString.split("&")) {
            String[] keyValue = param.split("=", 2); // "key=value" 형태 처리
            try {
                if (keyValue.length == 2) {
                    String key = URLDecoder.decode(keyValue[0], "UTF-8");
                    String value = URLDecoder.decode(keyValue[1], "UTF-8");
                    queryParams.put(key, value);
                } else {
                    // 값이 없는 파라미터 처리 (예: ?flag&key=value)
                    queryParams.put(URLDecoder.decode(keyValue[0], "UTF-8"), "");
                }
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Encoding not supported", e);
            }
        }

        // 특정 파라미터 변경 (예: "targetKey" 값을 "modifiedValue"로 변경)
        if (queryParams.containsKey("targetKey")) {
            queryParams.put("targetKey", "modifiedValue");
        }

        // 변경된 파라미터를 다시 문자열로 변환
        return queryParams.entrySet()
                .stream()
                .map(entry -> {
                    try {
                        return URLEncoder.encode(entry.getKey(), "UTF-8") + "=" +
                               URLEncoder.encode(entry.getValue(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException("Encoding error", e);
                    }
                })
                .collect(Collectors.joining("&"));
    }
}
