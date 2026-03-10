package com.codej.web.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.codej.base.dto.AppDto;
import com.codej.base.exception.CAuthenticationException;
import com.codej.base.provider.BaseJwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
// @ConditionalOnExpression("'${myconf.project:dev}' != 'ipms'")
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final BaseJwtTokenProvider baseJwtTokenProvider;
    private final AuthUserService authUserService;
    private final AppDto appDto;


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

    // Request로 들어오는 Jwt Token의 유효성을 검증(baseJwtTokenProvider.validateToken)하는 filter를 filterChain에 등록합니다.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
            if (appDto.getAuthUse()) {
                HttpServletRequest httpRequest = (HttpServletRequest) request;
                String requestURI = httpRequest.getRequestURI();
                if ("/mock".equals(requestURI)) {
                    throw new Exception("JwtAuthenticationFilter PASS");
                }

                String token = baseJwtTokenProvider.resolveToken(httpRequest);
                boolean isBanned = false; //token != null && baseJwtTokenProvider.isBannedTocken(token, true);
                boolean validate = token != null && baseJwtTokenProvider.validateToken(token, "JwtAuthenticationFilter");
                if (isBanned) {
                    JSONObject entity = new JSONObject();
                    entity.put("result", "kickout");
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write(entity.toString());
                    return;
                }

                if (appDto.getSessionJwtCompare()) {
                    log.debug("JWT의 SessionId 와 현재 세션의 SessionId 비교");
                    if (token != null) {
                        HttpSession session = request.getSession(false); // 현재 세션이 없으면 새로 생성하지 않음
                        String sessionIdFromJwt = baseJwtTokenProvider.extractSessionId(token);

                        if (session == null || !sessionIdFromJwt.equals(session.getId())) {
                            // 세션 ID가 일치하지 않으면 인증 실패 응답
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("인증 실패: 세션이 유효하지 않습니다.");
                            log.debug("JWT의 SessionId 와 현재 세션의 SessionId 비교 결과: 불일치");
                            return;
                        } else {
                            log.debug("JWT의 SessionId 와 현재 세션의 SessionId 비교 결과: 일치");
                        }
                    }
                }

                if (validate) {
                    Authentication auth = baseJwtTokenProvider.getAuthentication(token);
                    authUserService.setAuthentication(auth);
                } else {
                    // com.codej.web.Intercepts.AuthInterceptor 에서 처리하도록 수정 2023.10.13
                }
            }
        } catch (CAuthenticationException ex) {
            HttpServletResponse httpResponse = (HttpServletResponse)response;
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN); 
            httpResponse.setContentType("application/json");
            httpResponse.setCharacterEncoding("UTF-8");

            JSONObject json = new JSONObject();
            json.put("code", -999);
            json.put("message","validateToken 실패");

            httpResponse.getWriter().write(json.toString());
            return;
        } catch (Exception ex) {
            // 예외 처리
            ex.printStackTrace();
            throw new CAuthenticationException();
        }

        try {
             /* 전처리 코드 */
             chain.doFilter(request, response);
             /* 후처리 코드 */
        } catch (IOException | ServletException ex) {
            response(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, -999, ex.getMessage());
        }
    }
}
