package com.codej.web.social.dto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter

public class SocialProperties {
    @Value("${spring.social.url.base:http://127.0.0.1:8080}")
    private String baseUrl;

    @Value("${spring.social.kakao.client_id:XXXXXXXXXXXXXXXXXXXXXXXXXX}")
    private String kakaoClientId;

    @Value("${spring.social.kakao.redirect:/social/login/kakao}")
    private String kakaoRedirect;
}
