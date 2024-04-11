package com.codej.nia.properties;

import com.codej.base.dto.AppDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class ApiServerProperites {

    @Autowired
    AppDto appDto;

    @Value("${myconf.mail.host:smtp.gmail.com}") // 구글 메일 서버 호스트 이름
    private String mailHost;
    @Value("${myconf.mail.port:587}")
    private Integer mailPort;
    @Value("${myconf.mail.sender:codej@koren.kr}") // 보내는 사람의 이메일 주소
    private String from;
    @Value("${myconf.mail.password:zgpi wyws mnko mokj}") // 보내는 사람의 이메일 계정 비밀번호
    private String mailPassword;

    public String getIpsdnUrl() {
        return appDto.getIpsdnUrl();
    }

}
