package com.codej.web.Intercepts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class IPAddressInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String ipAddress = request.getHeader("X-Forward-For");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }

        log.info("request preHandle ip={}, url={}, user={}", ipAddress, request.getRequestURI(),
                request.getRemoteUser());

        response.setHeader("Client-Addr", ipAddress);

        /*
         * IP 가 ipv6로 나올 경우 ~/.profile 또는 ~/.bash_profile 에 다음을 추가하세요 
         * export _JAVA_OPTIONS="-Djava.net.preferIPv4Stack=true"
         */
        return true;
    }
}