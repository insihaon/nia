package com.ipsdn_opt.app.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ipsdn_opt.app.config.PrincipalDetails;
import com.ipsdn_opt.app.repository.UserRepository;

import lombok.RequiredArgsConstructor;

// http://localhost:8080/login 에서 동작해야 하나, formLogin.disable()했으므로 동작안함.
@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String loginid) throws UsernameNotFoundException {
        return new PrincipalDetails(userRepository.findByLoginid(loginid));
    }
}
