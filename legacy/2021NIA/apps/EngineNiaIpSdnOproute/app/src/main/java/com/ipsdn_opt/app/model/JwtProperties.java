package com.ipsdn_opt.app.model;

public interface JwtProperties {
    String SECRET = "sbsksjkyk";
    int EXPIRATION_TIME = 1000 * 60 * 60 * 12; //12시간
    String TOKEN_PREFIX = "Bearer";
    String HEADER_STRING = "Authorization";
}