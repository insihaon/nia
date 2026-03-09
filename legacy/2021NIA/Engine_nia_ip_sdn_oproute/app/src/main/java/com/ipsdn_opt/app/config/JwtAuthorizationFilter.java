package com.ipsdn_opt.app.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.ipsdn_opt.app.component.JwtProcessor;
import com.ipsdn_opt.app.model.JwtProperties;
import com.ipsdn_opt.app.model.User;
import com.ipsdn_opt.app.repository.UserRepository;

// 시큐리티가 filter가지고 있는데 그 필터중에 BasicAuthenticationFilter가 있고
// 권한이나 인증이 필요한 특정 주소를 요청했을 때 위 필터를 무조건 타게 되어 있음.
// 만약에 권한이 인증이 필요한 주소가 아니라면 이 필터를 안탐
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final UserRepository userRepository;
    private final JwtProcessor jwtProcessor;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository,
                                  JwtProcessor jwtProcessor) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.jwtProcessor = jwtProcessor;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String jwtHeader = request.getHeader(JwtProperties.HEADER_STRING);
        if (jwtHeader == null || !jwtHeader.startsWith(JwtProperties.TOKEN_PREFIX)) {
            response.setHeader("AUTH_CODE", "NOT_AUTHENTICATED");
            chain.doFilter(request, response);
            //throw new ServletException("인증되지 않은 사용자입니다");
            return;
        }

        String jwtToken = jwtProcessor.extractBearer(jwtHeader);
        String loginid = jwtProcessor.decodeJwtToken(jwtToken, JwtProperties.SECRET, "username", response);

        if (loginid != null) {
            User user = userRepository.findByLoginid(loginid);
            //if(user==null) new Exception("해당 username을 갖는 Account를 찾을 수 없습니다.");
            if(user==null) response.setHeader("AUTH_CODE", "INVALID_USER");

            PrincipalDetails principalDetails = new PrincipalDetails(user);
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null,
                    principalDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
            response.setHeader("AUTH_CODE", "AUTHENTICATED");
        }
        chain.doFilter(request, response);
    }
}
