package com.codej.base.dto;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

public interface BaseUser extends UserDetails {

    abstract void setPassword(String pw);
    abstract void setIpAddress(String ip);
    abstract String getIpAddress();
    abstract List<String> getRolesList();
}
