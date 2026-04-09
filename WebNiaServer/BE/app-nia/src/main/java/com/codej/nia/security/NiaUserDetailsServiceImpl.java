package com.codej.nia.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.codej.nia.service.NiaUserService;

@Service
@Qualifier("niaUserDetailsServiceImpl")
public class NiaUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private NiaUserService userService;

    @Override
    public UserDetails loadUserByUsername(String uid) throws UsernameNotFoundException {
        try {
            return userService.findUserByUid(uid);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UsernameNotFoundException(String.format("%s는 인증 할 수 없는 ID입니다.", uid));
        }
    }

}
