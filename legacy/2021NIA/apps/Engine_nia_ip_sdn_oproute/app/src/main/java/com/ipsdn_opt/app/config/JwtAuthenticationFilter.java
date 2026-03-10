package com.ipsdn_opt.app.config;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipsdn_opt.app.component.JwtProcessor;
import com.ipsdn_opt.app.model.JwtProperties;
import com.ipsdn_opt.app.model.Response;
import com.ipsdn_opt.app.model.User;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter가 있는데
// /login요청해서 username, password 전송하면(post)
// UsernamePasswordAuthenticationFilter가 동작을 함
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtProcessor jwtProcessor;

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(request.getInputStream(), User.class);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getLoginid(), user.getPassword());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        PrincipalDetails principalDetails  = (PrincipalDetails) authResult.getPrincipal();

        String jwtToken = jwtProcessor.createAuthJwtToken(principalDetails);

        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + " " + jwtToken);

        ObjectMapper objectMapper = new ObjectMapper();
        response.setHeader("AUTH_CODE", "AUTHENTICATED");
        response.setStatus(200);
        response.setContentType("application/json;charset=utf-8");
        Response responseBody = new Response(true,"Login Success.",null);
        PrintWriter out = response.getWriter();
        String jsonResponse = objectMapper.writeValueAsString(responseBody);
        out.print(jsonResponse);
    }
}