package com.ipsdn_opt.app.model;

import lombok.Data;

@Data
public class LoginUser {
    private String loginid;
    private String password;

    public LoginUser() {
    }
    public LoginUser(String loginid, String password) {
        this.loginid = loginid;
        this.password = password;
    }
}
