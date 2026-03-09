package com.ipsdn_opt.app.config;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.ipsdn_opt.app.component.JwtProcessor;
import com.ipsdn_opt.app.repository.UserRepository;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserRepository userRepository;
    private final JwtProcessor jwtProcessor;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .formLogin().disable()
            .httpBasic().disable();
        http
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http
            .addFilter(corsFilter())
            .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtProcessor)) //로그인 처리
            .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository, jwtProcessor));
        http
            .authorizeRequests()
            .antMatchers("/login").permitAll()
            .anyRequest().authenticated();
        http
            .exceptionHandling()
            .authenticationEntryPoint(customAuthenticationEntryPoint);
    }
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        //return passwordEncoder; //new BCryptPasswordEncoder();
        return new BCryptPasswordEncoder();
    }
   
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); //내서버가 응답을 할 때 json을 자바스크립트에서 처리할 수 있게 할지를 설정하는 것
        //config.addAllowedOrigin("*"); //모든 IP의 응답을 허용
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*"); // 모든 Header에 응답을 허용
        config.addAllowedMethod("*"); // 모든 post, get, put, delete 요청을 허용
        config.addExposedHeader("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}

