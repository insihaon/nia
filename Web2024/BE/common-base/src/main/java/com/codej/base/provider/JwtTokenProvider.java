package com.codej.base.provider;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.codej.base.dto.DbUser;
import com.codej.base.exception.CAuthenticationException;
import com.codej.base.utils.EncryptUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider { // JWT 토큰을 생성 및 검증 모듈

    @Value("${spring.jwt.secret:jwtsecret12#$}")
    private String secretKey;

    @Value("${myconf.auth.expired-seconds:86400}")
    private long tokenValidSecond; // 토큰유효시간, default: 24hours

    @Autowired
    private UserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // Jwt 토큰 생성
    public String createToken(String userPk, List<String> roles, String address) {
        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put("roles", roles);
        claims.put("address", address);
        Date now = new Date();
        String token = Jwts.builder()
                .setClaims(claims) // 데이터
                .setIssuedAt(now) // 토큰 발행일자
                .setExpiration(new Date(now.getTime() + tokenValidSecond * 1000L)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey) // 암호화 알고리즘, secret값 세팅
                .compact();

        return token;
    }

    /**
     * 이름으로 Jwt Token을 생성한다.
     */
    public String generateToken(String name) {
        Date now = new Date();
        return Jwts.builder()
                .setId(name)
                .setIssuedAt(now) // 토큰 발행일자
                .setExpiration(new Date(now.getTime() + tokenValidSecond * 1000L)) // 유효시간 설정
                .signWith(SignatureAlgorithm.HS256, secretKey) // 암호화 알고리즘, secret값 세팅
                .compact();
    }

    /**
     * Jwt Token을 복호화 하여 이름을 얻는다.
     */
    public String getUserNameFromJwt(String jwt) throws Exception {
        return getClaims(jwt).getId();
    }

    public void removeToken(String token) {
        try {
            getClaims(token).setExpiration(new Date(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Jwt 토큰으로 인증 정보를 조회
    public Authentication getAuthentication(String token) throws Exception {
        String pk = this.getUserPk(token);
        log.info("getAuthentication > getUserPk: {}, token: {}", pk, token);

        UserDetails userDetails = userDetailsService.loadUserByUsername(pk);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // Jwt 토큰에서 회원 구별 정보 추출
    public String getUserPk(String token) throws Exception {
        return getClaims(token).getSubject();
    }

    public String decrypt(String text) {
        if (text == null) {
            return null;
        }

        String token = text;
        if (countOccurrences(token, ".") != 2) {
            token = EncryptUtil.decryptText(token);
        }
        return token;
    }

    public String resolveToken(HashMap<String, Object> header) {
        String token = (String) header.get("X-AUTH-TOKEN");
        return decrypt(token);
    }

    // Request의 Header에서 token 파싱 : "X-AUTH-TOKEN: jwt토큰"
    public String resolveToken(HttpServletRequest req) {
        String token = req.getHeader("X-AUTH-TOKEN");
        return decrypt(token);
    }

    // Jwt 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken, String caller) throws CAuthenticationException {
        Claims claims = null;
        try {
            claims = this.getClaims(jwtToken);
            log.info("validateToken > claims.body: {}", claims);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            log.info("validateToken: {}, {}, {}", caller, claims, e.getMessage());
            throw new CAuthenticationException("Error in validateToken", e);
        }
    }

    public Claims getClaims(String jwt) throws Exception {
        return (Claims) getJwtClaims(jwt).getBody();
    }

    private Jws<Claims> getJwtClaims(String jwt) throws Exception {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt);
        } catch (SignatureException ex) {
            log.info("Invalid JWT signature :{}", ex.toString());
            throw ex;
        } catch (MalformedJwtException ex) {
            log.info("Invalid JWT token :{}", ex.toString());
            throw ex;
        } catch (ExpiredJwtException ex) {
            log.info("Expired JWT token :{}", ex.toString());
            throw ex;
        } catch (UnsupportedJwtException ex) {
            log.info("Unsupported JWT token :{}", ex.toString());
            throw ex;
        } catch (IllegalArgumentException ex) {
            log.info("JWT claims string is empty :{}", ex.toString());
            throw ex;
        }
    }

    private Map<String, Boolean> mapBannedUser = new HashMap<String, Boolean>();

    public void putKickout(String uid) {
        mapBannedUser.put(uid, true);
    }

    public void popKickout(String uid) {
        mapBannedUser.remove(uid);
    }

    private Boolean isKickout(String uid) {
        return mapBannedUser.getOrDefault(uid, false);
    }

    public boolean isBannedTocken(String jwtToken, boolean pop) {
        try {
            if (jwtToken == null)
                return false;
            String uid = getUserPk(jwtToken);
            boolean banned = isKickout(uid);
            if (pop && banned)
                popKickout(uid);
            return banned;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Jwt Token을 복호화 하여 사용자 정보를 얻는다.
     */
    public DbUser getUser(String token) {
        DbUser user = null;
        try {
            user = (DbUser) getAuthentication(token).getPrincipal();
            if (user.getIpAddress() == null) {
                user.setIpAddress(getAddress(token));
            }
        } catch (Exception e) {
            log.error("getUser :{}", e.getMessage());
        }

        return user;
    }

    private String getAddress(String jwtToken) {
        try {
            return (String) this.getClaims(jwtToken).get("address");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 
     * @param str    입력문자
     * @param target 카운트를 할 문자
     * @return
     */
    public static int countOccurrences(String str, String target) {
        int count = 0;

        if (str == null || target == null || target.isEmpty()) {
            return count;
        }

        // Escape special characters in the target string for regex
        String escapedTarget = Pattern.quote(target);

        // Create a regex pattern for the target string
        Pattern pattern = Pattern.compile(escapedTarget);
        Matcher matcher = pattern.matcher(str);

        // Count occurrences using find() method
        while (matcher.find()) {
            count++;
        }

        return count;
    }

}
