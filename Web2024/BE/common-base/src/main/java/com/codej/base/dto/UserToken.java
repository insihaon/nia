package com.codej.base.dto;

import org.springframework.stereotype.Component;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
@Data
public class UserToken {

    private String name;
    private String token;

    @Builder
    public UserToken(String name, String token) {
        this.name = name;
        this.token = token;
    }
}
