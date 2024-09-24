package com.codej.nia.provider;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.codej.base.dto.BaseUser;
import com.codej.base.provider.BaseJwtTokenProvider;
import com.codej.base.utils.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.codej.nia.security.NiaUserDetailsServiceImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class NiaJwtTokenProvider extends BaseJwtTokenProvider { // JWT 토큰을 생성 및 검증 모듈

    @Autowired(required = true)
    @Qualifier("NiaUserDetailServiceImpl")
    private final NiaUserDetailsServiceImpl niaUserDetailsService;

    public NiaJwtTokenProvider() {
        niaUserDetailsService = null;
    }

    private UserDetailsService getDetailService() {
        return niaUserDetailsService;
    }
    @Override
    // Jwt 토큰 생성
    public String createToken(BaseUser user, String address) throws JsonProcessingException {
        String userPk = String.valueOf(user.getUsername());
        List<String> roles = user.getRolesList();
        Claims claims = Jwts.claims().setSubject(userPk);

        claims.put("roles", roles);
        claims.put("address", address);

        UserDetails userDetails = getDetailService().loadUserByUsername(userPk);
        String json = JsonUtil.convertClassToJsonString(userDetails);
        claims.put("details", json);

        Date now = new Date();
        String token = Jwts.builder()
                .setClaims(claims) // 데이터
                .setIssuedAt(now) // 토큰 발행일자
                .setExpiration(new Date(now.getTime() + tokenValidSecond * 1000L)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey) // 암호화 알고리즘, secret값 세팅
                .compact();

        return token;
    }


}
