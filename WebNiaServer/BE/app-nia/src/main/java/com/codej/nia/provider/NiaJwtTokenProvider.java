package com.codej.nia.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.codej.base.provider.BaseJwtTokenProvider;
import com.codej.nia.security.NiaUserDetailsServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class NiaJwtTokenProvider extends BaseJwtTokenProvider { // JWT 토큰을 생성 및 검증 모듈

    @Lazy
    @Autowired
    // @Qualifier("niaUserDetailServiceImpl")
    private final NiaUserDetailsServiceImpl niaUserDetailsService;

    public NiaJwtTokenProvider() {
        niaUserDetailsService = null;
    }

    @Override
    protected UserDetailsService getDetailService() {
        return niaUserDetailsService;
    }


}
