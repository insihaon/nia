package com.ipsdn_opt.app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipsdn_opt.app.model.Response;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();

        httpServletResponse.setStatus(200);
        httpServletResponse.setContentType("application/json;charset=utf-8");
        String authCode = httpServletResponse.getHeader("AUTH_CODE");
        String msg = "";
        switch(authCode) {
            case "NOT_AUTHENTICATED": 
                msg = "로그인되지 않은 사용자입니다.";
                break;
            case "EXPIRED": 
                msg = "인증기간이 만료되었습니다.";
                break;
            case "INVALID_TOKEN": 
                msg = "유효한 토큰이 아닙니다.";
                break;
            case "INVALID_USER": 
                msg = "유효하지 않은 사용자입니다.";
                break;
            default:
                msg = "인증에 실패했습니다.";
                break;
        }
        Response response = new Response(false, msg, null);
        PrintWriter out = httpServletResponse.getWriter();
        String jsonResponse = objectMapper.writeValueAsString(response);
        out.print(jsonResponse);
    }
}