package com.codej.web.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.codej.base.provider.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthUserService authUserService;

    private void response(ServletRequest request, ServletResponse response, int status, int code, String message ) throws IOException {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        httpResponse.setStatus(status); 
        httpResponse.setContentType("application/json");
        httpResponse.setCharacterEncoding("UTF-8");

        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("message",message);

        httpResponse.getWriter().write(json.toString());
    }

    // Request로 들어오는 Jwt Token의 유효성을 검증(jwtTokenProvider.validateToken)하는 filter를 filterChain에 등록합니다.
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
            boolean isBanned = token != null && jwtTokenProvider.isBannedTocken(token, true);
            boolean validate = token != null && jwtTokenProvider.validateToken(token, "JwtAuthenticationFilter");
            if (isBanned) {
                JSONObject entity = new JSONObject();
                entity.put("result", "kickout");
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(entity.toString());
                return;
            }

            if (validate) {
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                authUserService.setAuthentication(auth);
            } else {
                // com.codej.web.Intercepts.AuthInterceptor 에서 처리하도록 수정 2023.10.13
            }
        } catch (Exception ex) {
            // 예외 처리
            ex.printStackTrace();
        }

        try {
             /* 전처리 코드 */
             filterChain.doFilter(request, response);
             /* 후처리 코드 */
        } catch (IOException | ServletException ex) {
            try {
                response(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, -999, ex.getMessage());
            } catch (Exception ex2) {
                log.info("doFilter Exception: {}", ex2.toString());
            }
        }
    }
}
