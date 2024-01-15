package com.codej.base.dto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.PascalCaseStrategy.class)
public class DbUser implements BaseUser {

    static final long serialVersionUID = 4086111309539465670L;

    private String uid;
    private String password;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime connectTime;
    private String ipAddress;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Builder.Default
    @JsonProperty("roles")
    private String roles = "ROLE_USER";

    private String deptCd;
    private String deptName;
    private String agencyCd;
    private String agencyName;
    private String companyCd;
    private String companyName;
    private String mobile;
    private String email;
    private String myid;
    private String reserved0;
    private String reserved1;
    private String reserved2;
    private String reserved3;
    private List<String> blackDtlList;

    public List<String> getRolesList() {
        return Arrays.asList(this.getRoles().split(","));
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // for List<String>
        // return this.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        // return Arrays.stream(this.getRoles().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        List<SimpleGrantedAuthority> authorities = getRolesList().stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
        return authorities;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return this.password;
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return this.uid;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

}
