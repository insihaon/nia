package com.ipsdn_opt.app.component;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.ipsdn_opt.app.config.PrincipalDetails;
import com.ipsdn_opt.app.model.JwtProperties;

@Component
public class JwtProcessor {

    public String createAuthJwtToken(PrincipalDetails principalDetails) {
        // long expiredTimeMillis = System.currentTimeMillis();
        // if(principalDetails.getUsername().equals("admin")) expiredTimeMillis += (long)(1000 * 60 * 60 * 24) * 30;
        // else expiredTimeMillis += JwtProperties.EXPIRATION_TIME;
        long expiredTimeMillis = System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME;
        return JWT.create()
        .withSubject("IPSDN COS토큰")
        .withExpiresAt(new Date(expiredTimeMillis))
        .withClaim("username", principalDetails.getUsername())
        .sign(Algorithm.HMAC512(JwtProperties.SECRET));
    }

    public String decodeJwtToken(String jwtToken, String secretKey, String claim, HttpServletResponse response) {
        try {
            return JWT.require(Algorithm.HMAC512(secretKey))
                .build()
                .verify(jwtToken)
                .getClaim(claim)
                .asString();
        } catch(TokenExpiredException e) {
            response.setHeader("AUTH_CODE", "EXPIRED");
        } catch(JWTVerificationException e) {
            response.setHeader("AUTH_CODE", "INVALID_TOKEN");
        }
        return null;
    }

    public String extractBearer(String jwtHeader) {
        int pos = jwtHeader.lastIndexOf(" ");
        return jwtHeader.substring(pos + 1);
    }
}
