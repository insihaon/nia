package com.codej.web.Intercepts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
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
public class WebServiceProxyInterceptor implements HandlerInterceptor {

    @Autowired
    protected AppDto appDto;

    private final ObjectMapper objectMapper = new ObjectMapper();


    /*
     * IPMS v4 서버로 요청 전달
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(appDto.getRedirectServerEnable() == false) 
            return true;  // 컨트롤러로 요청 전달

        HttpMethod httpMethod = HttpMethod.valueOf(request.getMethod());
        if(HttpMethod.OPTIONS.equals(httpMethod)) 
            return true;  // 컨트롤러로 요청 전달

        // 원본 요청의 URI 및 쿼리 파라미터 가져오기
        String originalQuery = request.getQueryString();
        String requestUri = request.getRequestURI();
        String fullExternalUrl = appDto.getRedirectServerUrl() + requestUri + (originalQuery == null || originalQuery.isEmpty() ? "" : "?" + originalQuery);

        // 요청 Body에서 encrypt 값을 확인
        Boolean encrypt = isEncrypt(request);

        // 요청 Body 변경 (POST, PUT 경우만)
        String requestBody = null;
        if (HttpMethod.POST.equals(httpMethod) || HttpMethod.PUT.equals(httpMethod)) {
            requestBody = getRequestBody(request);

            if(encrypt) {
                // JSON 문자열을 HashMap 으로 변환
                Map<String, Object> requestMap = objectMapper.readValue(requestBody, HashMap.class);
                String encryptData = (String) requestMap.get(GlobalConstants.Common.DATA);
                String decryptData = EncryptUtil.decryptText(encryptData);
                Map<String, Object> decryptMap = objectMapper.readValue(decryptData, HashMap.class);
                decryptMap.put(GlobalConstants.Common.ENCRYPT, EncryptUtil.encrypt(Boolean.toString(false)));

                requestBody = objectMapper.writeValueAsString(decryptMap);
            }
        }

        CloseableHttpClient httpClient = HttpUtil.createHttpClient(fullExternalUrl, 60000);
        HttpRequestBase requestRelay = null;
        try {
            if (HttpMethod.GET.equals(httpMethod)) {
                requestRelay = new HttpGet(fullExternalUrl);
            } else if (HttpMethod.POST.equals(httpMethod)) {
                requestRelay = new HttpPost(fullExternalUrl);
                ((HttpPost)requestRelay).setEntity(new StringEntity(requestBody, StandardCharsets.UTF_8));
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
                if(encrypt) {
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
            } else  {
                response.getOutputStream().flush();
            }
        
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(response, 500, String.format("Internal Server Error : %s", e.getMessage()));
        }

        return false; // 컨트롤러로 요청을 전달하지 않음
    }

    @SuppressWarnings("unchecked")
    private Boolean isEncrypt(HttpServletRequest request) throws IOException {
        Boolean encrypt = true;
        String requestBody = getRequestBody(request);
        try {
            
            if(appDto.isDevProfile() == false) {
                // 타임스탬프 보안 체크
                long now = new Date().getTime();
                String tsHeader = request.getHeader("_t");
                String ts = EncryptUtil.decryptText(tsHeader);
    
                if (ts == null || Math.abs((now - Long.valueOf(ts)) / 1000) > 10) {
                    throw new CUntrustedRequestException();
                }
            }

            if (requestBody == null) {
                throw new CServiceIncorrectUse("요청 데이터가 비어 있습니다. ");
            }

            Map<String, Object> mapBody = JsonUtil.convertJsonToClass(requestBody, Map.class);
            Object value = mapBody.get(GlobalConstants.Common.ENCRYPT);
            if (value == null) {
                throw new NullPointerException();
            }

            String decryptText = EncryptUtil.decryptText((String) value);
            if (decryptText == null) {
                throw new CServiceIncorrectUse("요청 데이터가 규격에 맞지 안습니다.");
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

    // 요청 해더와 동일하게 설정. 단 content-length 를 제외한다 
    private void copyRequestHeaders(HttpServletRequest src, HttpRequestBase dest) {
        Enumeration<String> headerNames = src.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = src.getHeader(headerName);
            if (!headerName.equals("content-length")) {
                dest.setHeader(headerName, headerValue);
            }
        }
        // 기본 Content-Type을 UTF-8로 설정 (없을 경우)
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
     * @param queryString   user=John&targetKey=123&lang=en
     * @return  user=John&targetKey=modifiedValue&lang=en
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
