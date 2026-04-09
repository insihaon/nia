package com.codej.web.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.codej.base.dto.DbUser;

/**
 * AuthenticationUserService
 */
@Service
public class AuthUserService {
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public void setAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    // SecurityContext에서 인증받은 회원의 정보를 얻어온다.
    public DbUser getUser() {
        return (DbUser) getAuthentication().getPrincipal();
    }

    public String getUserUid() {
        try {
            return getUser().getUid();    
        } catch (Exception e) {
            // 예외 처리
        }
        return null;        
    }

    public String getUserName() {
        try {
            return getUser().getName();    
        } catch (Exception e) {
            // 예외 처리
        }
        return null;        
    }

    public String getUserRoles() {
        try {
            return getUser().getRoles();    
        } catch (Exception e) {
            // 예외 처리
        }
        return null;        
    }
}
