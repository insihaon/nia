package com.kt.ipms.provider;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.codej.base.provider.BaseJwtTokenProvider;
import com.codej.base.utils.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kt.ipms.legacy.opermgmt.loginmgmt.vo.LoginInfoVo;
import com.kt.ipms.security.IpmsUserDetailsServiceImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Component
public class IpmsJwtTokenProvider extends BaseJwtTokenProvider { // JWT 토큰을 생성 및 검증 모듈

    @Autowired
    @Qualifier("ipmsUserDetailsServiceImpl")
    private final IpmsUserDetailsServiceImpl ipmsUserDetailsService;

    public IpmsJwtTokenProvider() {
        ipmsUserDetailsService = null;
    }

    private UserDetailsService getDetailService() {
        return ipmsUserDetailsService;
    }
    // Jwt 토큰 생성
    public String createToken(LoginInfoVo user, String address) throws JsonProcessingException {
        String userPk = String.valueOf(user.getSuserId());
        List<String> roles = user.getRolesList();
        Claims claims = Jwts.claims().setSubject(userPk);

        claims.put("roles", roles);
        claims.put("address", address);

        UserDetails userDetails = getDetailService().loadUserByUsername(userPk);
        String json = JsonUtil.convertClassToJsonString(userDetails);
        claims.put("details", json);

        // jwtUtil.getCenterList()
        // String json = JsonUtil.convertClassToJsonString(userDetails);
        // claims.put("centerList",);

        Date now = new Date();
        String token = Jwts.builder()
                .setClaims(claims) // 데이터
                .setIssuedAt(now) // 토큰 발행일자
                .setExpiration(new Date(now.getTime() + tokenValidSecond * 1000L)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey) // 암호화 알고리즘, secret값 세팅
                .compact();

        return token;
    }

    // Jwt 토큰으로 인증 정보를 조회
    public Authentication getAuthentication(String token) throws Exception {
        String pk = this.getUserPk(token);
        log.info("getAuthentication > getUserPk: {}, token: {}", pk, token);

        /* 2024.01.24
            세션에 대한 사용자 정보를 가져올 때
            DB 나 메모리에 사용자정보를 저장/로드 하는 형태에서  
            JWT 에 정보를 관리하는 형태로 변경함
        */
        // UserDetails userDetails = userDetailsService.loadUserByUsername(pk);
        UserDetails userDetails = getIpmsUserDetails(token);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
    

    /**
     * Jwt Token을 복호화 하여 사용자 정보를 얻는다.
     */
    public LoginInfoVo getIpmsUserDetails(String jwtToken) {
        try {
            String json =  (String)this.getClaims(jwtToken).get("details");
            LoginInfoVo user = JsonUtil.convertJsonToClass(json, LoginInfoVo.class);
            if (user.getIpAddress() == null) {
                user.setIpAddress(getAddress(jwtToken));
            }
            return user;
        } catch (Exception e) {
            return null;
        }
    }
    
}
