package com.codej.base.utils;

import java.io.IOException;

import org.apache.http.Header;
// HttpEntity 및 HttpEntityEnclosingRequest는 Apache HttpClient의 클래스를 사용합니다.
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;

/**
 * 주어진 HttpUriRequest를 curl 커맨드 형식의 문자열로 변환하는 유틸 클래스
 */
public class CurlUtil {

    /**
     * HttpUriRequest 객체를 받아 curl 커맨드 문자열로 반환
     * 
     * @param request HttpUriRequest 객체
     * @return curl 명령어 문자열
     * @throws IOException 엔티티 문자열 변환 중 발생 가능
     */
    public static String generateCurlCommand(HttpUriRequest request) throws IOException {
        StringBuilder curlCmd = new StringBuilder("curl");

        // GET 요청이 아닌 경우 -X 옵션으로 HTTP 메소드 지정
        String method = request.getMethod();
        if (!"GET".equalsIgnoreCase(method)) {
            curlCmd.append(" -X ").append(method);
        }

        // 모든 헤더를 -H 옵션으로 추가
        for (Header header : request.getAllHeaders()) {
            curlCmd.append(" -H \"")
                   .append(header.getName())
                   .append(": ")
                   .append(header.getValue())
                   .append("\"");
        }

        // 엔티티가 포함된 요청 (POST, PUT 등) 처리
        if (request instanceof HttpEntityEnclosingRequest) {
            HttpEntityEnclosingRequest entityRequest = (HttpEntityEnclosingRequest) request;
            // 여기서 반드시 Apache HttpClient의 HttpEntity 사용
            HttpEntity entity = entityRequest.getEntity();
            if (entity != null) {
                String entityContent = EntityUtils.toString(entity);
                curlCmd.append(" --data '")
                       .append(entityContent.replace("'", "\\'"))
                       .append("'");
            }
        }

        // 최종적으로 요청 URL 추가
        curlCmd.append(" \"").append(request.getURI().toString()).append("\"");

        return curlCmd.toString();
    }
}
