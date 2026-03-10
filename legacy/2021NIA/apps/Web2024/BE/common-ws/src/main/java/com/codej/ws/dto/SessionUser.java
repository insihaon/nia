package com.codej.ws.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.codej.base.dto.DbUser;

import io.jsonwebtoken.Claims;
import lombok.Data;

@Data
public class SessionUser implements Serializable {

    private String uid;
    private String address;
    private LocalDateTime connectTime;

    public SessionUser() {
        this.connectTime = LocalDateTime.now();
    }

    public SessionUser(Claims claims) {
        if(claims != null) {
            this.uid = claims.getSubject();
            this.address = (String)claims.get("address");
        }
        this.connectTime = LocalDateTime.now();
    }

    public SessionUser(DbUser user) {
        if(user != null){
            this.uid = user.getUid();
            this.address = user.getIpAddress();
        }
        this.connectTime = LocalDateTime.now();
    }
    
}
